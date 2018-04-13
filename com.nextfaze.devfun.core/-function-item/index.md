[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [FunctionItem](./index.md)

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

### Extension Properties

| Name | Summary |
|---|---|
| [receiverClass](../../com.nextfaze.devfun.invoke/receiver-class.md) | `val `[`FunctionItem`](./index.md)`.receiverClass: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<*>`<br>Get the receiver class for this function item. |
| [receiverClassForInvocation](../../com.nextfaze.devfun.invoke/receiver-class-for-invocation.md) | `val `[`FunctionItem`](./index.md)`.receiverClassForInvocation: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<*>?`<br>Get the receiver class for this function item if you intend to invoke it. That is, it will return `null` if the type isn't needed. |

### Extension Functions

| Name | Summary |
|---|---|
| [call](../call.md) | `fun `[`FunctionItem`](./index.md)`.call(invoker: `[`Invoker`](../../com.nextfaze.devfun.invoke/-invoker/index.md)` = devFun.get()): `[`InvokeResult`](../-invoke-result/index.md)`?`<br>Convenience function for invoking a [FunctionItem](./index.md) using the current [devFun](../dev-fun.md) instance. |
| [parameterInstances](../../com.nextfaze.devfun.invoke/parameter-instances.md) | `fun `[`FunctionItem`](./index.md)`.parameterInstances(instanceProvider: `[`InstanceProvider`](../../com.nextfaze.devfun.inject/-instance-provider/index.md)` = devFun.instanceProviders): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?>`<br>Get the parameter instances for this function item for invocation. |
| [receiverInstance](../../com.nextfaze.devfun.invoke/receiver-instance.md) | `fun `[`FunctionItem`](./index.md)`.receiverInstance(instanceProvider: `[`InstanceProvider`](../../com.nextfaze.devfun.inject/-instance-provider/index.md)` = devFun.instanceProviders): `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?`<br>Get the receiver instance for this function item to be used for invocation. |

### Inheritors

| Name | Summary |
|---|---|
| [SimpleFunctionItem](../-simple-function-item/index.md) | `open class SimpleFunctionItem : `[`FunctionItem`](./index.md)<br>Convenience class for [FunctionItem](./index.md) to extend from, providing standard [equals](../-simple-function-item/equals.md) and [hashCode](../-simple-function-item/hash-code.md) implementations. |
