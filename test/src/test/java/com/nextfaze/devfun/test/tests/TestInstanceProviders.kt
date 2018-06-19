package com.nextfaze.devfun.test.tests

import com.nextfaze.devfun.core.FunctionItem
import com.nextfaze.devfun.core.devFunVerbose
import com.nextfaze.devfun.error.ErrorDetails
import com.nextfaze.devfun.error.ErrorHandler
import com.nextfaze.devfun.inject.*
import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.test.PrimitivesInstanceProvider
import com.nextfaze.devfun.test.SimpleTypesInstanceProvider
import org.testng.annotations.Test
import tested.instance_providers.classes
import kotlin.reflect.KClass
import kotlin.test.assertNotNull

@Test(groups = ["instanceProvider", "supported"])
class TestInstanceProviders {
    private val log = logger()

    fun testKObjectVisibilities() {
        val kObjectProvider = KObjectInstanceProvider()
        classes.forEach {
            assertNotNull(kObjectProvider[it]) { "$it was null!" }
        }
    }

    @Suppress("unused", "UNCHECKED_CAST")
    fun testRecursion() {
        class ClassProvidedByHigherLevelProvider

        class RecursiveInstanceProvider(private val root: RequiringInstanceProvider) : InstanceProvider {
            override fun <T : Any> get(clazz: KClass<out T>): T? {

                if (clazz == ClassProvidedByHigherLevelProvider::class) {
                    return root[ClassProvidedByHigherLevelProvider::class] as T
                }

                return null
            }
        }

        val instanceProviders = createDefaultCompositeInstanceProvider().apply {
            this += ConstructingInstanceProvider(this)
            this += KObjectInstanceProvider()
            this += captureInstance<InstanceProvider> { this }
            this += PrimitivesInstanceProvider()
            this += SimpleTypesInstanceProvider()

            this += singletonInstance { ClassProvidedByHigherLevelProvider() }
            this += RecursiveInstanceProvider(this)
        }

        assertNotNull(instanceProviders[ClassProvidedByHigherLevelProvider::class]) { "ClassProvidedByHigherLevelProvider was null!" }
    }

    @Suppress("unused", "UNCHECKED_CAST")
    fun testExceptionRemovesProvider() {
        val wasVerbose = devFunVerbose
        devFunVerbose = true

        class ClassProvidedByHigherLevelProvider
        class SomethingOnlyProvidedByBadProvider

        class BadInstanceProvider : InstanceProvider {
            override fun <T : Any> get(clazz: KClass<out T>): T? =
                when (clazz) {
                    SomethingOnlyProvidedByBadProvider::class -> SomethingOnlyProvidedByBadProvider() as T
                    ClassProvidedByHigherLevelProvider::class -> throw RuntimeException("Some exception")
                    else -> null
                }
        }

        /**
         * The composite instance provider fetches the error handler when a child provider throws an
         * exception other than [ClassInstanceNotFoundException]. However if the higher level provider (i.e. the more
         * recently added `BadInstanceProvider`) continues to throw exceptions then we get stuck in an infinite loop
         * trying to fetch/log the error handler.
         */
        class ErrorHandlerImpl(private val classProvidedByHigherLevelProvider: ClassProvidedByHigherLevelProvider) : ErrorHandler {
            override fun onWarn(title: CharSequence, body: CharSequence) = Unit
            override fun onError(t: Throwable, title: CharSequence, body: CharSequence, functionItem: FunctionItem?) = Unit
            override fun onError(error: ErrorDetails) = Unit
            override fun markSeen(key: Any) = Unit
            override fun remove(key: Any) = Unit
            override fun clearAll() = Unit
        }

        val instanceProviders = createDefaultCompositeInstanceProvider().apply {
            this += ConstructingInstanceProvider(this)
            this += KObjectInstanceProvider()
            this += captureInstance<InstanceProvider> { this }
            this += PrimitivesInstanceProvider()
            this += SimpleTypesInstanceProvider()

            this += singletonInstance { ErrorHandlerImpl(get(ClassProvidedByHigherLevelProvider::class)) }
            this += singletonInstance { ClassProvidedByHigherLevelProvider() }
            this += BadInstanceProvider()
        }

        // get from bad provider works fine
        log.i { "Get SomethingOnlyProvidedByBadProvider" }
        assertNotNull(instanceProviders[SomethingOnlyProvidedByBadProvider::class]) { "SomethingOnlyProvidedByBadProvider was null!" }

        // get something from bad provider - fails - continues up provider tree
        log.i { "Get ClassProvidedByHigherLevelProvider" }
        assertNotNull(instanceProviders[ClassProvidedByHigherLevelProvider::class]) { "ClassProvidedByHigherLevelProvider was null!" }

        // get from bad provider again - works fine still
        log.i { "Get SomethingOnlyProvidedByBadProvider again" }
        assertNotNull(instanceProviders[SomethingOnlyProvidedByBadProvider::class]) { "ClassProvidedByHigherLevelProvider was null!" }

        devFunVerbose = wasVerbose
    }
}
