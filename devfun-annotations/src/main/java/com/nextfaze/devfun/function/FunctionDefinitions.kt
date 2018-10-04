package com.nextfaze.devfun.function

import com.nextfaze.devfun.DebugException
import com.nextfaze.devfun.category.CategoryDefinition
import java.lang.reflect.Method
import kotlin.reflect.KClass

/**
 * Definition for user-supplied arguments (usually supplied from a [FunctionTransformer]).
 */
typealias FunctionArgs = List<Any?>?

/**
 * Definition of generated function to call that invokes the function definition.
 *
 * - The __receiver__ is the object to be invoked against (pass in `null` for static/`object`) types.
 *   Use convenience extension functions (e.g. [FunctionItem]`.receiverClassForInvocation`) to more easily locate/resolve receiver instance.
 *
 * - The __args__ is the arguments for the method, matching the methods argument count and ordering.
 *   Similarly to receiver, convenience extension functions exist to assist with argument resolution.
 *
 * Note: At present nullable types are not inherently supported.
 * KAPT does not provide enough information to determine if a type is nullable or not (and there are other
 * issues to be considered). It is intended to be permitted in the future.
 *
 * @return Invocation of function.
 */
typealias FunctionInvoke = (receiver: Any?, args: List<Any?>) -> Any?

/**
 * Functions/methods annotated with [DeveloperFunction] will be defined using this interface at compile time.
 *
 * Definitions will be convert to items via [FunctionTransformer].
 *
 * @see FunctionItem
 */
interface FunctionDefinition {
    /**
     * The method of this function was defined.
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
    val name: CharSequence get() = method.name.splitCamelCase().substringBefore("\$") // remove internal $suffix part

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
     * Be aware if invoking this directly; no error handling is provided.
     * You should use `devFun.get<Invoker>()` for missing arguments, user input, and exception handling.
     */
    val invoke: FunctionInvoke
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
