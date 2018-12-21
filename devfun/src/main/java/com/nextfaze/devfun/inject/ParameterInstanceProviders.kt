package com.nextfaze.devfun.inject

import com.nextfaze.devfun.category.DeveloperCategory
import com.nextfaze.devfun.core.Composite
import com.nextfaze.devfun.core.Composited
import com.nextfaze.devfun.core.DevFun
import com.nextfaze.devfun.error.BasicErrorLogger
import com.nextfaze.devfun.error.ErrorHandler
import com.nextfaze.devfun.function.DeveloperProperty
import com.nextfaze.devfun.internal.ParameterInstanceProvider
import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.internal.prop.*
import kotlin.reflect.KParameter

interface CompositeParameterInstanceProvider : ParameterInstanceProvider, Composite<ParameterInstanceProvider>

@DeveloperCategory("DevFun", group = "Instance Providers")
internal class DefaultCompositeParameterInstanceProvider(cacheLevel: CacheLevel, devFun: DevFun) :
    CompositeParameterInstanceProvider, Composited<ParameterInstanceProvider>() {
    private val log = logger()

    private val errorHandler by lazy { devFun.get<ErrorHandler>() }

    @DeveloperProperty(category = DeveloperCategory(group = "Instance Providers"))
    private var paramCaching: CacheLevel = cacheLevel

    private val aggressiveCachingProvider by lazy { AggressiveParameterCachingProvider(this) }
    private val singleLoopCachingProvider by lazy { SingleLoopParameterCachingProvider(this) }

    private val crumbs by threadLocal { mutableSetOf<ParameterCrumb>() }

    override fun <T : Any> get(parameter: KParameter): T? =
        when (paramCaching) {
            CacheLevel.AGGRESSIVE -> aggressiveCachingProvider[parameter]
            CacheLevel.SINGLE_LOOP -> singleLoopCachingProvider[parameter]
            CacheLevel.NONE -> getInstance(parameter, null)
        }

    internal fun <T : Any> getInstance(
        parameter: KParameter,
        onFound: ((parameter: KParameter, provider: ParameterInstanceProvider) -> Unit)?
    ): T? {
        forEach { provider ->
            val crumb = ParameterCrumb(parameter, provider).also {
                if (crumbs.contains(it)) {
                    log.t { "NOT Try-get instanceOf $parameter from $provider as just tried that." }
                    return@forEach
                } else {
                    log.t { "Try-get instanceOf $parameter from $provider" }
                }
            }

            crumbs += crumb
            try {
                provider.get<T>(parameter)?.let {
                    log.t { "> Got $it" }
                    onFound?.invoke(parameter, provider)
                    return it
                }
            } catch (ignore: ClassInstanceNotFoundException) {
                // we ignore these and just check others
            } catch (t: ConstructableException) {
                // we got to the lowest provider (ConstructingInstanceProvider) and still couldn't get it
                throw t
            } catch (t: Throwable) {
                try {
                    errorHandler.onError(
                        t,
                        "Instance Provider",
                        "The instance provider $provider threw an exception when trying to get type $parameter."
                    )
                } catch (t: Throwable) {
                    BasicErrorLogger.onError(
                        null,
                        this,
                        "Instance provider $provider threw an exception when trying to get type $parameter",
                        t
                    )
                }
            } finally {
                crumbs -= crumb
            }
        }

        return null
    }

    override fun onComponentsChanged() = aggressiveCachingProvider.onComponentsChanged()
}

private class AggressiveParameterCachingProvider(private val parent: DefaultCompositeParameterInstanceProvider) :
    ParameterInstanceProvider {
    private val log = logger()

    private val paramCacheLock = Any()
    private val paramCache = mutableMapOf<KParameter, ParameterInstanceProvider>()

    private val crumbs by threadLocal { mutableSetOf<ParameterCrumb>() }

    override fun <T : Any> get(parameter: KParameter): T? {
        val topLevel = crumbs.isEmpty()

        try {
            paramCache[parameter]?.let { provider ->
                val crumb = ParameterCrumb(parameter, provider).also {
                    if (crumbs.contains(it)) {
                        log.t { "NOT Try-get instanceOf $parameter from $provider as just tried that." }
                        return@let
                    } else {
                        log.t { "Try-get instanceOf $parameter from $provider" }
                    }
                }
                crumbs += crumb

                try {
                    provider.get<T>(parameter)?.let {
                        log.t { "Hit for $parameter in $provider" }
                        return it
                    }
                    log.t { "Miss for $parameter in $provider" }
                } finally {
                    crumbs -= crumb
                }
            }
        } finally {
            if (topLevel) {
                crumbs.clear()
            }
        }

        return parent.getInstance(parameter, ::onFound)
    }

    fun onFound(parameter: KParameter, instanceProvider: ParameterInstanceProvider) {
        synchronized(paramCacheLock) {
            paramCache[parameter] = instanceProvider
            log.t { "Found $parameter in $instanceProvider" }
        }
    }

    fun onComponentsChanged() = synchronized(paramCacheLock) { paramCache.clear() }
}

private class SingleLoopParameterCachingProvider(private val parent: DefaultCompositeParameterInstanceProvider) :
    ParameterInstanceProvider {
    private val log = logger()

    private var loopProviderCache: MutableMap<KParameter, ParameterInstanceProvider>? by threadLocal { null }

    override fun <T : Any> get(parameter: KParameter): T? {
        var knownProviders = loopProviderCache
        val topLevel = knownProviders == null
        if (knownProviders == null) {
            knownProviders = mutableMapOf()
            this.loopProviderCache = knownProviders
        }

        try {
            knownProviders[parameter]?.let { provider ->
                provider.get<T>(parameter)?.let {
                    log.t { "Hit for $parameter in $provider" }
                    return it
                }
                log.t { "Miss for $parameter in $provider" }
            }

            return parent.getInstance(parameter, ::onFound)
        } finally {
            if (topLevel) {
                this.loopProviderCache = null
            }
        }
    }

    fun onFound(parameter: KParameter, instanceProvider: ParameterInstanceProvider) {
        loopProviderCache?.put(parameter, instanceProvider)
    }
}

private data class ParameterCrumb(val parameter: KParameter, val provider: ParameterInstanceProvider)
