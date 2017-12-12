[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [InvokeResult](.)

# InvokeResult

`interface InvokeResult` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/core/Definitions.kt#L107)

Function invocations will be wrapped by this.

### Properties

| Name | Summary |
|---|---|
| [exception](exception.md) | `abstract val exception: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`?`<br>Any exceptions thrown while attempting to invoke the function. |
| [value](value.md) | `abstract val value: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?`<br>The return value of the function invocation. |

### Extension Functions

| Name | Summary |
|---|---|
| [log](../log.md) | `fun InvokeResult.log(logger: Logger = log, title: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "Invocation returned"): InvokeResult`<br>Convenience function to quickly log the result of a [FunctionItem.invoke](../-function-item/invoke.md). |
