package com.nextfaze.devfun.internal.prop

import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

class WeakProperty<T : Any>(initialValue: T?) {
    private var value = WeakReference(initialValue)

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? = value.get()
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        this.value = WeakReference(value)
    }
}

inline fun <reified T : Any> weak(initialValue: T? = null): WeakProperty<T> = WeakProperty(initialValue)
