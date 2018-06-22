package com.nextfaze.devfun.demo.util

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.nextfaze.devfun.demo.inject.Initializer
import java.lang.ref.WeakReference
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KProperty

internal typealias OnActivityCreated = (activity: Activity, savedInstanceState: Bundle?) -> Unit
internal typealias OnActivityStarted = (activity: Activity) -> Unit
internal typealias OnActivityResumed = (activity: Activity) -> Unit
internal typealias OnActivityPaused = (activity: Activity) -> Unit
internal typealias OnActivityStopped = (activity: Activity) -> Unit
internal typealias OnActivitySave = (activity: Activity, outState: Bundle) -> Unit
internal typealias OnActivityDestroyed = (activity: Activity) -> Unit

internal inline fun Context.registerActivityCallbacks(
    crossinline onCreated: OnActivityCreated,
    crossinline onStarted: OnActivityStarted,
    crossinline onResumed: OnActivityResumed,
    crossinline onPaused: OnActivityPaused,
    crossinline onDestroyed: OnActivityDestroyed
) =
    this.registerActivityCallbacks(onCreated, onStarted, onResumed, onPaused, {}, { _, _ -> }, onDestroyed)

internal inline fun Context.registerActivityCallbacks(
    crossinline onCreated: OnActivityCreated,
    crossinline onStarted: OnActivityStarted,
    crossinline onResumed: OnActivityResumed,
    crossinline onPaused: OnActivityPaused,
    crossinline onStopped: OnActivityStopped,
    crossinline onSave: OnActivitySave,
    crossinline onDestroyed: OnActivityDestroyed
) =
    (this.applicationContext as Application).registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) = onCreated.invoke(activity, savedInstanceState)
        override fun onActivityStarted(activity: Activity) = onStarted.invoke(activity)
        override fun onActivityResumed(activity: Activity) = onResumed.invoke(activity)
        override fun onActivityPaused(activity: Activity) = onPaused.invoke(activity)
        override fun onActivityStopped(activity: Activity) = onStopped.invoke(activity)
        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = onSave.invoke(activity, outState)
        override fun onActivityDestroyed(activity: Activity) = onDestroyed.invoke(activity)
    })

@Singleton
internal class ActivityTracker @Inject constructor() : Initializer {

    var activity by weak<Activity?> { null }
        private set

    var isResumed = false
        private set

    override fun invoke(application: Application) {
        application.registerActivityCallbacks(
            onCreated = this::onActivityCreated,
            onStarted = this::onActivityStarted,
            onResumed = this::onActivityResumed,
            onPaused = { isResumed = false },
            onDestroyed = this::onActivityDestroyed
        )
    }

    private fun onActivityCreated(activity: Activity, @Suppress("UNUSED_PARAMETER") savedInstanceState: Bundle?) {
        this.activity = activity
    }

    private fun onActivityStarted(activity: Activity) {
        this.activity = activity
    }

    private fun onActivityResumed(activity: Activity) {
        this.activity = activity
        isResumed = true
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
