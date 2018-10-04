package com.nextfaze.devfun.inject

import androidx.annotation.RestrictTo
import com.nextfaze.devfun.internal.log.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.LazyThreadSafetyMode.NONE
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

    private data class ConstructableType(val isConstructable: Boolean, val factory: () -> Any)

    private val constructablesLock = Any()
    private val constructables = mutableMapOf<KClass<*>, ConstructableType>()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> get(clazz: KClass<out T>): T? = get(clazz, requireConstructable)

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(clazz: KClass<out T>, requireConstructable: Boolean): T? {
        // Do we already have it?
        synchronized(singletonsLock) {
            singletons[clazz]?.also {
                log.t { "> Found singleton instance of $clazz" }
                return it as T
            }
        }

        // Can we already create it?
        synchronized(constructablesLock) {
            constructables[clazz]?.also {
                return if (requireConstructable && !it.isConstructable) null else it.factory() as T
            }
        }

        // must be annotated @Constructable
        val annotations by lazy(NONE) { clazz.java.annotations }
        val constructable by lazy(NONE) { annotations.firstOrNull { it is Constructable } as Constructable? }
        if (requireConstructable && constructable == null) {
            if (clazz.java.declaredConstructors.any { ctor -> ctor.annotations.any { it is Inject } }) {
                throw ConstructableException(
                    """
                        |
                        |> Could not construct instance of:
                        |>   ${clazz.qualifiedName}
                        |
                        |> Class is not @Constructable and requireConstructable=true
                        |>   An @Inject annotation is present, but was not resolved by inject providers.
                        |>   Ensure you have an inject provider (such as devfun-inject-dagger2 etc).
                        |
                        |> Also note: For Dagger 2.x to generate a getter/provider for DevFun to use, it must be injected into the
                        |> relevant scope (otherwise it is effectively ignored by Dagger and DevFun wont have anything to pull it from).
                        |
                        |> Alternatively add @Constructable to the type to allow DevFun to construct it normally.
                        |> The args will be resolved using your inject system et al. as normal.
                        |""".trimMargin()
                )
            } else {
                throw ConstructableException(
                    """
                        |
                        |> Could not construct instance of
                        |>   ${clazz.qualifiedName}
                        |
                        |> Class is not @Constructable and requireConstructable=true
                        |""".trimMargin()
                )
            }
        }

        // must have a single constructor
        val constructors = clazz.java.declaredConstructors
        if (constructors.isEmpty()) {
            throw ConstructableException(
                """
                    |
                    |> Could not construct instance of @Constructable
                    |>   ${clazz.qualifiedName}
                    |
                    |> No constructors found (is it an interface?)
                    |""".trimMargin()
            )
        }
        if (constructors.size != 1) {
            throw ConstructableException(
                """
                    |
                    |> Could not construct instance of @Constructable
                    |>   ${clazz.qualifiedName}
                    |
                    |> Multiple constructors found:
                    |${constructors.joinToString("\n") { "- $it" }}
                    |""".trimMargin()
            )
        }

        log.t { "> Constructing new instance of $clazz" }
        val ctor = constructors.first().apply { isAccessible = true }
        fun createInstance(): T =
            try {
                ctor.newInstance(*(ctor.parameterTypes.map { root[it.kotlin] }.toTypedArray())) as T
            } catch (t: Throwable) {
                throw ConstructableException(t)
            }

        if (constructable?.singleton == true || annotations.any { it is Singleton }) {
            log.t { "> Create singleton instance of $clazz" }
            synchronized(singletonsLock) {
                return singletons.getOrPut(clazz) { createInstance() } as T
            }
        } else {
            synchronized(constructablesLock) {
                return constructables.getOrPut(clazz) { ConstructableType(constructable != null, ::createInstance) }.factory() as T
            }
        }
    }

    override fun toString(): String = "ConstructingInstanceProvider(requireConstructable=$requireConstructable, root=$root)"
}

/**
 * Thrown when [ConstructingInstanceProvider] fails to create a new instance of a class.
 *
 * @see ClassInstanceNotFoundException
 */
@Suppress("unused")
class ConstructableException : Exception {
    constructor(cause: Throwable) : super(cause)
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}
