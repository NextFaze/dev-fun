[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [Composite](./index.md)

# Composite

`interface Composite<T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> : `[`Iterable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)`<`[`T`](index.md#T)`>` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/core/Composite.kt#L12)

Use by providers to facilitate user provided types [T](index.md#T) to the composting provider.

In general additions are checked in reverse order. i.e. newest added are checked first.

Users should remove their type when disposed of or out of scope.

### Functions

| Name | Summary |
|---|---|
| [minusAssign](minus-assign.md) | `abstract operator fun minusAssign(other: `[`T`](index.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Remove from this [Composite](./index.md) |
| [plusAssign](plus-assign.md) | `abstract operator fun plusAssign(other: `[`T`](index.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Add to this [Composite](./index.md). |

### Inheritors

| Name | Summary |
|---|---|
| [CompositeInstanceProvider](../../com.nextfaze.devfun.inject/-composite-instance-provider.md) | `interface CompositeInstanceProvider : `[`RequiringInstanceProvider`](../../com.nextfaze.devfun.inject/-requiring-instance-provider/index.md)`, `[`Composite`](./index.md)`<`[`InstanceProvider`](../../com.nextfaze.devfun.inject/-instance-provider/index.md)`>`<br>Instance provider that delegates to other providers. |
| [CompositeParameterViewFactoryProvider](../../com.nextfaze.devfun.invoke/-composite-parameter-view-factory-provider.md) | `interface CompositeParameterViewFactoryProvider : `[`ParameterViewFactoryProvider`](../../com.nextfaze.devfun.invoke/-parameter-view-factory-provider/index.md)`, `[`Composite`](./index.md)`<`[`ParameterViewFactoryProvider`](../../com.nextfaze.devfun.invoke/-parameter-view-factory-provider/index.md)`>`<br>A [ParameterViewFactoryProvider](../../com.nextfaze.devfun.invoke/-parameter-view-factory-provider/index.md) that delegates to other providers. |
| [CompositeViewFactoryProvider](../../com.nextfaze.devfun.view/-composite-view-factory-provider.md) | `interface CompositeViewFactoryProvider : `[`ViewFactoryProvider`](../../com.nextfaze.devfun.view/-view-factory-provider/index.md)`, `[`Composite`](./index.md)`<`[`ViewFactoryProvider`](../../com.nextfaze.devfun.view/-view-factory-provider/index.md)`>`<br>A [ViewFactoryProvider](../../com.nextfaze.devfun.view/-view-factory-provider/index.md) that delegates to other providers. |
