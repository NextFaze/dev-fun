package com.nextfaze.devfun.internal

import kotlin.reflect.KParameter

interface WithParameters {
    val parameters: List<KParameter>
}

interface ParameterInstanceProvider {
    /**
     * Try to get an instance of some [parameter].
     *
     * @return An instance of [parameter], or `null` if this provider can not handle the parameter.
     */
    operator fun <T : Any> get(parameter: KParameter): T?
}
