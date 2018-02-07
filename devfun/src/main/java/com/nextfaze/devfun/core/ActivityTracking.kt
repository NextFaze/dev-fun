package com.nextfaze.devfun.core

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.nextfaze.devfun.internal.registerActivityCallbacks
import com.nextfaze.devfun.internal.unregister
import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

/**
 * Function signature of DevFun's activity tracker/provider.
 *
 * In general you probably should use your own, however due to its convenience it is left public.
 */
typealias ActivityProvider = () -> Activity?

internal class ActivityTracker : ActivityProvider {
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
            onDestroyed = this::onActivityDestroyed,
            onPaused = { resumed = false },

            // note: need to declare these explicitly due to https://youtrack.jetbrains.com/issue/KT-22736
            onStopped = {},
            onSave = { _, _ -> }
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
