package com.nextfaze.devfun.compiler.properties

import com.nextfaze.devfun.compiler.*
import com.nextfaze.devfun.compiler.processing.KElements
import com.nextfaze.devfun.compiler.processing.Processor
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import javax.lang.model.element.*
import javax.lang.model.type.ArrayType
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.PrimitiveType
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements

internal abstract class PropertiesGenerator(
    final override val elements: Elements,
    final override val kElements: KElements,
    final override val preprocessor: StringPreprocessor,
    logging: Logging
) : Processor {
    private val log by logging()

    protected fun generateImplementation(annotation: AnnotationMirror): TypeSpec {
        val annotationElement = annotation.annotationType.asElement() as TypeElement

        log.note { "Generate implementation for $annotationElement" }

        val builder = TypeSpec.anonymousClassBuilder()
            .addSuperinterface(ClassName.bestGuess(annotationElement.interfaceFqn))

        annotation.elementValues.forEach { (element: ExecutableElement, annotationValue: AnnotationValue) ->
            log.note { "Processing (element: ${element.enclosingElement}.$element, annotationValue: $annotationValue (${annotationValue::class}) [value=${annotationValue.value} (${annotationValue.value?.let { it::class }}), valueType=${element.returnType} (${element.returnType?.let { it::class }})]" }

            val property = element.toPropertySpec(annotationValue, ValueInitType.INITIALIZER)
            builder.addProperty(property.addModifiers(KModifier.OVERRIDE).build())
        }

        return builder.build()
    }

    /**
     * We override a few of the types here;
     * - The dev annotation to "Properties" implementation type (for top-level and nested annotations)
     * - Primitive arrays to Array<Primitive>
     */
    private fun TypeMirror.toReturnTypeName(): TypeName =
        when (this) {
            is DeclaredType -> {
                val element = asElement()
                when (element.kind) {
                    ElementKind.CLASS -> if (isClassPublic) toTypeName(true) else TypeNames.kClassStar
                    ElementKind.ENUM -> if (isClassPublic) toTypeName(true) else TypeNames.enumStar
                    ElementKind.ANNOTATION_TYPE -> ClassName(element.interfacePackage, element.interfaceName)
                    else -> toTypeName(true)
                }
            }
            is ArrayType -> TypeNames.array.parameterizedBy(componentType.toReturnTypeName())
            else -> toTypeName(true)
        }

    protected enum class ValueInitType { GETTER, INITIALIZER }

    protected fun ExecutableElement.toPropertySpec(
        annotationValue: AnnotationValue? = defaultValue,
        valueInitType: ValueInitType
    ): PropertySpec.Builder {
        val value = annotationValue?.value
        val valueType: TypeMirror = returnType
        log.note(element = this) { "$enclosingElement.$this.toPropertySpec($annotationValue (${annotationValue?.let { it::class }}), valueInitType=$valueInitType), value=$value (${value?.let { it::class }}), valueType=$valueType (${valueType::class})" }

        val returnTypeName = valueType.toReturnTypeName()
        val property = PropertySpec.builder(simpleName.toString(), returnTypeName)

        var uncheckedCastAdded = false
        fun VariableElement.toEnumBlock(): CodeBlock {
            val t = this.asType()
            return if (t.isClassPublic) {
                CodeBlock.of("%T.%L", t, simpleName)
            } else {
                if (!uncheckedCastAdded) {
                    uncheckedCastAdded = true
                    property.addAnnotation(uncheckedCast)
                }
                val clazz = t.toKClassBlock(kotlinClass = false, castIfNotPublic = TypeNames.clazz.parameterizedBy(TypeNames.nothing))
                CodeBlock.of("java.lang.Enum.valueOf(%L, \"$this\") as Enum<*>", clazz)
            }
        }

        if (value == null) return property

        log.note { "Adding default :: value=$value (${value::class})" }

        val initBlock = when (valueType) {
            is PrimitiveType -> CodeBlock.of("%V", value)
            is DeclaredType ->
                when (value) {
                    is String -> CodeBlock.of("%V", value.toValue(this))
                    is TypeMirror -> value.toKClassBlock(castIfNotPublic = returnTypeName)
                    is VariableElement -> value.toEnumBlock()
                    is AnnotationMirror -> CodeBlock.of("%L", generateImplementation(value))
                    else -> throw NotImplementedError("DeclaredType not implement for valueType=$valueType (${valueType::class}) with value=$value (${value::class})")
                }
            is ArrayType -> {
                value as List<*>
                val componentType = valueType.componentType
                when (componentType) {
                    is PrimitiveType -> value.joinToString().replace("(byte)", "")
                    is DeclaredType -> {
                        if (componentType.isString) {
                            value.map { ((it as AnnotationValue).value as String).toValue(this) }.toTypedArray()
                        } else {
                            val kind = componentType.asElement().kind
                            when (kind) {
                                ElementKind.CLASS -> value.map { ((it as AnnotationValue).value as TypeMirror).toKClassBlock() }.toTypedArray()
                                ElementKind.ENUM -> value.map { ((it as AnnotationValue).value as VariableElement).toEnumBlock() }.toTypedArray()
                                ElementKind.ANNOTATION_TYPE -> value.mapNotNull { generateImplementation(it as AnnotationMirror) }.toTypedArray()
                                else -> throw NotImplementedError("Not implemented array componentType: $componentType (kind=$kind) for $value (${value::class})")
                            }
                        }
                    }
                    else -> throw NotImplementedError("Not implemented array componentType: $componentType for $value (${value::class})")
                }.let {
                    if (it is Array<*>) {
                        CodeBlock.of("%V", it)
                    } else {
                        CodeBlock.of("arrayOf<%T>(%L)", componentType.toReturnTypeName(), it)
                    }
                }
            }
            else -> throw NotImplementedError("Default value not implemented for valueType=$valueType (${valueType::class}) with value=$value (${value::class})")
        }

        when (valueInitType) {
            ValueInitType.GETTER -> property.getter(FunSpec.getterBuilder().addCode("%[return %L\n%]", initBlock).build())
            ValueInitType.INITIALIZER -> property.initializer(initBlock)
        }

        return property
    }

    private val uncheckedCast by lazy {
        AnnotationSpec.builder(Suppress::class)
            .addMember("%L", """"UNCHECKED_CAST", "CAST_NEVER_SUCCEEDS"""")
            .build()
    }

    val Element.interfaceName get() = "${simpleName}Properties"
    val Element.interfaceFileName get() = "$interfaceName.kt"
    val Element.interfacePackage get() = packageElement.toString()
    val Element.interfaceFqn get() = "$interfacePackage.$interfaceName"
}
