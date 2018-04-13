[gh-pages](../index.md) / [com.nextfaze.devfun.view](./index.md)

## Package com.nextfaze.devfun.view

### Types

| Name | Summary |
|---|---|
| [CompositeViewFactoryProvider](-composite-view-factory-provider.md) | `interface CompositeViewFactoryProvider : `[`ViewFactoryProvider`](-view-factory-provider/index.md)`, `[`Composite`](../com.nextfaze.devfun.core/-composite/index.md)`<`[`ViewFactoryProvider`](-view-factory-provider/index.md)`>`<br>A [ViewFactoryProvider](-view-factory-provider/index.md) that delegates to other providers. |
| [ViewFactory](-view-factory/index.md) | `interface ViewFactory<out V : `[`View`](https://developer.android.com/reference/android/view/View.html)`>`<br>Used by [DevFun](../com.nextfaze.devfun.core/-dev-fun/index.md) to inflate views when needed. |
| [ViewFactoryProvider](-view-factory-provider/index.md) | `interface ViewFactoryProvider`<br>Provides [ViewFactory](-view-factory/index.md) instances for some class type/key. |

### Functions

| Name | Summary |
|---|---|
| [inflate](inflate.md) | `fun <V : `[`View`](https://developer.android.com/reference/android/view/View.html)`> inflate(layoutId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, apply: `[`V`](inflate.md#V)`.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)` = {}): `[`ViewFactory`](-view-factory/index.md)`<`[`V`](inflate.md#V)`>`<br>Convenience method to create a view factory via standard inflation. |
| [viewFactory](view-factory.md) | `fun <K : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, V : `[`View`](https://developer.android.com/reference/android/view/View.html)`> viewFactory(layoutId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, apply: `[`V`](view-factory.md#V)`.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)` = {}): `[`ViewFactoryProvider`](-view-factory-provider/index.md)<br>Convenience method to create a view factory provider for a single type key. |
