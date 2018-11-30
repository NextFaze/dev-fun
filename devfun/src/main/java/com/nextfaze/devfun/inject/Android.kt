package com.nextfaze.devfun.inject

import android.app.Activity
import android.app.Application
import android.app.Service
import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.nextfaze.devfun.core.ActivityProvider
import com.nextfaze.devfun.internal.android.*
import java.util.ArrayDeque
import kotlin.reflect.KClass

interface AndroidInstanceProvider : AndroidInstanceProviderInternal {
    override val activity: Activity?
}

internal class AndroidInstanceProviderImpl(context: Context, private val activityProvider: ActivityProvider) : AndroidInstanceProvider {
    private val applicationContext = context.applicationContext

    override val activity: Activity? get() = activityProvider()

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> get(clazz: KClass<out T>): T? {
        // Activities
        if (clazz.isSubclassOf<Activity>()) {
            // if the class is of type activity, then we need to return null if no activity is found (can't return Context as it could result in type error)
            return activityProvider() as T?
        }

        // Application
        if (clazz.isSubclassOf<Application>()) {
            return applicationContext as T
        }

        // Service
        if (clazz.isSubclassOf<Service>()) {
            return applicationContext as T
        }

        // Context
        val activity = activityProvider()
        if (clazz.isSubclassOf<Context>()) {
            return (activity ?: applicationContext) as T
        }

        // Fragments
        if (activity is FragmentActivity && clazz.isSubclassOf<Fragment>()) {
            activity.supportFragmentManager.iterateChildren().forEach {
                // not sure why, but under some circumstances some of them are null...
                it ?: return@forEach

                if (it.isAdded && it::class.isSubclassOf(clazz)) {
                    @Suppress("UNCHECKED_CAST")
                    return it as T
                }
            }
        }

        // Views
        if (activity != null && clazz.isSubclassOf<View>()) {
            activity.findViewById<ViewGroup>(android.R.id.content)?.let { traverseViewHierarchy(it, clazz) }?.let { return it }
            if (activity is FragmentActivity) {
                activity.supportFragmentManager.iterateChildren().forEach { fragment ->
                    (fragment?.view as? ViewGroup)?.let { traverseViewHierarchy(it, clazz) }?.let { return it }
                }
            }
        }

        return null
    }
}

//
// Fragment Traversal
//

private val peekChildFragmentManagerMethod =
    Fragment::class.java.getDeclaredMethod("peekChildFragmentManager").apply { isAccessible = true }
private val Fragment.hasChildFragmentManager get() = peekChildFragmentManagerMethod.invoke(this) != null

private fun FragmentManager.iterateChildren(): Iterator<Fragment?> {
    class FragmentManagerIterator : Iterator<Fragment?> {
        private val iterators = ArrayDeque<Iterator<Fragment?>>().apply { push(fragments.iterator()) }

        override fun hasNext(): Boolean {
            while (true) {
                val it = iterators.peek() ?: return false
                if (it.hasNext()) {
                    return true
                } else {
                    iterators.pop()
                }
            }
        }

        override fun next(): Fragment? {
            val fragment = iterators.peek().next()
            if (fragment != null && fragment.hasChildFragmentManager) {
                iterators.push(fragment.childFragmentManager.iterateChildren())
            }

            return fragment
        }
    }

    return FragmentManagerIterator()
}

//
// View Traversal
//

private fun <T : Any> traverseViewHierarchy(viewGroup: ViewGroup, clazz: KClass<out T>): T? {
    viewGroup.children<View>().forEach {
        if (it::class.isSubclassOf(clazz)) {
            @Suppress("UNCHECKED_CAST")
            return it as T
        } else if (it is ViewGroup) {
            return traverseViewHierarchy(it, clazz)
        }
    }
    return null
}
