package com.nextfaze.devfun.inject

import android.support.annotation.RestrictTo
import kotlin.reflect.KClass
import kotlin.reflect.KVisibility

/**
 * Handles Kotlin `object` types.
 *
 * Automatically handles `internal` or `private` types.
 *
 * @internal Visible for testing - use at your own risk.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class KObjectInstanceProvider : InstanceProvider {
    /**
     * Get the Kotlin `object` instance of some [clazz] type.
     *
     * Automatically handles `internal` or `private` types.
     */
    override fun <T : Any> get(clazz: KClass<out T>): T? {
        if (clazz.visibility != KVisibility.PRIVATE) {
            return clazz.objectInstance?.let { return it }
        } else {
            try {
                @Suppress("UNCHECKED_CAST")
                return clazz.java.getDeclaredField("INSTANCE").apply { isAccessible = true }.get(null) as T
            } catch (ignore: NoSuchFieldException) {
                return null
            }
        }
    }
}
