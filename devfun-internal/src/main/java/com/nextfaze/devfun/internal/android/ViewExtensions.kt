package com.nextfaze.devfun.internal.android

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

@Suppress("UNCHECKED_CAST")
private class ViewGroupIterator<T : View>(private val view: ViewGroup) : Iterator<T> {
    private val childCount = view.childCount
    private var current = 0

    override fun hasNext() = current < childCount
    override fun next() = view.getChildAt(current++) as T
}

fun <T : View> ViewGroup.children(): Iterable<T> = Iterable { ViewGroupIterator<T>(this) }

inline fun <reified T : View> View.inflateView(@LayoutRes id: Int, body: T.() -> Unit) = (View.inflate(context, id, null) as T).apply(body)
