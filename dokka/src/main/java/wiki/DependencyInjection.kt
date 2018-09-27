@file:Suppress("IllegalIdentifier", "ClassName", "unused")

package wiki

import android.app.Activity
import android.app.Application
import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.nextfaze.devfun.annotations.Dagger2Component
import com.nextfaze.devfun.core.DevFun
import com.nextfaze.devfun.core.DevFunModule
import com.nextfaze.devfun.core.FunctionTransformer
import com.nextfaze.devfun.inject.*
import com.nextfaze.devfun.inject.dagger2.InjectFromDagger2
import kotlin.reflect.KClass

/**
 * DevFun supports a rudimentary form of dependency injection using an [InstanceProvider] (a
 * [CompositeInstanceProvider] at [DevFun.instanceProviders]).
 *
 * Dependency injection is used for:
 * - The receiver object - i.e. the object the function will be called upon `injectMe.someFun()`
 * - Parameters of the calling functions; `myActivity.updateSomething(injectThis: SomeObject, andThis: AnotherType)`
 * - Construction of objects annotated [Constructable] (see [below](#Constructable)
 * - A [FunctionTransformer] can inject/supply dynamically generated argument values *(see demo [AuthenticateFragment.signInAs](https://github.com/NextFaze/dev-fun/blob/master/demo/src/main/java/com/nextfaze/devfun/demo/AuthenticateScreen.kt#L225))*
 * - Effectively any time something in DevFun requests an object of some type - in general nothing in DevFun is static
 * (except for the occasional `object`, but even then that is usually an implementation and uses DI).
 *
 *
 * # Instance Providers
 * At runtime when an instance of an object is requested, [DevFun.instanceProviders] (a [CompositeInstanceProvider]
 * loops through requesting an instance, starting from the _most recently added_ (to allow users to add their own to
 * be checked first). If the type cannot be provided, a [ClassInstanceNotFoundException] is thrown (will not crash the
 * app - but it will be logged, and depending on the [DevFunModule] implementation will show an error).
 *
 * Unless otherwise stated, each instance provider does a top-down search, using [Class.isAssignableFrom]. Most of the
 * time this is sufficient - this could fail when complex inheritance hierarchies are used.
 *
 *
 * ## Inject Modules
 * At present only Dagger 2.x is explicitly supported ([InjectFromDagger2]). It is intended for Dagger 1.x and Guice to be
 * supported eventually.
 *
 * In short, it is a module that adds an instance provider. Your application (and its supers) are searched for any
 * object that has an `@Component` annotation. This instance is then traversed for type providers or modules that
 * provide the type. This may fail or be insufficient if your application has non-trivial object graphs.
 *
 * The annotation [Dagger2Component] can also be used to tell DevFun how to get your components.
 *
 * See [InjectFromDagger2] for more details and limitations, or for details and examples on implementing your own.
 *
 * _Due to limitations in KAPT it is not possible to generate Kotlin code that would then generate dagger bindings.
 * Once this has been resolved it should be possible to provide a more graceful implementation._
 *
 *
 * ## User Supplied Providers
 * Any object implementing [InstanceProvider] can be used. They can be added/removed at any time.
 *
 * Add to [DevFun.instanceProviders] (Remove using `-=`.);
 * ```kotlin
 * devFun.instanceProviders += myInstanceProvider
 * ```
 *
 * A utility function [captureInstance] can be used to quickly create an instance provider for a single type (creates
 * a [CapturingInstanceProvider]). _Be aware of leaks! The lambda could implicitly hold a local `this` reference._
 * e.g.
 * ```kotlin
 * class SomeType : BaseType
 *
 * val provider = captureInstance { someObject.someType } // triggers for SomeType or BaseType
 * ```
 *
 * If you want to reduce the type range then specify its base type manually:
 * ```kotlin
 * val provider = captureInstance<BaseType> { someObject.someType } // triggers only for BaseType
 * ```
 *
 * See [DevFun.initialize] for a source example. Other uses can be found throughout most module sources.
 *
 *
 * ## Default Providers
 * A number of providers are added by default, including all DevFun related types (modules, providers, etc.), some
 * android related types (`AndroidInstanceProvider`), and support for Kotlin `object` types ([KObjectInstanceProvider]).
 *
 *
 * ### Android
 * Standard Android types are provided by `AndroidInstanceProvider`, which includes [Application], [Activity],
 * [Context], [Fragment], and [View].
 *
 * __At present only *support* fragments are supported.__
 *
 * - When [Context] is requested the current activity is returned (if not `null`/destroyed), else the application.
 *
 * - When a view type is requested the view hierarchy is traversed, starting with the current activity (children of
 * [android.R.id.content]), and then each fragment [Fragment.getView] _(if the activity is a __support__ [FragmentActivity])_.
 *
 *
 * ### Kotlin `object`
 * This is the second to last provider as it's not reliable when the class is not public.
 *
 * - If the class is public, [KClass.objectInstance] is used.
 * - Otherwise a private static field `INSTANCE` is used. _(todo - might be possible to check Kotlin metadata to ensure it's a proper `object`?)_
 *
 *
 * ### Constructable
 * This is the last provider to be checked, which _can_ construct objects and inject constructors (using [DevFun.instanceProviders]).
 *
 * In general you should use your own instance provider (or use one of the `inject-` modules, see [Components]),
 * but for quick and dirty testing you can specify the type as [Constructable]. This tells the [ConstructingInstanceProvider]
 * that it's OK to create a new instance. The class can only have _one_ constructor. Any arguments will be requested
 * using [DevFun.instanceProviders]. _(todo - allow `@Constructable` on constructors?)_
 *
 * This process is opt-in (i.e. `@Constructable`) to avoid issues where incorrect instance provider chains attempt to
 * create new instances of something that should've been injected. The inject modules are simple and limited (at the
 * moment) thus it is preferred to fail rather than silently creating new instances.
 */
object `Dependency Injection`
