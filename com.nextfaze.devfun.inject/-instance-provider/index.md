[gh-pages](../../index.md) / [com.nextfaze.devfun.inject](../index.md) / [InstanceProvider](./index.md)

# InstanceProvider

`interface InstanceProvider` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/inject/InstanceProvider.kt#L37)

Provides object instances for one or more types.

A rudimentary form of dependency injection is used throughout all of DevFun (not just for user-code function
invocation, but also between modules, definition and item processing, and anywhere else an object of some type is
desired - in general nothing in DevFun is static (except for the occasional `object`, but even then that is usually
an implementation and uses DI).

This process is facilitated by various instance providers - most of which is described at the wiki entry on
[Dependency Injection](https://nextfaze.github.io/dev-fun/wiki/-dependency%20-injection.html).

To quickly and simply provide a single object type, use [captureInstance](../capture-instance.md) or [singletonInstance](../singleton-instance.md), which creates a
[CapturingInstanceProvider](../-capturing-instance-provider/index.md) that can be added to the root (composite) instance provider at `DevFun.instanceProviders`.
e.g.

``` kotlin
class SomeType : BaseType

val provider = captureInstance { someObject.someType } // triggers for SomeType or BaseType
val singleInstance = singletonInstance { SomeType() } // triggers for SomeType or BaseType (result of invocation is saved)
```

If you want to reduce the type range then specify its base type manually:

``` kotlin
val provider = captureInstance<BaseType> { someObject.someType } // triggers only for BaseType
```

*Be aware of leaks! The lambda could implicitly hold a local `this` reference.*

**See Also**

[RequiringInstanceProvider](../-requiring-instance-provider/index.md)

### Functions

| Name | Summary |
|---|---|
| [get](get.md) | `abstract operator fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> get(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`T`](get.md#T)`>): `[`T`](get.md#T)`?`<br>Try to get an instance of some [clazz](get.md#com.nextfaze.devfun.inject.InstanceProvider$get(kotlin.reflect.KClass((com.nextfaze.devfun.inject.InstanceProvider.get.T)))/clazz). |

### Inheritors

| Name | Summary |
|---|---|
| [AndroidInstanceProviderInternal](../../com.nextfaze.devfun.internal.android/-android-instance-provider-internal/index.md) | `interface AndroidInstanceProviderInternal : `[`InstanceProvider`](./index.md) |
| [CapturingInstanceProvider](../-capturing-instance-provider/index.md) | `class CapturingInstanceProvider<out T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> : `[`InstanceProvider`](./index.md)<br>An instance provider that requests an instance of a class from a captured lambda. |
| [ConstructingInstanceProvider](../-constructing-instance-provider/index.md) | `class ConstructingInstanceProvider : `[`InstanceProvider`](./index.md)<br>Provides objects via instance construction. Type must be annotated with [Constructable](../-constructable/index.md). |
| [Dagger2InstanceProvider](../../com.nextfaze.devfun.inject.dagger2/-dagger2-instance-provider/index.md) | `abstract class Dagger2InstanceProvider : `[`InstanceProvider`](./index.md) |
| [KObjectInstanceProvider](../-k-object-instance-provider/index.md) | `class KObjectInstanceProvider : `[`InstanceProvider`](./index.md)<br>Handles Kotlin `object` and `companion object` types. |
| [RequiringInstanceProvider](../-requiring-instance-provider/index.md) | `interface RequiringInstanceProvider : `[`InstanceProvider`](./index.md)<br>Same as [InstanceProvider](./index.md), but throws [ClassInstanceNotFoundException](../-class-instance-not-found-exception/index.md) instead of returning `null`. |
