[gh-pages](../index.md) / [com.nextfaze.devfun.core](index.md) / [call](.)

# call

`fun `[`FunctionItem`](-function-item/index.md)`.call(instanceProvider: `[`InstanceProvider`](../com.nextfaze.devfun.inject/-instance-provider/index.md)` = devFun.instanceProviders): `[`InvokeResult`](-invoke-result/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/core/Extensions.kt#L12)

Convenience function for invoking a [FunctionItem](-function-item/index.md) using the current [devFun](dev-fun.md) instance.

Invocation exceptions (excluding [DebugException](-debug-exception/index.md) will be in [InvokeResult.exception](-invoke-result/exception.md).

