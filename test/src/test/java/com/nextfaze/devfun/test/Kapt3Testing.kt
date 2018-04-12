package com.nextfaze.devfun.test

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Build
import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.compiler.*
import com.nextfaze.devfun.core.ActivityProvider
import com.nextfaze.devfun.core.DevFun
import com.nextfaze.devfun.generated.DevFunGenerated
import com.nextfaze.devfun.inject.ConstructingInstanceProvider
import com.nextfaze.devfun.inject.InstanceProvider
import com.nextfaze.devfun.inject.captureInstance
import com.nextfaze.devfun.internal.*
import com.nextfaze.devfun.invoke.parameterInstances
import com.nextfaze.devfun.invoke.receiverInstance
import com.nhaarman.mockito_kotlin.KStubbing
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.jetbrains.kotlin.cli.common.ExitCode
import org.jetbrains.kotlin.cli.common.arguments.K2JVMCompilerArguments
import org.jetbrains.kotlin.cli.common.messages.*
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.cli.jvm.config.addJvmClasspathRoots
import org.jetbrains.kotlin.codegen.AbstractClassBuilder
import org.jetbrains.kotlin.codegen.ClassBuilder
import org.jetbrains.kotlin.codegen.ClassBuilderFactory
import org.jetbrains.kotlin.codegen.ClassBuilderMode
import org.jetbrains.kotlin.codegen.state.GenerationState
import org.jetbrains.kotlin.com.intellij.openapi.Disposable
import org.jetbrains.kotlin.config.CommonConfigurationKeys
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.Services
import org.jetbrains.kotlin.kapt3.AbstractKapt3Extension
import org.jetbrains.kotlin.kapt3.AptMode
import org.jetbrains.kotlin.kapt3.KaptContext
import org.jetbrains.kotlin.kapt3.javac.KaptJavaFileObject
import org.jetbrains.kotlin.kapt3.stubs.ClassFileToSourceStubConverter
import org.jetbrains.kotlin.kapt3.util.KaptLogger
import org.jetbrains.kotlin.resolve.jvm.diagnostics.JvmDeclarationOrigin
import org.jetbrains.kotlin.resolve.jvm.extensions.AnalysisHandlerExtension
import org.jetbrains.kotlin.utils.PathUtil
import org.jetbrains.org.objectweb.asm.ClassWriter
import org.jetbrains.org.objectweb.asm.FieldVisitor
import org.jetbrains.org.objectweb.asm.MethodVisitor
import org.jetbrains.org.objectweb.asm.tree.ClassNode
import org.jetbrains.org.objectweb.asm.tree.FieldNode
import org.jetbrains.org.objectweb.asm.tree.MethodNode
import org.slf4j.Logger
import org.testng.ITestResult
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import java.io.File
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.net.URL
import java.net.URLClassLoader
import java.nio.file.Files
import java.util.Collections
import java.util.Enumeration
import javax.annotation.processing.Processor
import kotlin.collections.set
import kotlin.reflect.KClass
import kotlin.reflect.full.NoSuchPropertyException
import kotlin.reflect.full.isSubclassOf
import kotlin.test.Asserter
import kotlin.test.assertTrue
import com.sun.tools.javac.util.List as JCList

private val KEEP_TEST_OUTPUTS = "false".toBoolean()
private val COPY_FAILED_TESTS = "true".toBoolean()
private val COPY_SUCCESSFUL_TESTS = "false".toBoolean()

private const val COMPILER_VERBOSE = false

private const val TEST_MODULE_NAME = "kapt3-test-module"
private const val TEST_PACKAGE_ROOT = "tested"

private const val TEST_BUILD_TYPE = "kapt3Test"
private val TEST_SOURCES_DIR = File("src/test/java")
private val TEST_DATA_DIR = File("src/testData/kotlin")

fun singleFileTests(testMethod: Method, vararg classes: KClass<*>, useSdkInt: Int? = null, autoKaptAndCompile: Boolean = true) =
    classes.map {
        arrayOf(
            TestContext(
                testMethod.name,
                listOf(it),
                sdkInt = useSdkInt,
                autoKaptAndCompile = autoKaptAndCompile
            )
        )
    }.toTypedArray()

abstract class AbstractKotlinKapt3Tester {
    private val log = logger()

    protected val processors: List<Processor>
        get() = listOf<Processor>(DevFunProcessor())

    private val kotlinStdLib = PathUtil.getResourcePathForClass(AnnotationRetention::class.java)
    private val kotlinReflectLib = PathUtil.getResourcePathForClass(NoSuchPropertyException::class.java)
    private val kotlinTestLib = PathUtil.getResourcePathForClass(Asserter::class.java)
    private val annotationsJar = PathUtil.getResourcePathForClass(DeveloperCategory::class.java)
    private val internalJar = PathUtil.getResourcePathForClass(AbstractActivityLifecycleCallbacks::class.java)
    private val androidJar = PathUtil.getResourcePathForClass(Build::class.java)

    protected val compileClasspath = listOf(kotlinStdLib, kotlinReflectLib, kotlinTestLib, annotationsJar, internalJar, androidJar)

    @BeforeMethod(alwaysRun = true)
    fun beforeMethod(args: Array<Any>) {
        val test = args.getOrNull(0) as? TestContext ?: return
        runTest(test)
    }

    @AfterMethod(alwaysRun = true)
    fun afterMethod(result: ITestResult, args: Array<Any>) {
        val test = args.getOrNull(0) as? TestContext ?: return
        afterRunTest(test, result.isSuccess)
    }

    private var origLoader: ClassLoader? = null

    fun runTest(test: TestContext) {
        log.d(predicate = KEEP_TEST_OUTPUTS) { "testDir: ${test.testDir}" }
        log.d { "test=$test" }

        if (!test.autoKaptAndCompile) {
            return
        }

        test.sdkInt?.also { setSdkInt(it) }

        test.runKapt(compileClasspath, processors)
        test.runCompile(compileClasspath)

        val classpath = listOf(test.classesOutputDir).map { it.toURI().toURL() }
        test.classLoader =
                WrappingUrlClassLoader(classpath, Thread.currentThread().contextClassLoader, test.packageOverride ?: test.packageRoot)
        origLoader = Thread.currentThread().contextClassLoader
        Thread.currentThread().contextClassLoader = test.classLoader
    }

    fun afterRunTest(test: TestContext, successful: Boolean) {
        origLoader?.let {
            Thread.currentThread().contextClassLoader = it
            origLoader = null
        }

        test.sdkInt?.also { setSdkInt(0) }

        if (KEEP_TEST_OUTPUTS) {
            val generatedFile = test.sourcesOutputDir.walk().firstOrNull { it.extension == "kt" }
            log.d {
                """
                |========== Test Results ==========
                |Successful: $successful
                |${test.toString().replace(", ", "\n\t")}
                |Stubs: ${test.stubsDir}
                |Incremental: ${test.incrementalDir.canonicalPath}
                |Source Output: ${test.sourcesOutputDir.canonicalPath}
                |Classes Output: ${test.classesOutputDir.canonicalPath}
                |Test Files:
                |${test.files.joinToString("\n") { "> ${it.canonicalPath}" }}
                |Module Name: ${test.moduleName}
                |GeneratedFile: ${generatedFile?.canonicalPath}
                |""".trimMargin()
            }
        }

        fun copyGenerated(dir: String) {
            val failedOutputDir = File(File(test.testDataDir, dir), test.testDir.name).apply { mkdirs() }
            test.sourcesOutputDir.walk().forEach {
                if (it.extension == "kt") {
                    it.copyTo(File(failedOutputDir, it.name))
                }
            }
        }

        if (successful) {
            if (COPY_SUCCESSFUL_TESTS) {
                copyGenerated("_succeeded")
            }
            if (!KEEP_TEST_OUTPUTS) {
                test.testDir.deleteRecursively()
            }
        } else if (COPY_FAILED_TESTS) {
            copyGenerated("_failed")
        }
    }

    inline fun TestContext.use(body: TestContext.() -> Unit) {
        var successful = false
        runTest(this)
        try {
            body()
            successful = true
        } finally {
            afterRunTest(this, successful)
        }
    }

    fun TestContext.execute() = this.use { }
}

private fun kaptOptions(
    debugVerbose: Boolean? = true,
    packageRoot: String? = TEST_PACKAGE_ROOT,
    packageSuffix: String? = null
): Map<String, String> =
    mutableMapOf<String, String>().apply {
        debugVerbose?.let { this[FLAG_DEBUG_VERBOSE] = it.toString() }
        packageRoot?.let { this[PACKAGE_ROOT] = it }
        packageSuffix?.let { this[PACKAGE_SUFFIX] = it }
    }

data class TestContext(
    val testMethodName: String,
    val testFiles: List<KClass<*>>,
    private val testDirSuffix: String = testFiles.joinToString("_") { it.simpleName!! },
    val testDir: File = Files.createTempDirectory("devfun_testing.$testMethodName.$testDirSuffix").toFile(),
    val applicationId: String = "tested.com.nextfaze.devfun",
    val buildType: String = TEST_BUILD_TYPE,
    val flavor: String = "",
    val testDataDir: File = TEST_DATA_DIR,
    val autoKaptAndCompile: Boolean = true,
    val sdkInt: Int? = null,
    val kaptOptions: Map<String, String> = kaptOptions(packageSuffix = testDir.name)
) {
    private val log = logger()

    override fun toString() = "$testMethodName.$testDirSuffix"

    val variantDir = when {
        flavor.isEmpty() -> buildType
        buildType.isEmpty() -> flavor
        else -> "$flavor/$buildType"
    }
    val packageRoot = kaptOptions.getOrDefault(PACKAGE_ROOT, TEST_PACKAGE_ROOT)
    val packageOverride = kaptOptions[PACKAGE_OVERRIDE]

    val stubsDir = File(testDir, "kaptStubs")
    val incrementalDir = File(testDir, "kaptIncrementalData")

    private val buildDir = File(testDir, "kaptRunner")
    val sourcesOutputDir = File(buildDir, "/tmp/kapt3/classes/$variantDir/")

    val compileDir = File(testDir, "compileDest")
    val classesOutputDir = File(compileDir, "/tmp/kapt3/classes/$variantDir/")

    private val providedFiles = listOf(TestInstanceProviders::class, Assertions::class).map {
        File(TEST_SOURCES_DIR, "${it.qualifiedName!!.replace('.', File.separatorChar)}.kt")
    }
    private val testDataFiles = testFiles.map { File(TEST_DATA_DIR, "${it.qualifiedName!!.replace('.', File.separatorChar)}.kt") }
    val files = testDataFiles + providedFiles

    val testInstanceProviders = testFiles
        .filter { it.isSubclassOf(TestInstanceProviders::class) }
        .map { it.objectInstance as TestInstanceProviders }
        .flatMap { it.testProviders.map { it.qualifiedName!! } }
        .toSet()

    val moduleName = "$TEST_MODULE_NAME-${testDir.name}"

    lateinit var classLoader: ClassLoader

    fun runKapt(compileClasspath: List<File>, processors: List<Processor>) {
        log.d { "Run KAPT with classpath of:\n\t${compileClasspath.joinToString("\n\t")}" }

        val config = CompilerConfiguration().apply {
            put(CommonConfigurationKeys.MODULE_NAME, moduleName)
            addJvmClasspathRoots(PathUtil.getJdkClassesRootsFromCurrentJre())
            addJvmClasspathRoots(compileClasspath)
        }
        val env = KotlinCoreEnvironment.createForTests(createDisposable(testDir.name) {}, config, EnvironmentConfigFiles.JVM_CONFIG_FILES)
        val ktFiles = files.map { createFile(it.name, it.readText(), env.project) }

        val kapt3Extension = Kapt3ExtensionForTests(
            processors = processors,
            compileClasspath = compileClasspath,
            sourcesOutputDir = sourcesOutputDir,
            classFilesOutputDir = classesOutputDir,
            options = kaptOptions,
            stubsOutputDir = stubsDir,
            incrementalDataOutputDir = incrementalDir
        )

        try {
            AnalysisHandlerExtension.registerExtension(env.project, kapt3Extension)

            GenerationUtils.compileFiles(ktFiles, env, Kapt3BuilderFactory())

            kapt3Extension.savedStubs ?: error("Stubs were not saved")
            kapt3Extension.savedBindings ?: error("Bindings were not saved")
        } finally {
            AnalysisHandlerExtension.unregisterExtension(env.project, kapt3Extension)
        }
    }

    fun runCompile(compileClasspath: List<File>) {
        log.d { "Run compile with classpath of:\n\t${compileClasspath.joinToString("\n\t")}" }

        sourcesOutputDir.mkdirs()

        val compilerArgs = K2JVMCompilerArguments().apply {
            destination = classesOutputDir.canonicalPath
            classpath = (compileClasspath).joinToString(File.pathSeparator)
            verbose = COMPILER_VERBOSE
            version = true
            noStdlib = true
            moduleName = this@TestContext.moduleName

            // test sources + kapt generated sources
            freeArgs = files.map { it.canonicalPath } + sourcesOutputDir.canonicalPath
        }

        val collector = ThrowingPrintingMessageCollector(compilerArgs.verbose)
        val compiler = K2JVMCompiler()
        val exitCode = compiler.exec(collector, Services.EMPTY, compilerArgs)
        assertTrue("Expected compiler exit code of ${ExitCode.OK}(${ExitCode.OK.code}) but got $exitCode(${exitCode.code})") { exitCode == ExitCode.OK }
    }

    val devFun: DevFun by lazy {
        val application = mock<Application>()
        KStubbing(application).on { applicationContext } doReturn application

        val context = mock<Context> {
            on { applicationContext } doReturn application
        }
        val activity = mock<Activity> {
            on { applicationContext } doReturn application
        }
        val activityTracker = mock<ActivityProvider> {
            on { this.invoke() } doReturn activity
        }

        DevFun().apply {
            initialize(context)
            instanceProviders.apply {
                this[ConstructingInstanceProvider::class].requireConstructable = false
                this += captureInstance { activityTracker.invoke() }
                this += PrimitivesInstanceProvider()
                this += SimpleTypesInstanceProvider()
                classLoader.loadClasses<InstanceProvider>(testInstanceProviders).forEach {
                    this += instanceProviders[it]
                }
            }
        }
    }

    val funDefs by lazy { devFun.definitions.flatMap { it.functionDefinitions }.toSet() }
    val catDefs by lazy { devFun.definitions.flatMap { it.categoryDefinitions }.toSet() }
    private val devRefs by lazy { devFun.definitions.flatMap { it.developerReferences }.toSet() }

    private val allItems by lazy { devFun.categories.flatMap { it.items }.toSet().groupBy { it.function } }

    fun testInvocations(log: Logger) {
        funDefs.forEach { fd ->
            log.d { "Invoke $fd" }
            val v = fd.invoke(fd.receiverInstance(devFun.instanceProviders), fd.parameterInstances(devFun.instanceProviders, null))
            val value = if (v is Pair<*, *>) v.first else v
            val testable = when (value) {
                is List<*> -> value
                else -> listOf(value)
            }
            log.d { "Test $testable for $fd" }
            testable.filterIsInstance<Assertable>().forEach {
                val result = it.invoke(fd, allItems[fd].orEmpty())
                log.d { "> $result" }
            }
            log.d { "\n" }
        }

        allItems.forEach { fd, items ->
            items.forEach {
                log.d { "Invoke $it" }
                val v = it.invoke(fd.receiverInstance(devFun.instanceProviders), fd.parameterInstances(devFun.instanceProviders, it.args))
                val value = if (v is Pair<*, *>) v.second else v
                val testable = when (value) {
                    is List<*> -> value
                    else -> listOf(value)
                }
                log.d { "Test $testable for $it" }
                testable.filterIsInstance<Assertable>().forEach {
                    val result = it.invoke(fd, allItems[fd].orEmpty())
                    log.d { "> $result" }
                }
                log.d { "\n" }
            }
        }

        devRefs.forEach { ref ->
            log.d { "Invoke developer reference $ref ..." }
            ref.method!!.let {
                it.invoke(it.receiverInstance(devFun.instanceProviders))
            }
        }
    }
}


//
// Kapt3 test helpers - adapted from Kotlin's Kapt3 test sources
//

private class ThrowingPrintingMessageCollector(verbose: Boolean, private val throwOnWarnings: Boolean = true) :
    PrintingMessageCollector(System.err, MessageRenderer.PLAIN_RELATIVE_PATHS, verbose) {

    override fun report(severity: CompilerMessageSeverity, message: String, location: CompilerMessageLocation?) {
        super.report(severity, message, location)

        if (throwOnWarnings && (severity == CompilerMessageSeverity.WARNING || severity == CompilerMessageSeverity.STRONG_WARNING)) {
            throw RuntimeException("$severity message=$message @$location")
        }
    }
}

internal class Kapt3ExtensionForTests(
    private val processors: List<Processor>,
    compileClasspath: List<File>,
    javaSourceRoots: List<File> = listOf(),
    sourcesOutputDir: File,
    classFilesOutputDir: File,
    options: Map<String, String>,
    stubsOutputDir: File,
    incrementalDataOutputDir: File
) : AbstractKapt3Extension(
    compileClasspath = PathUtil.getJdkClassesRootsFromCurrentJre() + PathUtil.kotlinPathsForIdeaPlugin.stdlibPath + compileClasspath,
    annotationProcessingClasspath = emptyList(),
    javaSourceRoots = javaSourceRoots,
    sourcesOutputDir = sourcesOutputDir,
    classFilesOutputDir = classFilesOutputDir,
    stubsOutputDir = stubsOutputDir,
    incrementalDataOutputDir = incrementalDataOutputDir,
    options = options,
    javacOptions = emptyMap(),
    annotationProcessors = "",
    aptMode = AptMode.STUBS_AND_APT,
    pluginInitializedTime = System.currentTimeMillis(),
    logger = KaptLogger(true),
    correctErrorTypes = true,
    mapDiagnosticLocations = true,
    compilerConfiguration = CompilerConfiguration.EMPTY
) {
    var savedStubs: List<String>? = null
    var savedBindings: Map<String, KaptJavaFileObject>? = null

    override fun loadProcessors() = processors

    override fun saveStubs(kaptContext: KaptContext<*>, stubs: List<ClassFileToSourceStubConverter.KaptStub>) {
        if (savedStubs != null) {
            error("Stubs are already saved")
        }
        savedStubs = stubs.map { it.toString() }
        super.saveStubs(kaptContext, stubs)
    }

    override fun saveIncrementalData(
        kaptContext: KaptContext<GenerationState>,
        messageCollector: MessageCollector,
        converter: ClassFileToSourceStubConverter
    ) {
        if (savedBindings != null) {
            error("Bindings are already saved")
        }
        savedBindings = converter.bindings
        super.saveIncrementalData(kaptContext, messageCollector, converter)
    }
}

internal class Kapt3BuilderFactory : ClassBuilderFactory {
    private val compiledClasses = mutableListOf<ClassNode>()
    internal val origins = mutableMapOf<Any, JvmDeclarationOrigin>()

    override fun getClassBuilderMode(): ClassBuilderMode = ClassBuilderMode.KAPT3

    override fun newClassBuilder(origin: JvmDeclarationOrigin): AbstractClassBuilder.Concrete {
        val classNode = ClassNode()
        compiledClasses += classNode
        origins[classNode] = origin
        return Kapt3ClassBuilder(classNode)
    }

    private inner class Kapt3ClassBuilder(val classNode: ClassNode) : AbstractClassBuilder.Concrete(classNode) {
        override fun newField(
            origin: JvmDeclarationOrigin,
            access: Int,
            name: String,
            desc: String,
            signature: String?,
            value: Any?
        ): FieldVisitor {
//            val flags = Flags.asFlagSet(access.toLong())
//            log.i { "newField: origin=$origin, access=$access (flags=$flags), name=$name, desc=$desc, signature=$signature, value=$value" }
            val fieldNode = super.newField(origin, access, name, desc, signature, value) as FieldNode
            origins[fieldNode] = origin
            return fieldNode
        }

        override fun newMethod(
            origin: JvmDeclarationOrigin,
            access: Int,
            name: String,
            desc: String,
            signature: String?,
            exceptions: Array<out String>?
        ): MethodVisitor {
//            val flags = Flags.asFlagSet(access.toLong())
//            log.i { "newMethod: origin=$origin, access=$access (flags=$flags), name=$name, desc=$desc, signature=$signature, exceptions=$exceptions" }
            val methodNode = super.newMethod(origin, access, name, desc, signature, exceptions) as MethodNode
            origins[methodNode] = origin
            return methodNode
        }
    }

    override fun asBytes(builder: ClassBuilder): ByteArray =
        ClassWriter(0).also { (builder as Kapt3ClassBuilder).classNode.accept(it) }.toByteArray()

    override fun asText(builder: ClassBuilder) = throw UnsupportedOperationException()

    override fun close() {}
}

internal inline fun createDisposable(name: String, crossinline dispose: () -> Unit) =
    object : Disposable {
        override fun dispose() = dispose.invoke()
        override fun toString() = name
    }


//
// ClassLoader fun
//

@Suppress("UNCHECKED_CAST")
internal fun <T : Any> ClassLoader.loadClasses(classes: Iterable<String>) = classes.map { loadClass(it).kotlin as KClass<out T> }

private class WrappingUrlClassLoader(
    urls: List<URL>,
    private val wrapped: ClassLoader,
    testPackageRoot: String
) : URLClassLoader(urls.toTypedArray(), null) {
    private val testedRoot = "$TEST_PACKAGE_ROOT."
    private val packageRoot = "$testPackageRoot."
    private val noDelegateResources = ".${DevFunGenerated::class.simpleName}"

    override fun loadClass(name: String, resolve: Boolean): Class<*> {
        return if (name.startsWith(testedRoot) || name.startsWith(packageRoot)) {
            try {
                super.loadClass(name, resolve)
            } catch (t: Throwable) {
                wrapped.loadClass(name)
            }
        } else {
            wrapped.loadClass(name)
        }
    }

    override fun findResources(name: String): Enumeration<URL> {
        if (name.endsWith(noDelegateResources)) {
            return super.findResources(name)
        }

        val resources = super.findResources(name).toList()
        return when {
            resources.isNotEmpty() -> Collections.enumeration(resources)
            else -> wrapped.getResources(name)
        }
    }
}


//
// Reflectively set SDK_INT
//

private val MODIFIERS_FIELD by lazy { Field::class.java.getDeclaredField("modifiers").apply { isAccessible = true } }
private val SDK_INT_FIELD by lazy {
    Build.VERSION::class.java.getDeclaredField("SDK_INT").apply {
        isAccessible = true
        MODIFIERS_FIELD.setInt(this, this.modifiers and Modifier.FINAL.inv())
    }
}

private fun setSdkInt(sdkInt: Int) = SDK_INT_FIELD.set(null, sdkInt)
