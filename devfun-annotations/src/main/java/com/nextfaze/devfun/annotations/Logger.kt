@file:Suppress("unused")

package com.nextfaze.devfun.annotations

import com.nextfaze.devfun.overlay.Dock
import kotlin.annotation.AnnotationRetention.SOURCE
import kotlin.annotation.AnnotationTarget.*

/**
 * Annotated references will be rendered as an overlay.
 *
 * TODO support FIELD (if this is desired make an issue to expedite).
 */
@Target(FUNCTION, PROPERTY, CLASS, PROPERTY_GETTER)
@Retention(SOURCE)
@DeveloperAnnotation(developerReference = true)
annotation class DeveloperLogger(
    val enabled: Boolean = true,
    val refreshRate: Long = 1000L,
    val snapToEdge: Boolean = false,
    val dock: Dock = Dock.TOP_LEFT,
    val delta: Float = 0f,
    val top: Float = 0f,
    val left: Float = 0f
)
