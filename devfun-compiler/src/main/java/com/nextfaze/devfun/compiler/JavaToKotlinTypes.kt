package com.nextfaze.devfun.compiler

import com.squareup.kotlinpoet.ClassName
import javax.lang.model.element.QualifiedNameable
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.PrimitiveType
import javax.lang.model.type.TypeKind
import kotlin.reflect.jvm.internal.impl.name.ClassId
import kotlin.reflect.jvm.internal.impl.name.FqName

private val primitiveTypeArrays by lazy {
    mapOf(
        TypeKind.BOOLEAN to ClassName("kotlin", "BooleanArray"),
        TypeKind.BYTE to ClassName("kotlin", "ByteArray"),
        TypeKind.SHORT to ClassName("kotlin", "ShortArray"),
        TypeKind.INT to ClassName("kotlin", "IntArray"),
        TypeKind.LONG to ClassName("kotlin", "LongArray"),
        TypeKind.CHAR to ClassName("kotlin", "CharArray"),
        TypeKind.FLOAT to ClassName("kotlin", "FloatArray"),
        TypeKind.DOUBLE to ClassName("kotlin", "DoubleArray")
    )
}

val PrimitiveType.arrayTypeName get() = primitiveTypeArrays[kind]!!

private val classMapInstance: (declaredType: DeclaredType) -> ClassName by lazy {
    val clazz = try {
        Class.forName("kotlin.reflect.jvm.internal.impl.platform.JavaToKotlinClassMap")
    } catch (t: ClassNotFoundException) {
        Class.forName("kotlin.reflect.jvm.internal.impl.builtins.jvm.JavaToKotlinClassMap")
    }

    val function = clazz.getMethod("mapJavaToKotlin", FqName::class.java)
    val instance = clazz.getDeclaredField("INSTANCE").get(null)

    val cache = mutableMapOf<String, ClassName>()

    // mapJavaToKotlin does not map Class -> KClass
    cache["java.lang.Class"] = TypeNames.kClass

    return@lazy fun(declaredType: DeclaredType): ClassName {
        val name = (declaredType.asElement() as QualifiedNameable).qualifiedName.toString()
        return cache.getOrPut(name) {
            val fqName = FqName(name)
            val resolved = (function.invoke(instance, fqName) as ClassId?)?.asSingleFqName() ?: fqName
            ClassName(resolved.parent().asString(), resolved.shortName().asString())
        }
    }
}

val DeclaredType.className get() = classMapInstance(this)
