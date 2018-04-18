[gh-pages](../../index.md) / [com.nextfaze.devfun.error](../index.md) / [ErrorHandler](index.md) / [onError](./on-error.md)

# onError

`abstract fun onError(t: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`, title: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`, body: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`, functionItem: `[`FunctionItem`](../../com.nextfaze.devfun.core/-function-item/index.md)`? = null): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/error/Handler.kt#L72)

Call to log an error.

This could be anything from reading/processing throughout DevFun, to more specific scenarios such as when
invoking a [FunctionItem](../../com.nextfaze.devfun.core/-function-item/index.md).

This function simply delegates to [ErrorHandler.onError](./on-error.md).

### Parameters

`t` - The exception that occurred.

`title` - A title for the message/dialog.

`body` - A short description of how/why this exception was thrown.

`functionItem` - The relevant function item that lead to this error occurring (or `null`/absent) if not relevant.`abstract fun onError(error: `[`ErrorDetails`](../-error-details/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/error/Handler.kt#L82)

Call to log an error.

This could be anything from reading/processing throughout DevFun, to more specific scenarios such as when
invoking a [FunctionItem](../../com.nextfaze.devfun.core/-function-item/index.md).

### Parameters

`error` - The error details.