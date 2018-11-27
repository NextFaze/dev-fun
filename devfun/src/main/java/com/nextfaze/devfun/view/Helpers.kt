package com.nextfaze.devfun.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import kotlin.reflect.KClass

/**
 * Convenience method to create a view factory via standard inflation.
 *
 * In the simplest case, you can simply use: `inflate(R.layout.my_layout)`
 *
 * If you also wish to adjust it after inflation:
 * ```kotlin
 * viewFactory<TextInputLayout>(R.layout.my_text_input_layout) {
 *     editText!!.apply {
 *         inputType = TYPE_CLASS_TEXT
 *         text = "Default Text"
 *         ...
 *     }
 *     ...
 * }
 * ```
 *
 * _Be aware that this creates a [ViewFactory] so inflation code could be executed at any time in any context._
 *
 * If you want to create a single-typed/keyed provider then use convenience method [viewFactoryProvider].
 *
 * @see ViewFactoryProvider
 */
inline fun <reified V : View> viewFactory(@LayoutRes layoutId: Int, crossinline apply: (V.() -> Unit) = {}): ViewFactory<V> =
    object : ViewFactory<V> {
        override fun inflate(inflater: LayoutInflater, container: ViewGroup?): V =
            (inflater.inflate(layoutId, container, false) as V).apply(apply)
    }

/**
 * Convenience method to create a view factory provider for a single type key.
 *
 * Example usage from demo (~line 64 `demoMenuHeaderFactory` in `com.nextfaze.devfun.demo.devfun.DevFun.kt`):
 * ```kotlin
 * // MenuHeader is the "key" (used by DevMenu to inflate the menu header)
 * // DemoMenuHeaderView is the custom view type
 * devFun.viewFactories += viewFactoryProvider<MenuHeader, DemoMenuHeaderView>(R.layout.demo_menu_header) {
 *     setTitle(activityProvider()!!::class.splitSimpleName)
 *     setCurrentUser(session.user)
 * }
 * ```
 *
 * _Be aware that this creates a [ViewFactoryProvider] that returns a [ViewFactory] - thus inflation code could be executed at any time in any context._
 *
 * If you only need to create a [ViewFactory] then use convenience method [viewFactory].
 *
 * @See ViewFactory
 */
inline fun <reified K : Any, reified V : View> viewFactoryProvider(@LayoutRes layoutId: Int, crossinline apply: (V.() -> Unit) = {}): ViewFactoryProvider =
    object : ViewFactoryProvider {
        override fun get(clazz: KClass<*>): ViewFactory<V>? {
            if (clazz != K::class) return null

            return object : ViewFactory<V> {
                override fun inflate(inflater: LayoutInflater, container: ViewGroup?): V =
                    (inflater.inflate(layoutId, container, false) as V).apply(apply)
            }
        }
    }
