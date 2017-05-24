[gh-pages](../index.md) / [com.nextfaze.devfun.inject](.)

## Package com.nextfaze.devfun.inject

### Types

| Name | Summary |
|---|---|
| [CapturingInstanceProvider](-capturing-instance-provider/index.md) | `class CapturingInstanceProvider<out T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> : `[`InstanceProvider`](-instance-provider/index.md)<br>An instance provider that requests an instance of a class from a captured lambda. |
| [CompositeInstanceProvider](-composite-instance-provider/index.md) | `class CompositeInstanceProvider : `[`RequiringInstanceProvider`](-requiring-instance-provider/index.md)<br>Instance provider that delegates to other providers. |
| [ConstructingInstanceProvider](-constructing-instance-provider/index.md) | `class ConstructingInstanceProvider : `[`InstanceProvider`](-instance-provider/index.md)<br>Provides objects via instance construction. Type must be annotated with [Constructable](-constructable/index.md). |
| [InstanceProvider](-instance-provider/index.md) | `interface InstanceProvider`<br>Provides object instances for one or more types. |
| [KObjectInstanceProvider](-k-object-instance-provider/index.md) | `class KObjectInstanceProvider : `[`InstanceProvider`](-instance-provider/index.md)<br>Handles Kotlin `object` types. |
| [RequiringInstanceProvider](-requiring-instance-provider/index.md) | `interface RequiringInstanceProvider : `[`InstanceProvider`](-instance-provider/index.md)<br>Same as [InstanceProvider](-instance-provider/index.md), but throws [ClassInstanceNotFoundException](-class-instance-not-found-exception/index.md) instead of returning `null`. |

### Annotations

| Name | Summary |
|---|---|
| [Constructable](-constructable/index.md) | `annotation class Constructable`<br>Tag to allow classes to be instantiated when no other [InstanceProvider](-instance-provider/index.md) was able to provide the class. |

### Exceptions

| Name | Summary |
|---|---|
| [ClassInstanceNotFoundException](-class-instance-not-found-exception/index.md) | `class ClassInstanceNotFoundException : `[`Exception`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)<br>Exception thrown when attempting to provide a type that was not found from any [InstanceProvider](-instance-provider/index.md). |

### Functions

| Name | Summary |
|---|---|
| [captureInstance](capture-instance.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> captureInstance(instance: () -> T?): `[`InstanceProvider`](-instance-provider/index.md)<br>Utility function to capture an instance of an object. |
