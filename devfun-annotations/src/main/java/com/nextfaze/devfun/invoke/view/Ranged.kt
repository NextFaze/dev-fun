package com.nextfaze.devfun.invoke.view

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER

/**
 * Used to restrict the range of a [Number] for user input. Using this will render a slider rather than a text view.
 *
 * Behind the scenes this is scaling the value range within `0 → 100` (via `SeekBar`).
 * Thus if you want a small range (e.g. 0 → 1 for say a color value), then you should use `to = 255.0` and then normalize it.
 *
 * e.g.
 * ```kotlin
 * @DeveloperFunction
 * fun setRed(@Ranged(from = 0.0, to = 255.0) red: Int) {
 *     val redPct = red / 255f
 *     val someRedColor = Color.rgb(redPct, 0, 0) // pretend rgb() can't take ints...
 *     ...
 * }
 * ```
 *
 * Using this on anything other than a [Number] will do nothing.
 */
@Retention(RUNTIME)
@Target(VALUE_PARAMETER)
annotation class Ranged(
    /** Minimum value _(inclusive)_. */
    val from: Double = 0.0,

    /** Maximum value _(inclusive)_. */
    val to: Double = 100.0
)
