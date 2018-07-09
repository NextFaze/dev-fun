[gh-pages](../index.md) / [com.nextfaze.devfun.inject](index.md) / [CompositeInstanceProvider](./-composite-instance-provider.md)

# CompositeInstanceProvider

`interface CompositeInstanceProvider : `[`RequiringInstanceProvider`](-requiring-instance-provider/index.md)`, `[`Composite`](../com.nextfaze.devfun.core/-composite/index.md)`<`[`InstanceProvider`](-instance-provider/index.md)`>` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/inject/InstanceProviders.kt#L20)

Instance provider that delegates to other providers.

Checks in reverse order of added.
i.e. most recently added is checked first.

### Inherited Functions

| Name | Summary |
|---|---|
| [get](-requiring-instance-provider/get.md) | `abstract operator fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> get(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`T`](-requiring-instance-provider/get.md#T)`>): `[`T`](-requiring-instance-provider/get.md#T)<br>Get an instance of some [clazz](-requiring-instance-provider/get.md#com.nextfaze.devfun.inject.RequiringInstanceProvider$get(kotlin.reflect.KClass((com.nextfaze.devfun.inject.RequiringInstanceProvider.get.T)))/clazz). |
| [minusAssign](../com.nextfaze.devfun.core/-composite/minus-assign.md) | `abstract operator fun minusAssign(other: `[`T`](../com.nextfaze.devfun.core/-composite/index.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Remove from this [Composite](../com.nextfaze.devfun.core/-composite/index.md) |
| [plusAssign](../com.nextfaze.devfun.core/-composite/plus-assign.md) | `abstract operator fun plusAssign(other: `[`T`](../com.nextfaze.devfun.core/-composite/index.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Add to this [Composite](../com.nextfaze.devfun.core/-composite/index.md). |
