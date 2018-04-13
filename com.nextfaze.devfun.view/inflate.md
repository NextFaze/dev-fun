[gh-pages](../index.md) / [com.nextfaze.devfun.view](index.md) / [inflate](./inflate.md)

# inflate

`inline fun <reified V : `[`View`](https://developer.android.com/reference/android/view/View.html)`> inflate(@LayoutRes layoutId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, crossinline apply: `[`V`](inflate.md#V)`.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)` = {}): `[`ViewFactory`](-view-factory/index.md)`<`[`V`](inflate.md#V)`>` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/view/Helpers.kt#L32)

Convenience method to create a view factory via standard inflation.

In the simplest case, you can simply use: `inflate(R.layout.my_layout)`

If you also wish to adjust it after inflation:

``` kotlin
inflate<TextInputLayout>(R.layout.my_text_input_layout) {
    editText!!.apply {
        inputType = TYPE_CLASS_TEXT
        text = "Default Text"
        ...
    }
    ...
}
```

*Be aware that this creates a [ViewFactory](-view-factory/index.md) so inflation code could be executed at any time in any context.*

If you want to create a single-typed/keyed provider then use convenience method [viewFactory](view-factory.md).

**See Also**

[ViewFactoryProvider](-view-factory-provider/index.md)

