package com.nextfaze.devfun.annotations

import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.PROPERTY_GETTER

/**
 * Some range of scopes for use with [Dagger2Component]. Priority is based on their ordinal value (higher = broader scope).
 *
 * If for whatever reason you want more control or don't want named like this then you can manually set [Dagger2Component.priority].
 */
enum class Dagger2Scope {
    UNDEFINED,
    LOWEST,
    VIEW,
    LOWER,
    FRAGMENT,
    LOW,
    ACTIVITY,
    HIGH,
    RETAINED_FRAGMENT,
    HIGHER,
    APPLICATION,
    HIGHEST
}

/**
 * Annotated functions (`fun` or property getters with `@get:Dagger2Component`) will be checked/used as Dagger 2 components.
 *
 * If all [scope] and [priority] are unset/default then a best-guess will be made based on where the reference is.
 *
 * i.e.
 * - If its in an `Application` class then it'll be assumed to be application level etc.
 * - If it's an extension function then the receiver will be used.
 *
 * @see Dagger2Scope
 */
@Retention(RUNTIME)
@DeveloperAnnotation
@Target(PROPERTY_GETTER, FUNCTION)
annotation class Dagger2Component(
    /**
     * The scope of this component.
     *
     * If [scope] and [priority] are unset then a best-guess will be made based on where the component is.
     * i.e. If its in an Application class then it'll be assumed to be application level etc.
     * If it's an extension function then the receiver will be used.
     */
    val scope: Dagger2Scope = Dagger2Scope.UNDEFINED,

    /**
     * Here if for whatever reason you don't want to use [scope].
     *
     * If [scope] is set it takes priority (which is just the enum ordinal value).
     *
     * @see Dagger2Scope
     */
    val priority: Int = 0
)
