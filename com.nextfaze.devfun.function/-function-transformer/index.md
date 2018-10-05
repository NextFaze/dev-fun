[gh-pages](../../index.md) / [com.nextfaze.devfun.function](../index.md) / [FunctionTransformer](./index.md)

# FunctionTransformer

`interface FunctionTransformer` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/function/FunctionTransformer.kt#L55)

Function transformers filter and/or convert a [FunctionDefinition](../-function-definition/index.md) to [FunctionItem](../-function-item/index.md).

Item lifecycle:

* [DeveloperFunction](../-developer-function/index.md) → [FunctionDefinition](../-function-definition/index.md) → **[FunctionTransformer](./index.md)** → [FunctionItem](../-function-item/index.md) → `Menu Item`

They can be used to:

* Provide a list of items for a single function (such as a list of credentials for an authentic function see
[DeveloperFunction.transformer](../-developer-function/transformer.md) for an example)
* To remove items from further processing (such as functions that require an API greater than that available - see
[DeveloperFunction.requiresApi](../-developer-function/requires-api.md) for use)
* Change the category of items (put them into the "Context" category)
* Override anything about the function definition or invocation process
* And more - most things are described using interfaces so customising behaviour can be as simple as using Kotlin delegation and overriding a single function/value.

If a transformer chooses to [accept](accept.md) an item, it still has the choice of ignoring it by returning `null` from [apply](apply.md).
Anything but `null` will remove the item from further processing. Thus returning an empty list will effectively
filter the item (as done in `RequiresApiTransformer`).

## Standard Transformers

Currently there are four transformers, which are checked in order of declaration (from `Transformers.kt`);

``` kotlin
internal val TRANSFORMERS = listOf(
        RequiresApiTransformer::class,
        ContextTransformer::class,
        CustomProviderTransformer::class,
        SingleFunctionTransformer::class
)
```

### `RequiresApiTransformer`

Filters items that require an API greater than that available - as declared by [FunctionDefinition.requiresApi](../-function-definition/requires-api.md).

### `ContextTransformer`

Filters (must be active) and tweaks (overrides the category) items that are relevant to the current screen.

If an item is defined in an `Activity` or `Fragment`, and that `Activity` is active or the `Fragment` is added (to
the current `Activity`), then the result from the item's original [FunctionDefinition.transformer](../-function-definition/transformer.md) is wrapped with a
`ContextFunctionItem` that overrides the category with a `ContextCategory`. This sets the category to "Context", and
its group to the class where the function is defined -  e.g. "Main Activity" or "Navigation Fragment", etc.

Items that aren't on the current screen are filtered out (by returning an empty list - stopping further processing).

### `CustomProviderTransformer`

Delegates to a user declared transformer - as declared by [FunctionDefinition.transformer](../-function-definition/transformer.md).

### `SingleFunctionTransformer`

Final/default transformer that effectively just wraps a [FunctionDefinition](../-function-definition/index.md) in a [FunctionItem](../-function-item/index.md) using [SimpleFunctionItem](../-simple-function-item/index.md).

### Functions

| Name | Summary |
|---|---|
| [accept](accept.md) | `open fun accept(functionDefinition: `[`FunctionDefinition`](../-function-definition/index.md)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Should this transformer accept this item for transforming. |
| [apply](apply.md) | `abstract fun apply(functionDefinition: `[`FunctionDefinition`](../-function-definition/index.md)`, categoryDefinition: `[`CategoryDefinition`](../../com.nextfaze.devfun.category/-category-definition/index.md)`): `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`FunctionItem`](../-function-item/index.md)`>?`<br>Transforms a [FunctionDefinition](../-function-definition/index.md) to one or more [FunctionItem](../-function-item/index.md). |

### Inheritors

| Name | Summary |
|---|---|
| [ArgumentsTransformer](../-arguments-transformer.md) | `interface ArgumentsTransformer : `[`FunctionTransformer`](./index.md)<br>A function transformer that tells DevFun how to generate functions for annotation-defined arguments. |
| [PropertyTransformer](../-property-transformer.md) | `interface PropertyTransformer : `[`FunctionTransformer`](./index.md)<br>A function transformer that tells DevFun how to render Kotlin properties. |
| [SingleFunctionTransformer](../-single-function-transformer/index.md) | `object SingleFunctionTransformer : `[`FunctionTransformer`](./index.md)<br>The default transformer. Effectively just wraps the [FunctionDefinition](../-function-definition/index.md) to a [FunctionItem](../-function-item/index.md) (1:1). |
