package com.nextfaze.devfun.compiler

import com.squareup.kotlinpoet.ClassName
import javax.lang.model.type.PrimitiveType
import javax.lang.model.type.TypeKind

object J2K {
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

    fun getPrimitiveTypeArray(primitiveType: PrimitiveType) = primitiveTypeArrays[primitiveType.kind]!!
}

val PrimitiveType.arrayTypeName get() = J2K.getPrimitiveTypeArray(this)
