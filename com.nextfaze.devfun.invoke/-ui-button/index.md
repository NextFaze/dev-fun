[gh-pages](../../index.md) / [com.nextfaze.devfun.invoke](../index.md) / [UiButton](./index.md)

# UiButton

`interface UiButton` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/invoke/UiFunction.kt#L15)

Describes a dialog button for use with [UiFunction](../-ui-function/index.md) to be rendered via the DevFun Invocation UI.

**See Also**

[uiButton](../ui-button.md)

[UiFunction](../-ui-function/index.md)

[uiFunction](../ui-function.md)

### Properties

| Name | Summary |
|---|---|
| [invoke](invoke.md) | `abstract val invoke: `[`SimpleInvoke`](../-simple-invoke.md)`?`<br>The invocation of this button. Set this if you want the field values, otherwise use [onClick](on-click.md) instead. |
| [onClick](on-click.md) | `abstract val onClick: `[`OnClick`](../-on-click.md)`?`<br>Click listener of this button - called if [invoke](invoke.md) is null. Set this when you don't care about field values. |
| [text](text.md) | `abstract val text: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`?`<br>The button text. |
| [textId](text-id.md) | `abstract val textId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?`<br>The button string resource. |
