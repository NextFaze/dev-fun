package com.nextfaze.devfun.compiler.properties

import com.nextfaze.devfun.compiler.*
import com.nextfaze.devfun.compiler.processing.Processor
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.WildcardTypeName.Companion.STAR
import javax.lang.model.element.*
import javax.lang.model.type.*
import javax.lang.model.util.Elements
import kotlin.reflect.KClass

internal abstract class PropertiesGenerator(
    final override val elements: Elements,
    private val preprocessor: StringPreprocessor,
    logging: Logging
) : Processor {
    private val log by logging()

    protected var referencingKClassFunc = false // TODO this kind of gross be meh for now

    protected fun generateImplementation(annotation: AnnotationMirror): TypeSpec? {
        val annotationElement = annotation.annotationType.asElement() as TypeElement

        log.note { "Generate implementation for $annotationElement" }

        val builder = TypeSpec.anonymousClassBuilder()

        val implements = ClassName.bestGuess(annotationElement.interfaceFqn)
        builder.addSuperinterface(implements)

        annotation.elementValues.forEach { (element: ExecutableElement, annotationValue: AnnotationValue) ->
            log.note { "Processing (element: $element, annotationValue: $annotationValue (${annotationValue::class})" }

            val value = annotationValue.value!!
            log.note { "value=$value (${value::class})" }
            val valueType: TypeMirror = element.returnType
            log.note { "valueType=$valueType (${valueType::class})" }

            val property = element.toPropertySpec(annotationValue)

            builder.addProperty(property.addModifiers(KModifier.OVERRIDE).build())
        }

        return builder.build()
    }

    private fun TypeMirror.toTypeName(): TypeName =
        when (this) {
            is DeclaredType ->
                if (isString) String::class.asTypeName()
                else {
                    val element = asElement()
                    when (element.kind) {
                        ElementKind.CLASS -> KClass::class.asTypeName().parameterizedBy(STAR)
                        ElementKind.ENUM -> if (isClassPublic) asTypeName() else Enum::class.asTypeName().parameterizedBy(STAR)
                        ElementKind.ANNOTATION_TYPE -> ClassName(element.interfacePackage, element.interfaceName)
                        else -> TODO()
                    }
                }
            is ArrayType -> ClassName("kotlin", "Array").parameterizedBy(componentType.toTypeName())
            else -> asTypeName()
        }

    protected fun ExecutableElement.toPropertySpec(
        annotationValue: AnnotationValue? = defaultValue,
        withInitializer: Boolean = true
    ): PropertySpec.Builder {
        val value = annotationValue?.value
        val valueType: TypeMirror = returnType
        log.note(element = this) { "toPropertySpec($this, $annotationValue: ${annotationValue?.let { it::class }}, withInitializer=$withInitializer), value=$value (${value?.let { it::class }}), valueType=$valueType (${valueType::class})" }

        val typeName = valueType.toTypeName()
        val property = PropertySpec.builder(simpleName.toString(), typeName)

        fun TypeMirror.toKClass(kotlinClass: Boolean = true): String =
            when (this) {
                is PrimitiveType -> "${this.toKType().typeName}::class"
                is DeclaredType -> this.asElement().toClass(kotlinClass = kotlinClass)
                is ArrayType -> this.toClass(kotlinClass = kotlinClass)
                else -> throw NotImplementedError("Not implemented TypeMirror.toKClass(): $this (${this::class})")
            }.also {
                if (!referencingKClassFunc && it.contains("kClass<")) {
                    referencingKClassFunc = true
                }
            }

        fun Element.toKClass(kotlinClass: Boolean = true) = asType().toKClass(kotlinClass)

        var uncheckedCastAdded = false
        fun VariableElement.toEnumValue(): String =
            if (asType().isClassPublic) {
                "${asType()}.$this"
            } else {
                if (!uncheckedCastAdded) {
                    uncheckedCastAdded = true
                    property.addAnnotation(uncheckedCast)
                }
                "java.lang.Enum.valueOf(${toKClass(kotlinClass = false)} as Class<Nothing>, \"$this\") as Enum<*>"
            }

        if (value == null) return property

        log.note { "Adding default :: value=$value (${value::class})" }
        val func by lazy { FunSpec.getterBuilder() }

        fun addStatement(format: String, vararg args: Any) {
            when {
                withInitializer -> property.initializer(format.substring(7), *args)
                else -> func.addStatement(format, *args)
            }
        }

        when (valueType) {
            is PrimitiveType ->
                when (valueType.kind) {
                    TypeKind.BOOLEAN,
                    TypeKind.BYTE,
                    TypeKind.SHORT,
                    TypeKind.INT,
                    TypeKind.DOUBLE -> addStatement("return %L", value)
                    TypeKind.CHAR -> addStatement("return '%L'", value)
                    TypeKind.LONG -> addStatement("return %LL", value)
                    TypeKind.FLOAT -> addStatement("return %Lf", value)
                    else -> throw RuntimeException("Unexpected PrimitiveType.kind: ${valueType.kind}")
                }
            is DeclaredType ->
                when (value) {
                    is String -> addStatement("return %L", preprocessor.run(value, this).toKString(trimMargin = true))
                    is TypeMirror -> addStatement("return %L", value.toKClass())
                    is VariableElement -> addStatement("return %L", value.toEnumValue())
                    is AnnotationMirror -> generateImplementation(value)?.let { addStatement("return %L", it) }
                    else -> throw NotImplementedError("DeclaredType not implement for valueType=$valueType (${valueType::class}) with value=$value (${value::class})")
                }
            is ArrayType -> {
                value as List<*>
                val componentType = valueType.componentType
                when (componentType) {
                    is PrimitiveType -> value.joinToString().replace("(byte)", "")
                    is DeclaredType -> {
                        if (componentType.isString) {
                            value.joinToString {
                                preprocessor.run((it as AnnotationValue).value as String, this).toKString(trimMargin = true)
                            }
                        } else {
                            val kind = componentType.asElement().kind
                            when (kind) {
                                ElementKind.CLASS -> value.joinToString { ((it as AnnotationValue).value as TypeMirror).toKClass() }
                                ElementKind.ENUM -> value.joinToString { ((it as AnnotationValue).value as VariableElement).toEnumValue() }
                                ElementKind.ANNOTATION_TYPE -> value.mapNotNull { generateImplementation(it as AnnotationMirror) }.joinToString()
                                else -> throw NotImplementedError("Not implemented array componentType: $componentType (kind=$kind) for $value (${value::class})")
                            }
                        }
                    }
                    else -> throw NotImplementedError("Not implemented array componentType: $componentType for $value (${value::class})")
                }.also {
                    addStatement("return arrayOf<%T>(%L)", componentType.toTypeName(), it)
                }
            }
            else -> throw NotImplementedError("Default value not implemented for valueType=$valueType (${valueType::class}) with value=$value (${value::class})")
        }

        if (!withInitializer) {
            property.getter(func.build())
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
