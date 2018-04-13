[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [InvokeResult](./index.md)

# InvokeResult

`interface InvokeResult` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/core/Definitions.kt#L127)

Function invocations will be wrapped by this.

### Properties

| Name | Summary |
|---|---|
| [exception](exception.md) | `abstract val exception: `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)`?`<br>Any exceptions thrown while attempting to invoke the function. |
| [value](value.md) | `abstract val value: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?`<br>The return value of the function invocation. |
