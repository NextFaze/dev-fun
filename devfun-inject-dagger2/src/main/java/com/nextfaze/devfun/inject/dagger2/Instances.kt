package com.nextfaze.devfun.inject.dagger2

import android.app.Activity
import android.app.Application
import android.app.Fragment
import android.content.Context
import com.google.auto.service.AutoService
import com.nextfaze.devfun.core.*
import com.nextfaze.devfun.inject.InstanceProvider
import com.nextfaze.devfun.internal.*
import dagger.Component
import dagger.Module
import dagger.Provides
import java.lang.RuntimeException
import java.lang.reflect.AnnotatedElement
import java.lang.reflect.ParameterizedType
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

private val log = logger("${BuildConfig.APPLICATION_ID}.Instances")

/**
 * Flag to indicate if the default heavy-reflection based Dagger 2 injector should be used.
 *
 * For small/simple projects the default would probably be fine, but for larger projects the reflection may take its toll.
 * For implementing your own (slightly more efficient) see the demo project `DemoInstanceProvider`.
 *
 * This value can be disabled at any time - it can not be re-enabled without reinitializing DevFun.
 *
 * @see InjectFromDagger2
 * @see <a href="https://github.com/NextFaze/dev-fun/tree/master/demo/src/debug/java/com/nextfaze/devfun/demo/devfun/DevFun.kt#L29">DemoInstanceProvider</a>
 */
var useAutomaticDagger2Injector = true
    set(value) {
        field = value
        if (!value && isDevFunInitialized) {
            devFun.get<InjectFromDagger2>().dispose()
        }
    }

/**
 * Helper function to be used on Dagger 2.x [Component] implementations.
 *
 * Will traverse the component providers and modules for an instance type matching [clazz] - scoping is not considered.
 */
fun <T : Any> tryGetInstanceFromComponent(component: Any, clazz: KClass<T>): T? {
    // Get from Provider
    log.t { "Trying to get $clazz from $component providers..." }
    component::class.java.declaredFields
        .firstOrNull {
            it.type == Provider::class.java && (
                    (it.genericType as? ParameterizedType)?.let { it.actualTypeArguments[0] == clazz.java } ?: false ||
                            it.name.toLowerCase() == "${clazz.java.simpleName}Provider".toLowerCase() // raw types
                    )
        }
        ?.apply { isAccessible = true }
        ?.get(component)
        ?.also {
            @Suppress("UNCHECKED_CAST")
            return (it as Provider<T>).get()
        }

    // Get from Component getter (@Inject on constructor, but not @Singleton types)
    log.t { "Trying to get $clazz from $component getters..." }
    component::class.java.declaredMethods
        .firstOrNull { it.name.startsWith("get") && it.returnType == clazz.java }
        ?.apply { isAccessible = true }
        ?.also {
            @Suppress("UNCHECKED_CAST")
            return it.invoke(component) as T
        }

    // Get from Module @Provides
    log.t { "Trying to get $clazz from $component modules..." }
    component::class.java.declaredFields
        .filter { it.type.hasAnnotation<Module>() }
        .forEach { f ->
            log.t { "Checking module ${f.type.name}..." }
            f.type.declaredMethods
                .firstOrNull {
                    log.t { "it.returnType=${it.returnType} == clazz> ${it.returnType == clazz.java}, annotated=${it.hasAnnotation<Provides>()}" }
                    it.returnType == clazz.java && it.hasAnnotation<Provides>()
                }
                ?.let {
                    f.isAccessible = true
                    @Suppress("UNCHECKED_CAST")
                    return it.invoke(f.get(component)) as T
                }
        }

    // Not found in this component
    return null
}

/*
 * private class DemoInstanceProvider(private val application: Application, private val activityProvider: ActivityProvider) : InstanceProvider {
 *     private val applicationComponent by lazy { application.applicationComponent!! }
 * 
 *     override fun <T : Any> get(clazz: KClass<out T>): T? {
 *         tryGetInstanceFromComponent(applicationComponent, clazz)?.let { return it }
 * 
 *         activityProvider()?.let { activity ->
 *             if (activity is DaggerActivity) {
 *                 tryGetInstanceFromComponent(activity.retainedComponent, clazz)?.let { return it }
 *                 tryGetInstanceFromComponent(activity.activityComponent, clazz)?.let { return it }
 *             }
 *         }
 * 
 *         return null
 *     }
 * }
 */
/**
 * This module adds rudimentary support for searching Dagger 2.x component graphs for object instances.
 *
 * _Due to limitations in KAPT it is not possible to generate Kotlin code that would then generate dagger bindings.
 * Once this has been resolved it should be possible to resolve this more gracefully._
 *
 * On [DevFunModule.initialize], your application (and its subclasses) are searched for a [Component]. This is assumed
 * to be your top-level (singleton scoped) dagger component. An instance provider is then added referencing this instance.
 *
 * At runtime (upon [InstanceProvider.get]) the component is traversed in a top-down fashion for a [Provider] or [Module]
 * that [Provides] the requested type _(or subclasses the type)_.
 *
 * __This has not been tested extensively beyond simple object graphs!__
 *
 *
 * ### Complex Object Graphs
 * A simple object graph is one that is linear; [Singleton] -> [Activity] -> [Fragment] -> etc., or one that only ever
 * has a single type of some scope active at once. i.e. for the example below `MyChildScope1` and `MyChildScope2` - if
 * only one of the `ChildScope` is active at a time. It should also work for branched scopes _as long as the requested type is
 * effectively unique across scopes_.
 *
 * An example where this may fail:
 *
 * ![Dagger 2 Scopes](https://github.com/NextFaze/dev-fun/raw/gh-pages/assets/uml/dagger2-scopes.png)
 *
 * - Attempting to get an instance of the `PerScopeObject` will return the __first encountered__ (which could be from
 * either scope depending on what the standard Java reflection API returns - _which explicitly states order is undefined_).
 *
 * - Requesting `SomeFactory` or `SomeOtherObject` should work as expected since they are both unique.
 *
 * If your dependency graph is too complicated you will need to provide your own instance provider with customized behaviour.
 *
 * _I am looking into better ways to support this - suggestions/PRs welcomed._
 *
 *
 * ### Custom Provider
 * A helper function [tryGetInstanceFromComponent] can be used to reduce the search scope.
 *
 * See [DemoInstanceProvider](https://github.com/NextFaze/dev-fun/tree/master/demo/src/debug/java/com/nextfaze/devfun/demo/devfun/DevFun.kt#L33) for this in use.
 *
 * ```kotlin
 * private class DemoInstanceProvider(private val application: Application, private val activityProvider: ActivityProvider) : InstanceProvider {
 *     private val applicationComponent by lazy { application.applicationComponent!! }
 *
 *     override fun <T : Any> get(clazz: KClass<out T>): T? {
 *         tryGetInstanceFromComponent(applicationComponent, clazz)?.let { return it }
 *
 *         activityProvider()?.let { activity ->
 *             if (activity is DaggerActivity) {
 *                 tryGetInstanceFromComponent(activity.retainedComponent, clazz)?.let { return it }
 *                 tryGetInstanceFromComponent(activity.activityComponent, clazz)?.let { return it }
 *             }
 *         }
 *
 *         return null
 *     }
 * }
 * ```
 *
 * When supplying your own instance provider (but want to use [tryGetInstanceFromComponent]), set
 * [useAutomaticDagger2Injector] to `false` to disable the default instance provider.
 *
 * Add/remove providers using [DevFun.instanceProviders].
 */
@AutoService(DevFunModule::class)
class InjectFromDagger2 : AbstractDevFunModule() {
    private var instanceProvider: InstanceProvider? = null

    override fun init(context: Context) {
        val application = context.applicationContext as Application
        devFun.instanceProviders += Dagger2InstanceProvider(application, get()).also { instanceProvider = it }
    }

    override fun dispose() {
        instanceProvider?.let {
            devFun.instanceProviders -= it
            instanceProvider = null
        }
    }
}

private class Dagger2InstanceProvider(private val app: Application, private val activityProvider: ActivityProvider) : InstanceProvider {
    private val log = logger()
    private var applicationComponents: List<Any>? = null

    override fun <T : Any> get(clazz: KClass<out T>): T? {
        if (!useAutomaticDagger2Injector) return null
        resolveApplicationComponents()

        applicationComponents?.forEach {
            tryGetInstanceFromComponent(it, clazz)?.let { return it }
        }

        activityProvider()?.let { activity ->
            tryGetComponents(activity, required = false).forEach {
                tryGetInstanceFromComponent(it, clazz)?.let { return it }
            }
        }

        return null
    }

    private fun resolveApplicationComponents() {
        if (applicationComponents != null) return

        applicationComponents = try {
            tryGetComponents(app, required = true)
        } catch (t: PossiblyUninitializedException) {
            log.i(t) {
                """
                |Automatic Dagger 2.x instance provider failed to locate any application components.
                |However lazy uninitialized fields were encountered and additional checks will be made.
                |
                |If you see this message after Dagger 2.x has been initialized then automatic detection failed!
                |
                |For manual implementation see:
                |https://github.com/NextFaze/dev-fun/tree/master/demo/src/debug/java/com/nextfaze/devfun/demo/devfun/DevFun.kt#L29""".trimMargin()
            }
            null
        } catch (t: Throwable) {
            log.e(t) {
                """
                |Automatic Dagger 2.x instance provider failed to locate any application components.
                |
                |For manual implementation see:
                |https://github.com/NextFaze/dev-fun/tree/master/demo/src/debug/java/com/nextfaze/devfun/demo/devfun/DevFun.kt#L29""".trimMargin()
            }
            emptyList()
        }
        log.d { "Using Dagger2InstanceProvider with $applicationComponents" }
    }
}

private val ANY_CLASS = Any::class.java
private fun tryGetComponents(instance: Any, required: Boolean): List<Any> {
    var foundLazy = false
    val components = mutableListOf<Any>()
    var objClass: Class<*> = instance::class.java
    while (objClass != ANY_CLASS) {
        components.addAll(
            objClass.declaredFields.filter {
                it.type.hasAnnotation<Component>() || it.type.interfaces.any { it.hasAnnotation<Component>() }
            }.mapNotNull {
                it.isAccessible = true
                it.get(instance)
            }
        )

        components.addAll(
            objClass.declaredFields.filter {
                it.name.endsWith("\$delegate") && Lazy::class.java.isAssignableFrom(it.type)
            }.map {
                it.isAccessible = true
                val delegatedField = it.get(instance) as Lazy<*>
                if (delegatedField.isInitialized()) {
                    delegatedField.value?.run {
                        if (javaClass.hasAnnotation<Component>() || javaClass.interfaces.any { it.hasAnnotation<Component>() }) {
                            return@map this
                        }
                    }
                } else {
                    foundLazy = true
                }
                return@map null
            }.filterNotNull()
        )

        if (components.isNotEmpty()) {
            return components
        }

        objClass = objClass.superclass
    }
    when {
        required && !foundLazy -> throw RuntimeException("Failed to find field with type annotated with @Component")
        required && foundLazy -> throw PossiblyUninitializedException()
        else -> return components
    }
}

private inline fun <reified T : Annotation> AnnotatedElement.hasAnnotation() = isAnnotationPresent(T::class.java)

private class PossiblyUninitializedException : Throwable("Failed to find components but encountered lazy uninitialized fields.")
