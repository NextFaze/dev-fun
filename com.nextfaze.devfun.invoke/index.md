[gh-pages](../index.md) / [com.nextfaze.devfun.invoke](./index.md)

## Package com.nextfaze.devfun.invoke

### Types

| Name | Summary |
|---|---|
| [CompositeParameterViewFactoryProvider](-composite-parameter-view-factory-provider.md) | `interface CompositeParameterViewFactoryProvider : `[`ParameterViewFactoryProvider`](-parameter-view-factory-provider/index.md)`, `[`Composite`](../com.nextfaze.devfun.core/-composite/index.md)`<`[`ParameterViewFactoryProvider`](-parameter-view-factory-provider/index.md)`>`<br>A [ParameterViewFactoryProvider](-parameter-view-factory-provider/index.md) that delegates to other providers. |
| [Invoker](-invoker/index.md) | `interface Invoker`<br>Used to invoke a [FunctionItem](../com.nextfaze.devfun.core/-function-item/index.md) or [UiFunction](-ui-function/index.md) and automatically handles parameter injection and errors. |
| [Parameter](-parameter/index.md) | `interface Parameter`<br>Effectively just a wrapper for [KParameter](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-parameter/index.html) to allow libraries to use it without declaring a dependency on the kotlin-reflect lib. |
| [ParameterViewFactoryProvider](-parameter-view-factory-provider/index.md) | `interface ParameterViewFactoryProvider`<br>A factory that creates views based on parameter attributes to be used when invoking a function with non-injectable parameter types. |
| [UiButton](-ui-button/index.md) | `interface UiButton`<br>Describes a dialog button for use with [UiFunction](-ui-function/index.md) to be rendered via the DevFun Invocation UI. |
| [UiField](-ui-field/index.md) | `interface UiField<T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> : `[`Parameter`](-parameter/index.md)`, `[`WithInitialValue`](-with-initial-value/index.md)`<`[`T`](-ui-field/index.md#T)`>`<br>Utility interface to easily generate an invoke UI/dialog. *(experimental)* |
| [UiFunction](-ui-function/index.md) | `interface UiFunction`<br>Describes a function to be executed via the DevFun Invocation UI. |
| [WithInitialValue](-with-initial-value/index.md) | `interface WithInitialValue<out T>`<br>[Parameter](-parameter/index.md) objects that implement this will use the value provided by [WithInitialValue.value](-with-initial-value/value.md) rather than checking for an @[From](../com.nextfaze.devfun.invoke.view/-from/index.md) annotation. |
| [WithKParameter](-with-k-parameter/index.md) | `interface WithKParameter`<br>[Parameter](-parameter/index.md) objects that implement this will have their values backed by a native function parameter description. |
| [WithNullability](-with-nullability/index.md) | `interface WithNullability`<br>[Parameter](-parameter/index.md) objects that implement this will can be nullable. |

### Type Aliases

| Name | Summary |
|---|---|
| [OnClick](-on-click.md) | `typealias OnClick = () -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [SimpleInvoke](-simple-invoke.md) | `typealias SimpleInvoke = (`[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?>) -> `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?` |

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
| [uiButton](ui-button.md) | `fun uiButton(text: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`? = null, textId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`? = null, onClick: `[`OnClick`](-on-click.md)`? = null, invoke: `[`SimpleInvoke`](-simple-invoke.md)`? = null): `[`UiButton`](-ui-button/index.md)<br>Utility function to create a [UiButton](-ui-button/index.md) instance for use with [UiFunction](-ui-function/index.md). |
| [uiField](ui-field.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> uiField(name: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`, initialValue: `[`T`](ui-field.md#T)`, annotations: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Annotation`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-annotation/index.html)`> = emptyList(), onSetValue: (`[`T`](ui-field.md#T)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`UiField`](-ui-field/index.md)`<`[`T`](ui-field.md#T)`>`<br>Utility function to create a [UiField](-ui-field/index.md) instance. *(experimental)* |
| [uiFunction](ui-function.md) | `fun uiFunction(title: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`, subtitle: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`? = null, signature: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`? = null, parameters: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Parameter`](-parameter/index.md)`>, negativeButton: `[`UiButton`](-ui-button/index.md)`? = null, neutralButton: `[`UiButton`](-ui-button/index.md)`? = null, positiveButton: `[`UiButton`](-ui-button/index.md)`? = null, invoke: `[`SimpleInvoke`](-simple-invoke.md)`? = null): `[`UiFunction`](-ui-function/index.md)<br>Utility function to create a [UiFunction](-ui-function/index.md) instance. |
