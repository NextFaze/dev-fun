package com.nextfaze.devfun.inject.dagger2

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.app.Fragment
import android.content.Context
import android.text.SpannableStringBuilder
import android.view.View
import androidx.fragment.app.FragmentActivity
import com.google.auto.service.AutoService
import com.nextfaze.devfun.annotations.*
import com.nextfaze.devfun.core.*
import com.nextfaze.devfun.inject.ConstructingInstanceProvider
import com.nextfaze.devfun.inject.InstanceProvider
import com.nextfaze.devfun.inject.isSubclassOf
import com.nextfaze.devfun.internal.android.*
import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.internal.reflect.*
import com.nextfaze.devfun.internal.string.*
import com.nextfaze.devfun.invoke.parameterInstances
import com.nextfaze.devfun.invoke.receiverInstance
import dagger.Component
import dagger.Module
import dagger.Provides
import java.lang.reflect.*
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Scope
import javax.inject.Singleton
import kotlin.LazyThreadSafetyMode.NONE
import kotlin.reflect.KClass

private val log = logger("${BuildConfig.APPLICATION_ID}.instances")

/**
 * Flag to indicate if the default heavy-reflection based Dagger 2 injector should be used.
 *
 * For small/simple projects the default would probably be fine, but for larger projects the reflection may take its toll.
 * For implementing your own (slightly more efficient) see the demo project `DemoInstanceProvider`.
 *
 * This value can be disabled at any time - it can not be re-enabled without reinitializing DevFun.
 *
 * Alternatively use `@Dagger2Component` on your functions/properties (or `@get:Dagger2Component` for property getters)
 * that return components to tell DevFun where to find them (they can be whatever/where ever; static, in your app class,
 * activity class, etc).
 *
 * @see InjectFromDagger2
 * @see Dagger2Component
 * @see <a href="https://github.com/NextFaze/dev-fun/tree/master/demo/src/debug/java/com/nextfaze/devfun/demo/devfun/DevFun.kt#L52">DemoInstanceProvider</a>
 */
var useAutomaticDagger2Injector = true
    set(value) {
        field = value
        if (!value && isDevFunInitialized) {
            devFun.get<InjectFromDagger2>().dispose()
        }
    }

private data class ProviderField(val field: Field) {
    val genericType: Type = field.genericType
    val providerFieldName by lazy(NONE) { field.name.removeSuffix("Provider").toLowerCase() }
}

private val providerFields = mutableMapOf<Field, ProviderField>()

/**
 * Helper function to be used on Dagger 2.x [Component] implementations.
 *
 * Will traverse the component providers and modules for an instance type matching [clazz] - scoping is not considered.
 *
 * This function delegates to [tryGetInstanceFromComponentCache] and [tryGetInstanceFromComponentReflection]. If you are having issues with
 * new instances being created when they shouldn't be, ensure your `@Scope` annotations are `@Retention(RUNTIME)`.
 *
 * Alternatively use `@Dagger2Component` on your functions/properties (or `@get:Dagger2Component` for property getters)
 * that return components to tell DevFun where to find them (they can be whatever/where ever; static, in your app class,
 * activity class, etc) - which will end up using this method anyway.
 *
 * @see Dagger2Component
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any> tryGetInstanceFromComponent(component: Any, clazz: KClass<T>, cacheResolvedTypes: Boolean = true): T? {
    // We need to check component via resolve cache first as a scope may not be RUNTIME retained (in which case even though it's scoped, it
    // will be seen as not scoped and could be incorrectly instantiated below)
    if (cacheResolvedTypes) {
        tryGetInstanceFromComponentCache(component, clazz)?.let { return it }
    }

    return tryGetInstanceFromComponentReflection(component, clazz)
}

/**
 * Helper function to be used on Dagger 2.x [Component] implementations.
 *
 * Will traverse the component providers and modules for an instance type matching [clazz] - scoping is not considered.
 *
 * You should use this before [tryGetInstanceFromComponentReflection] as the reflection method may create new instances instead of reusing
 * them due to scoping limitations. This can be avoided to some degree if your `@Scope` annotations are `@Retention(RUNTIME)`.
 *
 * Rather then using this function directly you can use [tryGetInstanceFromComponent] which tries this first then the reflection method.
 *
 * Alternatively use `@Dagger2Component` on your functions/properties (or `@get:Dagger2Component` for property getters)
 * that return components to tell DevFun where to find them (they can be whatever/where ever; static, in your app class,
 * activity class, etc) - which will end up using this method anyway.
 *
 * @see Dagger2Component
 */
fun <T : Any> tryGetInstanceFromComponentCache(component: Any, clazz: KClass<T>): T? =
    resolvedComponents.getOrPut(component::class) { ResolvedComponent(component::class) }.getInstance(component, clazz)

/**
 * Helper function to be used on Dagger 2.x [Component] implementations.
 *
 * Will traverse the component providers and modules for an instance type matching [clazz] - scoping is not considered.
 *
 * You should use [tryGetInstanceFromComponentCache] before this method may create new instances instead of reusing
 * them due to scoping limitations. This can be avoided to some degree if your `@Scope` annotations are `@Retention(RUNTIME)`.
 *
 * Rather then using this function directly you can use [tryGetInstanceFromComponent] which tries this first then the reflection method.
 *
 * Alternatively use `@Dagger2Component` on your functions/properties (or `@get:Dagger2Component` for property getters)
 * that return components to tell DevFun where to find them (they can be whatever/where ever; static, in your app class,
 * activity class, etc) - which will end up using this method anyway.
 *
 * @see Dagger2Component
 */
@Suppress("UNCHECKED_CAST")
fun <T : Any> tryGetInstanceFromComponentReflection(component: Any, clazz: KClass<T>): T? {
    // Special case - a non-scoped no-args @Inject type will not have a provider or getter
    if (!clazz.isScoped) {
        val ctor = clazz.java.declaredConstructors.singleOrNull()
        if (ctor != null && ctor.isAnnotationPresent(Inject::class.java)) {
            if (ctor.parameterTypes.isEmpty()) {
                log.t { "$clazz is non-scoped no-args @Inject - creating new instance..." }
                return ctor.apply { isAccessible = true }.newInstance() as T
            } else if (isDagger212) {
                // Dagger 2.12 inlined types that didn't need a provider (non-scoped/single-instance types)
                // This was reversed/changed again in 2.13+
                //
                // So here we're making an assumption that an @Inject non-scoped *should* be constructed - this is not a valid assumption
                // for scopes that are not @Retention(RUNTIME). i.e. this would be the incorrect behaviour. However there's not much
                // else we can do about it (2.12 is getting old anyway and this is quite the edge case so meh.)
                log.d { "$clazz is non-scoped @Inject - Dagger version 2.12 detected - creating new instance..." }
                return devFun.get<ConstructingInstanceProvider>().get(clazz, false)
            }
        }
    }

    // Get from Provider
    log.t { "Trying to get $clazz from $component providers..." }
    val providerFieldName by lazy(NONE) { clazz.java.simpleName.toLowerCase() }
    component::class.java.declaredFields.forEach {
        if (it.type != Provider::class.java) return@forEach

        val providerField = providerFields.getOrPut(it) { ProviderField(it) }
        val genericType = providerField.genericType
        if (genericType is ParameterizedType) {
            if (genericType.actualTypeArguments[0] == clazz.java) {
                return it.apply { isAccessible = true }.get(component)?.let { (it as Provider<T>).get() }
            }
        }

        // raw types (package private non-singleton types)
        // since we can't know the type until we get it, we at least check the name matches first
        if (providerFieldName == providerField.providerFieldName) {
            try {
                val instance = it.apply { isAccessible = true }.get(component)?.let { (it as Provider<*>).get() }
                if (instance?.javaClass == clazz.java) {
                    return instance as T
                }
            } catch (t: Throwable) {
                log.d(t) { "Exception when trying to get $clazz from raw-type Provider $it (package private non-singleton type) - skipping provider." }
            }
        }
    }

    // Get from Component getter (@Inject on constructor, but not @Singleton types)
    log.t { "Trying to get $clazz from $component getters..." }
    component::class.java.declaredMethods
        .firstOrNull {
            (it.name.startsWith("get") && it.returnType == clazz.java) ||
                    (it.returnType == Any::class.java && it.name == "get${clazz.java.simpleName}")
        }
        ?.apply { isAccessible = true }
        ?.also { return it.invoke(component) as T }

    // Get from Module @Provides
    log.t { "Trying to get $clazz from $component modules..." }
    component::class.java.declaredFields
        .filter { it.type.hasAnnotation<Module>() }
        .forEach { f ->
            log.t { "Checking module ${f.type.name}..." }
            f.type.declaredMethods
                .firstOrNull {
                    log.t { "it.returnType=${it.returnType} == clazz (${it.returnType == clazz.java}), annotated=${it.hasAnnotation<Provides>()}" }
                    it.returnType == clazz.java && it.hasAnnotation<Provides>()
                }
                ?.let {
                    f.isAccessible = true
                    return it.invoke(f.get(component)) as T
                }
        }

    // Not found in this component
    return null
}

private val KClass<*>.isScoped get() = java.annotations.any { it.annotationClass.java.isAnnotationPresent(Scope::class.java) }

/**
 * This module adds rudimentary support for searching Dagger 2.x component graphs for object instances.
 *
 * _Due to limitations in KAPT it is not possible to generate Kotlin code that would then generate dagger bindings.
 * Once this has been resolved it should be possible to resolve this more gracefully._
 *
 * ### Automatic Reflection Based
 * On [DevFunModule.initialize], your application (and its subclasses) are searched for a [Component]. This is assumed
 * to be your top-level (singleton scoped) dagger component. An instance provider is then added referencing this instance.
 *
 * At runtime (upon [InstanceProvider.get]) the component is traversed in a top-down fashion for a [Provider] or [Module]
 * that [Provides] the requested type _(or subclasses the type)_.
 *
 * __This has not been tested extensively beyond simple object graphs!__
 *
 *
 * ### Annotation Based
 * Use [Dagger2Component] on functions/properties/getters that return components.
 *
 * Provides some level of support for manually specifying scopes (any/or attempts to guess them based on the context).
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
 *
 * @see Dagger2Component
 */
@DeveloperCategory("DevFun", group = "Instance Providers")
@AutoService(DevFunModule::class)
class InjectFromDagger2 : AbstractDevFunModule() {
    private val log = logger()

    private var instanceProvider: Dagger2InstanceProvider? = null

    override fun init(context: Context) {
        val application = context.applicationContext as Application
        val androidInstanceProvider = devFun.get<AndroidInstanceProviderInternal>()
        val provider = Dagger2AnnotatedInstanceProvider(devFun, androidInstanceProvider).takeIf { it.hasComponents }
                ?: Dagger2ReflectiveInstanceProvider(application, get(), androidInstanceProvider)
        devFun.instanceProviders += provider.also {
            log.d { "InjectFromDagger2 using instance provider $it" }
            instanceProvider = it
        }
    }

    override fun dispose() {
        instanceProvider?.let {
            devFun.instanceProviders -= it
            instanceProvider = null
        }
    }

    @DeveloperFunction("Dagger 2.x Provider Details")
    private fun providerDetails(activity: Activity) =
        SpannableStringBuilder().apply {
            this += "useAutomaticDagger2Injector: $useAutomaticDagger2Injector\n"
            this += "providerType: "
            this += pre("${instanceProvider?.let { it::class }}")
            this += "\nproviderDetails:\n"
            this += instanceProvider?.description() ?: "null"
        }.also {
            AlertDialog.Builder(activity)
                .setTitle("Inject From Dagger 2.x")
                .setMessage(it)
                .show()
        }
}

abstract class Dagger2InstanceProvider(
    val androidInstances: AndroidInstanceProviderInternal
) : InstanceProvider {
    @Suppress("MemberVisibilityCanBePrivate")
    var deferToAndroidInstanceProvider: Boolean = true

    override fun <T : Any> get(clazz: KClass<out T>) =
        when {
            deferToAndroidInstanceProvider -> androidInstances[clazz]
            else -> null
        }

    abstract fun description(): CharSequence
}

private class Dagger2AnnotatedInstanceProvider(
    private val devFun: DevFun,
    androidInstances: AndroidInstanceProviderInternal
) : Dagger2InstanceProvider(androidInstances) {
    private val log = logger()

    private data class ComponentReference(
        val annotation: Dagger2ComponentProperties,
        val method: Method,
        val scope: Int,
        val isActivityRequired: Boolean,
        val isFragmentActivityRequired: Boolean
    ) {
        private val toString by lazy {
            // render once for logging purposes
            """component {
                |  method: $method,
                |  annotation {
                |    scope: ${annotation.scope},
                |    priority: ${annotation.priority},
                |    isActivityRequired: ${annotation.isActivityRequired},
                |    isFragmentActivityRequired: ${annotation.isFragmentActivityRequired}
                |  },
                |  resolved {
                |    scope: $scope,
                |    isActivityRequired: $isActivityRequired,
                |    isFragmentActivityRequired: $isFragmentActivityRequired
                |  }
                |}""".trimMargin()
        }

        override fun toString() = toString
    }

    private val components: List<ComponentReference>

    val hasComponents get() = components.isNotEmpty()

    init {
        fun KClass<*>.toScope() =
            when {
                isSubclassOf<View>() -> Dagger2Scope.VIEW
                isSubclassOf<Activity>() -> Dagger2Scope.ACTIVITY
                isSubclassOf<Application>() -> Dagger2Scope.APPLICATION
                isSubclassOf<Context>() -> Dagger2Scope.APPLICATION
                else -> Dagger2Scope.UNDEFINED
            }

        components = devFun.developerReferences<Dagger2Component>()
            .also { log.d { "Dagger2Component references: $it" } }
            .filterIsInstance<MethodReference>()
            .map { ref ->
                val origMethod = ref.method
                val method = if (origMethod.name.endsWith("\$annotations")) {
                    // user has annotated a property
                    val getterName = "get${origMethod.name.substringBeforeLast('$').capitalize()}"
                    origMethod.declaringClass.getMethod(getterName, *origMethod.parameterTypes)
                } else {
                    origMethod
                }

                val annotation = ref.withProperties<Dagger2ComponentProperties>() ?: return@map null

                fun Dagger2Scope.toComponentReference() =
                    ComponentReference(annotation, method, ordinal, isActivityRequired, isFragmentActivityRequired)

                when {
                    annotation.scope != Dagger2Scope.UNDEFINED -> {
                        log.t { "@Dagger2Component $method has defined scope: $annotation" }
                        annotation.scope.toComponentReference()
                    }
                    annotation.priority != 0 -> {
                        log.t { "@Dagger2Component $method has custom scope: $annotation" }
                        ComponentReference(
                            annotation,
                            method,
                            annotation.priority,
                            annotation.isActivityRequired,
                            annotation.isFragmentActivityRequired
                        )
                    }
                    else -> bestGuess@ { // scope is UNDEFINED and priority is 0
                        if (method.isStatic) {
                            val receiverType = method.parameterTypes.firstOrNull()
                            when (receiverType) {
                                null -> Dagger2Scope.APPLICATION.toComponentReference()
                                else -> receiverType.kotlin.toScope().toComponentReference()
                            }.also {
                                log.t { "Assuming scope of $it for static $method as receiver type is $receiverType" }
                            }
                        } else {
                            val declaringClass = method.declaringClass.kotlin
                            declaringClass.toScope().toComponentReference().also {
                                log.t { "Assuming scope of $it for $method as receiver type is declaringClass is $declaringClass" }
                            }
                        }
                    }
                }.also {
                    log.t { "Found @Dagger2Component annotated reference:\n$it" }
                }
            }
            .filterNotNull()
            .sortedBy { it.scope }
        log.d {
            "Resolved & sorted component sources (lower scope # are checked first):\n${components.toString().replace(
                "ComponentReference",
                "\nComponentReference"
            )}"
        }
    }

    override fun <T : Any> get(clazz: KClass<out T>): T? {
        super.get(clazz)?.let {
            log.t { "Returning instance of $clazz from deferred Android provider" }
            return it
        }
        if (components.isEmpty()) return null

        val componentInstances = mutableListOf<Any>()

        // We need to check component via resolve cache first as a scope may not be RUNTIME retained (in which case even though it's scoped, it
        // will be seen as not scoped and could be incorrectly instantiated when using reflection)
        components.forEach { ref ->
            if (ref.isFragmentActivityRequired && androidInstances.activity !is FragmentActivity) {
                log.t { "Component out of scope - FragmentActivity required but not present: $ref" }
                return@forEach
            }
            if (ref.isActivityRequired && androidInstances.activity == null) {
                log.t { "Component out of scope - Activity required but not present: $ref" }
                return@forEach
            }

            try {
                log.t { "Check for $clazz from component reference $ref" }

                val receiver = when {
                    ref.method.isStatic -> null
                    else -> ref.method.receiverInstance(devFun.instanceProviders)
                }
                val args = ref.method.parameterInstances(devFun.instanceProviders)
                log.t { "Component receiver instance: $receiver, args: $args" }

                val component = when {
                    args != null -> ref.method.invoke(receiver, *args.toTypedArray())
                    else -> ref.method.invoke(receiver)
                } ?: return@forEach

                tryGetInstanceFromComponentCache(component, clazz)?.let { return it }
                componentInstances += component
            } catch (t: Throwable) {
                log.w(t) { "Component $ref threw $t (may be out of scope - check your configuration) - trying other providers." }
            }
        }

        // Now we try using on-the-fly introspection via reflection - much slower but can sometimes succeed for weird typing issues
        componentInstances.forEach { component ->
            try {
                tryGetInstanceFromComponentReflection(component, clazz)?.let { return it }
            } catch (t: Throwable) {
                log.w(t) { "Component $component threw $t (may be out of scope - check your configuration) - trying other providers." }
            }
        }

        return null
    }

    override fun description() =
        SpannableStringBuilder().apply {
            this += "Component References (lower scope # are checked first): \n\n"
            this += pre(components.joinToString(separator = "\n\n"))
        }
}

private class Dagger2ReflectiveInstanceProvider(
    private val app: Application,
    private val activityProvider: ActivityProvider,
    androidInstances: AndroidInstanceProviderInternal
) : Dagger2InstanceProvider(androidInstances) {
    private val log = logger()
    private var applicationComponents: List<Any>? = null

    override fun <T : Any> get(clazz: KClass<out T>): T? {
        super.get(clazz)?.let { return it }
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

    override fun description() = "applicationComponents=$applicationComponents"
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

internal inline fun <reified T : Annotation> AnnotatedElement.hasAnnotation() = isAnnotationPresent(T::class.java)

private class PossiblyUninitializedException : Throwable("Failed to find components but encountered lazy uninitialized fields.")
