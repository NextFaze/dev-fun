package com.nextfaze.devfun.test

import org.jetbrains.kotlin.analyzer.AnalysisResult
import org.jetbrains.kotlin.backend.jvm.JvmIrCodegenFactory
import org.jetbrains.kotlin.cli.jvm.compiler.CliLightClassGenerationSupport
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.cli.jvm.compiler.TopDownAnalyzerFacadeForJVM
import org.jetbrains.kotlin.codegen.*
import org.jetbrains.kotlin.codegen.state.GenerationState
import org.jetbrains.kotlin.com.intellij.openapi.extensions.Extensions
import org.jetbrains.kotlin.com.intellij.openapi.project.Project
import org.jetbrains.kotlin.com.intellij.openapi.vfs.CharsetToolkit
import org.jetbrains.kotlin.com.intellij.psi.PsiFileFactory
import org.jetbrains.kotlin.com.intellij.psi.impl.PsiFileFactoryImpl
import org.jetbrains.kotlin.com.intellij.psi.search.GlobalSearchScope
import org.jetbrains.kotlin.com.intellij.testFramework.LightVirtualFile
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.config.JVMConfigurationKeys
import org.jetbrains.kotlin.descriptors.PackagePartProvider
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.resolve.AnalyzingUtils
import org.jetbrains.kotlin.resolve.jvm.extensions.AnalysisHandlerExtension

internal fun createFile(name: String, text: String, project: Project): KtFile {
    val shortName = name.substring(name.lastIndexOf('/') + 1).let { it.substring(it.lastIndexOf('\\') + 1) }
    val virtualFile = object : LightVirtualFile(shortName, KotlinLanguage.INSTANCE, text) {
        override fun getPath() = "/$name"
    }.apply { charset = CharsetToolkit.UTF8_CHARSET }
    val factory = PsiFileFactory.getInstance(project) as PsiFileFactoryImpl
    return factory.trySetupPsiForFile(virtualFile, KotlinLanguage.INSTANCE, true, false) as KtFile
}

internal fun <T : AnalysisHandlerExtension> AnalysisHandlerExtension.Companion.unregisterExtension(project: Project, extension: T) {
    Extensions.getArea(project).getExtensionPoint(extensionPointName).unregisterExtension(extension)
}

/** @see org.jetbrains.kotlin.codegen */
internal object GenerationUtils {
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
        }
    }

    private fun analyze(
        project: Project,
        files: Collection<KtFile>,
        configuration: CompilerConfiguration,
        packagePartProviderFactory: (GlobalSearchScope) -> PackagePartProvider
    ): AnalysisResult =
        TopDownAnalyzerFacadeForJVM.analyzeFilesWithJavaIntegration(
            project, files, CliLightClassGenerationSupport.CliBindingTrace(), configuration, packagePartProviderFactory
        )
}
