package com.nextfaze.devfun.invoke

import android.view.View
import com.nextfaze.devfun.core.Composite
import com.nextfaze.devfun.core.Composited
import com.nextfaze.devfun.internal.logger
import com.nextfaze.devfun.internal.t
import com.nextfaze.devfun.invoke.view.types.BooleanParameterViewFactoryProvider
import com.nextfaze.devfun.invoke.view.types.StringOrNumberParameterViewFactoryProvider
import com.nextfaze.devfun.view.ViewFactory
import kotlin.reflect.KAnnotatedElement
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KType

/**
 * Effectively just a wrapper for [KParameter] to allow libraries to use it without declaring a dependency on the kotlin-reflect lib.
 *
 * Currently DevFun requires the reflect lib so its safe to hide it like this for implementors to use.
 * In the future (ideally) the need for the reflect lib will be removed without the need for old code to change.
 */
interface Parameter {
    /**
     * The name of the parameter.
     *
     * Will be something like `p0`, `p1`, etc for code compiled without debug information.
     *
     * @see KParameter.name
     */
    val name: String? get() = kParameter.name

    /**
     * The annotations on the parameter.
     *
     * @see KAnnotatedElement.annotations
     */
    val annotations: List<Annotation> get() = kParameter.annotations

    /**
     * The parameter's type/class.
     *
     * @see KParameter.type
     * @see KType.classifier
     */
    val clazz: KClass<*> get() = kParameter.type.classifier as KClass<*>

    /**
     * Reference to the underlying [KParameter].
     *
     * If you need to use this please create an issue for why so that the [Parameter] API can be updated accordingly.
     *
     * If you do you this, you can safely ignore the warnings regarding the kotlin-reflect lib as DevFun requires it.
     * If/when the reflect lib is no longer needed, this field will be deprecated and removed.
     * Having said that, I don't expect it to be removed any time soon.
     */
    val kParameter: KParameter
}

/**
 * A factory that creates views based on parameter attributes to be used when invoking a function with non-injectable parameter types.
 */
interface ParameterViewFactoryProvider {
    /**
     * Get a view factory for some function parameter.
     *
     * @param parameter Parameter information for a function parameter.
     *
     * @return The view factory for this parameter or `null` if this parameter should be handled by another.
     */
    operator fun get(parameter: Parameter): ViewFactory<View>?
}

/**
 * A [ParameterViewFactoryProvider] that delegates to other providers.
 *
 * Checks in reverse order of added.
 * i.e. most recently added is checked first.
 *
 * In general you should not need to use this.
 */
interface CompositeParameterViewFactoryProvider : ParameterViewFactoryProvider, Composite<ParameterViewFactoryProvider>

internal class DefaultCompositeParameterViewFactoryProvider : CompositeParameterViewFactoryProvider,
    Composited<ParameterViewFactoryProvider>() {
    private val log = logger()

    init {
        this += BooleanParameterViewFactoryProvider()
        this += StringOrNumberParameterViewFactoryProvider()
    }

    override fun get(parameter: Parameter): ViewFactory<View>? {
        iterator().forEach {
            log.t { "Try-get view for ${parameter.name} from $it" }
            it[parameter]?.let {
                log.t { "> Got $it" }
                return it
            }
        }
        return null
    }
}
