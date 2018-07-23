package com.nextfaze.devfun.compiler.handlers

import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.compiler.*
import com.nextfaze.devfun.core.CategoryDefinition
import com.nextfaze.devfun.generated.DevFunGenerated
import javax.annotation.processing.RoundEnvironment
import javax.inject.Inject
import javax.inject.Singleton
import javax.lang.model.element.ElementKind
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
) : AnnotationHandler {
    private val log by logging()

    private val isDebugCommentsEnabled get() = options.isDebugCommentsEnabled

    private val categoryDefinitions = HashMap<String, String>()
    override val willGenerateSource: Boolean get() = categoryDefinitions.isNotEmpty()

    override fun process(elements: Set<TypeElement>, env: RoundEnvironment) {
        env.getElementsAnnotatedWith(DeveloperCategory::class.java).forEach { element ->
            element as TypeElement
            log.note { "Processing ${element.enclosingElement}::$element..." }

            val annotation = element.annotationMirrors.single { it.annotationType.toString() == DeveloperCategory::class.qualifiedName }
            if (element.kind == ElementKind.ANNOTATION_TYPE) { // meta categories
                env.getElementsAnnotatedWith(element).forEach {
                    val useSiteAnnotation = it.annotationMirrors.first { it.annotationType.toString() == element.qualifiedName.toString() }
                    addCategoryDefinition(
                        annotations.createDevCatAnnotation(annotation, element, useSiteAnnotation),
                        it as TypeElement
                    )
                }
            } else {
                addCategoryDefinition(annotations.createDevCatAnnotation(annotation), element)
            }
        }
    }

    override fun generateSource() =
        """    override val ${DevFunGenerated::categoryDefinitions.name} = listOf<${CategoryDefinition::class.simpleName}>(
${categoryDefinitions.values.sorted().joinToString(",").replaceIndentByMargin("        ", "#|")}
    )"""

    fun generateCatDef(devFunCat: DevFunCategory, ref: String, element: TypeElement) = mutableMapOf<KCallable<*>, Any>().apply {
        this += CategoryDefinition::clazz to ref
        devFunCat.value?.let { this += CategoryDefinition::name to preprocessor.run(it.toKString(), element) }
        devFunCat.group?.let { this += CategoryDefinition::group to preprocessor.run(it.toKString(), element) }
        devFunCat.order?.let { this += CategoryDefinition::order to it }
    }.let { "SimpleCategoryDefinition(${it.entries.joinToString { "${it.key.name} = ${it.value}" }})" }

    private fun addCategoryDefinition(devFunCat: DevFunCategory, element: TypeElement) {
        // Debugging
        val categoryDefinition = "${element.enclosingElement}::$element"
        var debugAnnotationInfo = ""
        if (isDebugCommentsEnabled) {
            debugAnnotationInfo = "\n#|// $categoryDefinition"
        }

        // Generate definition
        categoryDefinitions[element.asType().toString()] =
                """$debugAnnotationInfo
                     #|${generateCatDef(devFunCat, element.toClass(), element)}"""
    }

}
