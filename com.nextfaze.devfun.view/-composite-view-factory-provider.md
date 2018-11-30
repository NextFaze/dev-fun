[gh-pages](../index.md) / [com.nextfaze.devfun.view](index.md) / [CompositeViewFactoryProvider](./-composite-view-factory-provider.md)

# CompositeViewFactoryProvider

`interface CompositeViewFactoryProvider : `[`ViewFactoryProvider`](-view-factory-provider/index.md)`, `[`Composite`](../com.nextfaze.devfun.core/-composite/index.md)`<`[`ViewFactoryProvider`](-view-factory-provider/index.md)`>` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/view/Factory.kt#L58)

A [ViewFactoryProvider](-view-factory-provider/index.md) that delegates to other providers.

Checks in reverse order of added.
i.e. most recently added is checked first.

In general you should not need to use this.

### Inherited Functions

| Name | Summary |
|---|---|
| [get](-view-factory-provider/get.md) | `abstract operator fun get(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<*>): `[`ViewFactory`](-view-factory/index.md)`<`[`View`](https://developer.android.com/reference/android/view/View.html)`>?`<br>Get a view factory for some [clazz](-view-factory-provider/get.md#com.nextfaze.devfun.view.ViewFactoryProvider$get(kotlin.reflect.KClass((kotlin.Any)))/clazz) key. |
| [minusAssign](../com.nextfaze.devfun.core/-composite/minus-assign.md) | `abstract operator fun minusAssign(other: `[`T`](../com.nextfaze.devfun.core/-composite/index.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Remove from this [Composite](../com.nextfaze.devfun.core/-composite/index.md) |
| [plusAssign](../com.nextfaze.devfun.core/-composite/plus-assign.md) | `abstract operator fun plusAssign(other: `[`T`](../com.nextfaze.devfun.core/-composite/index.md#T)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Add to this [Composite](../com.nextfaze.devfun.core/-composite/index.md). |
