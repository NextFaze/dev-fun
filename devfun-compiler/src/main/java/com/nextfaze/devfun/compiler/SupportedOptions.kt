package com.nextfaze.devfun.compiler

import javax.annotation.processing.ProcessingEnvironment
import javax.inject.Inject
import javax.inject.Singleton
import javax.lang.model.element.Element
import javax.lang.model.element.QualifiedNameable
import javax.lang.model.util.Elements

@Singleton
internal class Options @Inject constructor(
    override val elements: Elements,
    private val env: ProcessingEnvironment
) : WithElements {
    private fun String.optionOf(): String? = env.options[this]?.trim()?.takeIf { it.isNotBlank() }
    private fun String.booleanOf(default: Boolean = false): Boolean = optionOf()?.toBoolean() ?: default

    val useKotlinReflection = FLAG_USE_KOTLIN_REFLECTION.booleanOf()
    val isDebugVerbose = FLAG_DEBUG_VERBOSE.booleanOf()
    val isDebugCommentsEnabled = isDebugVerbose || FLAG_DEBUG_COMMENTS.booleanOf()

    val packageRoot = PACKAGE_ROOT.optionOf()
    val packageSuffix = PACKAGE_SUFFIX.optionOf()
    val packageOverride = PACKAGE_OVERRIDE.optionOf()

    val applicationPackage = APPLICATION_PACKAGE.optionOf()
    val applicationVariant = APPLICATION_VARIANT.optionOf()

    val extPackageRoot = EXT_PACKAGE_ROOT.optionOf()
    val extPackageSuffix = EXT_PACKAGE_SUFFIX.optionOf()
    val extPackageOverride = EXT_PACKAGE_OVERRIDE.optionOf()

    val generateInterfaces = GENERATE_INTERFACES.booleanOf(true)
    val generateDefinitions = GENERATE_DEFINITIONS.booleanOf(true)

    private val elementFilterInclude = ELEMENTS_FILTER_INCLUDE.optionOf()?.split(",")?.map { it.trim() } ?: emptyList()
    private val elementFilterExclude = ELEMENTS_FILTER_EXCLUDE.optionOf()?.split(",")?.map { it.trim() } ?: emptyList()

    fun shouldProcessElement(element: Element): Boolean {
        if (elementFilterInclude.isEmpty() && elementFilterExclude.isEmpty()) return true

        val fqn = (element as? QualifiedNameable)?.qualifiedName?.toString() ?: "${element.packageElement}.${element.simpleName}"
        return (elementFilterInclude.isEmpty() || elementFilterInclude.any { fqn.startsWith(it) }) &&
                elementFilterExclude.none { fqn.startsWith(it) }
    }
}
