[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [FunctionDefinition](index.md) / [invoke](./invoke.md)

# invoke

`abstract val invoke: `[`FunctionInvoke`](../-function-invoke.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/core/Definitions.kt#L85)

Called when this item is to be invoked.

Be aware if invoking this directly; no error handling is provided.
You should use `devFun.get<Invoker>()` for missing arguments, user input, and exception handling.

