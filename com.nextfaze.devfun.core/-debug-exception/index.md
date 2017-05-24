[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [DebugException](.)

# DebugException

`class DebugException : `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/core/Definitions.kt#L128)

This will not be caught by the generated [FunctionInvoke](../-function-invoke.md) call.

i.e. Under most conditions, if this is thrown it will crash your app.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `DebugException(message: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "Debug")`<br>This will not be caught by the generated [FunctionInvoke](../-function-invoke.md) call. |
