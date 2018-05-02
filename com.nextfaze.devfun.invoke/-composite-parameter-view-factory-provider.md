[gh-pages](../index.md) / [com.nextfaze.devfun.invoke](index.md) / [CompositeParameterViewFactoryProvider](./-composite-parameter-view-factory-provider.md)

# CompositeParameterViewFactoryProvider

`interface CompositeParameterViewFactoryProvider : `[`ParameterViewFactoryProvider`](-parameter-view-factory-provider/index.md)`, `[`Composite`](../com.nextfaze.devfun.core/-composite/index.md)`<`[`ParameterViewFactoryProvider`](-parameter-view-factory-provider/index.md)`>` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/invoke/View.kt#L115)

A [ParameterViewFactoryProvider](-parameter-view-factory-provider/index.md) that delegates to other providers.

Checks in reverse order of added.
i.e. most recently added is checked first.

In general you should not need to use this.

### Inherited Functions

| Name | Summary |
|---|---|
| [get](-parameter-view-factory-provider/get.md) | `abstract operator fun get(parameter: `[`Parameter`](-parameter/index.md)`): `[`ViewFactory`](../com.nextfaze.devfun.view/-view-factory/index.md)`<`[`View`](https://developer.android.com/reference/android/view/View.html)`>?`<br>Get a view factory for some function parameter. |
| [minusAssign](../com.nextfaze.devfun.core/-composite/minus-assign.md) | `abstract operator fun minusAssign(other: `[`T`](../com.nextfaze.devfun.core/-composite/index.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Remove from this [Composite](../com.nextfaze.devfun.core/-composite/index.md) |
| [plusAssign](../com.nextfaze.devfun.core/-composite/plus-assign.md) | `abstract operator fun plusAssign(other: `[`T`](../com.nextfaze.devfun.core/-composite/index.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Add to this [Composite](../com.nextfaze.devfun.core/-composite/index.md). |
