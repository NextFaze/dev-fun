package com.nextfaze.devfun.core

import android.app.Activity
import android.app.Application
import android.content.Context
import com.nextfaze.devfun.internal.android.*
import com.nextfaze.devfun.internal.prop.*

/**
 * Function signature of DevFun's activity tracker/provider.
 *
 * In general you probably should use your own, however due to its convenience it is left public.
 */
typealias ActivityProvider = () -> Activity?

internal class ActivityTracker : ActivityProvider {
    override fun invoke() = activity

    var activity by weak<Activity>()
        private set

    var resumed = false
        private set

    private var callbacks: Application.ActivityLifecycleCallbacks? = null

    fun init(application: Application) {
        dispose(application)
        callbacks = application.registerActivityCallbacks(
            onCreated = { it, _ -> activity = it },
            onStarted = { activity = it },
            onResumed = { activity = it; resumed = true },
            onDestroyed = {
                if (it === activity) {
                    activity = null
                }
            },
            onPaused = { resumed = false }
        )
    }

    fun dispose(context: Context) {
        callbacks?.unregister(context)
        callbacks = null
    }
}
