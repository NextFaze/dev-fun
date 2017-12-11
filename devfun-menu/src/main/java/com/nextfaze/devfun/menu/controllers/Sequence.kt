package com.nextfaze.devfun.menu.controllers

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks
import android.view.KeyEvent
import android.view.Window
import com.nextfaze.devfun.internal.*
import com.nextfaze.devfun.menu.DeveloperMenu
import com.nextfaze.devfun.menu.MenuController
import java.util.Arrays

internal val GRAVE_KEY_SEQUENCE = intArrayOf(KeyEvent.KEYCODE_GRAVE)
internal val VOLUME_KEY_SEQUENCE = intArrayOf(
        KeyEvent.KEYCODE_VOLUME_DOWN,
        KeyEvent.KEYCODE_VOLUME_DOWN,
        KeyEvent.KEYCODE_VOLUME_UP,
        KeyEvent.KEYCODE_VOLUME_DOWN
)

/**
 * Allows toggling the Developer Menu using button/key sequences.
 *
 * Two sequences are added by default:
 * - The grave/tilde key: `~`
 * - Volume button sequence: `down,down,up,down`
 */
class KeySequence(context: Context, private val activityProvider: ActivityProvider) : MenuController {
    private val application = context.applicationContext as Application
    private val handler = Handler(Looper.getMainLooper())
    private val sequenceStates = mutableSetOf<Sequence>()

    private var listener: Application.ActivityLifecycleCallbacks? = null
    private var developerMenu: DeveloperMenu? = null

    operator fun plusAssign(keySequence: IntArray) {
        sequenceStates += Sequence(keySequence)
    }

    operator fun minusAssign(keySequence: IntArray) {
        sequenceStates -= Sequence(keySequence)
    }

    override fun attach(developerMenu: DeveloperMenu) {
        this.developerMenu = developerMenu
        listener = application.registerOnActivityCreatedAndResumed(this::onActivityCreated, this::onActivityResumed)
    }

    override fun detach() {
        application.unregisterActivityLifecycleCallbacks(listener)
        listener = null
        developerMenu = null
    }

    private fun onActivityCreated(activity: Activity, @Suppress("UNUSED_PARAMETER") savedInstanceState: Bundle?) {
        if (activity is FragmentActivity) {
            activity.supportFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentLifecycleCallbacks() {
                override fun onFragmentStarted(fm: FragmentManager?, f: Fragment?) {
                    when (f) {
                        is DialogFragment -> f.dialog?.window?.wrapCallbackIfNecessary()
                        is android.app.DialogFragment -> f.dialog?.window?.wrapCallbackIfNecessary()
                    }
                }
            }, false)
        }
    }

    private fun onActivityResumed(activity: Activity) {
        activity.window?.wrapCallbackIfNecessary()
    }

    private fun onKeyEvent(action: Int, code: Int) {
        if (action != KeyEvent.ACTION_UP) return

        if (sequenceStates.any { it.sequenceComplete(code) }) {
            resetAllSequences()

            // we post to next event loop to stop InputEventReceiver complaining about missing/destroyed object
            handler.post {
                (activityProvider() as? FragmentActivity)?.takeIf { !it.isFinishing }?.let {
                    if (developerMenu?.isVisible == true) {
                        developerMenu?.hide(it)
                    } else {
                        developerMenu?.show(it)
                    }
                }
            }
        }
    }

    override fun onShown() {
        resetAllSequences()
    }

    override fun onDismissed() {
        resetAllSequences()
    }

    private fun resetAllSequences() {
        sequenceStates.forEach { it.reset() }
    }

    private fun Window.wrapCallbackIfNecessary() {
        if (callback !is WindowCallbackWrapper) callback = WindowCallbackWrapper(callback)
    }

    private inner class WindowCallbackWrapper(callback: Window.Callback) : android.support.v7.view.WindowCallbackWrapper(callback) {
        @SuppressLint("RestrictedApi")
        override fun dispatchKeyEvent(event: KeyEvent): Boolean {
            onKeyEvent(event.action, event.keyCode)
            return super.dispatchKeyEvent(event)
        }
    }

    private data class Sequence(val keyCodes: IntArray, var currIndex: Int = 0) {
        fun sequenceComplete(code: Int): Boolean {
            if (code == keyCodes[currIndex]) {
                currIndex++
                if (currIndex >= keyCodes.size) {
                    currIndex = 0
                    return true
                }
            } else {
                currIndex = 0
            }
            return false
        }

        fun reset() {
            currIndex = 0
        }

        override fun equals(other: Any?) = when {
            this === other -> true
            other !is Sequence -> false
            !Arrays.equals(keyCodes, other.keyCodes) -> false
            else -> true
        }

        override fun hashCode() = Arrays.hashCode(keyCodes)
    }
}
