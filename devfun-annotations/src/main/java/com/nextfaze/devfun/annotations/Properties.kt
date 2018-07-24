package com.nextfaze.devfun.annotations

import com.nextfaze.devfun.core.FunctionTransformer
import kotlin.annotation.AnnotationRetention.BINARY
import kotlin.annotation.AnnotationTarget.PROPERTY
import kotlin.reflect.KClass

/**
 * A function transformer that tells DevFun how to render Kotlin properties.
 *
 * @see DeveloperProperty
 */
interface PropertyTransformer : FunctionTransformer

/**
 * An annotation that, when used on Kotlin properties, allows DevFun to provide the means of getting/setting properties on the fly.
 *
 * For simple/registered types DevFun will render a UI by the same means as when invoking a function.
 *
 * DevFun will correctly use the delegated getter/setter.
 *
 * _Be aware, when DevFun attempts to process its generated code (such as when you open the DevMenu or access the web server), property
 * getters will be called for their `toString` (special exception for `lazy` types however)._
 *
 * __This feature is experimental.__
 *
 * @see PropertyTransformer
 * @see DeveloperFunction
 */
@Target(PROPERTY)
@Retention(BINARY)
@DeveloperAnnotation(developerFunction = true)
annotation class DeveloperProperty(
    val value: String = "",
    val category: DeveloperCategory = DeveloperCategory(group = "Properties"),
    val requiresApi: Int = 0,
    val transformer: KClass<out FunctionTransformer> = PropertyTransformer::class
)
