package com.nextfaze.devfun.annotations

import com.nextfaze.devfun.annotations.Dagger2Scope.UNDEFINED
import kotlin.annotation.AnnotationRetention.SOURCE
import kotlin.annotation.AnnotationTarget.*

/**
 * Some range of scopes for use with [Dagger2Component]. Priority is based on their ordinal value (higher = broader scope).
 *
 * If for whatever reason you want more control or don't want named like this then you can manually set [Dagger2Component.priority].
 */
enum class Dagger2Scope(val isFragmentActivityRequired: Boolean = false, val isActivityRequired: Boolean = isFragmentActivityRequired) {
    UNDEFINED,
    LOWEST(isActivityRequired = true),
    VIEW(isActivityRequired = true),
    LOWER(isActivityRequired = true),
    FRAGMENT(isFragmentActivityRequired = true),
    LOW(isActivityRequired = true),
    ACTIVITY(isActivityRequired = true),
    HIGH(isActivityRequired = true),
    RETAINED_FRAGMENT(isFragmentActivityRequired = true),
    HIGHER,
    APPLICATION,
    HIGHEST
}

/**
 * Annotated functions (`fun`, properties, or property getters (`@get:Dagger2Component`)) will be checked/used as Dagger 2 components.
 *
 * If all [scope] and [priority] are unset/default then a best-guess will be made based on where the reference is.
 *
 * i.e.
 * - If its in an `Application` class then it'll be assumed to be application level etc.
 * - If it's an extension function then the receiver will be used.
 *
 * @see Dagger2Scope
 */
@Suppress("unused")
@Retention(SOURCE)
@DeveloperAnnotation(developerReference = true)
@Target(PROPERTY, PROPERTY_GETTER, FUNCTION)
annotation class Dagger2Component(
    /**
     * The scope of this component.
     *
     * If [scope] is [UNDEFINED] and [priority] is `0` then a best-guess will be made based on where the component is.
     * i.e. If its in an Application class then it'll be assumed to be application level etc.
     * If it's an extension function then the receiver type will be used as to "where".
     */
    val scope: Dagger2Scope = UNDEFINED,

    /**
     * Here if for whatever reason you can't/don't want to use [scope] - will only be used if [scope] is [UNDEFINED].
     *
     * If [scope] is [UNDEFINED] and [priority] is `0` then a best-guess will be made based on where the component is.
     * i.e. If its in an Application class then it'll be assumed to be application level etc.
     * If it's an extension function then the receiver type will be used as to "where".
     *
     * If [scope] is set it takes priority (which is just the enum ordinal value).
     *
     * @see Dagger2Scope
     */
    val priority: Int = 0,

    /**
     * Here if for whatever reason you can't/don't want to use [scope] - will only be used if [scope] is [UNDEFINED] and [priority] is non-zero.
     *
     * @see Dagger2Scope
     */
    val isActivityRequired: Boolean = false,

    /**
     * Here if for whatever reason you can't/don't want to use [scope] - will only be used if [scope] is [UNDEFINED] and [priority] is non-zero.
     *
     * @see Dagger2Scope
     */
    val isFragmentActivityRequired: Boolean = false
)
