package com.nextfaze.devfun.compiler

import javax.annotation.processing.ProcessingEnvironment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class Options @Inject constructor(private val env: ProcessingEnvironment) {
    private fun String.optionOf(): String? = env.options[this]?.trim()?.takeIf { it.isNotBlank() }

    val useKotlinReflection = FLAG_USE_KOTLIN_REFLECTION.optionOf()?.toBoolean() ?: false
    val isDebugVerbose = FLAG_DEBUG_VERBOSE.optionOf()?.toBoolean() ?: false
    val isDebugCommentsEnabled = isDebugVerbose || FLAG_DEBUG_COMMENTS.optionOf()?.toBoolean() ?: false

    val packageRoot = PACKAGE_ROOT.optionOf()
    val packageSuffix = PACKAGE_SUFFIX.optionOf()
    val packageOverride = PACKAGE_OVERRIDE.optionOf()

    val applicationPackage = APPLICATION_PACKAGE.optionOf()
    val applicationVariant = APPLICATION_VARIANT.optionOf()

    val extPackageRoot = EXT_PACKAGE_ROOT.optionOf()
    val extPackageSuffix = EXT_PACKAGE_SUFFIX.optionOf()
    val extPackageOverride = EXT_PACKAGE_OVERRIDE.optionOf()
}
