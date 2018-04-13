[gh-pages](../index.md) / [com.nextfaze.devfun.core](./index.md)

## Package com.nextfaze.devfun.core

Core [DevFun](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.core/-dev-fun/) package - handles loading and processing of modules and definitions.

### Types

| Name | Summary |
|---|---|
| [AbstractDevFunModule](-abstract-dev-fun-module/index.md) | `abstract class AbstractDevFunModule : `[`DevFunModule`](-dev-fun-module/index.md)<br>Implementation of [DevFunModule](-dev-fun-module/index.md) providing various convenience functions. |
| [CategoryDefinition](-category-definition/index.md) | `interface CategoryDefinition`<br>Classes annotated with [DeveloperCategory](../com.nextfaze.devfun.annotations/-developer-category/index.md) will be defined using this interface at compile time. |
| [CategoryItem](-category-item/index.md) | `interface CategoryItem`<br>Items are derived from [CategoryDefinition](-category-definition/index.md) at run-time during [FunctionDefinition](-function-definition/index.md) processing. |
| [Composite](-composite/index.md) | `interface Composite<T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> : `[`Iterable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)`<`[`T`](-composite/index.md#T)`>`<br>Use by providers to facilitate user provided types [T](-composite/index.md#T) to the composting provider. |
| [DevFun](-dev-fun/index.md) | `class DevFun`<br>Primary entry point and initializer of DevFun and associated libraries. |
| [DevFunInitializerProvider](-dev-fun-initializer-provider/index.md) | `class DevFunInitializerProvider : `[`ContentProvider`](https://developer.android.com/reference/android/content/ContentProvider.html)<br>Used to automatically initialize [DevFun](-dev-fun/index.md) without user input. |
| [DevFunModule](-dev-fun-module/index.md) | `interface DevFunModule`<br>Modules that extend/use the functionality of [DevFun](-dev-fun/index.md). |
| [DeveloperReference](-developer-reference/index.md) | `interface DeveloperReference`<br>Defines references to annotations that are annotated by meta annotation [DeveloperAnnotation](../com.nextfaze.devfun.annotations/-developer-annotation/index.md). |
| [FunctionDefinition](-function-definition/index.md) | `interface FunctionDefinition`<br>Functions/methods annotated with [DeveloperFunction](../com.nextfaze.devfun.annotations/-developer-function/index.md) will be defined using this interface at compile time. |
| [FunctionItem](-function-item/index.md) | `interface FunctionItem`<br>Items are converted from [FunctionDefinition](-function-definition/index.md) at run-time via [FunctionTransformer](-function-transformer/index.md). |
| [FunctionTransformer](-function-transformer/index.md) | `interface FunctionTransformer`<br>Function transformers filter and/or convert a [FunctionDefinition](-function-definition/index.md) to [FunctionItem](-function-item/index.md). |
| [InvokeResult](-invoke-result/index.md) | `interface InvokeResult`<br>Function invocations will be wrapped by this. |
| [SimpleFunctionItem](-simple-function-item/index.md) | `open class SimpleFunctionItem : `[`FunctionItem`](-function-item/index.md)<br>Convenience class for [FunctionItem](-function-item/index.md) to extend from, providing standard [equals](-simple-function-item/equals.md) and [hashCode](-simple-function-item/hash-code.md) implementations. |
| [SingleFunctionTransformer](-single-function-transformer/index.md) | `object SingleFunctionTransformer : `[`FunctionTransformer`](-function-transformer/index.md)<br>The default transformer. Effectively just wraps the [FunctionDefinition](-function-definition/index.md) to a [FunctionItem](-function-item/index.md) (1:1). |

### Exceptions

| Name | Summary |
|---|---|
| [DebugException](-debug-exception/index.md) | `class DebugException : `[`Throwable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-throwable/index.html)<br>This will not be caught by the standard DevFun Invoker. |

### Type Aliases

| Name | Summary |
|---|---|
| [ActivityProvider](-activity-provider.md) | `typealias ActivityProvider = () -> `[`Activity`](https://developer.android.com/reference/android/app/Activity.html)`?`<br>Function signature of DevFun's activity tracker/provider. |
| [FunctionArgs](-function-args.md) | `typealias FunctionArgs = `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?>?`<br>Definition for user-supplied arguments (usually supplied from a [FunctionTransformer](-function-transformer/index.md)). |
| [FunctionInvoke](-function-invoke.md) | `typealias FunctionInvoke = (receiver: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?, args: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?>) -> `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?`<br>Definition of generated function to call that invokes the function definition. |
| [OnInitialized](-on-initialized.md) | `typealias OnInitialized = `[`DevFun`](-dev-fun/index.md)`.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Callback signature if/when [DevFun](-dev-fun/index.md) has been initialized. |

### Properties

| Name | Summary |
|---|---|
| [devFun](dev-fun.md) | `val devFun: `[`DevFun`](-dev-fun/index.md)<br>Currently active/initialized instance of [DevFun](-dev-fun/index.md) |
| [devFunVerbose](dev-fun-verbose.md) | `var devFunVerbose: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Controls trace-level logging. Disabled (`false`) by default. |
| [isDevFunInitialized](is-dev-fun-initialized.md) | `val isDevFunInitialized: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Flag indicating if DevFun has been initialized yet. |

### Functions

| Name | Summary |
|---|---|
| [call](call.md) | `fun `[`FunctionItem`](-function-item/index.md)`.call(invoker: `[`Invoker`](../com.nextfaze.devfun.invoke/-invoker/index.md)` = devFun.get()): `[`InvokeResult`](-invoke-result/index.md)`?`<br>Convenience function for invoking a [FunctionItem](-function-item/index.md) using the current [devFun](dev-fun.md) instance. |
