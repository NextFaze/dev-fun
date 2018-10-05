[gh-pages](../../index.md) / [com.nextfaze.devfun.error](../index.md) / [ErrorHandler](./index.md)

# ErrorHandler

`interface ErrorHandler` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/error/Handler.kt#L71)

Handles errors that occur during/throughout DevFun.

You should use this in your own modules to provide consistent error handling.
It's unlikely you'll need to implement this yourself.

The default error handler will show a dialog with the exception stack trace and some error details.

### Functions

| Name | Summary |
|---|---|
| [clearAll](clear-all.md) | `abstract fun clearAll(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Clears all errors, seen or otherwise. |
| [markSeen](mark-seen.md) | `abstract fun markSeen(key: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Mark an error as seen by the user. |
| [onError](on-error.md) | `abstract fun onError(t: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`, title: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`, body: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`, functionItem: `[`FunctionItem`](../../com.nextfaze.devfun.function/-function-item/index.md)`? = null): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>`abstract fun onError(error: `[`ErrorDetails`](../-error-details/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Call to log an error. |
| [onWarn](on-warn.md) | `abstract fun onWarn(title: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`, body: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Log a simple warning message. |
| [remove](remove.md) | `abstract fun remove(key: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Remove an error from the history. |
