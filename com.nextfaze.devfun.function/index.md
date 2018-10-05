[gh-pages](../index.md) / [com.nextfaze.devfun.function](./index.md)

## Package com.nextfaze.devfun.function

### Types

| Name | Summary |
|---|---|
| [ArgsProperties](-args-properties/index.md) | `interface ArgsProperties`<br>Properties interface for @[Args](-args/index.md). |
| [ArgumentsTransformer](-arguments-transformer.md) | `interface ArgumentsTransformer : `[`FunctionTransformer`](-function-transformer/index.md)<br>A function transformer that tells DevFun how to generate functions for annotation-defined arguments. |
| [DeveloperArgumentsProperties](-developer-arguments-properties/index.md) | `interface DeveloperArgumentsProperties`<br>Properties interface for @[DeveloperArguments](-developer-arguments/index.md). |
| [DeveloperFunctionProperties](-developer-function-properties/index.md) | `interface DeveloperFunctionProperties`<br>Properties interface for @[DeveloperFunction](-developer-function/index.md). |
| [DeveloperPropertyProperties](-developer-property-properties/index.md) | `interface DeveloperPropertyProperties`<br>Properties interface for @[DeveloperProperty](-developer-property/index.md). |
| [FunctionDefinition](-function-definition/index.md) | `interface FunctionDefinition`<br>Functions/methods annotated with [DeveloperFunction](-developer-function/index.md) will be defined using this interface at compile time. |
| [FunctionItem](-function-item/index.md) | `interface FunctionItem`<br>Items are converted from [FunctionDefinition](-function-definition/index.md) at run-time via [FunctionTransformer](-function-transformer/index.md). |
| [FunctionTransformer](-function-transformer/index.md) | `interface FunctionTransformer`<br>Function transformers filter and/or convert a [FunctionDefinition](-function-definition/index.md) to [FunctionItem](-function-item/index.md). |
| [InvokeResult](-invoke-result/index.md) | `interface InvokeResult`<br>Function invocations will be wrapped by this. |
| [PropertyTransformer](-property-transformer.md) | `interface PropertyTransformer : `[`FunctionTransformer`](-function-transformer/index.md)<br>A function transformer that tells DevFun how to render Kotlin properties. |
| [SimpleFunctionItem](-simple-function-item/index.md) | `open class SimpleFunctionItem : `[`FunctionItem`](-function-item/index.md)<br>Convenience class for [FunctionItem](-function-item/index.md) to extend from, providing standard [equals](-simple-function-item/equals.md) and [hashCode](-simple-function-item/hash-code.md) implementations. |
| [SingleFunctionTransformer](-single-function-transformer/index.md) | `object SingleFunctionTransformer : `[`FunctionTransformer`](-function-transformer/index.md)<br>The default transformer. Effectively just wraps the [FunctionDefinition](-function-definition/index.md) to a [FunctionItem](-function-item/index.md) (1:1). |

### Annotations

| Name | Summary |
|---|---|
| [Args](-args/index.md) | `annotation class Args`<br>Nested annotation for declaring the arguments of a function invocation. |
| [DeveloperArguments](-developer-arguments/index.md) | `annotation class DeveloperArguments`<br>An alternative to [DeveloperFunction](-developer-function/index.md) that allows you to provide arguments for multiple invocations. |
| [DeveloperFunction](-developer-function/index.md) | `annotation class DeveloperFunction`<br>Functions/methods annotated with this will be shown on the Developer Menu (and other modules). |
| [DeveloperProperty](-developer-property/index.md) | `annotation class DeveloperProperty`<br>An annotation that, when used on Kotlin properties, allows DevFun to provide the means of getting/setting properties on the fly. |

### Type Aliases

| Name | Summary |
|---|---|
| [FunctionArgs](-function-args.md) | `typealias FunctionArgs = `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?>?`<br>Definition for user-supplied arguments (usually supplied from a [FunctionTransformer](-function-transformer/index.md)). |
| [FunctionInvoke](-function-invoke.md) | `typealias FunctionInvoke = (receiver: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?, args: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?>) -> `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?`<br>Definition of generated function to call that invokes the function definition. |
