[gh-pages](../../index.md) / [com.nextfaze.devfun.function](../index.md) / [SimpleFunctionItem](./index.md)

# SimpleFunctionItem

`open class SimpleFunctionItem : `[`FunctionItem`](../-function-item/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/function/FunctionItems.kt#L72)

Convenience class for [FunctionItem](../-function-item/index.md) to extend from, providing standard [equals](equals.md) and [hashCode](hash-code.md) implementations.

*If overriding [equals](equals.md)/[hashCode](hash-code.md), do not to include [invoke](../-function-item/invoke.md) in the implementations as it is usually a non-comparable anonymous instance.*

**See Also**

[FunctionTransformer](../-function-transformer/index.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `SimpleFunctionItem(function: `[`FunctionDefinition`](../-function-definition/index.md)`, category: `[`CategoryDefinition`](../../com.nextfaze.devfun.category/-category-definition/index.md)`)`<br>Convenience class for [FunctionItem](../-function-item/index.md) to extend from, providing standard [equals](equals.md) and [hashCode](hash-code.md) implementations. |

### Properties

| Name | Summary |
|---|---|
| [category](category.md) | `open val category: `[`CategoryDefinition`](../../com.nextfaze.devfun.category/-category-definition/index.md)<br>The resolved category definition. |
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

### Extension Properties

| Name | Summary |
|---|---|
| [receiverClass](../../com.nextfaze.devfun.invoke/receiver-class.md) | `val `[`FunctionItem`](../-function-item/index.md)`.receiverClass: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<*>`<br>Get the receiver class for this function item. |
| [receiverClassForInvocation](../../com.nextfaze.devfun.invoke/receiver-class-for-invocation.md) | `val `[`FunctionItem`](../-function-item/index.md)`.receiverClassForInvocation: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<*>?`<br>Get the receiver class for this function item if you intend to invoke it. That is, it will return `null` if the type isn't needed. |

### Extension Functions

| Name | Summary |
|---|---|
| [call](../../com.nextfaze.devfun.core/call.md) | `fun `[`FunctionItem`](../-function-item/index.md)`.call(invoker: `[`Invoker`](../../com.nextfaze.devfun.invoke/-invoker/index.md)` = devFun.get()): `[`InvokeResult`](../-invoke-result/index.md)`?`<br>Convenience function for invoking a [FunctionItem](../-function-item/index.md) using the current [devFun](../../com.nextfaze.devfun.core/dev-fun.md) instance. |
| [parameterInstances](../../com.nextfaze.devfun.invoke/parameter-instances.md) | `fun `[`FunctionItem`](../-function-item/index.md)`.parameterInstances(instanceProvider: `[`InstanceProvider`](../../com.nextfaze.devfun.inject/-instance-provider/index.md)` = devFun.instanceProviders): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?>`<br>Get the parameter instances for this function item for invocation. |
| [receiverInstance](../../com.nextfaze.devfun.invoke/receiver-instance.md) | `fun `[`FunctionItem`](../-function-item/index.md)`.receiverInstance(instanceProvider: `[`InstanceProvider`](../../com.nextfaze.devfun.inject/-instance-provider/index.md)` = devFun.instanceProviders): `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?`<br>Get the receiver instance for this function item to be used for invocation. |
