package com.nextfaze.devfun.invoke

import android.support.annotation.StringRes

typealias SimpleInvoke = (List<Any?>) -> Any?
typealias OnClick = () -> Unit

/**
 * Describes a dialog button for use with [UiFunction] to be rendered via the DevFun Invocation UI.
 *
 * @see uiButton
 * @see UiFunction
 * @see uiFunction
 */
interface UiButton {
    /**
     * The button text.
     *
     * If unset the text will be default "Cancel", "Other", or "Execute" depending on the button type.
     *
     * @see textId
     */
    val text: CharSequence?

    /**
     * The button string resource.
     *
     * If unset the text will be default "Cancel", "Other", or "Execute" depending on the button type.
     *
     * @see text
     */
    val textId: Int?

    /** Click listener of this button - called if [invoke] is null. Set this when you don't care about field values. */
    val onClick: OnClick?

    /**
     * The invocation of this button. Set this if you want the field values, otherwise use [onClick] instead.
     *
     * The values will be provided in the same order as defined by [UiFunction.parameters].
     */
    val invoke: SimpleInvoke?
}

/**
 * Describes a function to be executed via the DevFun Invocation UI.
 *
 * @see uiFunction
 * @see UiButton
 * @see uiButton
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

    /** The cancel button. */
    val negativeButton: UiButton? get() = null

    /** An optional neutral button. */
    val neutralButton: UiButton? get() = null

    /** The execute button. */
    val positiveButton: UiButton? get() = null
}

/**
 * Utility function to create a [UiFunction] instance.
 *
 * @see Invoker.invoke
 * @see UiButton
 * @see uiButton
 */
fun uiFunction(
    title: CharSequence,
    subtitle: CharSequence? = null,
    signature: CharSequence? = null,
    parameters: List<Parameter>,
    negativeButton: UiButton? = null,
    neutralButton: UiButton? = null,
    positiveButton: UiButton? = null,
    invoke: SimpleInvoke? = null
): UiFunction =
    SimpleUiFunction(title, subtitle, signature, parameters, negativeButton, neutralButton, positiveButton ?: uiButton(invoke = invoke))

/**
 * Utility function to create a [UiButton] instance for use with [UiFunction].
 *
 * @see uiFunction
 */
fun uiButton(
    text: CharSequence? = null,
    @StringRes textId: Int? = null,
    onClick: OnClick? = null,
    invoke: SimpleInvoke? = null
): UiButton = SimpleUiButton(text, textId, onClick, invoke)

private data class SimpleUiFunction(
    override val title: CharSequence,
    override val subtitle: CharSequence? = null,
    override val signature: CharSequence? = null,
    override val parameters: List<Parameter>,
    override val negativeButton: UiButton? = null,
    override val neutralButton: UiButton? = null,
    override val positiveButton: UiButton? = null
) : UiFunction

private data class SimpleUiButton(
    override val text: CharSequence? = null,
    override val textId: Int? = null,
    override val onClick: OnClick? = null,
    override val invoke: SimpleInvoke? = null
) : UiButton
