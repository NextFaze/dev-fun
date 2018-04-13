[gh-pages](../../index.md) / [com.nextfaze.devfun.invoke](../index.md) / [ParameterViewFactoryProvider](index.md) / [get](./get.md)

# get

`abstract operator fun get(parameter: `[`Parameter`](../-parameter/index.md)`): `[`ViewFactory`](../../com.nextfaze.devfun.view/-view-factory/index.md)`<`[`View`](https://developer.android.com/reference/android/view/View.html)`>?` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/invoke/View.kt#L72)

Get a view factory for some function parameter.

### Parameters

`parameter` - Parameter information for a function parameter.

**Return**
The view factory for this parameter or `null` if this parameter should be handled by another.

