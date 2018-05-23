package com.nextfaze.devfun.annotations

import kotlin.annotation.AnnotationRetention.BINARY
import kotlin.annotation.AnnotationTarget.*

/**
 * Annotated references will be rendered as an overlay.
 *
 * TODO support FIELD (if this is desired make an issue to expedite).
 */
@Target(FUNCTION, PROPERTY, CLASS, PROPERTY_GETTER)
@Retention(BINARY)
@DeveloperAnnotation
annotation class DeveloperLogger
