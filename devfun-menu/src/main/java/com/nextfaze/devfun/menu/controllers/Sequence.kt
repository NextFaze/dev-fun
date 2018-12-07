package com.nextfaze.devfun.menu.controllers

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.SpannableStringBuilder
import android.view.KeyEvent
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentActivity
import com.nextfaze.devfun.core.ActivityProvider
import com.nextfaze.devfun.core.devFun
import com.nextfaze.devfun.internal.KeyEventListener
import com.nextfaze.devfun.internal.WindowCallbacks
import com.nextfaze.devfun.internal.string.*
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
class KeySequence(
    context: Context,
    private val activityProvider: ActivityProvider,
    private val windowCallbacks: WindowCallbacks = devFun.get() // internal - not ideal...
) : MenuController {
    private val application = context.applicationContext as Application
    private val handler = Handler(Looper.getMainLooper())
    private val sequenceStates = mutableSetOf<Definition>()

    private var keyEventListener: KeyEventListener? = null
    private var developerMenu: DeveloperMenu? = null

    operator fun plusAssign(sequenceDefinition: Definition) {
        sequenceStates += sequenceDefinition
    }

    operator fun minusAssign(sequenceDefinition: Definition) {
        sequenceStates -= sequenceDefinition
    }

    override fun attach(developerMenu: DeveloperMenu) {
        this.developerMenu = developerMenu
        keyEventListener = windowCallbacks.addKeyEventListener { event ->
            onKeyEvent(event.action, event.keyCode)
        }
    }

    override fun detach() {
        developerMenu = null
        keyEventListener?.also { windowCallbacks -= it }
        keyEventListener = null
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
