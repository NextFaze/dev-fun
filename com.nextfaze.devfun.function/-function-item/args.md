[gh-pages](../../index.md) / [com.nextfaze.devfun.function](../index.md) / [FunctionItem](index.md) / [args](./args.md)

# args

`open val args: `[`FunctionArgs`](../-function-args.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/function/FunctionItems.kt#L55)

Custom arguments for the [invoke](invoke.md) invocation. Otherwise arguments will be requested from an [InstanceProvider](../../com.nextfaze.devfun.inject/-instance-provider/index.md).

The list can be any size, with arguments passed to [invoke](invoke.md) based on their index within the list.

Entries that are [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) (or index out of range) will fallback to using the [InstanceProvider](../../com.nextfaze.devfun.inject/-instance-provider/index.md).

