package com.nextfaze.devfun.inject

import android.support.annotation.RestrictTo
import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperProperty
import com.nextfaze.devfun.core.Composite
import com.nextfaze.devfun.core.Composited
import com.nextfaze.devfun.error.ErrorHandler
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
fun createDefaultCompositeInstanceProvider(): CompositeInstanceProvider = DefaultCompositeInstanceProvider()

@DeveloperCategory("DevFun", group = "Instance Providers")
internal class DefaultCompositeInstanceProvider : CompositeInstanceProvider, Composited<InstanceProvider>() {
    private val log = logger()

    private enum class CacheLevel { AGGRESSIVE, TOP_LEVEL, NONE }
    private data class Crumb(val clazz: KClass<*>, val provider: InstanceProvider)

    private val errorHandler by lazy { get(ErrorHandler::class) }

    @DeveloperProperty(category = DeveloperCategory(group = "Instance Providers"))
    private var caching: CacheLevel = CacheLevel.AGGRESSIVE

    private val aggressiveCachingProvider: RequiringInstanceProvider by lazy { AggressiveTypeCachingProvider() }
    private val topLevelCachingProvider: RequiringInstanceProvider by lazy { TopLevelTypeCachingProvider() }

    private val crumbs by threadLocal { mutableSetOf<Crumb>() }

    private inner class AggressiveTypeCachingProvider : RequiringInstanceProvider {
        private val typeCacheLock = Any()
        private val typeCache = mutableMapOf<KClass<*>, InstanceProvider>()

        override fun <T : Any> get(clazz: KClass<out T>): T {
            typeCache[clazz]?.let { provider ->
                provider[clazz]?.let {
                    log.t { "Hit for $clazz in $provider" }
                    return it
                }
                log.t { "Miss for $clazz in $provider" }
            }

            return getInstance(clazz, ::onFound)
        }

        fun onFound(clazz: KClass<*>, instanceProvider: InstanceProvider) {
            synchronized(typeCacheLock) {
                typeCache[clazz] = instanceProvider
            }
        }
    }

    private inner class TopLevelTypeCachingProvider : RequiringInstanceProvider {
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

                return getInstance(clazz, ::onFound)
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

    override fun <T : Any> get(clazz: KClass<out T>): T {
        return when (caching) {
            CacheLevel.AGGRESSIVE -> aggressiveCachingProvider[clazz]
            CacheLevel.TOP_LEVEL -> topLevelCachingProvider[clazz]
            CacheLevel.NONE -> getInstance(clazz, null)
        }
    }

    private fun <T : Any> getInstance(clazz: KClass<out T>, onFound: ((clazz: KClass<*>, provider: InstanceProvider) -> Unit)?): T {
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
                    crumbs -= crumb
                    return it
                }
            } catch (ignore: ClassInstanceNotFoundException) {
                // we ignore these and just check others
            } catch (t: Throwable) {
                errorHandler.onError(
                    t,
                    "Instance Provider",
                    "The instance provider $provider threw an exception when trying to get type $clazz."
                )
            }
            crumbs -= crumb
        }

        if (clazz.isSubclassOf<InstanceProvider>()) {
            iterator().asSequence().firstOrNull { it::class.isSubclassOf(clazz) }?.let {
                @Suppress("UNCHECKED_CAST")
                return it as T
            }
        }

        throw ClassInstanceNotFoundException(clazz)
    }
}
