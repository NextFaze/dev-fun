package com.nextfaze.devfun.core

import com.nextfaze.devfun.annotations.Dagger2Component
import com.nextfaze.devfun.annotations.DeveloperAnnotation
import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import java.lang.reflect.Field
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
 * This will not be caught by the standard DevFun Invoker.
 *
 * i.e. Under most conditions, if this is thrown it will crash your app.
 */
class DebugException(message: String = "Debug") : Throwable(message)

/**
 * Defines references to annotations that are annotated by meta annotation [DeveloperAnnotation].
 *
 * Developer Annotation annotations will be noted by DevFun and wrapped by this interface.
 * An example of this is used by the `devfun-inject-dagger2` module.
 *
 * _At present the scope of references is limited to only `ExecutableElement` but is likely to increase as needed (feel free to make an issue or PR)._
 *
 * __This is an experimental feature and subject to change. External input/suggestions welcome.__
 *
 * @see Dagger2Component
 */
interface DeveloperReference {
    /** The annotation that wanted the reference.*/
    val annotation: KClass<out Annotation>

    /** Map of properties declared on the annotation. */
    val properties: Map<String, *>? get() = null
}

/**
 * A reference to a method generated by meta annotation [DeveloperAnnotation].
 *
 * See [DeveloperReference] for more details.
 */
interface DeveloperMethodReference : DeveloperReference {
    /**
     * The annotated method.
     *
     * TODO In the future this may be changed to resemble [FunctionDefinition.invoke]?
     */
    val method: Method
}

/**
 * A reference to a type generated by meta annotation [DeveloperAnnotation].
 *
 * See [DeveloperReference] for more details.
 */
interface DeveloperTypeReference : DeveloperReference {
    /** The annotated type. */
    val type: KClass<*>
}

/**
 * A reference to a field generated by meta annotation [DeveloperAnnotation].
 *
 * See [DeveloperReference] for more details.
 */
interface DeveloperFieldReference : DeveloperReference {
    /** The annotated field. */
    val field: Field
}
