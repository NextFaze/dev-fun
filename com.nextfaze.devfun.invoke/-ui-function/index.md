[gh-pages](../../index.md) / [com.nextfaze.devfun.invoke](../index.md) / [UiFunction](./index.md)

# UiFunction

`interface UiFunction` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/invoke/UiFunction.kt#L52)

Describes a function to be executed via the DevFun Invocation UI.

**See Also**

[uiFunction](../ui-function.md)

[UiButton](../-ui-button/index.md)

[uiButton](../ui-button.md)

### Properties

| Name | Summary |
|---|---|
| [negativeButton](negative-button.md) | `open val negativeButton: `[`UiButton`](../-ui-button/index.md)`?`<br>The cancel button. |
| [neutralButton](neutral-button.md) | `open val neutralButton: `[`UiButton`](../-ui-button/index.md)`?`<br>An optional neutral button. |
| [parameters](parameters.md) | `abstract val parameters: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Parameter`](../-parameter/index.md)`>`<br>The parameters of the function. |
| [positiveButton](positive-button.md) | `open val positiveButton: `[`UiButton`](../-ui-button/index.md)`?`<br>The execute button. |
| [signature](signature.md) | `open val signature: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`?`<br>The function signature (or something more technical and relevant to the invocation). |
| [subtitle](subtitle.md) | `open val subtitle: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`?`<br>The subtitle/description of this function (dialog subtitle). |
| [title](title.md) | `abstract val title: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)<br>The title of this function (the dialog title). |
