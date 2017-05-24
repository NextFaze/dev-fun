package com.nextfaze.devfun.core

import android.content.Context
import java.util.ServiceLoader
import kotlin.reflect.KClass

/**
 * Modules that extend/use the functionality of [DevFun].
 *
 * Instances will be loaded using Java's [ServiceLoader].
 */
interface DevFunModule {
    /**
     * Name of the module.
     *
     * Primarily used for logging/debugging purposes.
     */
    val name get() = this::class.qualifiedName!!

    /**
     * List of dependencies that this module requires to function correctly.
     *
     * Dependencies will always be initialized before this module can be used.
     */
    val dependsOn: List<KClass<out DevFunModule>> get() = listOf()

    /**
     * Module initialization.
     */
    fun initialize(devFun: DevFun)

    /**
     * Module cleanup.
     */
    fun dispose() = Unit
}

/**
 * Implementation of [DevFunModule] providing various convenience functions.
 */
abstract class AbstractDevFunModule : DevFunModule {
    private lateinit var _devFun: DevFun

    /**
     * Reference to owning [DevFun] instance.
     */
    val devFun: DevFun get() = _devFun

    /**
     * Convenience delegate to [DevFun.context].
     */
    val context: Context get() = devFun.context

    override fun initialize(devFun: DevFun) {
        _devFun = devFun
        init(context)
    }

    /**
     * Called upon [initialize].
     */
    abstract fun init(context: Context)

    /**
     * @see DevFun.get
     * @see DevFun.instanceOf
     * @see DevFun.instanceProviders
     */
    inline fun <reified T : Any> get(): T = devFun.get<T>()

    /**
     * @see DevFun.instanceOf
     * @see DevFun.get
     * @see DevFun.instanceProviders
     */
    @Suppress("unused")
    fun <T : Any> instanceOf(clazz: KClass<out T>): T = devFun.instanceOf(clazz)
}
