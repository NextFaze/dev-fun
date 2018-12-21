package com.nextfaze.devfun.compiler

import com.nextfaze.devfun.compiler.processing.KElements
import com.nextfaze.devfun.compiler.processing.Processor
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.joinToCode
import javax.inject.Inject
import javax.inject.Singleton
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.AnnotationValue
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.type.ArrayType
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.PrimitiveType
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements

@Singleton
internal class AnnotationGenerator @Inject constructor(
    override val elements: Elements,
    override val kElements: KElements,
    override val preprocessor: StringPreprocessor,
    logging: Logging
) : Processor {
    private val log by logging()

    fun generateInstance(annotation: AnnotationMirror): CodeBlock? {
        val annotationElement = annotation.annotationType.asElement() as TypeElement
        if (annotationElement.qualifiedName.startsWith("org.jetbrains.annotations")) {
            return null
        }

        log.note { "Generate instance for $annotationElement" }

        val klass = annotationElement.toClassElement().klassBlock
        val ctor = CodeBlock.of("%L.constructors.single()", klass)

        // to call the annotation's ctor we need to pass the default values as well
        val args = annotation.annotationType.asElement().enclosedElements
            .associate { it as ExecutableElement to (annotation.elementValues[it] ?: it.defaultValue) }
            .map { (element: ExecutableElement, annotationValue: AnnotationValue) ->
                log.note { "Processing (element: ${element.enclosingElement}.$element, annotationValue: $annotationValue (${annotationValue::class}) [value=${annotationValue.value} (${annotationValue.value::class}), valueType=${element.returnType} (${element.returnType::class})]" }
                element.toInstanceBlock(annotationValue)
            }

        return if (args.isEmpty()) {
            CodeBlock.of("%L.call() as Annotation", ctor)
        } else {
            CodeBlock.of("%L.call(%L) as Annotation", ctor, args.joinToCode())
        }
    }

    /**
     * We override a few of the types here;
     * - The dev annotation to "Properties" implementation type (for top-level and nested annotations)
     * - Primitive arrays to Array<Primitive>
     */
    private fun TypeMirror.toReturnTypeName(): TypeName =
        when (this) {
            is DeclaredType ->
                when (asElement().kind) {
                    ElementKind.CLASS -> if (isClassPublic) toTypeName(true) else TypeNames.kClassStar
                    ElementKind.ENUM -> if (isClassPublic) toTypeName(true) else TypeNames.enumStar
                    else -> toTypeName(true)
                }
            is ArrayType -> TypeNames.array.parameterizedBy(componentType.toReturnTypeName())
            else -> toTypeName(true)
        }

    private fun ExecutableElement.toInstanceBlock(annotationValue: AnnotationValue): CodeBlock {
        val value = annotationValue.value!!
        val valueType: TypeMirror = returnType
        log.note(element = this) { "$enclosingElement.$this.toPropertySpec($annotationValue (${annotationValue::class})), value=$value (${value::class}), valueType=$valueType (${valueType::class})" }

        val returnTypeName = valueType.toReturnTypeName()

        fun VariableElement.toEnumBlock(): CodeBlock {
            val t = this.asType()
            return if (t.isClassPublic) {
                CodeBlock.of("%T.%L", t, simpleName)
            } else {
                val clazz = t.toKClassBlock(kotlinClass = false, castIfNotPublic = TypeNames.clazz.parameterizedBy(TypeNames.nothing))
                CodeBlock.of("java.lang.Enum.valueOf(%L, \"$this\") as Enum<*>", clazz)
            }
        }

        log.note { "Adding default :: value=$value (${value::class})" }

        return when (valueType) {
            is PrimitiveType -> CodeBlock.of("%V", value)
            is DeclaredType ->
                when (value) {
                    is String -> CodeBlock.of("%V", value.toValue(this))
                    is TypeMirror -> value.toKClassBlock(castIfNotPublic = returnTypeName)
                    is VariableElement -> value.toEnumBlock()
                    is AnnotationMirror -> CodeBlock.of("%L", generateInstance(value))
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
                                ElementKind.ANNOTATION_TYPE -> value.mapNotNull { generateInstance(it as AnnotationMirror) }.toTypedArray()
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
    }
}
