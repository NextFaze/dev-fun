package com.nextfaze.devfun.annotations

import kotlin.annotation.AnnotationRetention.BINARY
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.PROPERTY

@Target(FUNCTION, PROPERTY)
@Retention(BINARY)
@DeveloperAnnotation
annotation class DeveloperLogger
