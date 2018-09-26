package com.nextfaze.devfun.compiler

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.WildcardTypeName.Companion.STAR
import com.squareup.kotlinpoet.WildcardTypeName.Companion.subtypeOf
import com.squareup.kotlinpoet.WildcardTypeName.Companion.supertypeOf
import com.squareup.kotlinpoet.asTypeName
import javax.lang.model.element.*
import javax.lang.model.type.*
import javax.lang.model.util.Elements
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.jvm.internal.impl.builtins.jvm.JavaToKotlinClassMap
import kotlin.reflect.jvm.internal.impl.name.FqName

internal inline val Element.isPublic get() = modifiers.contains(Modifier.PUBLIC)
internal inline val Element.isStatic get() = modifiers.contains(Modifier.STATIC)
internal inline val Element.isProperty get() = isStatic && simpleName.endsWith("\$annotations")

internal inline val TypeMirror.isPrimitive get() = kind.isPrimitive
internal val TypeMirror.isClassPublic: Boolean
    get() {
        return when (this) {
            is PrimitiveType -> true
            is DeclaredType -> (asElement() as TypeElement).isClassPublic && typeArguments.all { it.isClassPublic } && asElement().enclosingElement.asType().isClassPublic
            is ExecutableType -> returnType.isClassPublic && parameterTypes.all { it.isClassPublic } && typeVariables.all { it.isClassPublic }
            is ArrayType -> componentType.isClassPublic
            is WildcardType -> extendsBound?.isClassPublic != false && superBound?.isClassPublic != false
            is TypeVariable -> upperBound?.isClassPublic != false && lowerBound?.isClassPublic != false
            is NoType -> true
            is NullType -> true
            else -> throw RuntimeException("isClassPublic not implemented for $this (${this::class})")
        }
    }

internal inline val TypeElement.isClassPublic: Boolean
    get() {
        var element = this
        while (true) {
            if (!element.isPublic) return false
            element = element.enclosingElement as? TypeElement ?: return true // hit package
        }
    }

internal inline operator fun <reified T : Any> AnnotationMirror.get(callable: KCallable<T>) =
    elementValues.filter { it.key.simpleName.toString() == callable.name }.values.singleOrNull()?.value as T?

internal inline operator fun <reified T : Any> AnnotationMirror.get(name: String) =
    elementValues.filter { it.key.simpleName.toString() == name }.values.singleOrNull()?.value as T?

internal inline operator fun <reified T : Annotation> AnnotationMirror.get(callable: KCallable<T>) =
    elementValues.filter { it.key.simpleName.toString() == callable.name }.values.singleOrNull()?.value as AnnotationMirror?

internal operator fun <K : KClass<*>> AnnotationMirror.get(
    callable: KCallable<K>,
    orDefault: (() -> DeclaredType?)? = null
): DeclaredType? {
    val entry = elementValues.filter { it.key.simpleName.toString() == callable.name }.entries.singleOrNull() ?: return orDefault?.invoke()
    return (entry.value.value ?: orDefault?.invoke()) as DeclaredType?
}

internal fun Name.stripInternal() = toString().substringBefore("\$")
internal fun CharSequence.escapeDollar() = toString().replace("\$", "\\\$")

val TypeMirror.isPublic: Boolean
    get() = when (this) {
        is PrimitiveType -> true
        is ArrayType -> this.componentType.isPublic
        is TypeVariable -> this.upperBound.isPublic
        is DeclaredType -> this.asElement().isPublic && this.typeArguments.all { it.isPublic } && this.asElement().enclosingElement.asType().isPublic
        is WildcardType -> this.extendsBound?.isPublic ?: true && this.superBound?.isPublic ?: true
        is ExecutableType -> returnType.isPublic && parameterTypes.all { it.isPublic } && typeVariables.all { it.isPublic }
        is NoType -> true
        else -> throw NotImplementedError("TypeMirror.isPublic not implemented for this=$this (${this::class})")
    }

internal fun Element.getAnnotation(typeElement: TypeElement): AnnotationMirror? =
    annotationMirrors.singleOrNull { it.annotationType.toString() == typeElement.qualifiedName.toString() }

private fun DeclaredType.toClassName(): ClassName {
    val name = (asElement() as QualifiedNameable).qualifiedName
    val fqName = FqName(name.toString())
    val resolved = JavaToKotlinClassMap.INSTANCE.mapJavaToKotlin(fqName)?.asSingleFqName() ?: fqName
    return ClassName(resolved.parent().asString(), resolved.shortName().asString())
}

internal fun TypeMirror.toKClassBlock(
    kotlinClass: Boolean = true,
    isKtFile: Boolean = false,
    castIfNotPublic: TypeName? = null,
    elements: Elements
): CodeBlock {
    if (!isKtFile && isClassPublic) {
        val suffix = when {
            kotlinClass -> ""
            isPrimitiveObject -> ".javaObjectType"
            else -> ".java"
        }

        fun TypeMirror.toType(): TypeName =
            when (this) {
                is PrimitiveType -> asTypeName()
                is DeclaredType -> toClassName()
                is ArrayType -> when {
                    componentType.isPrimitive -> (componentType as PrimitiveType).arrayTypeName
                    else -> TypeNames.array.parameterizedBy(componentType.toType())
                }
                else -> throw NotImplementedError("TypeMirror.toTypeName not implemented for this=$this (${this::class})")
            }

        return CodeBlock.of("%T::class$suffix", toType())
    }

    return when (this) {
        is DeclaredType -> {
            val suffix = if (kotlinClass) ".kotlin" else ""
            val type = asElement() as TypeElement
            val binaryName = elements.getBinaryName(type).escapeDollar()
            if (castIfNotPublic != null) {
                CodeBlock.of("Class.forName(\"$binaryName\")$suffix as %T", castIfNotPublic)
            } else {
                CodeBlock.of("Class.forName(\"$binaryName\")$suffix")
            }
        }
        is ArrayType -> CodeBlock.of(
            "java.lang.reflect.Array.newInstance(%L, 0)::class",
            componentType.toKClassBlock(kotlinClass = false, elements = elements)
        )
        else -> throw NotImplementedError("TypeMirror.toCodeBlock not implemented for this=$this (${this::class})")
    }
}

private val TypeMirror.isPrimitiveObject
    get() = this is DeclaredType &&
            when (toString()) {
                "java.lang.Boolean",
                "java.lang.Character",
                "java.lang.Byte",
                "java.lang.Short",
                "java.lang.Integer",
                "java.lang.Float",
                "java.lang.Long",
                "java.lang.Double",
                "java.lang.Void" -> true
                else -> false
            }

internal fun TypeMirror.toTypeName(subtypeArrayVariance: Boolean = false): TypeName = when (this) {
    is PrimitiveType -> asTypeName()
    is ArrayType -> when {
        componentType.isPrimitive -> (componentType as PrimitiveType).arrayTypeName
        // We need this when getting the typeName for use as a property return type as we can't actually know the
        // variance because we see it as Java "ArrayType[]", not Kotlin "Array<VARIANCE Type>".
        // Thus by using "out" in the property's return type, it will always be accepted (though could technically be wrong under certain circumstances)
        subtypeArrayVariance -> TypeNames.array.parameterizedBy(subtypeOf(componentType.toTypeName(subtypeArrayVariance)))
        else -> TypeNames.array.parameterizedBy(componentType.toTypeName(subtypeArrayVariance))
    }
    is DeclaredType -> {
        val name = (asElement() as QualifiedNameable).qualifiedName
        val typeName = if (name.toString() == "java.lang.Class") { // mapJavaToKotlin to not consider Class -> KClass
            TypeNames.kClass
        } else {
            val fqName = FqName(name.toString())
            val resolved = JavaToKotlinClassMap.INSTANCE.mapJavaToKotlin(fqName)?.asSingleFqName() ?: fqName
            ClassName(resolved.parent().asString(), resolved.shortName().asString())
        }

        if (typeArguments.isEmpty()) {
            typeName
        } else {
            typeName.parameterizedBy(*typeArguments.map { it.toTypeName(subtypeArrayVariance) }.toTypedArray())
        }
    }
    is WildcardType -> extendsBound?.toTypeName(subtypeArrayVariance)?.let { subtypeOf(it) }
            ?: superBound?.toTypeName(subtypeArrayVariance)?.let { supertypeOf(it) }
            ?: STAR
    is TypeVariable -> upperBound?.toTypeName(subtypeArrayVariance) ?: lowerBound?.toTypeName(subtypeArrayVariance) ?: STAR
    else -> throw NotImplementedError("TypeMirror.toTypeName not implemented for this=$this (${this::class})")
}

internal fun TypeName.toCodeBlock() = CodeBlock.of("%T", this)
