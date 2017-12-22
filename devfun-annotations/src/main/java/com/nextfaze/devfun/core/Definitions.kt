package com.nextfaze.devfun.core

import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.inject.InstanceProvider
import com.nextfaze.devfun.internal.*
import java.lang.reflect.Method
import kotlin.reflect.KClass

typealias FunctionArgs = List<Any?>?
typealias FunctionInvoke = (instanceProvider: InstanceProvider, providedArgs: FunctionArgs) -> InvokeResult

/**
 * Functions/methods annotated with [DeveloperFunction] will be defined using this interface at compile time.
 *
 * Definitions will be convert to items via [FunctionTransformer].
 *
 * @see FunctionItem
 */
interface FunctionDefinition {
    /**
     * The method where this function was defined.
     */
    val method: Method

    /**
     * The class where this item was defined.
     */
    val clazz: KClass<out Any> get() = method.declaringClass.kotlin

    /**
     * The name of this item as taken from [DeveloperFunction.value].
     *
     * If unset the method name split-camel-case will be used.
     */
    val name: CharSequence get() = method.name.splitCamelCase()

    /**
     * The category for this definition as taken from [DeveloperFunction.category].
     *
     * This value is ignored when it is empty.
     */
    val category: CategoryDefinition? get(): CategoryDefinition? = null

    /**
     * Required API to allow this item to be shown as taken from [DeveloperFunction.requiresApi].
     *
     * This value is ignored when it is `<= 0`.
     */
    val requiresApi: Int get() = 0

    /**
     * Function transformer for this instance as taken from [DeveloperFunction.transformer].
     *
     * Adds to and/or transforms the standard invoke call.
     */
    val transformer: KClass<out FunctionTransformer> get() = SingleFunctionTransformer::class

    /**
     * Called when this item is to be invoked.
     *
     * Return `true` to dismiss menu on invoke.
     */
    val invoke: FunctionInvoke
}

/**
 * Classes annotated with [DeveloperCategory] will be defined using this interface at compile time.
 */
interface CategoryDefinition {
    /**
     * [DeveloperFunction] uses in [clazz] will use this category.
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

/**
 * Function invocations will be wrapped by this.
 */
interface InvokeResult {
    /**
     *  The return value of the function invocation.
     *
     *  [exception] will be non-null on failure - (not including [DebugException], which will not be caught).
     */
    val value: Any?

    /**
     * Any exceptions thrown while attempting to invoke the function.
     *
     * This will be `null` on success.
     */
    val exception: Throwable?
}

/**
 * This will not be caught by the generated [FunctionInvoke] call.
 *
 * i.e. Under most conditions, if this is thrown it will crash your app.
 */
class DebugException(message: String = "Debug") : Throwable(message)
