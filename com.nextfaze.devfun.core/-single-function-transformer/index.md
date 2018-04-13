[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [SingleFunctionTransformer](./index.md)

# SingleFunctionTransformer

`object SingleFunctionTransformer : `[`FunctionTransformer`](../-function-transformer/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/core/FunctionTransformer.kt#L78)

The default transformer. Effectively just wraps the [FunctionDefinition](../-function-definition/index.md) to a [FunctionItem](../-function-item/index.md) (1:1).

See [FunctionTransformer](../-function-transformer/index.md) for more details.

### Functions

| Name | Summary |
|---|---|
| [apply](apply.md) | `fun apply(functionDefinition: `[`FunctionDefinition`](../-function-definition/index.md)`, categoryDefinition: `[`CategoryDefinition`](../-category-definition/index.md)`): `[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`SimpleFunctionItem`](../-simple-function-item/index.md)`>`<br>Transforms a [FunctionDefinition](../-function-definition/index.md) to one or more [FunctionItem](../-function-item/index.md). |

### Inherited Functions

| Name | Summary |
|---|---|
| [accept](../-function-transformer/accept.md) | `open fun accept(functionDefinition: `[`FunctionDefinition`](../-function-definition/index.md)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Should this transformer accept this item for transforming. |
