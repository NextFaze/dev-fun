[gh-pages](../../index.md) / [com.nextfaze.devfun.view](../index.md) / [ViewFactoryProvider](./index.md)

# ViewFactoryProvider

`interface ViewFactoryProvider` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/view/Factory.kt#L38)

Provides [ViewFactory](../-view-factory/index.md) instances for some class type/key.

The class "key" does need to be in any way related to the resulting view factory, and is merely just a tag-like system.

### Functions

| Name | Summary |
|---|---|
| [get](get.md) | `abstract operator fun get(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<*>): `[`ViewFactory`](../-view-factory/index.md)`<`[`View`](https://developer.android.com/reference/android/view/View.html)`>?`<br>Get a view factory for some [clazz](get.md#com.nextfaze.devfun.view.ViewFactoryProvider$get(kotlin.reflect.KClass((kotlin.Any)))/clazz) key. |

### Inheritors

| Name | Summary |
|---|---|
| [CompositeViewFactoryProvider](../-composite-view-factory-provider.md) | `interface CompositeViewFactoryProvider : `[`ViewFactoryProvider`](./index.md)`, `[`Composite`](../../com.nextfaze.devfun.core/-composite/index.md)`<`[`ViewFactoryProvider`](./index.md)`>`<br>A [ViewFactoryProvider](./index.md) that delegates to other providers. |
