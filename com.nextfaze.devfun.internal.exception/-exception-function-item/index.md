[gh-pages](../../index.md) / [com.nextfaze.devfun.internal.exception](../index.md) / [ExceptionFunctionItem](./index.md)

# ExceptionFunctionItem

`class ExceptionFunctionItem : `[`FunctionItem`](../../com.nextfaze.devfun.function/-function-item/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-internal/src/main/java/com/nextfaze/devfun/internal/exception/ExceptionTypes.kt#L27)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `ExceptionFunctionItem(ex: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)` |

### Properties

| Name | Summary |
|---|---|
| [category](category.md) | `val category: `[`ExceptionCategoryDefinition`](../-exception-category-definition/index.md)<br>The resolved category definition. |
| [function](function.md) | `val function: `[`ExceptionFunctionDefinition`](../-exception-function-definition/index.md)<br>The original definition this item was derived from. |
| [name](name.md) | `val name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The formatted display name. |

### Inherited Properties

| Name | Summary |
|---|---|
| [args](../../com.nextfaze.devfun.function/-function-item/args.md) | `open val args: `[`FunctionArgs`](../../com.nextfaze.devfun.function/-function-args.md)<br>Custom arguments for the [invoke](../../com.nextfaze.devfun.function/-function-item/invoke.md) invocation. Otherwise arguments will be requested from an [InstanceProvider](../../com.nextfaze.devfun.inject/-instance-provider/index.md). |
| [group](../../com.nextfaze.devfun.function/-function-item/group.md) | `open val group: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`?`<br>A "grouping" for this item. |
| [invoke](../../com.nextfaze.devfun.function/-function-item/invoke.md) | `open val invoke: `[`FunctionInvoke`](../../com.nextfaze.devfun.function/-function-invoke.md)<br>Function to be invoked on click. |

### Extension Properties

| Name | Summary |
|---|---|
| [receiverClass](../../com.nextfaze.devfun.invoke/receiver-class.md) | `val `[`FunctionItem`](../../com.nextfaze.devfun.function/-function-item/index.md)`.receiverClass: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<*>`<br>Get the receiver class for this function item. |
| [receiverClassForInvocation](../../com.nextfaze.devfun.invoke/receiver-class-for-invocation.md) | `val `[`FunctionItem`](../../com.nextfaze.devfun.function/-function-item/index.md)`.receiverClassForInvocation: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<*>?`<br>Get the receiver class for this function item if you intend to invoke it. That is, it will return `null` if the type isn't needed. |

### Extension Functions

| Name | Summary |
|---|---|
| [call](../../com.nextfaze.devfun.core/call.md) | `fun `[`FunctionItem`](../../com.nextfaze.devfun.function/-function-item/index.md)`.call(invoker: `[`Invoker`](../../com.nextfaze.devfun.invoke/-invoker/index.md)` = devFun.get()): `[`InvokeResult`](../../com.nextfaze.devfun.function/-invoke-result/index.md)`?`<br>Convenience function for invoking a [FunctionItem](../../com.nextfaze.devfun.function/-function-item/index.md) using the current [devFun](../../com.nextfaze.devfun.core/dev-fun.md) instance. |
| [parameterInstances](../../com.nextfaze.devfun.invoke/parameter-instances.md) | `fun `[`FunctionItem`](../../com.nextfaze.devfun.function/-function-item/index.md)`.parameterInstances(instanceProvider: `[`InstanceProvider`](../../com.nextfaze.devfun.inject/-instance-provider/index.md)` = devFun.instanceProviders): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?>`<br>Get the parameter instances for this function item for invocation. |
| [receiverInstance](../../com.nextfaze.devfun.invoke/receiver-instance.md) | `fun `[`FunctionItem`](../../com.nextfaze.devfun.function/-function-item/index.md)`.receiverInstance(instanceProvider: `[`InstanceProvider`](../../com.nextfaze.devfun.inject/-instance-provider/index.md)` = devFun.instanceProviders): `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?`<br>Get the receiver instance for this function item to be used for invocation. |
