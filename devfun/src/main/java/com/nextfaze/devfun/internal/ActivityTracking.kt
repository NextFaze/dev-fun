@file:Suppress("NON_PUBLIC_CALL_FROM_PUBLIC_INLINE")

package com.nextfaze.devfun.internal

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

typealias ActivityProvider = () -> Activity?

internal typealias OnActivityCreated = (activity: Activity, savedInstanceState: Bundle?) -> Unit
internal typealias OnActivityStarted = (activity: Activity) -> Unit
internal typealias OnActivityResumed = (activity: Activity) -> Unit
internal typealias OnActivityPaused = (activity: Activity) -> Unit
internal typealias OnActivityStopped = (activity: Activity) -> Unit
internal typealias OnActivitySave = (activity: Activity, outState: Bundle) -> Unit
internal typealias OnActivityDestroyed = (activity: Activity) -> Unit

private val NOP1 = { _: Activity -> }
private val NOP2 = { _: Activity, _: Bundle? -> }

abstract class AbstractActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = Unit
    override fun onActivityStarted(activity: Activity) = Unit
    override fun onActivityResumed(activity: Activity) = Unit
    override fun onActivityPaused(activity: Activity) = Unit
    override fun onActivityStopped(activity: Activity) = Unit
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit
    override fun onActivityDestroyed(activity: Activity) = Unit
}

inline fun Context.registerOnActivityCreatedAndResumed(
    crossinline onCreated: OnActivityCreated,
    crossinline onResumed: OnActivityResumed
) = registerActivityLifecycleCallbacks(object : AbstractActivityLifecycleCallbacks() {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = onCreated.invoke(activity, savedInstanceState)
    override fun onActivityResumed(activity: Activity) = onResumed.invoke(activity)
})

inline fun Context.registerOnActivityCreatedResumedAndStopped(
    crossinline onCreated: OnActivityCreated,
    crossinline onResumed: OnActivityResumed,
    crossinline onStopped: OnActivityStopped
) = registerActivityLifecycleCallbacks(object : AbstractActivityLifecycleCallbacks() {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = onCreated.invoke(activity, savedInstanceState)
    override fun onActivityResumed(activity: Activity) = onResumed.invoke(activity)
    override fun onActivityStopped(activity: Activity) = onStopped.invoke(activity)
})

internal inline fun Context.registerActivityCallbacks(
    crossinline onCreated: OnActivityCreated,
    crossinline onStarted: OnActivityStarted,
    crossinline onResumed: OnActivityResumed,
    crossinline onPaused: OnActivityPaused,
    crossinline onStopped: OnActivityStopped,
    crossinline onSave: OnActivitySave,
    crossinline onDestroyed: OnActivityDestroyed
) = registerActivityLifecycleCallbacks(object : AbstractActivityLifecycleCallbacks() {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = onCreated.invoke(activity, savedInstanceState)
    override fun onActivityStarted(activity: Activity) = onStarted.invoke(activity)
    override fun onActivityResumed(activity: Activity) = onResumed.invoke(activity)
    override fun onActivityPaused(activity: Activity) = onPaused.invoke(activity)
    override fun onActivityStopped(activity: Activity) = onStopped.invoke(activity)
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = onSave.invoke(activity, outState)
    override fun onActivityDestroyed(activity: Activity) = onDestroyed.invoke(activity)
})

internal fun Context.unregisterActivityCallbacks(callbacks: Application.ActivityLifecycleCallbacks?) {
    callbacks?.let {
        (this.applicationContext as Application).unregisterActivityLifecycleCallbacks(callbacks)
    }
}

fun Application.ActivityLifecycleCallbacks.unregister(context: Context) = context.unregisterActivityCallbacks(this)

class ActivityTracker : ActivityProvider {
    override fun invoke() = activity

    var activity by weak<Activity?> { null }
        private set

    var resumed = false
        private set

    private var callbacks: Application.ActivityLifecycleCallbacks? = null

    fun init(application: Application) {
        callbacks?.unregister(application)
        callbacks = application.registerActivityCallbacks(
            onCreated = this::onActivityCreated,
            onStarted = this::onActivityStarted,
            onResumed = this::onActivityResumed,
            onPaused = { resumed = false },
            onStopped = NOP1,
            onSave = NOP2,
            onDestroyed = this::onActivityDestroyed
        )
    }

    fun dispose(context: Context) = callbacks?.unregister(context)

    private fun onActivityCreated(activity: Activity, @Suppress("UNUSED_PARAMETER") savedInstanceState: Bundle?) {
        this.activity = activity
    }

    private fun onActivityStarted(activity: Activity) {
        this.activity = activity
    }

    private fun onActivityResumed(activity: Activity) {
        this.activity = activity
        resumed = true
    }

    private fun onActivityDestroyed(activity: Activity) {
        if (this.activity == activity) {
            this.activity = null
        }
    }
}

private class WeakProperty<T>(initialValue: T?) {
    private var value = WeakReference(initialValue)

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? = value.get()
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        this.value = WeakReference(value)
    }
}

private inline fun <reified T> weak(body: () -> T) = WeakProperty(body.invoke())
