package com.nextfaze.devfun.menu.controllers

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.support.annotation.StringRes
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks
import android.text.SpannableStringBuilder
import android.view.KeyEvent
import android.view.Window
import com.nextfaze.devfun.core.ActivityProvider
import com.nextfaze.devfun.internal.*
import com.nextfaze.devfun.menu.DeveloperMenu
import com.nextfaze.devfun.menu.MenuController
import com.nextfaze.devfun.menu.R
import java.util.Arrays

internal val GRAVE_KEY_SEQUENCE = KeySequence.Definition(
    keyCodes = intArrayOf(KeyEvent.KEYCODE_GRAVE),
    description = R.string.df_menu_grave_sequence,
    consumeEvent = true
)
internal val VOLUME_KEY_SEQUENCE = KeySequence.Definition(
    keyCodes = intArrayOf(
        KeyEvent.KEYCODE_VOLUME_DOWN,
        KeyEvent.KEYCODE_VOLUME_DOWN,
        KeyEvent.KEYCODE_VOLUME_UP,
        KeyEvent.KEYCODE_VOLUME_DOWN
    ),
    description = R.string.df_menu_volume_sequence,
    consumeEvent = false
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
    private val sequenceStates = mutableSetOf<Definition>()

    private var listener: Application.ActivityLifecycleCallbacks? = null
    private var developerMenu: DeveloperMenu? = null

    operator fun plusAssign(sequenceDefinition: Definition) {
        sequenceStates += sequenceDefinition
    }

    operator fun minusAssign(sequenceDefinition: Definition) {
        sequenceStates -= sequenceDefinition
    }

    override fun attach(developerMenu: DeveloperMenu) {
        this.developerMenu = developerMenu
        listener = application.registerActivityCallbacks(
            // note: cant use function references due to https://youtrack.jetbrains.com/issue/KT-22736
            onCreated = { activity, _ -> onActivityCreated(activity) },
            onResumed = { onActivityResumed(it) }
        )
    }

    override fun detach() {
        listener?.unregister(application)
        listener = null
        developerMenu = null
    }

    override val title: String get() = application.getString(R.string.df_menu_key_sequence)
    override val actionDescription
        get() = sequenceStates
            .takeIf { it.isNotEmpty() }
            ?.joinTo(SpannableStringBuilder(), "\n") { def ->
                SpannableStringBuilder().also {
                    it += " • "
                    it += application.getText(def.description)
                }
            }

    private fun onActivityCreated(activity: Activity) {
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

    private fun onKeyEvent(action: Int, code: Int): Boolean {
        if (action != KeyEvent.ACTION_DOWN) return false

        val matched = sequenceStates.firstOrNull { it.sequenceComplete(code) } ?: return false
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

        return matched.consumeEvent
    }

    override fun onShown() = resetAllSequences()
    override fun onDismissed() = resetAllSequences()

    private fun resetAllSequences() = sequenceStates.forEach { it.reset() }

    private fun Window.wrapCallbackIfNecessary() {
        if (callback !is WindowCallbackWrapper) callback = WindowCallbackWrapper(callback)
    }

    @SuppressLint("RestrictedApi")
    private inner class WindowCallbackWrapper(callback: Window.Callback) : android.support.v7.view.WindowCallbackWrapper(callback) {
        @SuppressLint("RestrictedApi")
        override fun dispatchKeyEvent(event: KeyEvent): Boolean =
            when {
                onKeyEvent(event.action, event.keyCode) -> true
                else -> super.dispatchKeyEvent(event)
            }
    }

    data class Definition(private val keyCodes: IntArray, @StringRes val description: Int, val consumeEvent: Boolean) {
        private var currIndex: Int = 0

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
            other !is Definition -> false
            !Arrays.equals(keyCodes, other.keyCodes) -> false
            else -> true
        }

        override fun hashCode() = Arrays.hashCode(keyCodes)
    }
}
