package com.nextfaze.devfun.overlay

import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.view.Display
import android.view.WindowManager
import com.nextfaze.devfun.internal.android.*
import java.util.concurrent.CopyOnWriteArrayList

/**
 * Function signature of callbacks for display bound changes.
 *
 * @see DisplayBoundsTracker
 */
typealias DisplayBoundsChangeListener = (old: Rect, new: Rect) -> Unit

/**
 * Tracks the *activity/app* bounds (not the device).
 *
 * @see DisplayBoundsChangeListener
 */
interface DisplayBoundsTracker {
    /** The foreground status of the app. */
    val displayBounds: Rect

    /**
     * Add a listener for when the display bounds change.
     *
     * @see plusAssign
     */
    fun addDisplayBoundsChangeListener(listener: DisplayBoundsChangeListener): DisplayBoundsChangeListener

    /**
     * Add a listener for when the display bounds change.
     *
     * @see addDisplayBoundsChangeListener
     */
    operator fun plusAssign(listener: DisplayBoundsChangeListener) {
        addDisplayBoundsChangeListener(listener)
    }

    /**
     * Remove a listener for when the display bounds change.
     *
     * @see minusAssign
     */
    fun removeDisplayBoundsChangeListener(listener: DisplayBoundsChangeListener): DisplayBoundsChangeListener

    /**
     * Remove a listener for when the display bounds change.
     *
     * @see removeDisplayBoundsChangeListener
     */
    operator fun minusAssign(listener: DisplayBoundsChangeListener) {
        removeDisplayBoundsChangeListener(listener)
    }
}

internal class DisplayBoundsTrackerImpl(context: Context) : DisplayBoundsTracker {
    private val listeners = CopyOnWriteArrayList<DisplayBoundsChangeListener>()

    private val application = context.applicationContext
    override val displayBounds = Rect()

    init {
        context.registerActivityCallbacks(onResumed = { updateBounds(it) })
    }

    override fun addDisplayBoundsChangeListener(listener: DisplayBoundsChangeListener): DisplayBoundsChangeListener {
        listeners += listener
        return listener
    }

    override fun removeDisplayBoundsChangeListener(listener: DisplayBoundsChangeListener): DisplayBoundsChangeListener {
        listeners -= listener
        return listener
    }

    private fun updateBounds(activity: Activity) {
        val tmp = Rect()
        activity.defaultDisplay.getRectSize(tmp)
        if (!tmp.isEmpty) {
            tmp.bottom = tmp.bottom - statusBarHeight
        }
        if (displayBounds.isEmpty || displayBounds != tmp) {
            val old = Rect(displayBounds)
            displayBounds.set(tmp)
            listeners.forEach { it(old, displayBounds) }
        }
    }

    private val statusBarHeight: Int
        get() = application.resources.let { res ->
            val resourceId = res.getIdentifier("status_bar_height", "dimen", "android")
            // If manufacturer has broken this for some reason, it's not the end of the world to assume 0
            return if (resourceId > 0) res.getDimensionPixelSize(resourceId) else 0
        }

    /**
     * Using the activity like this ensures that rotation, status bar, and other system views are taken into account.
     * Thus for multi-window use it should be properly bounded to the relevant app window area.
     * i.e. This is the space available for the *app* not the *device*
     */
    private val Activity.defaultDisplay: Display get() = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
}
