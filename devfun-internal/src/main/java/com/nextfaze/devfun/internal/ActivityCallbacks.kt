package com.nextfaze.devfun.internal

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle

typealias OnActivityCreated = (activity: Activity, savedInstanceState: Bundle?) -> Unit
typealias OnActivityStarted = (activity: Activity) -> Unit
typealias OnActivityResumed = (activity: Activity) -> Unit
typealias OnActivityPaused = (activity: Activity) -> Unit
typealias OnActivityStopped = (activity: Activity) -> Unit
typealias OnActivitySave = (activity: Activity, outState: Bundle) -> Unit
typealias OnActivityDestroyed = (activity: Activity) -> Unit

abstract class AbstractActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit
    override fun onActivityStarted(activity: Activity) = Unit
    override fun onActivityResumed(activity: Activity) = Unit
    override fun onActivityPaused(activity: Activity) = Unit
    override fun onActivityStopped(activity: Activity) = Unit
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
    override fun onActivityDestroyed(activity: Activity) = Unit
}

inline fun Context.registerActivityCallbacks(
    crossinline onCreated: OnActivityCreated = { _, _ -> },
    crossinline onStarted: OnActivityStarted = {},
    crossinline onResumed: OnActivityResumed = {},
    crossinline onPaused: OnActivityPaused = {},
    crossinline onStopped: OnActivityStopped = {},
    crossinline onSave: OnActivitySave = { _, _ -> },
    crossinline onDestroyed: OnActivityDestroyed = {}
) = registerActivityLifecycleCallbacks(object : AbstractActivityLifecycleCallbacks() {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = onCreated.invoke(activity, savedInstanceState)
    override fun onActivityStarted(activity: Activity) = onStarted.invoke(activity)
    override fun onActivityResumed(activity: Activity) = onResumed.invoke(activity)
    override fun onActivityPaused(activity: Activity) = onPaused.invoke(activity)
    override fun onActivityStopped(activity: Activity) = onStopped.invoke(activity)
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = onSave.invoke(activity, outState)
    override fun onActivityDestroyed(activity: Activity) = onDestroyed.invoke(activity)
})

fun Application.ActivityLifecycleCallbacks.unregister(context: Context) = context.unregisterActivityCallbacks(this)
