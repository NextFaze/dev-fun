@file:Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")

package com.nextfaze.devfun.test

import com.nextfaze.devfun.core.FunctionItem
import com.nextfaze.devfun.inject.InstanceProvider
import java.lang.reflect.Array
import java.util.ArrayDeque
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

interface TestInstanceProviders {
    val testProviders: List<KClass<out InstanceProvider>> get() = listOf()
}

internal class SimpleTypesInstanceProvider : InstanceProvider {
    override fun <T : Any> get(clazz: KClass<out T>) = when (clazz) {
        List::class -> ArrayList::class.createInstance()
        ArrayList::class -> ArrayList::class.createInstance()
        Iterable::class -> ArrayList::class.createInstance()
        ArrayDeque::class -> ArrayDeque::class.createInstance()
        CharSequence::class -> ""
        FunctionItem::class -> object : FunctionItem {
            override val function get() = TODO("not implemented")
            override val category get() = TODO("not implemented")
        }
        else -> null
    } as T?
}

internal class PrimitivesInstanceProvider : InstanceProvider {
    override fun <T : Any> get(clazz: KClass<out T>): T? {
        if (clazz.java.isArray) {
            return Array.newInstance(clazz.java.componentType, 0) as T
        }

        return when (clazz) {
            Boolean::class -> false
            Byte::class -> 0.toByte()
            Short::class -> 0.toShort()
            Int::class -> 0
            Long::class -> 0L
            Char::class -> '0'
            Float::class -> 0f
            Double::class -> 0.toDouble()
            BooleanArray::class -> BooleanArray(0)
            ByteArray::class -> ByteArray(0)
            ShortArray::class -> ShortArray(0)
            IntArray::class -> IntArray(0)
            LongArray::class -> LongArray(0)
            CharArray::class -> CharArray(0)
            FloatArray::class -> FloatArray(0)
            DoubleArray::class -> DoubleArray(0)
            KClass::class -> clazz
            else -> return null
        } as T
    }
}
