[gh-pages](../../index.md) / [com.nextfaze.devfun.invoke.view](../index.md) / [WithLabel](./index.md)

# WithLabel

`interface WithLabel` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/invoke/view/ParameterView.kt#L28)

View used with the Invoke UI will automatically be wrapped and be given a label unless they provide their own with this interface.

For example, the Switch view has its own text value and thus is used instead of the one provided by DevFun.

### Properties

| Name | Summary |
|---|---|
| [label](label.md) | `abstract var label: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)<br>The label/title for this parameter. |

### Inheritors

| Name | Summary |
|---|---|
| [InvokeParameterView](../-invoke-parameter-view/index.md) | `interface InvokeParameterView : `[`WithLabel`](./index.md)<br>Parameter views rendered for the Invoke UI will be wrapped with this type (to provide a label etc.). |
