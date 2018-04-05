package com.nextfaze.devfun.view

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Convenience method to create a view factory via standard inflation.
 *
 * In the simplest case, you can simply use: `inflate(R.layout.my_layout)`
 *
 * If you also wish to adjust it after inflation:
 * ```kotlin
 * inflate<TextInputLayout>(R.layout.my_text_input_layout) {
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
 * @see ViewFactoryProvider
 */
inline fun <reified V : View> inflate(@LayoutRes layoutId: Int, crossinline apply: (V.() -> Unit) = {}): ViewFactory<V> =
    object : ViewFactory<V> {
        override fun inflate(inflater: LayoutInflater, container: ViewGroup?): V =
            (inflater.inflate(layoutId, container, false) as V).apply(apply)
    }
