[gh-pages](../../index.md) / [com.nextfaze.devfun.internal.exception](../index.md) / [ExceptionFunctionDefinition](./index.md)

# ExceptionFunctionDefinition

`object ExceptionFunctionDefinition : `[`FunctionDefinition`](../../com.nextfaze.devfun.core/-function-definition/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-internal/src/main/java/com/nextfaze/devfun/internal/exception/ExceptionTypes.kt#L12)

### Properties

| Name | Summary |
|---|---|
| [invoke](invoke.md) | `val invoke: `[`FunctionInvoke`](../../com.nextfaze.devfun.core/-function-invoke.md)<br>Called when this item is to be invoked. |
| [method](method.md) | `val method: `[`Method`](https://developer.android.com/reference/java/lang/reflect/Method.html)<br>The method of this function was defined. |

### Inherited Properties

| Name | Summary |
|---|---|
| [category](../../com.nextfaze.devfun.core/-function-definition/category.md) | `open val category: `[`CategoryDefinition`](../../com.nextfaze.devfun.core/-category-definition/index.md)`?`<br>The category for this definition as taken from [DeveloperFunction.category](../../com.nextfaze.devfun.annotations/-developer-function/category.md). |
| [clazz](../../com.nextfaze.devfun.core/-function-definition/clazz.md) | `open val clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>`<br>The class where this item was defined. |
| [name](../../com.nextfaze.devfun.core/-function-definition/name.md) | `open val name: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)<br>The name of this item as taken from [DeveloperFunction.value](../../com.nextfaze.devfun.annotations/-developer-function/value.md). |
| [requiresApi](../../com.nextfaze.devfun.core/-function-definition/requires-api.md) | `open val requiresApi: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Required API to allow this item to be shown as taken from [DeveloperFunction.requiresApi](../../com.nextfaze.devfun.annotations/-developer-function/requires-api.md). |
| [transformer](../../com.nextfaze.devfun.core/-function-definition/transformer.md) | `open val transformer: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`FunctionTransformer`](../../com.nextfaze.devfun.core/-function-transformer/index.md)`>`<br>Function transformer for this instance as taken from [DeveloperFunction.transformer](../../com.nextfaze.devfun.annotations/-developer-function/transformer.md). |

### Functions

| Name | Summary |
|---|---|
| [exception](exception.md) | `fun exception(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Extension Properties

| Name | Summary |
|---|---|
| [receiverClass](../../com.nextfaze.devfun.invoke/receiver-class.md) | `val `[`FunctionDefinition`](../../com.nextfaze.devfun.core/-function-definition/index.md)`.receiverClass: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<*>`<br>Get the receiver class for this function definition. |
| [receiverClassForInvocation](../../com.nextfaze.devfun.invoke/receiver-class-for-invocation.md) | `val `[`FunctionDefinition`](../../com.nextfaze.devfun.core/-function-definition/index.md)`.receiverClassForInvocation: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<*>?`<br>Get the receiver class for this function definition if you intend to invoke it. That is, it will return `null` if the type isn't needed. |

### Extension Functions

| Name | Summary |
|---|---|
| [parameterInstances](../../com.nextfaze.devfun.invoke/parameter-instances.md) | `fun `[`FunctionDefinition`](../../com.nextfaze.devfun.core/-function-definition/index.md)`.parameterInstances(instanceProvider: `[`InstanceProvider`](../../com.nextfaze.devfun.inject/-instance-provider/index.md)` = devFun.instanceProviders, args: `[`FunctionArgs`](../../com.nextfaze.devfun.core/-function-args.md)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?>`<br>Get the parameter instances for this function definition for invocation. |
| [receiverInstance](../../com.nextfaze.devfun.invoke/receiver-instance.md) | `fun `[`FunctionDefinition`](../../com.nextfaze.devfun.core/-function-definition/index.md)`.receiverInstance(instanceProvider: `[`InstanceProvider`](../../com.nextfaze.devfun.inject/-instance-provider/index.md)` = devFun.instanceProviders): `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?`<br>Get the receiver instance for this function definition to be used for invocation. |
