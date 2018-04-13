[gh-pages](../index.md) / [com.nextfaze.devfun.invoke](./index.md)

## Package com.nextfaze.devfun.invoke

### Types

| Name | Summary |
|---|---|
| [CompositeParameterViewFactoryProvider](-composite-parameter-view-factory-provider.md) | `interface CompositeParameterViewFactoryProvider : `[`ParameterViewFactoryProvider`](-parameter-view-factory-provider/index.md)`, `[`Composite`](../com.nextfaze.devfun.core/-composite/index.md)`<`[`ParameterViewFactoryProvider`](-parameter-view-factory-provider/index.md)`>`<br>A [ParameterViewFactoryProvider](-parameter-view-factory-provider/index.md) that delegates to other providers. |
| [Invoker](-invoker/index.md) | `interface Invoker`<br>Used to invoke a [FunctionItem](../com.nextfaze.devfun.core/-function-item/index.md) and automatically handles parameter injection and errors. |
| [Parameter](-parameter/index.md) | `interface Parameter`<br>Effectively just a wrapper for [KParameter](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-parameter/index.html) to allow libraries to use it without declaring a dependency on the kotlin-reflect lib. |
| [ParameterViewFactoryProvider](-parameter-view-factory-provider/index.md) | `interface ParameterViewFactoryProvider`<br>A factory that creates views based on parameter attributes to be used when invoking a function with non-injectable parameter types. |

### Extensions for External Classes

| Name | Summary |
|---|---|
| [java.lang.reflect.Method](java.lang.reflect.-method/index.md) |  |

### Properties

| Name | Summary |
|---|---|
| [receiverClass](receiver-class.md) | `val `[`FunctionDefinition`](../com.nextfaze.devfun.core/-function-definition/index.md)`.receiverClass: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<*>`<br>Get the receiver class for this function definition.`val `[`FunctionItem`](../com.nextfaze.devfun.core/-function-item/index.md)`.receiverClass: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<*>`<br>Get the receiver class for this function item. |
| [receiverClassForInvocation](receiver-class-for-invocation.md) | `val `[`FunctionDefinition`](../com.nextfaze.devfun.core/-function-definition/index.md)`.receiverClassForInvocation: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<*>?`<br>Get the receiver class for this function definition if you intend to invoke it. That is, it will return `null` if the type isn't needed.`val `[`FunctionItem`](../com.nextfaze.devfun.core/-function-item/index.md)`.receiverClassForInvocation: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<*>?`<br>Get the receiver class for this function item if you intend to invoke it. That is, it will return `null` if the type isn't needed. |

### Functions

| Name | Summary |
|---|---|
| [parameterInstances](parameter-instances.md) | `fun `[`FunctionDefinition`](../com.nextfaze.devfun.core/-function-definition/index.md)`.parameterInstances(instanceProvider: `[`InstanceProvider`](../com.nextfaze.devfun.inject/-instance-provider/index.md)` = devFun.instanceProviders, args: `[`FunctionArgs`](../com.nextfaze.devfun.core/-function-args.md)`): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?>`<br>Get the parameter instances for this function definition for invocation.`fun `[`FunctionItem`](../com.nextfaze.devfun.core/-function-item/index.md)`.parameterInstances(instanceProvider: `[`InstanceProvider`](../com.nextfaze.devfun.inject/-instance-provider/index.md)` = devFun.instanceProviders): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?>`<br>Get the parameter instances for this function item for invocation. |
| [receiverInstance](receiver-instance.md) | `fun `[`FunctionDefinition`](../com.nextfaze.devfun.core/-function-definition/index.md)`.receiverInstance(instanceProvider: `[`InstanceProvider`](../com.nextfaze.devfun.inject/-instance-provider/index.md)` = devFun.instanceProviders): `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?`<br>Get the receiver instance for this function definition to be used for invocation.`fun `[`FunctionItem`](../com.nextfaze.devfun.core/-function-item/index.md)`.receiverInstance(instanceProvider: `[`InstanceProvider`](../com.nextfaze.devfun.inject/-instance-provider/index.md)` = devFun.instanceProviders): `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?`<br>Get the receiver instance for this function item to be used for invocation. |
