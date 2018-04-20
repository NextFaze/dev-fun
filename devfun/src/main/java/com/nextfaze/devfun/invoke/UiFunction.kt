package com.nextfaze.devfun.invoke

typealias SimpleInvoke = (List<Any?>) -> Any?

/**
 * Describes a function to be executed via the DevFun Invocation UI.
 *
 * @see uiFunction
 */
interface UiFunction {
    /** The title of this function (the dialog title). */
    val title: CharSequence

    /** The subtitle/description of this function (dialog subtitle). */
    val subtitle: CharSequence? get() = null

    /** The function signature (or something more technical and relevant to the invocation). */
    val signature: CharSequence? get() = null

    /**
     * The parameters of the function.
     *
     * Example usage from shared preferences dialog:
     * ```kotlin
     * class Preference(key: String, override var value: Any) : Parameter, WithInitialValue<Any>, WithNullability {
     *     override val name: String = key
     *     override val type = value::class
     * }
     * ```
     */
    val parameters: List<Parameter>

    /**
     * The invocation of this function.
     *
     * Will be called when the user hits "Execute" with the arguments in the same order as defined by [parameters].
     */
    val invoke: SimpleInvoke
}

/**
 * Utility function to create a [UiFunction] instance.
 *
 * @see Invoker.invoke
 */
fun uiFunction(
    title: CharSequence,
    subtitle: CharSequence? = null,
    signature: CharSequence? = null,
    parameters: List<Parameter>,
    invoke: SimpleInvoke
): UiFunction = SimpleFunction(title, subtitle, signature, parameters, invoke)

private data class SimpleFunction(
    override val title: CharSequence,
    override val subtitle: CharSequence? = null,
    override val signature: CharSequence? = null,
    override val parameters: List<Parameter>,
    override val invoke: SimpleInvoke
) : UiFunction
