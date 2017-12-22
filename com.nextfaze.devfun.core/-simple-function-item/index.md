[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [SimpleFunctionItem](.)

# SimpleFunctionItem

`open class SimpleFunctionItem : `[`FunctionItem`](../-function-item/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/core/Items.kt#L74)

Convenience class for [FunctionItem](../-function-item/index.md) to extend from, providing standard [equals](equals.md) and [hashCode](hash-code.md) implementations.

*If overriding [equals](equals.md)/[hashCode](hash-code.md), do not to include [invoke](../-function-item/invoke.md) in the implementations as it is usually a non-comparable anonymous instance.*

**See Also**

[FunctionTransformer](../-function-transformer/index.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `SimpleFunctionItem(function: `[`FunctionDefinition`](../-function-definition/index.md)`, category: `[`CategoryDefinition`](../-category-definition/index.md)`)`<br>Convenience class for [FunctionItem](../-function-item/index.md) to extend from, providing standard [equals](equals.md) and [hashCode](hash-code.md) implementations. |

### Properties

| Name | Summary |
|---|---|
| [category](category.md) | `open val category: `[`CategoryDefinition`](../-category-definition/index.md)<br>The resolved category definition. |
| [function](function.md) | `open val function: `[`FunctionDefinition`](../-function-definition/index.md)<br>The original definition this item was derived from. |

### Inherited Properties

| Name | Summary |
|---|---|
| [args](../-function-item/args.md) | `open val args: `[`FunctionArgs`](../-function-args.md)<br>Custom arguments for the [invoke](../-function-item/invoke.md) invocation. Otherwise arguments will be requested from an [InstanceProvider](../../com.nextfaze.devfun.inject/-instance-provider/index.md). |
| [group](../-function-item/group.md) | `open val group: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`?`<br>A "grouping" for this item. |
| [invoke](../-function-item/invoke.md) | `open val invoke: `[`FunctionInvoke`](../-function-invoke.md)<br>Function to be invoked on click. |
| [name](../-function-item/name.md) | `open val name: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)<br>The formatted display name. |

### Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | `open fun equals(other: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | `open fun hashCode(): `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [toString](to-string.md) | `open fun toString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

### Extension Functions

| Name | Summary |
|---|---|
| [call](../call.md) | `fun `[`FunctionItem`](../-function-item/index.md)`.call(instanceProvider: `[`InstanceProvider`](../../com.nextfaze.devfun.inject/-instance-provider/index.md)` = devFun.instanceProviders): `[`InvokeResult`](../-invoke-result/index.md)<br>Convenience function for invoking a [FunctionItem](../-function-item/index.md) using the current [devFun](../dev-fun.md) instance. |
| [callAndLog](../call-and-log.md) | `fun `[`FunctionItem`](../-function-item/index.md)`.callAndLog(instanceProvider: `[`InstanceProvider`](../../com.nextfaze.devfun.inject/-instance-provider/index.md)` = devFun.instanceProviders, logger: Logger = log): `[`InvokeResult`](../-invoke-result/index.md)<br>Convenience function for invoking and logging a [FunctionItem.invoke](../-function-item/invoke.md). |
