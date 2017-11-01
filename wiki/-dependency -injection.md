[gh-pages](../index.md) / [wiki](index.md) / [Dependency Injection](.)

# Dependency Injection

`object Dependency Injection` [(source)](https://github.com/NextFaze/dev-fun/tree/master/dokka/src/main/java/wiki/DependencyInjection.kt#L114)

DevFun supports a rudimentary form of dependency injection using an [InstanceProvider](../com.nextfaze.devfun.inject/-instance-provider/index.md) (a
[CompositeInstanceProvider](../com.nextfaze.devfun.inject/-composite-instance-provider/index.md) at [DevFun.instanceProviders](../com.nextfaze.devfun.core/-dev-fun/instance-providers.md)).

Dependency injection is used for:

* The receiver object - i.e. the object the function will be called upon `injectMe.someFun()`
* Parameters of the calling functions; `myActivity.updateSomething(injectThis: SomeObject, andThis: AnotherType)`
* Construction of objects annotated [Constructable](../com.nextfaze.devfun.inject/-constructable/index.md) (see [below](#Constructable)
* A [FunctionTransformer](../com.nextfaze.devfun.core/-function-transformer/index.md) can inject/supply dynamically generated argument values *(see demo [AuthenticateFragment.signInAs](https://github.com/NextFaze/dev-fun/blob/master/demo/src/main/java/com/nextfaze/devfun/demo/AuthenticateScreen.kt#L225))*
* Effectively any time something in DevFun requests an object of some type - in general nothing in DevFun is static
(except for the occasional `object`, but even then that is usually an implementation and uses DI).

# Instance Providers

At runtime when an instance of an object is requested, [DevFun.instanceProviders](../com.nextfaze.devfun.core/-dev-fun/instance-providers.md) (a [CompositeInstanceProvider](../com.nextfaze.devfun.inject/-composite-instance-provider/index.md)
loops through requesting an instance, starting from the *most recently added* (to allow users to add their own to
be checked first). If the type cannot be provided, a [ClassInstanceNotFoundException](../com.nextfaze.devfun.inject/-class-instance-not-found-exception/index.md) is thrown (will not crash the
app - but it will be logged, and depending on the [DevFunModule](../com.nextfaze.devfun.core/-dev-fun-module/index.md) implementation will show an error).

Unless otherwise stated, each instance provider does a top-down search, using [Class.isAssignableFrom](https://developer.android.com/reference/java/lang/Class.html#isAssignableFrom(java.lang.Class<?>)). Most of the
time this is sufficient - this could fail when complex inheritance hierarchies are used.

## Inject Modules

At present only Dagger 2.x is explicitly supported ([InjectFromDagger2](../com.nextfaze.devfun.inject.dagger2/-inject-from-dagger2/index.md)). It is intended for Dagger 1.x and Guice to be
supported eventually.

In short, it is a module that adds an instance provider. Your application (and its supers) are searched for any
object that has an `@Component` annotation. This instance is then traversed for type providers or modules that
provide the type. This may fail or be insufficient if your application has non-trivial object graphs.

See [InjectFromDagger2](../com.nextfaze.devfun.inject.dagger2/-inject-from-dagger2/index.md) for more details and limitations, or for details and examples on implementing your own.

*Due to limitations in KAPT it is not possible to generate Kotlin code that would then generate dagger bindings.Once this has been resolved it should be possible to provide a more graceful implementation.*

## User Supplied Providers

Any object implementing [InstanceProvider](../com.nextfaze.devfun.inject/-instance-provider/index.md) can be used. They can be added/removed at any time.

Add to [DevFun.instanceProviders](../com.nextfaze.devfun.core/-dev-fun/instance-providers.md) (Remove using `-=`.);

``` kotlin
devFun.instanceProviders += myInstanceProvider
```

A utility function [captureInstance](../com.nextfaze.devfun.inject/capture-instance.md) can be used to quickly create an instance provider for a single type (creates
a [CapturingInstanceProvider](../com.nextfaze.devfun.inject/-capturing-instance-provider/index.md)). *Be aware of leaks! The lambda could implicitly hold a local `this` reference.*
e.g.

``` kotlin
class SomeType : BaseType

val provider = captureInstance { someObject.someType } // triggers for SomeType or BaseType
```

If you want to reduce the type range then specify its base type manually:

``` kotlin
val provider = captureInstance<BaseType> { someObject.someType } // triggers only for BaseType
```

See [DevFun.initialize](../com.nextfaze.devfun.core/-dev-fun/initialize.md) for a source example. Other uses can be found throughout most module sources.

## Default Providers

A number of providers are added by default, including all DevFun related types (modules, providers, etc.), some
android related types (`AndroidInstanceProvider`), and support for Kotlin `object` types ([KObjectInstanceProvider](../com.nextfaze.devfun.inject/-k-object-instance-provider/index.md)).

### Android

Standard Android types are provided by `AndroidInstanceProvider`, which includes [Application](https://developer.android.com/reference/android/app/Application.html), [Activity](https://developer.android.com/reference/android/app/Activity.html),
[Context](https://developer.android.com/reference/android/content/Context.html), [Fragment](#), and [View](https://developer.android.com/reference/android/view/View.html).

**At present only *support* fragments are supported.**

* When [Context](https://developer.android.com/reference/android/content/Context.html) is requested the current activity is returned (if not `null`/destroyed), else the application.

* When a view type is requested the view hierarchy is traversed, starting with the current activity (children of
[android.R.id.content](https://developer.android.com/reference/android/R/id.html#content)), and then each fragment [Fragment.getView](#) *(if the activity is a **support** [FragmentActivity](#))*.

### Kotlin `object`

This is the second to last provider as it's not reliable when the class is not public.

* If the class is public, [KClass.objectInstance](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/object-instance.html) is used.
* Otherwise a private static field `INSTANCE` is used. *(todo - might be possible to check Kotlin metadata to ensure it's a proper `object`?)*

### Constructable

This is the last provider to be checked, which *can* construct objects and inject constructors (using [DevFun.instanceProviders](../com.nextfaze.devfun.core/-dev-fun/instance-providers.md)).

In general you should use your own instance provider (or use one of the `inject-` modules, see [Components](-components.md)),
but for quick and dirty testing you can specify the type as [Constructable](../com.nextfaze.devfun.inject/-constructable/index.md). This tells the [ConstructingInstanceProvider](../com.nextfaze.devfun.inject/-constructing-instance-provider/index.md)
that it's OK to create a new instance. The class can only have *one* constructor. Any arguments will be requested
using [DevFun.instanceProviders](../com.nextfaze.devfun.core/-dev-fun/instance-providers.md). *(todo - allow `@Constructable` on constructors?)*

This process is opt-in (i.e. `@Constructable`) to avoid issues where incorrect instance provider chains attempt to
create new instances of something that should've been injected. The inject modules are simple and limited (at the
moment) thus it is preferred to fail rather than silently creating new instances.

