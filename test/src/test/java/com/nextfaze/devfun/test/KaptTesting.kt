package com.nextfaze.devfun.test

import org.jetbrains.kotlin.analyzer.AnalysisResult
import org.jetbrains.kotlin.cli.jvm.compiler.CliLightClassGenerationSupport
import org.jetbrains.kotlin.cli.jvm.compiler.JvmPackagePartProvider
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.codegen.ClassBuilderFactory
import org.jetbrains.kotlin.codegen.ClassFileFactory
import org.jetbrains.kotlin.codegen.CompilationErrorHandler
import org.jetbrains.kotlin.codegen.KotlinCodegenFacade
import org.jetbrains.kotlin.codegen.state.GenerationState
import org.jetbrains.kotlin.com.intellij.openapi.extensions.Extensions
import org.jetbrains.kotlin.com.intellij.openapi.project.Project
import org.jetbrains.kotlin.com.intellij.openapi.vfs.CharsetToolkit
import org.jetbrains.kotlin.com.intellij.psi.PsiFileFactory
import org.jetbrains.kotlin.com.intellij.psi.impl.PsiFileFactoryImpl
import org.jetbrains.kotlin.com.intellij.psi.search.GlobalSearchScope
import org.jetbrains.kotlin.com.intellij.testFramework.LightVirtualFile
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.descriptors.PackagePartProvider
import org.jetbrains.kotlin.idea.KotlinLanguage
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.resolve.AnalyzingUtils
import org.jetbrains.kotlin.resolve.jvm.TopDownAnalyzerFacadeForJVM
import org.jetbrains.kotlin.resolve.jvm.extensions.AnalysisHandlerExtension

internal fun createFile(name: String, text: String, project: Project): KtFile {
    val shortName = name.substring(name.lastIndexOf('/') + 1).let { it.substring(it.lastIndexOf('\\') + 1) }
    val virtualFile = object : LightVirtualFile(shortName, KotlinLanguage.INSTANCE, text) {
        override fun getPath() = "/" + name
    }.apply { charset = CharsetToolkit.UTF8_CHARSET }
    val factory = PsiFileFactory.getInstance(project) as PsiFileFactoryImpl
    return factory.trySetupPsiForFile(virtualFile, KotlinLanguage.INSTANCE, true, false) as KtFile
}

internal fun generateFiles(
        environment: KotlinCoreEnvironment,
        files: List<KtFile>,
        classBuilderFactory: ClassBuilderFactory
): ClassFileFactory {
    val analysisResult = analyzeAndCheckForErrors(files, environment).apply {
        throwIfError()
        AnalyzingUtils.throwExceptionOnErrors(bindingContext)
    }
    val state = GenerationState(
            environment.project,
            classBuilderFactory,
            analysisResult.moduleDescriptor,
            analysisResult.bindingContext,
            files,
            environment.configuration)

    if (analysisResult.shouldGenerateCode) {
        KotlinCodegenFacade.compileCorrectFiles(state, CompilationErrorHandler.THROW_EXCEPTION)
    }

    // For JVM-specific errors
    AnalyzingUtils.throwExceptionOnErrors(state.collectedExtraJvmDiagnostics)

    return state.factory
}

private fun analyzeAndCheckForErrors(files: Collection<KtFile>, environment: KotlinCoreEnvironment) =
        analyzeAndCheckForErrors(environment.project, files, environment.configuration) {
            JvmPackagePartProvider(environment, it)
        }

private fun analyzeAndCheckForErrors(
        project: Project,
        files: Collection<KtFile>,
        configuration: CompilerConfiguration,
        packagePartProvider: (GlobalSearchScope) -> PackagePartProvider
): AnalysisResult {
    files.forEach { AnalyzingUtils.checkForSyntacticErrors(it) }
    return analyze(project, files, configuration, packagePartProvider).apply {
        AnalyzingUtils.throwExceptionOnErrors(bindingContext)
    }
}

private fun analyze(
        project: Project,
        files: Collection<KtFile>,
        configuration: CompilerConfiguration,
        packagePartProviderFactory: (GlobalSearchScope) -> PackagePartProvider
): AnalysisResult {
    return TopDownAnalyzerFacadeForJVM.analyzeFilesWithJavaIntegration(
            project, files, CliLightClassGenerationSupport.CliBindingTrace(), configuration, packagePartProviderFactory
    )
}

internal fun <T : AnalysisHandlerExtension> AnalysisHandlerExtension.Companion.unregisterExtension(project: Project, extension: T) {
    Extensions.getArea(project).getExtensionPoint(extensionPointName).unregisterExtension(extension)
}
