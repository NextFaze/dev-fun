package com.nextfaze.devfun.function

import com.nextfaze.devfun.category.CategoryDefinition
import com.nextfaze.devfun.inject.InstanceProvider

/**
 * Items are converted from [FunctionDefinition] at run-time via [FunctionTransformer].
 *
 * Implementers of this need to ensure a valid [equals] and [hashCode] implementation.
 * Unless necessary, you should extend from convenience class [SimpleFunctionItem] that does this already.
 *
 * _Do not to include [invoke] in the `equals`/`hashCode` implementations as it is usually a non-comparable anonymous instance._
 *
 * @see SimpleFunctionItem.equals
 * @see SimpleFunctionItem.hashCode
 */
interface FunctionItem {
    /**
     * The original definition this item was derived from.
     */
    val function: FunctionDefinition

    /**
     * The resolved category definition.
     *
     * This may be the same from [function], as declared on the class, or a combination of both.
     *
     * @see FunctionDefinition.category
     */
    val category: CategoryDefinition

    /**
     * The formatted display name.
     *
     * @see FunctionDefinition.name
     */
    val name: CharSequence get() = function.name

    /**
     * A "grouping" for this item.
     *
     * Items with the same group will be grouped together under this title/header.
     *
     * @see CategoryDefinition.group
     */
    val group: CharSequence? get() = category.group?.takeIf { it.isNotEmpty() }

    /**
     * Custom arguments for the [invoke] invocation. Otherwise arguments will be requested from an [InstanceProvider].
     *
     * The list can be any size, with arguments passed to [invoke] based on their index within the list.
     *
     * Entries that are [Unit] (or index out of range) will fallback to using the [InstanceProvider].
     */
    val args: FunctionArgs get() = null

    /**
     * Function to be invoked on click.
     *
     * This is usually just [FunctionDefinition.invoke].
     */
    val invoke: FunctionInvoke get() = function.invoke
}

/**
 * Convenience class for [FunctionItem] to extend from, providing standard [equals] and [hashCode] implementations.
 *
 * _If overriding [equals]/[hashCode], do not to include [invoke] in the implementations as it is usually a non-comparable anonymous instance._
 *
 * @see FunctionTransformer
 */
open class SimpleFunctionItem(
    override val function: FunctionDefinition,
    override val category: CategoryDefinition
) : FunctionItem {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FunctionItem) return false

        if (name != other.name) return false
        if (group != other.group) return false
        if (args != other.args) return false
        if (function != other.function) return false
        if (category != other.category) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + (group?.hashCode() ?: 0)
        result = 31 * result + (args?.filter { it !== this }?.hashCode() ?: 0)
        result = 31 * result + function.hashCode()
        result = 31 * result + category.hashCode()
        return result
    }

    override fun toString() =
        "SimpleFunctionItem(name='$name', group=$group, args=${args?.filter { it !== this }}, function=$function, category=$category)"
}
