package com.nextfaze.devfun.compiler.processing

import com.nextfaze.devfun.compiler.*
import com.nextfaze.devfun.core.CategoryDefinition
import com.nextfaze.devfun.generated.DevFunGenerated
import javax.annotation.processing.RoundEnvironment
import javax.inject.Inject
import javax.inject.Singleton
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import kotlin.reflect.KCallable

@Singleton
internal class DeveloperCategoryHandler @Inject constructor(
    override val elements: Elements,
    private val options: Options,
    private val preprocessor: StringPreprocessor,
    private val annotations: AnnotationElements,
    logging: Logging
) : AnnotationProcessor {
    private val log by logging()

    private val isDebugCommentsEnabled get() = options.isDebugCommentsEnabled

    private val categoryDefinitions = HashMap<String, String>()
    override val willGenerateSource: Boolean get() = categoryDefinitions.isNotEmpty()

    override fun processAnnotatedElement(annotatedElement: AnnotatedElement, env: RoundEnvironment) {
        if (!annotatedElement.asCategory) return

        val (element, annotationElement) = annotatedElement

        if (element !is TypeElement) {
            log.error(element = element) {
                """Only type elements are supported with DeveloperCategory (elementType=${element::class}).
                            |Please make an issue if you want something else (or feel free to make a PR)""".trimMargin()
            }
            return
        }

        addDefinition(annotations.createDevCatAnnotation(annotatedElement.annotation, annotationElement), element)
    }

    override fun generateSource() =
        """    override val ${DevFunGenerated::categoryDefinitions.name} = listOf<${CategoryDefinition::class.simpleName}>(
${categoryDefinitions.values.sorted().joinToString(",").replaceIndentByMargin("        ", "#|")}
    )"""

    fun createCatDefSource(devFunCat: DevFunCategory, ref: String, element: TypeElement) = mutableMapOf<KCallable<*>, Any>().apply {
        this += CategoryDefinition::clazz to ref
        devFunCat.value?.let { this += CategoryDefinition::name to preprocessor.run(it.toKString(), element) }
        devFunCat.group?.let { this += CategoryDefinition::group to preprocessor.run(it.toKString(), element) }
        devFunCat.order?.let { this += CategoryDefinition::order to it }
    }.let { "SimpleCategoryDefinition(${it.entries.joinToString { "${it.key.name} = ${it.value}" }})" }

    private fun addDefinition(devFunCat: DevFunCategory, element: TypeElement) {
        // Debugging
        val categoryDefinition = "${element.enclosingElement}::$element"
        var debugAnnotationInfo = ""
        if (isDebugCommentsEnabled) {
            debugAnnotationInfo = "\n#|// $categoryDefinition"
        }

        // Generate definition
        categoryDefinitions[element.asType().toString()] =
                """$debugAnnotationInfo
                     #|${createCatDefSource(devFunCat, element.toClass(), element)}"""
    }
}
