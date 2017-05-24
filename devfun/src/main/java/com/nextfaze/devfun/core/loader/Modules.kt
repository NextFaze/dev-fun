package com.nextfaze.devfun.core.loader

import android.content.Context
import com.nextfaze.devfun.core.DevFun
import com.nextfaze.devfun.core.DevFunModule
import com.nextfaze.devfun.inject.InstanceProvider
import com.nextfaze.devfun.internal.*
import java.util.ServiceLoader
import kotlin.reflect.KClass

internal class ModuleLoader(private val devFun: DevFun) : InstanceProvider {
    private val log = logger()

    private var _modules = mutableMapOf<KClass<*>, Module>()

    private inner class Module(val module: DevFunModule, private var initialized: Boolean = false, private var initializing: Boolean = false) {
        val isInitialized get() = initialized
        val isInitializing get() = initializing

        fun initialize(context: Context) {
            if (initialized || initializing) return
            initializing = true
            log.d { "Initializing ${module.name}..." }

            module.dependsOn.forEach {
                val dep = _modules[it]
                if (dep == null) {
                    log.w { "Unable to initialize ${module.name}. Could not initialize dependency $it. $it not found." }
                    return
                } else if (!dep.isInitialized && !dep.isInitializing) {
                    try {
                        dep.initialize(context)
                    } catch (t: Throwable) {
                        log.w(t) { "Unable to initialize ${module.name}. Could not initialize dependency $it" }
                        return
                    }
                }
            }
            module.initialize(devFun)
            initialized = true
        }

        fun dispose() {
            if (initialized) {
                module.dispose()
            }
        }
    }

    fun init(modules: Iterable<DevFunModule>, useServiceLoader: Boolean = true) {
        if (useServiceLoader) {
            ServiceLoader.load(DevFunModule::class.java).forEach {
                this += it
            }
        }

        modules.forEach {
            this += it
        }
    }

    operator fun plusAssign(module: DevFunModule) {
        _modules.put(module::class, Module(module))
    }

    override fun <T : Any> get(clazz: KClass<out T>): T? {
        @Suppress("UNCHECKED_CAST")
        return _modules[clazz]?.takeIf { it.isInitialized }?.module as? T
    }

    /**
     * Attempts to initialize uninitialized modules.
     *
     * Can be called at any time any number of times.
     *
     * Modules without dependencies will fail to initialize. Add them and call this again.
     */
    fun tryInitModules() {
        _modules.values.forEach {
            try {
                it.initialize(devFun.context)
            } catch (t: Throwable) {
                log.w(t) { "Failed to initialize ${it.module.name}" }
            }
        }
    }

    /**
     * Disposes initialized modules and clears all module references.
     */
    fun dispose() {
        _modules.values.forEach {
            try {
                it.dispose()
            } catch (t: Throwable) {
                log.w(t) { "Failed to dispose ${it.module.name}" }
            }
        }
        _modules.clear()
    }
}
