[gh-pages](../../index.md) / [com.nextfaze.devfun](../index.md) / [DebugException](./index.md)

# DebugException

`class DebugException : `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/DebugException.kt#L8)

This will not be caught by the standard DevFun error handler.

i.e. Under most conditions, if this is thrown it will crash your app.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `DebugException(message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "Debug")`<br>This will not be caught by the standard DevFun error handler. |
