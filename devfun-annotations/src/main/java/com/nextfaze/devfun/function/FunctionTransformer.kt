package com.nextfaze.devfun.function

import com.nextfaze.devfun.category.CategoryDefinition

/**
 * Function transformers filter and/or convert a [FunctionDefinition] to [FunctionItem].
 *
 * Item lifecycle:
 * - [DeveloperFunction] → [FunctionDefinition] → __[FunctionTransformer]__ → [FunctionItem] → `Menu Item`
 *
 * They can be used to:
 * - Provide a list of items for a single function (such as a list of credentials for an authentic function see
 * [DeveloperFunction.transformer] for an example)
 * - To remove items from further processing (such as functions that require an API greater than that available - see
 * [DeveloperFunction.requiresApi] for use)
 * - Change the category of items (put them into the "Context" category)
 * - Override anything about the function definition or invocation process
 * - And more - most things are described using interfaces so customising behaviour can be as simple as using Kotlin delegation and overriding a single function/value.
 *
 *  If a transformer chooses to [accept] an item, it still has the choice of ignoring it by returning `null` from [apply].
 *  Anything but `null` will remove the item from further processing. Thus returning an empty list will effectively
 *  filter the item (as done in `RequiresApiTransformer`).
 *
 * ## Standard Transformers
 *  Currently there are four transformers, which are checked in order of declaration (from `Transformers.kt`);
 *  ```kotlin
 * internal val TRANSFORMERS = listOf(
 *         RequiresApiTransformer::class,
 *         ContextTransformer::class,
 *         CustomProviderTransformer::class,
 *         SingleFunctionTransformer::class
 * )
 *  ```
 *
 * ### `RequiresApiTransformer`
 * Filters items that require an API greater than that available - as declared by [FunctionDefinition.requiresApi].
 *
 * ### `ContextTransformer`
 * Filters (must be active) and tweaks (overrides the category) items that are relevant to the current screen.
 *
 * If an item is defined in an `Activity` or `Fragment`, and that `Activity` is active or the `Fragment` is added (to
 * the current `Activity`), then the result from the item's original [FunctionDefinition.transformer] is wrapped with a
 * `ContextFunctionItem` that overrides the category with a `ContextCategory`. This sets the category to "Context", and
 * its group to the class where the function is defined -  e.g. "Main Activity" or "Navigation Fragment", etc.
 *
 * Items that aren't on the current screen are filtered out (by returning an empty list - stopping further processing).
 *
 * ### `CustomProviderTransformer`
 * Delegates to a user declared transformer - as declared by [FunctionDefinition.transformer].
 *
 * ### `SingleFunctionTransformer`
 * Final/default transformer that effectively just wraps a [FunctionDefinition] in a [FunctionItem] using [SimpleFunctionItem].
 *
 */
interface FunctionTransformer {
    /**
     * Should this transformer accept this item for transforming.
     *
     * Returning `true` does not require a transformation, only that you're interested in it.
     */
    fun accept(functionDefinition: FunctionDefinition) = functionDefinition.transformer == this::class

    /**
     * Transforms a [FunctionDefinition] to one or more [FunctionItem].
     *
     * Return `null` to ignore the item and allow another transformer to process it.
     *
     * Returning anything but `null` will remove the item from other transformers.
     */
    fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition): Collection<FunctionItem>?
}

/**
 * The default transformer. Effectively just wraps the [FunctionDefinition] to a [FunctionItem] (1:1).
 *
 * See [FunctionTransformer] for more details.
 */
object SingleFunctionTransformer : FunctionTransformer {
    override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition) =
        setOf(SimpleFunctionItem(functionDefinition, categoryDefinition))
}
