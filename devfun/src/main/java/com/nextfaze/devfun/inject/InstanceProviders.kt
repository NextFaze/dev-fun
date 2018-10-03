package com.nextfaze.devfun.inject

import androidx.annotation.RestrictTo
import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperProperty
import com.nextfaze.devfun.core.Composite
import com.nextfaze.devfun.core.Composited
import com.nextfaze.devfun.error.BasicErrorLogger
import com.nextfaze.devfun.error.ErrorHandler
import com.nextfaze.devfun.inject.CacheLevel.*
import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.internal.prop.*
import kotlin.reflect.KClass

/**
 * Instance provider that delegates to other providers.
 *
 * Checks in reverse order of added.
 * i.e. most recently added is checked first.
 */
interface CompositeInstanceProvider : RequiringInstanceProvider, Composite<InstanceProvider>

/**
 * Creates an instance provider that delegates to other providers.
 *
 * Checks in reverse order of added.
 * i.e. most recently added is checked first
 *
 * @internal Visible for testing - use at your own risk.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun createDefaultCompositeInstanceProvider(cacheLevel: CacheLevel = CacheLevel.AGGRESSIVE): CompositeInstanceProvider =
    DefaultCompositeInstanceProvider(cacheLevel)

/**
 * Controls how aggressively the [CompositeInstanceProvider] caches the sources of class instances.
 *
 * - [AGGRESSIVE] Once a type has been found from a provider, that provider will checked before others.
 *   If it returns `null` the next time then the lookup will continue be as if [NONE].
 *   Can result in a significant performance improvement.
 *
 * - [SINGLE_LOOP] Behaves the same as [AGGRESSIVE], but is thread-local and caching is only present once per top-level call.
 *   i.e. If something calls `devFun.get<Type>()` - for that one "top-level" call (and thus present for recursion).
 *   This will likely resolve any issues you have with [AGGRESSIVE] while still providing a small improvement - please report any issues!
 *
 * - [NONE] No caching - all providers are checked in reverse order they are added as normal.
 *   Can be quite slow for complex hierarchies (e.g. large Dagger graphs).
 *
 * The cache level can be changed on the fly via DevFun.
 */
enum class CacheLevel { AGGRESSIVE, SINGLE_LOOP, NONE }

@DeveloperCategory("DevFun", group = "Instance Providers")
internal class DefaultCompositeInstanceProvider(cacheLevel: CacheLevel) : CompositeInstanceProvider, Composited<InstanceProvider>() {
    private val log = logger()

    private val errorHandler by lazy { get(ErrorHandler::class) }

    @DeveloperProperty(category = DeveloperCategory(group = "Instance Providers"))
    private var caching: CacheLevel = cacheLevel

    private val aggressiveCachingProvider by lazy { AggressiveTypeCachingProvider(this) }
    private val singleLoopCachingProvider by lazy { SingleLoopTypeCachingProvider(this) }

    private val crumbs by threadLocal { mutableSetOf<Crumb>() }

    override fun <T : Any> get(clazz: KClass<out T>): T =
        when (caching) {
            CacheLevel.AGGRESSIVE -> aggressiveCachingProvider[clazz]
            CacheLevel.SINGLE_LOOP -> singleLoopCachingProvider[clazz]
            CacheLevel.NONE -> getInstance(clazz, null)
        }

    internal fun <T : Any> getInstance(clazz: KClass<out T>, onFound: ((clazz: KClass<*>, provider: InstanceProvider) -> Unit)?): T {
        fun tryGetInstanceOfInstanceProvider(): T? {
            if (clazz.isSubclassOf<InstanceProvider>()) {
                if (clazz.isSubclassOf<DefaultCompositeInstanceProvider>()) {
                    @Suppress("UNCHECKED_CAST")
                    return this as T
                }
                forEach {
                    if (it::class.isSubclassOf(clazz)) {
                        @Suppress("UNCHECKED_CAST")
                        return it as T
                    }
                }
            }
            return null
        }

        forEach { provider ->
            val crumb = Crumb(clazz, provider).also {
                if (crumbs.contains(it)) {
                    log.t { "NOT Try-get instanceOf $clazz from $provider as just tried that." }
                    return@forEach
                } else {
                    log.t { "Try-get instanceOf $clazz from $provider" }
                }
            }

            crumbs += crumb
            try {
                provider[clazz]?.let {
                    log.t { "> Got $it" }
                    onFound?.invoke(clazz, provider)
                    return it
                }
            } catch (ignore: ClassInstanceNotFoundException) {
                // we ignore these and just check others
            } catch (t: ConstructableException) {
                // we got to the lowest provider (ConstructingInstanceProvider) and still couldn't get it
                return tryGetInstanceOfInstanceProvider() ?: throw t
            } catch (t: Throwable) {
                try {
                    errorHandler.onError(
                        t,
                        "Instance Provider",
                        "The instance provider $provider threw an exception when trying to get type $clazz."
                    )
                } catch (t: Throwable) {
                    BasicErrorLogger.onError(null, this, "Instance provider $provider threw an exception when trying to get type $clazz", t)
                }
            } finally {
                crumbs -= crumb
            }
        }

        return tryGetInstanceOfInstanceProvider() ?: throw ClassInstanceNotFoundException(clazz)
    }

    override fun onComponentsChanged() = aggressiveCachingProvider.onComponentsChanged()
}

private class AggressiveTypeCachingProvider(private val parent: DefaultCompositeInstanceProvider) : RequiringInstanceProvider {
    private val log = logger()

    private val typeCacheLock = Any()
    private val typeCache = mutableMapOf<KClass<*>, InstanceProvider>()

    private val crumbs by threadLocal { mutableSetOf<Crumb>() }

    override fun <T : Any> get(clazz: KClass<out T>): T {
        val topLevel = crumbs.isEmpty()

        try {
            typeCache[clazz]?.let { provider ->
                val crumb = Crumb(clazz, provider).also {
                    if (crumbs.contains(it)) {
                        log.t { "NOT Try-get instanceOf $clazz from $provider as just tried that." }
                        return@let
                    } else {
                        log.t { "Try-get instanceOf $clazz from $provider" }
                    }
                }
                crumbs += crumb

                try {
                    provider[clazz]?.let {
                        log.t { "Hit for $clazz in $provider" }
                        return it
                    }
                    log.t { "Miss for $clazz in $provider" }
                } finally {
                    crumbs -= crumb
                }
            }
        } finally {
            if (topLevel) {
                crumbs.clear()
            }
        }

        return parent.getInstance(clazz, ::onFound)
    }

    fun onFound(clazz: KClass<*>, instanceProvider: InstanceProvider) {
        synchronized(typeCacheLock) {
            typeCache[clazz] = instanceProvider
            log.t { "Found $clazz in $instanceProvider" }
        }
    }

    fun onComponentsChanged() = synchronized(typeCacheLock) { typeCache.clear() }
}

private class SingleLoopTypeCachingProvider(private val parent: DefaultCompositeInstanceProvider) : RequiringInstanceProvider {
    private val log = logger()

    private var loopProviderCache: MutableMap<KClass<*>, InstanceProvider>? by threadLocal { null }

    override fun <T : Any> get(clazz: KClass<out T>): T {
        var knownProviders = loopProviderCache
        val topLevel = knownProviders == null
        if (knownProviders == null) {
            knownProviders = mutableMapOf()
            this.loopProviderCache = knownProviders
        }

        try {
            knownProviders[clazz]?.let { provider ->
                provider[clazz]?.let {
                    log.t { "Hit for $clazz in $provider" }
                    return it
                }
                log.t { "Miss for $clazz in $provider" }
            }

            return parent.getInstance(clazz, ::onFound)
        } finally {
            if (topLevel) {
                this.loopProviderCache = null
            }
        }
    }

    fun onFound(clazz: KClass<*>, instanceProvider: InstanceProvider) {
        loopProviderCache?.put(clazz, instanceProvider)
    }
}

private data class Crumb(val clazz: KClass<*>, val provider: InstanceProvider)
