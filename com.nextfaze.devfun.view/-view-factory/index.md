[gh-pages](../../index.md) / [com.nextfaze.devfun.view](../index.md) / [ViewFactory](./index.md)

# ViewFactory

`interface ViewFactory<out V : `[`View`](https://developer.android.com/reference/android/view/View.html)`>` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/view/Factory.kt#L21)

Used by [DevFun](../../com.nextfaze.devfun.core/-dev-fun/index.md) to inflate views when needed.

**See Also**

[ViewFactoryProvider](../-view-factory-provider/index.md)

### Functions

| Name | Summary |
|---|---|
| [inflate](inflate.md) | `abstract fun inflate(inflater: `[`LayoutInflater`](https://developer.android.com/reference/android/view/LayoutInflater.html)`, container: `[`ViewGroup`](https://developer.android.com/reference/android/view/ViewGroup.html)`?): `[`V`](index.md#V)<br>Called when this view should be inflated. |
