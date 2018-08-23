package com.nextfaze.devfun.compiler.processing

import com.nextfaze.devfun.compiler.*
import com.nextfaze.devfun.compiler.properties.ImplementationGenerator
import com.nextfaze.devfun.core.*
import com.nextfaze.devfun.generated.DevFunGenerated
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.lang.reflect.Field
import javax.annotation.processing.RoundEnvironment
import javax.inject.Inject
import javax.inject.Singleton
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.util.Elements
import kotlin.reflect.KCallable
import kotlin.reflect.KClass

@Singleton
internal class DeveloperReferenceHandler @Inject constructor(
    override val elements: Elements,
    private val options: Options,
    private val importsTracker: ImportsTracker,
    private val implementations: ImplementationGenerator,
    logging: Logging
) : AnnotationProcessor {
    private val log by logging()

    private val useKotlinReflection get() = options.useKotlinReflection
    private val isDebugCommentsEnabled get() = options.isDebugCommentsEnabled

    private val developerReferences = HashMap<String, TypeSpec>()
    override val willGenerateSource: Boolean get() = developerReferences.isNotEmpty()

    override fun processAnnotatedElement(annotatedElement: AnnotatedElement, env: RoundEnvironment) {
        if (!annotatedElement.asReference) return

        val ref = TypeSpec.anonymousClassBuilder()
            .addProperty(
                ReferenceDefinition::annotation.toPropertySpec(
                    // TODO https://github.com/square/kotlinpoet/pull/445
                    propReturnType = KClass::class.asTypeName().parameterizedBy(WildcardTypeName.subtypeOf(Annotation::class))
                ).apply {
                    val t = annotatedElement.annotationElement
                    when {
                        t.isClassPublic -> initializer("%T::class", t.asClassName())
                        else -> initializer("%L", t.toClass(castIfNotPublic = KClass::class, types = *arrayOf(Annotation::class)))
                    }
                }.build()
            )

        val properties = implementations.processAnnotatedElement(annotatedElement, env)
        if (properties != null) {
            ref.addSuperinterface(WithProperties::class.asTypeName().parameterizedBy(Any::class.asTypeName()))
                .addProperty(
                    WithProperties<Any>::properties.toPropertySpec(propReturnType = Any::class.asTypeName())
                        .initializer("%L", properties).build()
                )
        }

        val element = annotatedElement.element
        when (element) {
            is ExecutableElement -> generateDeveloperExecutableReference(annotatedElement, ref)
            is TypeElement -> generateDeveloperTypeReference(annotatedElement, ref)
            is VariableElement -> generateDeveloperFieldReference(annotatedElement, ref)
            else -> log.error(element = element) {
                """Only executable, type, and variable elements are supported at the moment (elementType=${element::class}).
                    |Please make an issue if you want something else (or feel free to make a PR)""".trimMargin()
            }
        }
    }

    override fun generateSource() =
        DevFunGenerated::developerReferences.toPropertySpec()
            .initializer(
                "listOf<%T>(%L)",
                ReferenceDefinition::class,
                developerReferences.values.map { it.toString() }.sorted().joinToString(",").replaceIndentByMargin("        ", "#|")
            )
            .build()
            .toString()

    private fun generateDeveloperFieldReference(annotatedElement: AnnotatedElement, ref: TypeSpec.Builder) {
        importsTracker += FieldReference::class
        importsTracker += Field::class

        val element = annotatedElement.element as VariableElement
        val clazz = element.enclosingElement as TypeElement
        log.note { "Processing $clazz::$element for ${annotatedElement.annotationElement}..." }

        // Can we reference the field directly
        val fieldIsPublic = element.isPublic
        val classIsPublic = fieldIsPublic && clazz.isClassPublic

        // If true the the field is top-level (file-level) declared (and thus we cant directly reference its enclosing class)
        val isClassKtFile = clazz.isClassKtFile

        // Generate field reference
        val field = run {
            val fieldName = element.simpleName.escapeDollar()
            val setAccessible = if (!classIsPublic || !element.isPublic) ".apply { isAccessible = true }" else ""
            """${clazz.toClass(false, isClassKtFile)}.getDeclaredField("$fieldName")$setAccessible"""
        }

        val developerAnnotation = "${element.enclosingElement}::$element"
        developerReferences[developerAnnotation] = ref
            .addSuperinterface(FieldReference::class.asTypeName())
            .addProperty(
                FieldReference::field.toPropertySpec().delegate("lazy { %L }", field)
                    .applyIf(isDebugCommentsEnabled) { addKdoc("%L", developerAnnotation) }
                    .build()
            )
            .build()
    }

    private fun generateDeveloperTypeReference(annotatedElement: AnnotatedElement, ref: TypeSpec.Builder) {
        importsTracker += TypeReference::class

        val element = annotatedElement.element as TypeElement
        log.note { "Processing $element for ${annotatedElement.annotationElement}..." }

        val developerAnnotation = "${element.enclosingElement}::$element"
        developerReferences[developerAnnotation] = ref
            .addSuperinterface(TypeReference::class.asTypeName())
            .addProperty(
                TypeReference::type.toPropertySpec()
                    .apply {
                        when {
                            element.isClassPublic -> initializer("%T::class", element.asClassName())
                            else -> initializer("%L", element.toClass())
                        }
                    }
                    .applyIf(isDebugCommentsEnabled) { addKdoc("%L", developerAnnotation) }
                    .build()
            )
            .build()
    }

    private fun generateDeveloperExecutableReference(annotatedElement: AnnotatedElement, ref: TypeSpec.Builder) {
        importsTracker += MethodReference::class

        val element = annotatedElement.element as ExecutableElement
        val clazz = element.enclosingElement as TypeElement
        log.note { "Processing $clazz::$element for ${annotatedElement.annotationElement}..." }

        // Can we call the function directly
        val funIsPublic = element.isPublic
        val classIsPublic = funIsPublic && clazz.isClassPublic

        // If true the the function is top-level (file-level) declared (and thus we cant directly reference its enclosing class)
        val isClassKtFile = clazz.isClassKtFile

        // Generate method reference
        val method = run {
            val funName = element.simpleName.escapeDollar()
            val setAccessible = if (!classIsPublic || !element.isPublic) ".apply { isAccessible = true }" else ""
            val methodArgTypes = element.parameters.joiner(prefix = ", ") { it.toClass(false) }
            when {
                useKotlinReflection -> """${clazz.toClass()}.declaredFunctions.filter { it.name == "${element.simpleName.stripInternal()}" && it.parameters.size == ${element.parameters.size + 1} }.single().javaMethod!!$setAccessible"""
                else -> """${clazz.toClass(false, isClassKtFile)}.getDeclaredMethod("$funName"$methodArgTypes)$setAccessible"""
            }
        }

        val developerAnnotation = "${element.enclosingElement}::$element"
        developerReferences[developerAnnotation] = ref
            .addSuperinterface(MethodReference::class.asTypeName())
            .addProperty(
                MethodReference::method.toPropertySpec()
                    .initializer("%L", method)
                    .applyIf(isDebugCommentsEnabled) { addKdoc(developerAnnotation) }
                    .build()
            )
            .build()
    }

    private fun KCallable<*>.toPropertySpec(
        propName: String = name,
        propReturnType: TypeName = returnType.asTypeName()
    ) =
        PropertySpec.builder(
            propName,
            propReturnType,
            KModifier.OVERRIDE
        )
}
