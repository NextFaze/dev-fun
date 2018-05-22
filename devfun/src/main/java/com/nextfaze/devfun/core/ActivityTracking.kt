@file:Suppress("MemberVisibilityCanBePrivate")

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

    private var _activity by weak<Activity>()
    val activity get() = if (isResumed) _activity else null

    private var _resumed = false
    val isResumed get() = _resumed

    private var callbacks: Application.ActivityLifecycleCallbacks? = null

    fun init(application: Application) {
        dispose(application)
        callbacks = application.registerActivityCallbacks(
            onCreated = { it, _ -> _activity = it },
            onStarted = { _activity = it },
            onResumed = { _activity = it; _resumed = true },
            onDestroyed = {
                if (it === _activity) {
                    _activity = null
                }
            },
            onPaused = { _resumed = false }
        )
    }

    fun dispose(context: Context) {
        callbacks?.unregister(context)
        callbacks = null
    }
}
