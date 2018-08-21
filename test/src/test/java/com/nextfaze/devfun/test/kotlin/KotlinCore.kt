package com.nextfaze.devfun.test.kotlin

import com.nextfaze.devfun.internal.log.*
import org.jetbrains.kotlin.analyzer.AnalysisResult
import org.jetbrains.kotlin.backend.jvm.JvmIrCodegenFactory
import org.jetbrains.kotlin.cli.common.CLIConfigurationKeys
import org.jetbrains.kotlin.cli.common.ExitCode
import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageLocation
import org.jetbrains.kotlin.cli.common.messages.CompilerMessageSeverity
import org.jetbrains.kotlin.cli.common.messages.MessageRenderer
import org.jetbrains.kotlin.cli.common.messages.PrintingMessageCollector
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import org.jetbrains.kotlin.cli.jvm.compiler.CliBindingTrace
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.cli.jvm.compiler.TopDownAnalyzerFacadeForJVM
import org.jetbrains.kotlin.cli.jvm.config.addJvmClasspathRoots
import org.jetbrains.kotlin.codegen.*
import org.jetbrains.kotlin.codegen.state.GenerationState
import org.jetbrains.kotlin.com.intellij.openapi.Disposable
import org.jetbrains.kotlin.com.intellij.openapi.extensions.Extensions
import org.jetbrains.kotlin.com.intellij.openapi.project.Project
import org.jetbrains.kotlin.com.intellij.openapi.util.TextRange
import org.jetbrains.kotlin.com.intellij.openapi.vfs.CharsetToolkit
import org.jetbrains.kotlin.com.intellij.psi.PsiFileFactory
import org.jetbrains.kotlin.com.intellij.psi.impl.PsiFileFactoryImpl
import org.jetbrains.kotlin.com.intellij.psi.search.GlobalSearchScope
import org.jetbrains.kotlin.com.intellij.testFramework.LightVirtualFile
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.JVMConfigurationKeys
import org.jetbrains.kotlin.config.Services
import org.jetbrains.kotlin.descriptors.PackagePartProvider
import org.jetbrains.kotlin.diagnostics.PsiDiagnosticUtils
import org.jetbrains.kotlin.diagnostics.Severity
import org.jetbrains.kotlin.diagnostics.rendering.DefaultErrorMessages
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.kapt3.base.KaptPaths
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.resolve.AnalyzingUtils
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.jvm.extensions.AnalysisHandlerExtension
import org.jetbrains.kotlin.utils.PathUtil
import java.io.File
import javax.annotation.processing.Processor
import kotlin.test.assertTrue

private const val COMPILER_VERBOSE = true

class KotlinCore(
    private val testDir: File,
    variantDirName: String,
    private val kaptOptions: Map<String, String>
) {
    private val log = logger()

    private val moduleName = "kapt3-test-module-${testDir.name}"

    private val stubsDir = File(testDir, "kaptStubs")
    private val incrementalDir = File(testDir, "kaptIncrementalData")

    private val buildDir = File(testDir, "kaptRunner")
    private val sourcesOutputDir = File(buildDir, "/tmp/kapt3/classes/$variantDirName/")

    val compileDir = File(testDir, "compileDest")
    val classesOutputDir = File(compileDir, "/tmp/kapt3/classes/$variantDirName/")

    fun runKapt(
        compileClasspath: List<File>,
        processors: List<Processor>,
        kotlinFiles: List<File>,
        javaFiles: List<File>
    ) {
        log.d {
            """
                |
                |===== Run KAPT =====
                |Classpath:
                |    ${compileClasspath.joinToString("\n\t")}
                |
                |Kotlin Files:
                |    ${if (kotlinFiles.isEmpty()) "<none>" else kotlinFiles.joinToString("\n\t")}
                |
                |Java Files:
                |    ${if (javaFiles.isEmpty()) "<none>" else javaFiles.joinToString("\n\t")}
                |
                |Test Dir:
                |    $testDir
                |""".trimMargin()
        }

        val messageCollector = WarningsMessageCollector(COMPILER_VERBOSE)
        val config = CompilerConfiguration().apply {
            put(CommonConfigurationKeys.MODULE_NAME, moduleName)
            addJvmClasspathRoots(PathUtil.getJdkClassesRootsFromCurrentJre())
            addJvmClasspathRoots(compileClasspath)
            if (javaFiles.isNotEmpty()) {
                put(JVMConfigurationKeys.USE_JAVAC, true)
                put(JVMConfigurationKeys.COMPILE_JAVA, true)
                put(CLIConfigurationKeys.MESSAGE_COLLECTOR_KEY, messageCollector)
            }
        }
        val env = KotlinCoreEnvironment.createForTests(createDisposable(testDir.name) {}, config, EnvironmentConfigFiles.JVM_CONFIG_FILES)

        if (javaFiles.isNotEmpty()) {
            log.d { "Using Javac - found Java files: $javaFiles" }
            env.registerJavac(javaFiles)
        }
        val javaSourceRoots = javaFiles.map { it.parentFile }.toSet().toList()

        val paths = KaptPaths(
            projectBaseDir = null,
            compileClasspath = PathUtil.getJdkClassesRootsFromCurrentJre() + PathUtil.kotlinPathsForIdeaPlugin.stdlibPath + compileClasspath,
            annotationProcessingClasspath = emptyList(),
            javaSourceRoots = javaSourceRoots,
            sourcesOutputDir = sourcesOutputDir,
            classFilesOutputDir = classesOutputDir,
            stubsOutputDir = stubsDir,
            incrementalDataOutputDir = incrementalDir
        )

        val kapt3Extension = Kapt3ExtensionForTests(
            processors = processors,
            paths = paths,
            options = kaptOptions
        )

        try {
            AnalysisHandlerExtension.registerExtension(env.project, kapt3Extension)

            val ktFiles = kotlinFiles.map { createFile(it.name, it.readText(), env.project) }
            GenerationUtils.compileFiles(ktFiles, env, Kapt3BuilderFactory())

            kapt3Extension.savedStubs ?: error("Stubs were not saved")
            kapt3Extension.savedBindings ?: error("Bindings were not saved")
        } finally {
            AnalysisHandlerExtension.unregisterExtension(env.project, kapt3Extension)
            messageCollector.throwOnWarnings()
        }
    }

    fun runCompile(
        compileClasspath: List<File>,
        kotlinFiles: List<File>,
        javaFiles: List<File>
    ) {
        log.d {
            """
                |
                |===== Run Compile =====
                |Classpath:
                |    ${compileClasspath.joinToString("\n\t")}
                |
                |Kotlin Files:
                |    ${if (kotlinFiles.isEmpty()) "<none>" else kotlinFiles.joinToString("\n\t")}
                |
                |Java Files:
                |    ${if (javaFiles.isEmpty()) "<none>" else javaFiles.joinToString("\n\t")}
                |
                |Test Dir:
                |    $testDir
                |""".trimMargin()
        }

        sourcesOutputDir.mkdirs()

        val compilerArgs = K2JVMCompilerArguments().apply {
            destination = classesOutputDir.canonicalPath
            classpath = (compileClasspath).joinToString(File.pathSeparator)
            verbose = COMPILER_VERBOSE
            version = true
            noStdlib = true
            moduleName = this@KotlinCore.moduleName

            if (javaFiles.isNotEmpty()) {
                compileJava = true
                useJavac = true
            }

            // test kotlin sources + kapt generated sources + test java sources
            freeArgs = kotlinFiles.map { it.canonicalPath } + sourcesOutputDir.canonicalPath + javaFiles.map { it.toString() }
        }

        val collector = WarningsMessageCollector(compilerArgs.verbose)
        val compiler = K2JVMCompiler()
        val exitCode = compiler.exec(collector, Services.EMPTY, compilerArgs)
        collector.throwOnWarnings()
        assertTrue("Expected compiler exit code of ${ExitCode.OK}(${ExitCode.OK.code}) but got $exitCode(${exitCode.code})") { exitCode == ExitCode.OK }
    }

    fun logTestOutputs(
        successful: Boolean,
        testDetails: String,
        kotlinFiles: List<File>,
        javaFiles: List<File>
    ) {
        val generatedFile = sourcesOutputDir.walk().firstOrNull { it.extension == "kt" }
        log.i {
            """
                |
                |========== Test Results & Outputs ==========
                |Tested:
                |$testDetails
                |
                |Module Name: $moduleName
                |Successful: $successful
                |Stubs: ${stubsDir.canonicalPath}
                |Incremental: ${incrementalDir.canonicalPath}
                |Source Output: ${sourcesOutputDir.canonicalPath}
                |Classes Output: ${classesOutputDir.canonicalPath}
                |
                |Test Files:
                |${(kotlinFiles + javaFiles).joinToString("\n") { "    ${it.canonicalPath}" }}
                |
                |Generated File: ${generatedFile?.canonicalPath}
                |""".trimMargin()
        }
    }

    fun copyGenerated(dir: File) {
        val failedOutputDir = File(dir, testDir.name).apply { mkdirs() }
        sourcesOutputDir.walk().forEach {
            if (it.extension == "kt") {
                it.copyTo(File(failedOutputDir, it.name))
            }
        }
    }
}

private class WarningsMessageCollector(
    verbose: Boolean,
    private val throwOnWarnings: Boolean = true
) : PrintingMessageCollector(System.err, MessageRenderer.PLAIN_RELATIVE_PATHS, verbose) {
    private val warnings = mutableListOf<String>()

    override fun report(severity: CompilerMessageSeverity, message: String, location: CompilerMessageLocation?) {
        super.report(severity, message, location)

        if (throwOnWarnings && severity.isWarning) {
            warnings +=
                    if (location == null) {
                        "$severity\n> $message"
                    } else {
                        val severityChar = severity.toString()[0].toLowerCase()
                        val tmpPath = "${location.path}: (${location.line}, ${location.column}):" // : at end triggers file hotlink in logs
                        "$severity\n$severityChar: $tmpPath\n\t$message"
                    }
        }
    }

    fun throwOnWarnings() {
        if (throwOnWarnings && warnings.isNotEmpty()) {
            throw RuntimeException(warnings.joinToString("\n\n"))
        }
    }
}

private inline fun createDisposable(name: String, crossinline dispose: () -> Unit) =
    object : Disposable {
        override fun dispose() = dispose.invoke()
        override fun toString() = name
    }

private fun createFile(name: String, text: String, project: Project): KtFile {
    val shortName = name.substring(name.lastIndexOf('/') + 1).let { it.substring(it.lastIndexOf('\\') + 1) }
    val virtualFile = object : LightVirtualFile(shortName, KotlinLanguage.INSTANCE, text) {
        override fun getPath() = "/$name"
    }.apply { charset = CharsetToolkit.UTF8_CHARSET }
    val factory = PsiFileFactory.getInstance(project) as PsiFileFactoryImpl
    return factory.trySetupPsiForFile(virtualFile, KotlinLanguage.INSTANCE, true, false) as KtFile
}

private fun <T : AnalysisHandlerExtension> AnalysisHandlerExtension.Companion.unregisterExtension(project: Project, extension: T) {
    Extensions.getArea(project).getExtensionPoint(extensionPointName).unregisterExtension(extension)
}

/** @see org.jetbrains.kotlin.codegen */
private object GenerationUtils {
    fun compileFiles(
        files: List<KtFile>,
        environment: KotlinCoreEnvironment,
        classBuilderFactory: ClassBuilderFactory = ClassBuilderFactories.TEST
    ): GenerationState =
        compileFiles(files, environment.configuration, classBuilderFactory, environment::createPackagePartProvider)

    private fun compileFiles(
        files: List<KtFile>,
        configuration: CompilerConfiguration,
        classBuilderFactory: ClassBuilderFactory,
        packagePartProvider: (GlobalSearchScope) -> PackagePartProvider
    ): GenerationState {
        val analysisResult = JvmResolveUtil.analyzeAndCheckForErrors(files.first().project, files, configuration, packagePartProvider)
        val state = GenerationState
            .Builder(
                project = files.first().project,
                builderFactory = classBuilderFactory,
                module = analysisResult.moduleDescriptor,
                bindingContext = analysisResult.bindingContext,
                files = files,
                configuration = configuration
            )
            .codegenFactory(if (configuration.getBoolean(JVMConfigurationKeys.IR)) JvmIrCodegenFactory else DefaultCodegenFactory)
            .build()

        if (analysisResult.shouldGenerateCode) {
            KotlinCodegenFacade.compileCorrectFiles(state, CompilationErrorHandler.THROW_EXCEPTION)
        }

        // For JVM-specific errors
        AnalyzingUtils.throwExceptionOnErrors(state.collectedExtraJvmDiagnostics)
        return state
    }
}

/** @see org.jetbrains.kotlin.resolve.lazy */
private object JvmResolveUtil {
    fun analyzeAndCheckForErrors(
        project: Project,
        files: Collection<KtFile>,
        configuration: CompilerConfiguration,
        packagePartProvider: (GlobalSearchScope) -> PackagePartProvider
    ): AnalysisResult {
        files.forEach { file -> AnalyzingUtils.checkForSyntacticErrors(file) }
        return analyze(project, files, configuration, packagePartProvider).apply {
            AnalyzingUtils.throwExceptionOnErrors(bindingContext)
            throwExceptionOnWarnings(bindingContext)
        }
    }

    private fun analyze(
        project: Project,
        files: Collection<KtFile>,
        configuration: CompilerConfiguration,
        packagePartProviderFactory: (GlobalSearchScope) -> PackagePartProvider
    ): AnalysisResult =
        TopDownAnalyzerFacadeForJVM.analyzeFilesWithJavaIntegration(
            project, files, CliBindingTrace(), configuration, packagePartProviderFactory
        )

    private fun throwExceptionOnWarnings(bindingContext: BindingContext) {
        bindingContext.diagnostics.forEach {
            if (it.severity == Severity.WARNING) {
                val location = PsiDiagnosticUtils.atLocation(it.psiFile, it.textRanges[0] as TextRange)
                throw IllegalStateException("${it.factory.name}: ${DefaultErrorMessages.render(it)} $location")
            }
        }
    }
}
