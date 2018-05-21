package com.nextfaze.devfun.inject

import android.support.annotation.RestrictTo
import com.nextfaze.devfun.internal.log.*
import javax.inject.Singleton
import kotlin.reflect.KClass

/**
 * Provides objects via instance construction. Type must be annotated with [Constructable].
 *
 * Only supports objects with a single constructor. Constructor arguments will fetched using param `rootInstanceProvider`.
 *
 * If [Constructable.singleton] is `true` or type is annotated @[Singleton] then only one instance will be created and shared.
 *
 * @param rootInstanceProvider An instance provider used to fetch constructor args. If `null`,  then self (`this`) is used
 * @param requireConstructable Flag indicating if a type must be [Constructable] to be instantiable
 *
 * @internal Visible for testing - use at your own risk.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
class ConstructingInstanceProvider(rootInstanceProvider: InstanceProvider? = null, var requireConstructable: Boolean = true) :
    InstanceProvider {
    private val log = logger()
    private val root = rootInstanceProvider ?: this

    private val singletonsLock = Any()
    private val singletons = mutableMapOf<KClass<*>, Any>()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> get(clazz: KClass<out T>): T? {
        /*
        Using Java reflection (faster and better ProGuard comparability).
         */

        // must be annotated @Constructable
        val constructable = clazz.java.annotations.firstOrNull { it is Constructable } as Constructable?
        if (requireConstructable && constructable == null) {
            return null
        }

        // must have a single constructor
        val constructors = clazz.java.constructors
        if (constructors.isEmpty()) {
            throw ClassInstanceNotFoundException("Could not get instance of @Constructable $clazz; No constructors found (is it an interface?)")
        }
        if (constructors.size != 1) {
            throw ClassInstanceNotFoundException(
                "Could not get instance of @Constructable $clazz: Multiple constructors found; \n${constructors.joinToString("\n")}"
            )
        }

        val isSingleton = constructable?.singleton == true || clazz.java.annotations.any { it is Singleton }
        if (isSingleton) {
            synchronized(singletonsLock) {
                singletons[clazz]?.also {
                    log.t { "> Found singleton instance of $clazz" }
                    return it as T
                }
            }
        }

        log.t { "> Constructing new instance of $clazz" }
        val ctor = constructors.first().apply { isAccessible = true }
        return (ctor.newInstance(*(ctor.parameterTypes.map { root[it.kotlin] }.toTypedArray())) as T)
            .also {
                if (isSingleton) {
                    log.t { "> Created singleton instance of $clazz" }
                    synchronized(singletonsLock) {
                        singletons[clazz] = it
                    }
                }
            }
    }
}
