[gh-pages](../index.md) / [com.nextfaze.devfun.inject](./index.md)

## Package com.nextfaze.devfun.inject

### Types

| Name | Summary |
|---|---|
| [AndroidInstanceProvider](-android-instance-provider/index.md) | `interface AndroidInstanceProvider : `[`AndroidInstanceProviderInternal`](../com.nextfaze.devfun.internal.android/-android-instance-provider-internal/index.md) |
| [CacheLevel](-cache-level/index.md) | `enum class CacheLevel`<br>Controls how aggressively the [CompositeInstanceProvider](-composite-instance-provider.md) caches the sources of class instances. |
| [CapturingInstanceProvider](-capturing-instance-provider/index.md) | `class CapturingInstanceProvider<out T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> : `[`InstanceProvider`](-instance-provider/index.md)<br>An instance provider that requests an instance of a class from a captured lambda. |
| [CompositeInstanceProvider](-composite-instance-provider.md) | `interface CompositeInstanceProvider : `[`RequiringInstanceProvider`](-requiring-instance-provider/index.md)`, `[`Composite`](../com.nextfaze.devfun.core/-composite/index.md)`<`[`InstanceProvider`](-instance-provider/index.md)`>`<br>Instance provider that delegates to other providers. |
| [ConstructingInstanceProvider](-constructing-instance-provider/index.md) | `class ConstructingInstanceProvider : `[`InstanceProvider`](-instance-provider/index.md)<br>Provides objects via instance construction. Type must be annotated with [Constructable](-constructable/index.md). |
| [InstanceProvider](-instance-provider/index.md) | `interface InstanceProvider`<br>Provides object instances for one or more types. |
| [KObjectInstanceProvider](-k-object-instance-provider/index.md) | `class KObjectInstanceProvider : `[`InstanceProvider`](-instance-provider/index.md)<br>Handles Kotlin `object` and `companion object` types. |
| [RequiringInstanceProvider](-requiring-instance-provider/index.md) | `interface RequiringInstanceProvider : `[`InstanceProvider`](-instance-provider/index.md)<br>Same as [InstanceProvider](-instance-provider/index.md), but throws [ClassInstanceNotFoundException](-class-instance-not-found-exception/index.md) instead of returning `null`. |

### Annotations

| Name | Summary |
|---|---|
| [Constructable](-constructable/index.md) | `annotation class Constructable`<br>Tag to allow classes to be instantiated when no other [InstanceProvider](-instance-provider/index.md) was able to provide the class. |

### Exceptions

| Name | Summary |
|---|---|
| [ClassInstanceNotFoundException](-class-instance-not-found-exception/index.md) | `class ClassInstanceNotFoundException : `[`Exception`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)<br>Exception thrown when attempting to provide a type that was not found from any [InstanceProvider](-instance-provider/index.md). |
| [ConstructableException](-constructable-exception/index.md) | `class ConstructableException : `[`Exception`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)<br>Thrown when [ConstructingInstanceProvider](-constructing-instance-provider/index.md) fails to create a new instance of a class. |

### Extensions for External Classes

| Name | Summary |
|---|---|
| [kotlin.reflect.KClass](kotlin.reflect.-k-class/index.md) |  |

### Functions

| Name | Summary |
|---|---|
| [captureInstance](capture-instance.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> captureInstance(instance: () -> `[`T`](capture-instance.md#T)`?): `[`InstanceProvider`](-instance-provider/index.md)<br>Utility function to capture an instance of an object. |
| [createDefaultCompositeInstanceProvider](create-default-composite-instance-provider.md) | `fun createDefaultCompositeInstanceProvider(cacheLevel: `[`CacheLevel`](-cache-level/index.md)` = CacheLevel.AGGRESSIVE): `[`CompositeInstanceProvider`](-composite-instance-provider.md)<br>Creates an instance provider that delegates to other providers. |
| [singletonInstance](singleton-instance.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> singletonInstance(instance: () -> `[`T`](singleton-instance.md#T)`?): `[`InstanceProvider`](-instance-provider/index.md)<br>Utility function to provide a single instance of some type. |
