package com.nextfaze.devfun.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nextfaze.devfun.core.Composite
import com.nextfaze.devfun.core.Composited
import com.nextfaze.devfun.core.DevFun
import com.nextfaze.devfun.core.R
import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.invoke.view.simple.ErrorParameterView
import com.nextfaze.devfun.invoke.view.simple.InjectedParameterView
import kotlin.reflect.KClass

/**
 * Used by [DevFun] to inflate views when needed.
 *
 * @see ViewFactoryProvider
 * @see viewFactory
 */
interface ViewFactory<out V : View> {
    /**
     * Called when this view should be inflated.
     *
     * @param inflater The [LayoutInflater] object that can be used to inflate the view.
     * @param container If non-null, this is the parent view that the inflated view will be attached to.
     * You should not add the view itself, but this can be used to generate the `LayoutParams` of the view.
     *
     * @return The inflated view.
     */
    fun inflate(inflater: LayoutInflater, container: ViewGroup?): V
}

/**
 * Provides [ViewFactory] instances for some class type/key.
 *
 * The class "key" does need to be in any way related to the resulting view factory, and is merely just a tag-like system.
 *
 * @see viewFactoryProvider
 */
interface ViewFactoryProvider {
    /**
     * Get a view factory for some [clazz] key.
     *
     * @return The view factory assigned to this [clazz] key or `null` if no factory is assigned to this key yet.
     */
    operator fun get(clazz: KClass<*>): ViewFactory<View>?
}

/**
 * A [ViewFactoryProvider] that delegates to other providers.
 *
 * Checks in reverse order of added.
 * i.e. most recently added is checked first.
 *
 * In general you should not need to use this.
 */
interface CompositeViewFactoryProvider : ViewFactoryProvider, Composite<ViewFactoryProvider>

internal class DefaultCompositeViewFactory :
    CompositeViewFactoryProvider,
    Composited<ViewFactoryProvider>() {

    private val log = logger()

    init {
        this += DevFunSimpleInvokeViewsFactory()
    }

    override operator fun get(clazz: KClass<*>): ViewFactory<View>? {
        iterator().forEach { factory ->
            log.t { "Try-get view for $clazz from $factory" }
            factory[clazz]?.let {
                log.t { "> Got $it" }
                return it
            }
        }
        return null
    }
}

private class DevFunSimpleInvokeViewsFactory : ViewFactoryProvider {
    override fun get(clazz: KClass<*>): ViewFactory<View>? =
        when (clazz) {
            InjectedParameterView::class -> viewFactory(R.layout.df_devfun_injected)
            ErrorParameterView::class -> viewFactory(R.layout.df_devfun_type_error)
            else -> null
        }
}
