package com.nextfaze.devfun.internal.prop

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T> threadLocal(initializer: () -> T): ThreadLocalDelegate<T> = ThreadLocalDelegate(initializer)
class ThreadLocalDelegate<T>(initializer: () -> T) : ReadWriteProperty<Any, T> {
    private val threadLocal: ThreadLocal<T> = ThreadLocal()

    init {
        threadLocal.set(initializer())
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): T = threadLocal.get()
    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) = threadLocal.set(value)
}
