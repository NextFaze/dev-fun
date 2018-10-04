package com.nextfaze.devfun.compiler.processing

import com.nextfaze.devfun.category.CategoryDefinition
import com.nextfaze.devfun.compiler.AnnotationElements
import com.nextfaze.devfun.compiler.DevFunCategory
import com.nextfaze.devfun.compiler.Logging
import com.nextfaze.devfun.compiler.Options
import com.nextfaze.devfun.compiler.StringPreprocessor
import com.nextfaze.devfun.compiler.applyNotNull
import com.nextfaze.devfun.generated.DevFunGenerated
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import javax.annotation.processing.RoundEnvironment
import javax.inject.Inject
import javax.inject.Singleton
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import kotlin.collections.set
import kotlin.reflect.KClass

@Singleton
internal class DeveloperCategoryHandler @Inject constructor(
    override val elements: Elements,
    override val preprocessor: StringPreprocessor,
    override val options: Options,
    override val kElements: KElements,
    private val annotations: AnnotationElements,
    logging: Logging
) : AnnotationProcessor {
    private val log by logging()

    private val isDebugCommentsEnabled get() = options.isDebugCommentsEnabled

    private val categoryDefinitions = sortedMapOf<String, CodeBlock>()
    override val willGenerateSource get() = categoryDefinitions.isNotEmpty()

    private var simpleCategoryDefinitionUsed = false
    private val simpleCategoryDefinitionName = ClassName.bestGuess("SimpleCategoryDefinition")
    private val simpleCategoryDefinition by lazy {
        val kClassType = KClass::class.asTypeName().parameterizedBy(WildcardTypeName.STAR).asNullable()
        val stringType = String::class.asTypeName().asNullable()
        val intType = Int::class.asTypeName().asNullable()

        TypeSpec.classBuilder(simpleCategoryDefinitionName)
            .addSuperinterface(CategoryDefinition::class)
            .addModifiers(KModifier.PRIVATE, KModifier.DATA)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter(ParameterSpec.builder("clazz", kClassType, KModifier.OVERRIDE).defaultValue("null").build())
                    .addParameter(ParameterSpec.builder("name", stringType, KModifier.OVERRIDE).defaultValue("null").build())
                    .addParameter(ParameterSpec.builder("group", stringType, KModifier.OVERRIDE).defaultValue("null").build())
                    .addParameter(ParameterSpec.builder("order", intType, KModifier.OVERRIDE).defaultValue("null").build())
                    .build()
            )
            .addProperty(PropertySpec.builder("clazz", kClassType).initializer("clazz").build())
            .addProperty(PropertySpec.builder("name", stringType).initializer("name").build())
            .addProperty(PropertySpec.builder("group", stringType).initializer("group").build())
            .addProperty(PropertySpec.builder("order", intType).initializer("order").build())
            .build()
    }

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

        addDefinition(annotations.createDevCatAnnotation(annotatedElement.annotation, annotationElement), element.toClassElement())
    }

    override fun applyToFileSpec(fileSpec: FileSpec.Builder) {
        if (simpleCategoryDefinitionUsed) {
            fileSpec.addType(simpleCategoryDefinition)
        }
    }

    override fun applyToTypeSpec(typeSpec: TypeSpec.Builder) {
        typeSpec.addProperty(
            DevFunGenerated::categoryDefinitions.toPropertySpec(
                initBlock = categoryDefinitions.values.toListOfBlock(CategoryDefinition::class)
            ).build()
        )
    }

    fun createCatDefSource(cat: DevFunCategory, nameField: String, element: TypeElement, debugComment: String? = null) =
        createCatDefSource(cat, nameField, null, element, debugComment)

    private fun createCatDefSource(
        cat: DevFunCategory,
        nameClass: KElements.ClassElement,
        element: TypeElement,
        debugComment: String? = null
    ) = createCatDefSource(cat, null, nameClass, element, debugComment)

    private fun createCatDefSource(
        devFunCat: DevFunCategory,
        nameFieldRef: String? = null,
        nameFieldClass: KElements.ClassElement? = null,
        element: TypeElement,
        debugComment: String? = null
    ) = buildCodeBlock {
        simpleCategoryDefinitionUsed = true
        debugComment?.let { add(debugComment) }
        add("%T(%L = ", simpleCategoryDefinitionName, CategoryDefinition::clazz.name)
        applyNotNull(nameFieldRef) { add("%L", it) }
        applyNotNull(nameFieldClass) { add("%L", it.klassBlock) }
        devFunCat.value?.let { add(", %L = %V", CategoryDefinition::name.name, it.toValue(element)) }
        devFunCat.group?.let { add(", %L = %V", CategoryDefinition::group.name, it.toValue(element)) }
        devFunCat.order?.let { add(", %L = %V", CategoryDefinition::order.name, it) }
        add(")")
    }

    private fun addDefinition(devFunCat: DevFunCategory, element: KElements.ClassElement) {
        // Debugging
        val debugAnnotationInfo = if (isDebugCommentsEnabled) "\n// ${element.enclosingElement}::$element\n" else null

        // Generate definition
        categoryDefinitions[element.type.toString()] = createCatDefSource(devFunCat, element, element, debugAnnotationInfo)
    }
}
