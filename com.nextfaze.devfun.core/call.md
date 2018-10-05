[gh-pages](../index.md) / [com.nextfaze.devfun.core](index.md) / [call](./call.md)

# call

`fun `[`FunctionItem`](../com.nextfaze.devfun.function/-function-item/index.md)`.call(invoker: `[`Invoker`](../com.nextfaze.devfun.invoke/-invoker/index.md)` = devFun.get()): `[`InvokeResult`](../com.nextfaze.devfun.function/-invoke-result/index.md)`?` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/core/Extensions.kt#L17)

Convenience function for invoking a [FunctionItem](../com.nextfaze.devfun.function/-function-item/index.md) using the current [devFun](dev-fun.md) instance.

Invocation exceptions (excluding [DebugException](../com.nextfaze.devfun/-debug-exception/index.md) will be wrapped to [InvokeResult.exception](../com.nextfaze.devfun.function/-invoke-result/exception.md).

Invocations pending user interaction will return `null`.

**Return**
See [Invoker.invoke](../com.nextfaze.devfun.invoke/-invoker/invoke.md).

