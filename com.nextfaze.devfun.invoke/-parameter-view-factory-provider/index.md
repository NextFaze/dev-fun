[gh-pages](../../index.md) / [com.nextfaze.devfun.invoke](../index.md) / [ParameterViewFactoryProvider](./index.md)

# ParameterViewFactoryProvider

`interface ParameterViewFactoryProvider` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/invoke/View.kt#L96)

A factory that creates views based on parameter attributes to be used when invoking a function with non-injectable parameter types.

### Functions

| Name | Summary |
|---|---|
| [get](get.md) | `abstract operator fun get(parameter: `[`Parameter`](../-parameter/index.md)`): `[`ViewFactory`](../../com.nextfaze.devfun.view/-view-factory/index.md)`<`[`View`](https://developer.android.com/reference/android/view/View.html)`>?`<br>Get a view factory for some function parameter. |

### Inheritors

| Name | Summary |
|---|---|
| [CompositeParameterViewFactoryProvider](../-composite-parameter-view-factory-provider.md) | `interface CompositeParameterViewFactoryProvider : `[`ParameterViewFactoryProvider`](./index.md)`, `[`Composite`](../../com.nextfaze.devfun.core/-composite/index.md)`<`[`ParameterViewFactoryProvider`](./index.md)`>`<br>A [ParameterViewFactoryProvider](./index.md) that delegates to other providers. |
