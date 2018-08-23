package com.nextfaze.devfun.compiler.processing

import com.nextfaze.devfun.compiler.*
import com.nextfaze.devfun.core.CategoryDefinition
import com.nextfaze.devfun.generated.DevFunGenerated
import com.squareup.kotlinpoet.CodeBlock
import javax.annotation.processing.RoundEnvironment
import javax.inject.Inject
import javax.inject.Singleton
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

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

    private val categoryDefinitions = HashMap<String, CodeBlock>()
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
        DevFunGenerated::categoryDefinitions.toPropertySpec()
            .initializer(
                "listOf<%T>(%L%W)",
                CategoryDefinition::class,
                categoryDefinitions.values.map { it.toString() }.sorted().joinToString(",")
            )
            .build()
            .toString()

    fun createCatDefSource(devFunCat: DevFunCategory, ref: String, element: TypeElement, debugComment: String? = null) =
        CodeBlock.builder()
            .apply { debugComment?.let { add(debugComment) } }
            .add("%T(%L = %L", simpleCategoryDefinitionName, CategoryDefinition::clazz.name, ref)
            .apply {
                devFunCat.value?.let { add(", %L = %L", CategoryDefinition::name.name, it.toLiteral(preprocessor, element)) }
                devFunCat.group?.let { add(", %L = %L", CategoryDefinition::group.name, it.toLiteral(preprocessor, element)) }
                devFunCat.order?.let { add(", %L = %L", CategoryDefinition::order.name, it) }
            }
            .add(")")
            .build()

    private fun addDefinition(devFunCat: DevFunCategory, element: TypeElement) {
        // Debugging
        val debugAnnotationInfo = if (isDebugCommentsEnabled) "\n// ${element.enclosingElement}::$element\n" else null

        // Generate definition
        categoryDefinitions[element.asType().toString()] = createCatDefSource(devFunCat, element.toClass(), element, debugAnnotationInfo)
    }
}
