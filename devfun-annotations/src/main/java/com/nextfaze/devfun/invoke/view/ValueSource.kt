package com.nextfaze.devfun.invoke.view

import kotlin.reflect.KClass

/**
 * Annotate parameters with this specifying a [ValueSource] class to initialize invoke views with an initial value.
 *
 * See the [ValueSource] documentation for more information and examples.
 *
 *
 * Note: This annotation is optional. If not present then the view state will be whatever it is by default.
 * i.e. A `Switch` will be off, a `TextView` will be empty, etc.
 *
 * @see Values
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class From(
    /** The [ValueSource] class that will be injected/instantiated when the parameter value is needed. */
    val source: KClass<out ValueSource<*>>
)

/**
 * Used in conjunction with [From] or [Values] to provide the Invoke UI with an initial value or set of values.
 *
 * Example usage from DevMenu (~line 215 in `com.nextfaze.devfun.menu.controllers.Cog.kt`):
 * ```kotlin
 * @Constructable
 * private inner class CurrentVisibility : ValueSource<Boolean> {
 *     override val value get() = cogVisible
 * }
 *
 * @DeveloperFunction
 * private fun setCogVisibility(@From(CurrentVisibility::class) visible: Boolean) {
 *     cogVisible = visible
 *     ...
 * }
 * ```
 *
 * The above example will render an Invoke UI with a `Switch` with an initial state representative of the actual state.
 */
interface ValueSource<out V : Any> {
    /** The initial value passed to the Invoke UI dialog. */
    val value: V
}

/**
 * Annotate parameters with this specifying an [Iterable] [ValueSource] class to initialize invoke views with a list of values.
 *
 * See the [ValueSource] documentation for more information and examples.
 *
 *
 * Note: This annotation is optional. If not present then the view state will be whatever it is by default.
 * i.e. A `Switch` will be off, a `TextView` will be empty, etc.
 *
 * This annotation is somewhat experimental.
 * TODO? Allow specifying default value on annotation
 *
 * @see From
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class Values(
    /** The [ValueSource] class that will be injected/instantiated when the parameter value is needed. */
    val source: KClass<out ValueSource<Iterable<*>>>
)
