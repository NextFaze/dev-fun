[gh-pages](../../index.md) / [com.nextfaze.devfun.view](../index.md) / [ViewFactory](index.md) / [inflate](./inflate.md)

# inflate

`abstract fun inflate(inflater: `[`LayoutInflater`](https://developer.android.com/reference/android/view/LayoutInflater.html)`, container: `[`ViewGroup`](https://developer.android.com/reference/android/view/ViewGroup.html)`?): `[`V`](index.md#V) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/view/Factory.kt#L30)

Called when this view should be inflated.

### Parameters

`inflater` - The [LayoutInflater](https://developer.android.com/reference/android/view/LayoutInflater.html) object that can be used to inflate the view.

`container` - If non-null, this is the parent view that the inflated view will be attached to.
You should not add the view itself, but this can be used to generate the `LayoutParams` of the view.

**Return**
The inflated view.

