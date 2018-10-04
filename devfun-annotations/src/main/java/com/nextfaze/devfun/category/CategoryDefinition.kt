package com.nextfaze.devfun.category

import com.nextfaze.devfun.function.DeveloperFunction
import com.nextfaze.devfun.function.FunctionDefinition
import kotlin.reflect.KClass

/**
 * Classes annotated with [DeveloperCategory] will be defined using this interface at compile time.
 */
interface CategoryDefinition {
    /**
     * [DeveloperFunction] usages in [clazz] will use this category.
     */
    val clazz: KClass<*>? get() = null

    /**
     * The name of this category as taken from [DeveloperCategory.value].
     *
     * If unset the name will be resolved from [clazz] (simple name split-camel-case), or "Misc".
     */
    val name: CharSequence? get() = null

    /**
     * Items that match this category will be placed in this group, as taken from [DeveloperCategory.group].
     *
     * When set to an empty string on a [FunctionDefinition.category], will remove any inherited groups.
     *
     * This value is ignored when null.
     */
    val group: CharSequence? get() = null

    /**
     * The category ordering as taken from [DeveloperCategory.order].
     *
     * If this is `null` then it was not set explicitly for this definition and will be resolved at runtime (i.e. if
     * this category was defined elsewhere already). Otherwise it will default to `0`.
     *
     * **When a category's order is declared more than once the outcome is effectively undefined, leaving you at the
     * mercy of the annotation processor (javac does not define one) and parsing/processing order.**
     */
    val order: Int? get() = null
}
