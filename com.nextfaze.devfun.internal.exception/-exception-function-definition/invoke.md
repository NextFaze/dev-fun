[gh-pages](../../index.md) / [com.nextfaze.devfun.internal.exception](../index.md) / [ExceptionFunctionDefinition](index.md) / [invoke](./invoke.md)

# invoke

`val invoke: `[`FunctionInvoke`](../../com.nextfaze.devfun.function/-function-invoke.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-internal/src/main/java/com/nextfaze/devfun/internal/exception/ExceptionTypes.kt#L19)

Overrides [FunctionDefinition.invoke](../../com.nextfaze.devfun.function/-function-definition/invoke.md)

Called when this item is to be invoked.

Be aware if invoking this directly; no error handling is provided.
You should use `devFun.get<Invoker>()` for missing arguments, user input, and exception handling.

