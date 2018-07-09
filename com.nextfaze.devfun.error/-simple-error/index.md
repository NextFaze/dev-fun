[gh-pages](../../index.md) / [com.nextfaze.devfun.error](../index.md) / [SimpleError](./index.md)

# SimpleError

`data class SimpleError : `[`ErrorDetails`](../-error-details/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/error/Handler.kt#L46)

Convenience class that implements [ErrorDetails](../-error-details/index.md) and automatically time stamps it.

**See Also**

[ErrorDetails.time](../-error-details/time.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `SimpleError(t: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`, title: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`, body: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`, functionItem: `[`FunctionItem`](../../com.nextfaze.devfun.core/-function-item/index.md)`? = null, time: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = System.currentTimeMillis())`<br>Convenience class that implements [ErrorDetails](../-error-details/index.md) and automatically time stamps it. |

### Properties

| Name | Summary |
|---|---|
| [body](body.md) | `val body: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)<br>Some details about why it occurred and/or resolution details, etc. |
| [functionItem](function-item.md) | `val functionItem: `[`FunctionItem`](../../com.nextfaze.devfun.core/-function-item/index.md)`?`<br>The function item to lead to this error (such as when attempting to invoke/prepare/whatever). Will be `null` for general errors. |
| [t](t.md) | `val t: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)<br>The exception that was thrown. |
| [time](time.md) | `val time: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>When this error occurred in millis since epoch. |
| [title](title.md) | `val title: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)<br>A title for the dialog - the "kind" if you will. |
