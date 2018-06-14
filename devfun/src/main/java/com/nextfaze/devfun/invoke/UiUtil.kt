package com.nextfaze.devfun.invoke

import kotlin.reflect.KClass

/**
 * Utility interface to easily generate an invoke UI/dialog. _(experimental)_
 *
 * @see uiField
 * @see uiButton
 * @see uiFunction
 */
interface UiField<T : Any> : Parameter, WithInitialValue<T> {
    /** Callback when the value is to be set. */
    val setValue: (T) -> Unit
}

/**
 * Utility function to create a [UiField] instance. _(experimental)_
 *
 * @param name The name of this option.
 * @param initialValue The initial value.
 * @param onSetValue  Callback when the value is to be set.
 */
fun <T : Any> uiField(
    name: CharSequence,
    initialValue: T,
    annotations: List<Annotation> = emptyList(),
    onSetValue: (T) -> Unit
): UiField<T> = SimpleUiOption(name, initialValue, annotations = annotations, setValue = onSetValue)

private class SimpleUiOption<T : Any>(
    override val name: CharSequence,
    override var value: T,
    override val type: KClass<out T> = value::class,
    override val annotations: List<Annotation>,
    override val setValue: (T) -> Unit
) : UiField<T>
