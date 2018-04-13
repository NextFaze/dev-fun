[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [FunctionDefinition](./index.md)

# FunctionDefinition

`interface FunctionDefinition` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/core/Definitions.kt#L39)

Functions/methods annotated with [DeveloperFunction](../../com.nextfaze.devfun.annotations/-developer-function/index.md) will be defined using this interface at compile time.

Definitions will be convert to items via [FunctionTransformer](../-function-transformer/index.md).

**See Also**

[FunctionItem](../-function-item/index.md)

### Properties

| Name | Summary |
|---|---|
| [category](category.md) | `open val category: `[`CategoryDefinition`](../-category-definition/index.md)`?`<br>The category for this definition as taken from [DeveloperFunction.category](../../com.nextfaze.devfun.annotations/-developer-function/category.md). |
| [clazz](clazz.md) | `open val clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>`<br>The class where this item was defined. |
| [invoke](invoke.md) | `abstract val invoke: `[`FunctionInvoke`](../-function-invoke.md)<br>Called when this item is to be invoked. |
| [method](method.md) | `abstract val method: `[`Method`](https://developer.android.com/reference/java/lang/reflect/Method.html)<br>The method of this function was defined. |
| [name](name.md) | `open val name: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)<br>The name of this item as taken from [DeveloperFunction.value](../../com.nextfaze.devfun.annotations/-developer-function/value.md). |
| [requiresApi](requires-api.md) | `open val requiresApi: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Required API to allow this item to be shown as taken from [DeveloperFunction.requiresApi](../../com.nextfaze.devfun.annotations/-developer-function/requires-api.md). |
| [transformer](transformer.md) | `open val transformer: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`FunctionTransformer`](../-function-transformer/index.md)`>`<br>Function transformer for this instance as taken from [DeveloperFunction.transformer](../../com.nextfaze.devfun.annotations/-developer-function/transformer.md). |

### Extension Properties

| Name | Summary |
|---|---|
| [receiverClass](../../com.nextfaze.devfun.invoke/receiver-class.md) | `val `[`FunctionDefinition`](./index.md)`.receiverClass: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<*>`<br>Get the receiver class for this function definition. |
| [receiverClassForInvocation](../../com.nextfaze.devfun.invoke/receiver-class-for-invocation.md) | `val `[`FunctionDefinition`](./index.md)`.receiverClassForInvocation: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<*>?`<br>Get the receiver class for this function definition if you intend to invoke it. That is, it will return `null` if the type isn't needed. |

### Extension Functions

| Name | Summary |
|---|---|
| [parameterInstances](../../com.nextfaze.devfun.invoke/parameter-instances.md) | `fun `[`FunctionDefinition`](./index.md)`.parameterInstances(instanceProvider: `[`InstanceProvider`](../../com.nextfaze.devfun.inject/-instance-provider/index.md)` = devFun.instanceProviders, args: `[`FunctionArgs`](../-function-args.md)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?>`<br>Get the parameter instances for this function definition for invocation. |
| [receiverInstance](../../com.nextfaze.devfun.invoke/receiver-instance.md) | `fun `[`FunctionDefinition`](./index.md)`.receiverInstance(instanceProvider: `[`InstanceProvider`](../../com.nextfaze.devfun.inject/-instance-provider/index.md)` = devFun.instanceProviders): `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?`<br>Get the receiver instance for this function definition to be used for invocation. |
