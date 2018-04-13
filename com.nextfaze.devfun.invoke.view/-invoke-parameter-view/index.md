[gh-pages](../../index.md) / [com.nextfaze.devfun.invoke.view](../index.md) / [InvokeParameterView](./index.md)

# InvokeParameterView

`interface InvokeParameterView : `[`WithLabel`](../-with-label/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/invoke/view/ParameterView.kt#L38)

Parameter views rendered for the Invoke UI will be wrapped with this type (to provide a label etc.).

Be careful if using this as a custom implementation has not been tested (though *should* work - create an issue if not).

### Properties

| Name | Summary |
|---|---|
| [attributes](attributes.md) | `abstract var attributes: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)<br>Attributes of the parameter this view represents (e.g. if it is injected or missing etc.). |
| [view](view.md) | `abstract var view: `[`View`](https://developer.android.com/reference/android/view/View.html)`?`<br>The wrapped view the represents the parameter. |

### Inherited Properties

| Name | Summary |
|---|---|
| [label](../-with-label/label.md) | `abstract var label: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)<br>The label/title for this parameter. |
