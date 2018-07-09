[gh-pages](../../index.md) / [com.nextfaze.devfun.internal.exception](../index.md) / [ExceptionInvokeResult](index.md) / [value](./value.md)

# value

`val value: `[`Nothing`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-nothing/index.html)`?` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-internal/src/main/java/com/nextfaze/devfun/internal/exception/ExceptionTypes.kt#L8)

Overrides [InvokeResult.value](../../com.nextfaze.devfun.core/-invoke-result/value.md)

The return value of the function invocation.

[exception](../../com.nextfaze.devfun.core/-invoke-result/exception.md) will be non-null on failure - (not including [DebugException](../../com.nextfaze.devfun.core/-debug-exception/index.md), which will not be caught).

