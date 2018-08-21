package com.nextfaze.devfun.compiler.processing

import com.nextfaze.devfun.compiler.*
import javax.inject.Inject
import javax.inject.Singleton
import javax.lang.model.element.*
import javax.lang.model.type.*
import javax.lang.model.util.Elements

@Singleton
internal class AnnotationSerializer @Inject constructor(
    override val elements: Elements,
    private val stringPreprocessor: StringPreprocessor,
    logging: Logging
) : Processor {
    private val log by logging()

    fun findAndSerialize(
        annotatedElement: Element,
        annotationTypeElement: TypeElement,
        liftDefaults: Boolean = true,
        excludeFields: List<String> = emptyList()
    ): String? = findAndSerialize(annotatedElement, annotationTypeElement.qualifiedName.toString(), liftDefaults, excludeFields)

    private fun findAndSerialize(
        annotatedElement: Element,
        annotationQualifiedName: String,
        liftDefaults: Boolean = true,
        excludeFields: List<String> = emptyList()
    ): String? =
        serialize(
            annotatedElement,
            annotatedElement.annotationMirrors.first { it.annotationType.toString() == annotationQualifiedName },
            liftDefaults = liftDefaults,
            excludeFields = excludeFields
        )

    private fun serialize(
        annotatedElement: Element,
        annotationMirror: AnnotationMirror,
        liftDefaults: Boolean = true,
        excludeFields: List<String> = emptyList()
    ): String? = annotationMirror.toMap(annotatedElement, liftDefaults, excludeFields)

    // todo this is a bit gross, but might be moving to Kotlin Poet so meh for now
    private fun AnnotationMirror.toMap(
        annotationElement: Element?,
        liftDefaults: Boolean,
        excludeFields: List<String> = emptyList()
    ): String? {
//        val liftDefaults = annotationType.asElement().getAnnotation(LiftDefaults::class.java) != null
        val annotationElements =
            if (liftDefaults) {
                annotationType.asElement().enclosedElements.associate { it as ExecutableElement to elementValues[it] }
            } else {
                elementValues
            }

        val entries = annotationElements.mapNotNull { (element: ExecutableElement, annotationValue) ->
            if (element.simpleName.toString() in excludeFields) return@mapNotNull null

            val value = annotationValue?.value ?: element.defaultValue?.value ?: return@mapNotNull null
            val valueType: TypeMirror = element.returnType
            val mapValue = when (valueType) {
                is PrimitiveType ->
                    when (valueType.kind) {
                        TypeKind.BOOLEAN -> value.toString()
                        TypeKind.BYTE -> "$value"
                        TypeKind.SHORT -> value.toString()
                        TypeKind.INT -> value.toString()
                        TypeKind.LONG -> "${value}L"
                        TypeKind.CHAR -> "'$value'"
                        TypeKind.FLOAT -> "${value}f"
                        TypeKind.DOUBLE -> value.toString()
                        else -> throw RuntimeException("Unexpected PrimitiveType.kind: ${valueType.kind}")
                    }
                is DeclaredType -> {
                    val elementKind by lazy { valueType.asElement().kind }
                    when {
                        value is String -> stringPreprocessor.run(value, annotationElement).toKString()
                        elementKind == ElementKind.CLASS ->
                            when (value) {
                                is PrimitiveType -> "${value.toKType().typeName}::class"
                                is DeclaredType -> value.asElement().toClass()
                                is ArrayType -> value.toClass()
                                else -> throw NotImplementedError("Not implemented elementKind: $elementKind for $value (${value::class})")
                            }
                        elementKind == ElementKind.ENUM -> {
                            if (valueType.isClassPublic) {
                                "${valueType.toType()}.$value"
                            } else {
                                "java.lang.Enum.valueOf(${valueType.toClass(kotlinClass = false)} as Class<Nothing>, \"$value\") as Any"
                            }
                        }
                        elementKind == ElementKind.ANNOTATION_TYPE -> (value as AnnotationMirror).toMap(annotationElement, liftDefaults)
                        else -> throw NotImplementedError("Not implemented elementKind: $elementKind for $value (${value::class})")
                    }
                }
                is ArrayType -> {
                    value as List<*>
                    val componentType = valueType.componentType
                    when (componentType) {
                        is PrimitiveType ->
                            when (componentType.kind) {
                                TypeKind.BOOLEAN -> "arrayOf<Boolean>($value)"
                                TypeKind.BYTE -> "arrayOf<Byte>(${value.toString().replace("(byte)", "")})"
                                TypeKind.SHORT -> "arrayOf<Short>($value)"
                                TypeKind.INT -> "arrayOf<Int>($value)"
                                TypeKind.LONG -> "arrayOf<Long>($value)"
                                TypeKind.CHAR -> "arrayOf<Char>($value)"
                                TypeKind.FLOAT -> "arrayOf<Float>($value)"
                                TypeKind.DOUBLE -> "arrayOf<Double>($value)"
                                else -> throw RuntimeException("Unexpected array component type PrimitiveType.kind: ${componentType.kind}")
                            }
                        is DeclaredType -> {
                            val componentKind by lazy { componentType.asElement().kind }
                            when {
                                componentType.isString -> "arrayOf<String>(${value.joinToString {
                                    val arrValue = (it as AnnotationValue).value as String
                                    stringPreprocessor.run(arrValue, annotationElement).toKString()
                                }})"
                                componentKind == ElementKind.CLASS -> "arrayOf<KClass<*>>(${value.joinToString {
                                    val arrValue = (it as AnnotationValue).value
                                    when (arrValue) {
                                        is PrimitiveType -> "${arrValue.toKType().typeName}::class"
                                        is DeclaredType -> arrValue.asElement().toClass()
                                        is ArrayType -> arrValue.toClass()
                                        else -> throw NotImplementedError("Not implemented arrValue: $arrValue (${arrValue::class}) for $value (${value::class})")
                                    }
                                }})"
                                componentKind == ElementKind.ENUM ->
                                    "arrayOf<Enum<*>>(${value.joinToString {
                                        val arrAnnotationValue = (it as AnnotationValue).value
                                        val arrValue = (arrAnnotationValue as VariableElement).asType()
                                        if (arrValue.isClassPublic) {
                                            "$it"
                                        } else {
                                            "java.lang.Enum.valueOf(${arrValue.toClass(kotlinClass = false)} as Class<Nothing>, \"$arrAnnotationValue\") as Enum<*>"
                                        }
                                    }})"
                                componentKind == ElementKind.ANNOTATION_TYPE -> {
                                    val entries = value.mapNotNull { (it as AnnotationMirror).toMap(annotationElement, liftDefaults) }
                                    "arrayOf<Map<String, *>>(${entries.joinToString()})"
                                }
                                else -> throw NotImplementedError("Not implemented array componentType: $componentType for $value (${value::class})")
                            }
                        }
                        else -> throw NotImplementedError("Not implemented array componentType: $componentType for $value (${value::class})")
                    }
                }
                else -> throw NotImplementedError("Not implemented type: $valueType (${valueType::class})")
            }

            "\"${element.simpleName}\" to $mapValue"
        }

        return if (entries.isEmpty()) "null" else "mapOf(${entries.joinToString()})"
    }
}
