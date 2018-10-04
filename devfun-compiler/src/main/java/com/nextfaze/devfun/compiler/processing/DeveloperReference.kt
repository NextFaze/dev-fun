package com.nextfaze.devfun.compiler.processing

import com.nextfaze.devfun.compiler.*
import com.nextfaze.devfun.compiler.properties.ImplementationGenerator
import com.nextfaze.devfun.generated.DevFunGenerated
import com.nextfaze.devfun.reference.*
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import javax.annotation.processing.RoundEnvironment
import javax.inject.Inject
import javax.inject.Singleton
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.util.Elements

@Singleton
internal class DeveloperReferenceHandler @Inject constructor(
    override val elements: Elements,
    override val preprocessor: StringPreprocessor,
    override val options: Options,
    override val kElements: KElements,
    private val implementations: ImplementationGenerator,
    logging: Logging
) : AnnotationProcessor {
    private val log by logging()

    private val developerReferences = sortedMapOf<String, TypeSpec>()
    override val willGenerateSource get() = developerReferences.isNotEmpty()

    override fun processAnnotatedElement(annotatedElement: AnnotatedElement, env: RoundEnvironment) {
        if (!annotatedElement.asReference) return

        val ref = TypeSpec.anonymousClassBuilder()
            .addProperty(
                ReferenceDefinition::annotation.let {
                    it.toPropertySpec(
                        initBlock = annotatedElement.annotationElement.type.toKClassBlock(castIfNotPublic = it.returnType.asTypeName())
                    ).build()
                }
            )

        val properties = implementations.processAnnotatedElement(annotatedElement, env)
        if (properties != null) {
            val (typeName, impl) = properties
            ref.addSuperinterface(WithProperties::class.asTypeName().parameterizedBy(typeName))
                .addProperty(WithProperties<Any>::properties.toPropertySpec(returns = typeName, init = impl).build())
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

    override fun applyToTypeSpec(typeSpec: TypeSpec.Builder) {
        typeSpec.addProperty(
            DevFunGenerated::developerReferences.toPropertySpec(
                initBlock = developerReferences.values.toListOfBlock(ReferenceDefinition::class)
            ).build()
        )
    }

    private fun generateDeveloperFieldReference(annotatedElement: AnnotatedElement, ref: TypeSpec.Builder) {
        val element = annotatedElement.element as VariableElement
        val clazz = element.classElement
        log.note { "Processing $clazz::$element for ${annotatedElement.annotationElement}..." }

        // Can we reference the field directly
        val fieldIsPublic = element.isPublic
        val classIsPublic = fieldIsPublic && clazz.isPublic

        // If true the the field is top-level (file-level) declared (and thus we cant directly reference its enclosing class)
        val isKtFile = clazz.isKtFile

        // Generate field reference
        val field = run {
            val fieldName = element.simpleName.escapeDollar()
            val setAccessible = element.getSetAccessible(classIsPublic)
            CodeBlock.of(
                """%L.getDeclaredField("$fieldName")$setAccessible""",
                clazz.type.toKClassBlock(kotlinClass = false, isKtFile = isKtFile)
            )
        }

        val elementDesc = "${element.enclosingElement}::$element"
        developerReferences[elementDesc] = ref
            .addSuperinterface(FieldReference::class.asTypeName())
            .addProperty(FieldReference::field.toPropertySpec(lazy = field, kDoc = elementDesc).build())
            .build()
    }

    private fun generateDeveloperTypeReference(annotatedElement: AnnotatedElement, ref: TypeSpec.Builder) {
        val element = annotatedElement.element as TypeElement
        log.note { "Processing $element for ${annotatedElement.annotationElement}..." }

        val elementDesc = "${element.enclosingElement}::$element"
        developerReferences[elementDesc] = ref
            .addSuperinterface(TypeReference::class.asTypeName())
            .addProperty(TypeReference::type.toPropertySpec(initBlock = element.toClassElement().klassBlock, kDoc = elementDesc).build())
            .build()
    }

    private fun generateDeveloperExecutableReference(annotatedElement: AnnotatedElement, ref: TypeSpec.Builder) {
        val element = annotatedElement.element as ExecutableElement
        log.note { "Processing ${element.enclosingElement}::$element for ${annotatedElement.annotationElement}..." }

        val elementDesc = "${element.enclosingElement}::$element"
        developerReferences[elementDesc] = ref
            .addSuperinterface(MethodReference::class.asTypeName())
            .addProperty(MethodReference::method.toPropertySpec(initBlock = element.toMethodRef(), kDoc = elementDesc).build())
            .build()
    }
}
