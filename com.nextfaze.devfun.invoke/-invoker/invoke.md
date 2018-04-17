[gh-pages](../../index.md) / [com.nextfaze.devfun.invoke](../index.md) / [Invoker](index.md) / [invoke](./invoke.md)

# invoke

`abstract fun invoke(item: `[`FunctionItem`](../../com.nextfaze.devfun.core/-function-item/index.md)`): `[`InvokeResult`](../../com.nextfaze.devfun.core/-invoke-result/index.md)`?` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/invoke/Invoker.kt#L25)

Invokes the provided [FunctionItem](../../com.nextfaze.devfun.core/-function-item/index.md).

Any exceptions thrown during invocation should be caught and returned as an [InvokeResult](../../com.nextfaze.devfun.core/-invoke-result/index.md) - exception for
[DebugException](../../com.nextfaze.devfun.core/-debug-exception/index.md), which is intended to crash the app and should simply be re-thrown when encountered.

**Return**
The result of the invocation and/or any exception thrown (other than [DebugException](../../com.nextfaze.devfun.core/-debug-exception/index.md)).
    If the result is `null` then the invocation is pending user interaction.
