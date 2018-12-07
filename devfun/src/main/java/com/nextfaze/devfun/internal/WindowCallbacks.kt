package com.nextfaze.devfun.internal

import android.app.Application
import android.os.Bundle
import android.view.*
import android.view.accessibility.AccessibilityEvent
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.nextfaze.devfun.core.ActivityTracker
import com.nextfaze.devfun.core.resumedActivity
import com.nextfaze.devfun.internal.android.*
import com.nextfaze.devfun.internal.log.*
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Function signature of a listener for [Window] key events.
 *
 * @return Flag indicating if the event should be consumed (not forwarded to the system level handler). i.e. `true` will block propagation.
 *         All key event listeners will receive the event even if one of them returns `true`.
 *
 * @see WindowCallbacks.addKeyEventListener
 * @see WindowCallbacks.removeKeyEventListener
 */
typealias KeyEventListener = (event: KeyEvent) -> Boolean

/**
 * Function signature of a listener for [Window] focus changes.
 *
 * @see WindowCallbacks.resumedActivityHasFocus
 * @see WindowCallbacks.addResumedActivityFocusChangeListener
 * @see WindowCallbacks.removeResumedActivityFocusChangeListener
 */
typealias FocusChangeListener = (focus: Boolean) -> Unit

/**
 * Handles wrapping and posting [Window] events throughout an app's life.
 *
 * Used to tell the DevMenu overlays key events, and overlays when the current activity has focus. i.e. when they should
 * hide if a dialog is visible etc.
 *
 * @see KeyEventListener
 * @see FocusChangeListener
 */
@Suppress("MemberVisibilityCanBePrivate")
class WindowCallbacks(private val application: Application, private val activityTracker: ActivityTracker) {
    private val log = logger()
    private val resumedActivityFocusChangeListeners = CopyOnWriteArrayList<FocusChangeListener>()
    private val keyEventListeners = CopyOnWriteArrayList<KeyEventListener>()

    private inner class Callbacks(private val window: Window) : WindowCallbackWrapper(window.callback) {
        override fun onWindowFocusChanged(hasFocus: Boolean) {
            super.onWindowFocusChanged(hasFocus)

            val resumedActivityWindow = activityTracker.resumedActivity?.window ?: return
            if (window === resumedActivityWindow) { // i.e. this callback is for the currently resumed activity
                resumedActivityHasFocus = hasFocus
                log.t { "Activity $window hasFocus: $hasFocus" }
            } else {
                log.t { "Non-activity $window hasFocus: $hasFocus" }
            }
        }

        override fun dispatchKeyEvent(event: KeyEvent): Boolean {
            var blockNormalHandlers = false
            keyEventListeners.forEach {
                blockNormalHandlers = blockNormalHandlers || it(event)
            }
            return blockNormalHandlers || super.dispatchKeyEvent(event)
        }
    }

    private val callbacks = application.registerActivityCallbacks(
        onCreated = { it, _ ->
            it.window.wrapCallbackIfNecessary(true)

            if (it is FragmentActivity) {
                it.supportFragmentManager.registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
                    override fun onFragmentActivityCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) = f.wrap(true)
                    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) = f.wrap(false)

                    @Suppress("DEPRECATION")
                    private fun Fragment.wrap(initialWrap: Boolean) {
                        when (this) {
                            is DialogFragment -> dialog?.window?.wrapCallbackIfNecessary(initialWrap)
                            is android.app.DialogFragment -> dialog?.window?.wrapCallbackIfNecessary(initialWrap)
                        }
                    }
                }, false)
            }
        },
        onResumed = { it.window.wrapCallbackIfNecessary(false) }
    )

    /**
     * AppCompatDelegate is created in AppCompatActivity.onCreate, grabbing an instance of the current window callback (which
     * unless changed already is the activity itself).
     *
     * However when applying the support action bar (Toolbar) via setSupportActionBar, the window callback is changed to
     * ToolbarActionBar.ToolbarCallbackWrapper wrapping the *original* (activity, not the current) callback (as that's what the delegate
     * grabbed during onCreate).
     *
     * Thus our callback will be clobbered as we set the callback *after* the activity is created (as the activity lifecycle callbacks
     * are run after Activity.onCreate and all the delegate stuff is setup)
     *
     * Ideally we'd set our callback before Activity.super.onCreate(), but that is not possible(?) without a custom activity base class
     * (which we can't ask people to use as that'd just be a PITA)
     *
     * Hence we must re-apply our callback, but to avoid a potential wrapping nightmare, we only do it if it's the ToolbarCallbackWrapper
     *
     * @param initialWrap Should be true for *first* call. Subsequent calls (`onResume` etc) should be false to avoid re-wapping if something else wrapped us.
     */
    private fun Window.wrapCallbackIfNecessary(initialWrap: Boolean) {
        val currentCallback = callback
        if (currentCallback !is WindowCallbackWrapper) {
            if (initialWrap || currentCallback.toString().contains("ToolbarCallbackWrapper")) {
                Callbacks(this).also { callback = it }
            } else {
                log.w { "DevFun's Window callback wrapper was overridden by $currentCallback - some functionality may be lost." }
            }
        }
    }

    var resumedActivityHasFocus = true
        get() {
            val resumedActivity = activityTracker.resumedActivity ?: return false

            // if our callback has been clobbered always assume we have focus
            val callback = resumedActivity.window.callback
            if (callback !is WindowCallbackWrapper) {
                log.w { "Window callback was changed - assuming activity in focus (callback=$callback)" }
                return true
            }

            return field
        }
        private set(value) {
            field = value
            resumedActivityFocusChangeListeners.forEach { listener ->
                listener(value)
            }
        }

    internal fun dispose() {
        callbacks.unregister(application)
    }

    // Resumed activity focus change listeners

    fun addResumedActivityFocusChangeListener(listener: FocusChangeListener): FocusChangeListener {
        resumedActivityFocusChangeListeners += listener
        return listener
    }

    @JvmName("plusAssignResumedActivityFocusChangeListener")
    operator fun plusAssign(listener: FocusChangeListener) {
        addResumedActivityFocusChangeListener(listener)
    }

    fun removeResumedActivityFocusChangeListener(listener: FocusChangeListener): FocusChangeListener {
        resumedActivityFocusChangeListeners -= listener
        return listener
    }

    @JvmName("minusAssignResumedActivityFocusChangeListener")
    operator fun minusAssign(listener: FocusChangeListener) {
        removeResumedActivityFocusChangeListener(listener)
    }

    // Window key event listeners

    fun addKeyEventListener(listener: KeyEventListener): KeyEventListener {
        keyEventListeners += listener
        return listener
    }

    @JvmName("plusAssignKeyEventListener")
    operator fun plusAssign(listener: KeyEventListener) {
        addKeyEventListener(listener)
    }

    fun removeKeyEventListener(listener: KeyEventListener): KeyEventListener {
        keyEventListeners -= listener
        return listener
    }

    @JvmName("minusAssignKeyEventListener")
    operator fun minusAssign(listener: KeyEventListener) {
        removeKeyEventListener(listener)
    }
}

// TODO use me once https://youtrack.jetbrains.com/issue/KT-26725 is fixed (Kotlin 1.3.20)
//internal open class ActivityWindowCallbackWrapper(private val callback: Window.Callback) : Window.Callback by callback

private open class WindowCallbackWrapper(private val wrapped: Window.Callback) : Window.Callback {
    override fun dispatchKeyEvent(event: KeyEvent): Boolean = wrapped.dispatchKeyEvent(event)
    override fun dispatchKeyShortcutEvent(event: KeyEvent): Boolean = wrapped.dispatchKeyShortcutEvent(event)
    override fun dispatchTouchEvent(event: MotionEvent): Boolean = wrapped.dispatchTouchEvent(event)
    override fun dispatchTrackballEvent(event: MotionEvent): Boolean = wrapped.dispatchTrackballEvent(event)
    override fun dispatchGenericMotionEvent(event: MotionEvent): Boolean = wrapped.dispatchGenericMotionEvent(event)
    override fun dispatchPopulateAccessibilityEvent(event: AccessibilityEvent): Boolean = wrapped.dispatchPopulateAccessibilityEvent(event)
    override fun onCreatePanelView(featureId: Int): View? = wrapped.onCreatePanelView(featureId)
    override fun onCreatePanelMenu(featureId: Int, menu: Menu): Boolean = wrapped.onCreatePanelMenu(featureId, menu)
    override fun onPreparePanel(featureId: Int, view: View?, menu: Menu): Boolean = wrapped.onPreparePanel(featureId, view, menu)
    override fun onMenuOpened(featureId: Int, menu: Menu): Boolean = wrapped.onMenuOpened(featureId, menu)
    override fun onMenuItemSelected(featureId: Int, item: MenuItem): Boolean = wrapped.onMenuItemSelected(featureId, item)
    override fun onWindowAttributesChanged(attrs: WindowManager.LayoutParams) = wrapped.onWindowAttributesChanged(attrs)
    override fun onContentChanged() = wrapped.onContentChanged()
    override fun onWindowFocusChanged(hasFocus: Boolean) = wrapped.onWindowFocusChanged(hasFocus)
    override fun onAttachedToWindow() = wrapped.onAttachedToWindow()
    override fun onDetachedFromWindow() = wrapped.onDetachedFromWindow()
    override fun onPanelClosed(featureId: Int, menu: Menu) = wrapped.onPanelClosed(featureId, menu)
    override fun onSearchRequested(): Boolean = wrapped.onSearchRequested()
    override fun onWindowStartingActionMode(callback: ActionMode.Callback): ActionMode? = wrapped.onWindowStartingActionMode(callback)
    override fun onActionModeStarted(mode: ActionMode) = wrapped.onActionModeStarted(mode)
    override fun onActionModeFinished(mode: ActionMode) = wrapped.onActionModeFinished(mode)

    @RequiresApi(23)
    override fun onSearchRequested(searchEvent: SearchEvent): Boolean = wrapped.onSearchRequested(searchEvent)

    @RequiresApi(23)
    override fun onWindowStartingActionMode(callback: ActionMode.Callback, type: Int): ActionMode? =
        wrapped.onWindowStartingActionMode(callback, type)

    @RequiresApi(24)
    override fun onProvideKeyboardShortcuts(data: List<KeyboardShortcutGroup>, menu: Menu?, deviceId: Int) =
        wrapped.onProvideKeyboardShortcuts(data, menu, deviceId)

    @RequiresApi(26)
    override fun onPointerCaptureChanged(hasCapture: Boolean) = wrapped.onPointerCaptureChanged(hasCapture)
}
