package com.nextfaze.devfun.category

import com.nextfaze.devfun.function.FunctionDefinition
import com.nextfaze.devfun.function.FunctionItem

/**
 * Items are derived from [CategoryDefinition] at run-time during [FunctionDefinition] processing.
 *
 * Undefined categories will be derived from a definition's enclosing class.
 */
interface CategoryItem {
    /**
     * Categories with the same name will be merged at runtime (case-sensitive).
     *
     * @see CategoryDefinition.name
     */
    val name: CharSequence

    /**
     * Categories are ordered by [order] (top->bottom is lowest->highest), then alphabetically by [name].
     *
     * @see CategoryDefinition.order
     */
    val order: Int get() = 0

    /**
     * List of items for this category.
     *
     * Categories with no items will be ignored at run-time.
     */
    val items: List<FunctionItem>
}
