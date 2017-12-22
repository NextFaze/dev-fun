[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [FunctionItem](.)

# FunctionItem

`interface FunctionItem` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/core/Items.kt#L16)

Items are converted from [FunctionDefinition](../-function-definition/index.md) at run-time via [FunctionTransformer](../-function-transformer/index.md).

Implementers of this need to ensure a valid [equals](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/equals.html) and [hashCode](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/hash-code.html) implementation.
Unless necessary, you should extend from convenience class [SimpleFunctionItem](../-simple-function-item/index.md) that does this already.

*Do not to include [invoke](invoke.md) in the `equals`/`hashCode` implementations as it is usually a non-comparable anonymous instance.*

**See Also**

[SimpleFunctionItem.equals](../-simple-function-item/equals.md)

[SimpleFunctionItem.hashCode](../-simple-function-item/hash-code.md)

### Properties

| Name | Summary |
|---|---|
| [args](args.md) | `open val args: `[`FunctionArgs`](../-function-args.md)<br>Custom arguments for the [invoke](invoke.md) invocation. Otherwise arguments will be requested from an [InstanceProvider](../../com.nextfaze.devfun.inject/-instance-provider/index.md). |
| [category](category.md) | `abstract val category: `[`CategoryDefinition`](../-category-definition/index.md)<br>The resolved category definition. |
| [function](function.md) | `abstract val function: `[`FunctionDefinition`](../-function-definition/index.md)<br>The original definition this item was derived from. |
| [group](group.md) | `open val group: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`?`<br>A "grouping" for this item. |
| [invoke](invoke.md) | `open val invoke: `[`FunctionInvoke`](../-function-invoke.md)<br>Function to be invoked on click. |
| [name](name.md) | `open val name: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)<br>The formatted display name. |

### Extension Functions

| Name | Summary |
|---|---|
| [call](../call.md) | `fun FunctionItem.call(instanceProvider: `[`InstanceProvider`](../../com.nextfaze.devfun.inject/-instance-provider/index.md)` = devFun.instanceProviders): `[`InvokeResult`](../-invoke-result/index.md)<br>Convenience function for invoking a FunctionItem using the current [devFun](../dev-fun.md) instance. |
| [callAndLog](../call-and-log.md) | `fun FunctionItem.callAndLog(instanceProvider: `[`InstanceProvider`](../../com.nextfaze.devfun.inject/-instance-provider/index.md)` = devFun.instanceProviders, logger: Logger = log): `[`InvokeResult`](../-invoke-result/index.md)<br>Convenience function for invoking and logging a [FunctionItem.invoke](invoke.md). |

### Inheritors

| Name | Summary |
|---|---|
| [SimpleFunctionItem](../-simple-function-item/index.md) | `open class SimpleFunctionItem : FunctionItem`<br>Convenience class for FunctionItem to extend from, providing standard [equals](../-simple-function-item/equals.md) and [hashCode](../-simple-function-item/hash-code.md) implementations. |
