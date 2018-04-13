[gh-pages](../index.md) / [com.nextfaze.devfun.core](index.md) / [call](./call.md)

# call

`fun `[`FunctionItem`](-function-item/index.md)`.call(invoker: `[`Invoker`](../com.nextfaze.devfun.invoke/-invoker/index.md)` = devFun.get()): `[`InvokeResult`](-invoke-result/index.md)`?` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/core/Extensions.kt#L14)

Convenience function for invoking a [FunctionItem](-function-item/index.md) using the current [devFun](dev-fun.md) instance.

Invocation exceptions (excluding [DebugException](-debug-exception/index.md) will be wrapped to [InvokeResult.exception](-invoke-result/exception.md).

Invocations pending user interaction will return `null`.

**Return**
See [Invoker.invoke](../com.nextfaze.devfun.invoke/-invoker/invoke.md).

