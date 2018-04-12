package com.nextfaze.devfun.invoke.view

import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.inject.Constructable
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER

/**
 * Annotated `Int` value parameters will render a color picker view rather than an input/edit for use with invoke UI.
 *
 * DevFun's invocation UI is shown when one ore more parameters could not be injected, and thus a UI is provided to
 * allow for manual entry by the user.
 *
 * Example usage from DevMenu (~line 240 in `com.nextfaze.devfun.menu.controllers.Cog.kt`):
 * _(the `@From` is not required and is used to provide the initial value)_
 * ```kotlin
 * @Constructable
 * private inner class CurrentColor : ValueSource<Int> {
 *     override val value get() = cogColor
 * }
 *
 * @DeveloperFunction
 * private fun setColor(@ColorPicker @From(CurrentColor::class) color: Int) {
 *     cogColor = color
 *     ...
 * }
 * ```
 *
 * _Usage Note: The DevFun library `devfun-invoke-view-colorpicker` must be present for the annotation to be handled (it will
 * simply be ignored otherwise).
 * **If you have `devfun-menu` then you will have the colorpicker transitively.**_
 *
 * @see From
 * @See ValueSource
 * @see Constructable
 * @see DeveloperFunction
 */
@Retention(RUNTIME)
@Target(VALUE_PARAMETER)
annotation class ColorPicker
