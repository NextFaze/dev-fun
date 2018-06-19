package com.nextfaze.devfun.inject

import android.support.annotation.RestrictTo
import kotlin.reflect.KClass

/**
 * Handles Kotlin `object` and `companion object` types.
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
    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> get(clazz: KClass<out T>): T? =
        try {
            clazz.objectInstance?.let { return it }
        } catch (t: IllegalAccessException) {
            if (clazz.isCompanion) { // the companion object is "public" but the outer class is private
                val outerClass = clazz.java.declaringClass
                try {
                    outerClass.getDeclaredField(clazz.simpleName).apply { isAccessible = true }.get(null) as T
                } catch (t: NoSuchFieldException) {
                    throw RuntimeException(
                        "Failed to get companion class $clazz instance field from outer class $outerClass - please report this!",
                        t
                    )
                }
            } else {
                // non-public KObject - get by reflection
                clazz.java.getDeclaredField("INSTANCE").apply { isAccessible = true }.get(null) as T
            }
        }
}
