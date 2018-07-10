package com.nextfaze.devfun.core

import android.app.Activity
import android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
import android.content.Context
import android.os.Build
import android.os.Process
import android.os.SystemClock
import com.nextfaze.devfun.internal.android.*
import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.internal.prop.*
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Function signature of DevFun's activity tracker/provider.
 *
 * @see ActivityTracker
 */
interface ActivityProvider : kotlin.Function0<Activity?>

/**
 * Activity tracker that provides the currently (resumed) activity if present.
 *
 * @see ActivityProvider
 */
interface ActivityTracker {
    /**
     * The current activity - may not be in a resumed state.
     *
     * @see isResumed
     * @see resumedActivity
     */
    val activity: Activity?

    /**
     * Is the current activity resumed.
     *
     * @see activity
     */
    val isResumed: Boolean
}

/** Extension function to only get the activity if it is resumed. */
val ActivityTracker.resumedActivity: Activity? get() = if (isResumed) activity else null

/**
 * Function signature of callbacks for foreground status changes.
 *
 * @see ForegroundTracker
 */
typealias ForegroundChangeListener = (foreground: Boolean) -> Unit

/**
 * Application foreground state tracker.
 *
 * @see ActivityTracker
 * @see ActivityProvider
 * @see ForegroundChangeListener
 */
interface ForegroundTracker {
    /** The foreground status of the app. */
    val isAppInForeground: Boolean

    /**
     * Add a listener for when the app foreground status changes.
     *
     * @see plusAssign
     */
    fun addForegroundChangeListener(listener: ForegroundChangeListener): ForegroundChangeListener

    /**
     * Add a listener for when the app foreground status changes.
     *
     * @see addForegroundChangeListener
     */
    operator fun plusAssign(listener: ForegroundChangeListener) {
        addForegroundChangeListener(listener)
    }

    /**
     * Remove a listener for when the app foreground status changes.
     *
     * @see minusAssign
     */
    fun removeForegroundChangeListener(listener: ForegroundChangeListener): ForegroundChangeListener

    /**
     * Remove a listener for when the app foreground status changes.
     *
     * @see removeForegroundChangeListener
     */
    operator fun minusAssign(listener: ForegroundChangeListener) {
        removeForegroundChangeListener(listener)
    }
}

internal class AppStateTracker(context: Context) : ActivityTracker, ActivityProvider, ForegroundTracker {
    private val log = logger()

    private val application = context.applicationContext
    private val activityManager = application.activityManager
    private val keyguardManager = application.keyguardManager

    private var _activity by weak<Activity>()
    override val activity: Activity? get() = _activity

    private var _resumed = false
    override val isResumed: Boolean get() = _resumed

    private val foregroundChangeListeners = CopyOnWriteArrayList<ForegroundChangeListener>()
    private val myPid by lazy { Process.myPid() }
    private var _appInForeground = false
        set(value) {
            if (field != value) {
                field = value
                foregroundChangeListeners.forEach { it(value) }
            }
        }
    override val isAppInForeground: Boolean get() = _appInForeground

    var logCallbacks = false

    private val loggingCallbacks = application.registerActivityCallbacks(
        onCreated = { it, _ -> log.d(predicate = logCallbacks) { "onCreated($it)" } },
        onStarted = { log.d(predicate = logCallbacks) { "onStarted($it)" } },
        onResumed = { log.d(predicate = logCallbacks) { "onResumed($it)" } },
        onPaused = { log.d(predicate = logCallbacks) { "onPaused($it)" } },
        onStopped = { log.d(predicate = logCallbacks) { "onStopped($it)" } },
        onSave = { it, _ -> log.d(predicate = logCallbacks) { "onSave($it)" } },
        onDestroyed = { log.d(predicate = logCallbacks) { "onStarted($it)" } }
    )

    private val callbacks = application.registerActivityCallbacks(
        onCreated = { it, _ -> _activity = it },
        onStarted = { _activity = it },
        onResumed = {
            _activity = it
            _resumed = true
            _appInForeground = getForegroundStatus()
        },
        onPaused = { _resumed = false },
        onStopped = { _appInForeground = getForegroundStatus() },
        onDestroyed = {
            if (it === _activity) {
                _activity = null
            }
        }
    )

    override fun invoke(): Activity? = activity

    override fun addForegroundChangeListener(listener: ForegroundChangeListener): ForegroundChangeListener {
        foregroundChangeListeners += listener
        return listener
    }

    override fun removeForegroundChangeListener(listener: ForegroundChangeListener): ForegroundChangeListener {
        foregroundChangeListeners -= listener
        return listener
    }

    private fun getForegroundStatus(): Boolean {
        if (keyguardManager.inKeyguardRestrictedInputMode()) return false

        if (Build.VERSION.SDK_INT < 21) {
            SystemClock.sleep(10L)
        }

        return activityManager.runningAppProcesses?.any { it.pid == myPid && it.importance == IMPORTANCE_FOREGROUND } == true
    }

    fun dispose() {
        callbacks.unregister(application)
        loggingCallbacks.unregister(application)
        foregroundChangeListeners.clear()
    }
}
