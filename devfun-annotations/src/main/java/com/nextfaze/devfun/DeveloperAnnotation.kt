package com.nextfaze.devfun

import com.nextfaze.devfun.category.ContextCategory
import com.nextfaze.devfun.category.DeveloperCategory
import com.nextfaze.devfun.function.DeveloperArguments
import com.nextfaze.devfun.function.DeveloperFunction
import com.nextfaze.devfun.function.DeveloperProperty
import com.nextfaze.devfun.reference.Dagger2Component
import com.nextfaze.devfun.reference.DeveloperLogger
import com.nextfaze.devfun.reference.DeveloperLoggerProperties
import com.nextfaze.devfun.reference.DeveloperReference
import com.nextfaze.devfun.reference.ReferenceDefinition
import com.nextfaze.devfun.reference.getProperties
import com.nextfaze.devfun.reference.withProperties
import kotlin.annotation.AnnotationRetention.BINARY
import kotlin.annotation.AnnotationTarget.ANNOTATION_CLASS

/**
 * Annotation used to by DevFun to "tag" references to other DevFun/developer -related annotations.
 *
 *
 * # Behaviour
 * By default this annotation will do nothing - you must set one of the flags to `true` to take effect.
 *
 * _This is a somewhat experimental annotation - though DevFun uses it directly now (`@DeveloperFunction` is annotated `@DeveloperAnnotation(developerFunction = true)`).
 * Having said that, attempts have been made for it to be quite versatile but it is still quite a recent aspect._
 *
 * ## [developerFunction]
 * When `true` the compiler will treat it as if it was an @[DeveloperFunction] annotation. In this state the compiler will check for the
 * same fields of `@DeveloperFunction`, falling back to the standard defaults if they are absent.
 *
 * If you have different defaults defined compared to [DeveloperFunction] then these values will be written as if you had used
 * `@DeveloperFunction(field = value)` at the declaration site - this behaviour is somewhat experimental. Please report any issues you encounter.
 *
 * An example of this can be seen with @[DeveloperProperty].
 *
 * ## [developerCategory]
 * When `true` the compiler will treat it as if it was an @[DeveloperCategory] annotation. In this state the compiler will check for the
 * same fields of `@DeveloperCategory`, falling back to the standard defaults if they are absent.
 *
 * An example of this can be seen with @[ContextCategory].
 *
 * ## [developerReference]
 * When `true` the compiler will treat it as if it was an @[DeveloperReference] annotation.
 *
 * An example of this can be seen with @[Dagger2Component].
 *
 *
 * # Custom Properties
 * Properties named the same as the DevFun properties have the same meaning/behaviour.
 * Other properties will be "serialized" and are available on the associated definition - DevFun will generate a "properties"
 * object for annotations (e.g. [DeveloperLogger] -> [DeveloperLoggerProperties])
 *
 * Use helper functions [withProperties] or [getProperties] to get an instance of the properties object.
 * Unset values will return their default values as defined in the annotation.
 *
 * @param developerFunction Set to `true` to have the compiler treat the annotation as a @[DeveloperFunction]. _(experimental)_
 * @param developerCategory Set to `true` to have the compiler treat the annotation as a @[DeveloperCategory]. _(experimental)_
 * @param developerReference Set to `true` to have the compiler treat the annotation as a @[DeveloperReference]. _(experimental)_
 *
 * @see Dagger2Component
 * @see DeveloperArguments
 * @see ReferenceDefinition
 */
@Retention(BINARY)
@Target(ANNOTATION_CLASS)
annotation class DeveloperAnnotation(
    val developerFunction: Boolean = false,
    val developerCategory: Boolean = false,
    val developerReference: Boolean = false
)
