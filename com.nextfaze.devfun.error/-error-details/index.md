[gh-pages](../../index.md) / [com.nextfaze.devfun.error](../index.md) / [ErrorDetails](./index.md)

# ErrorDetails

`interface ErrorDetails` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/error/Handler.kt#L24)

Details/information of an error.

**See Also**

[SimpleError](../-simple-error/index.md)

### Properties

| Name | Summary |
|---|---|
| [body](body.md) | `abstract val body: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)<br>Some details about why it occurred and/or resolution details, etc. |
| [functionItem](function-item.md) | `abstract val functionItem: `[`FunctionItem`](../../com.nextfaze.devfun.core/-function-item/index.md)`?`<br>The function item to lead to this error (such as when attempting to invoke/prepare/whatever). Will be `null` for general errors. |
| [t](t.md) | `abstract val t: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)<br>The exception that was thrown. |
| [time](time.md) | `abstract val time: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>When this error occurred in millis since epoch. |
| [title](title.md) | `abstract val title: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)<br>A title for the dialog - the "kind" if you will. |

### Inheritors

| Name | Summary |
|---|---|
| [SimpleError](../-simple-error/index.md) | `data class SimpleError : `[`ErrorDetails`](./index.md)<br>Convenience class that implements [ErrorDetails](./index.md) and automatically time stamps it. |
