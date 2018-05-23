package com.nextfaze.devfun.overlay

import android.animation.ValueAnimator
import android.app.Application
import android.content.Context
import android.graphics.PixelFormat
import android.graphics.PointF
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.support.annotation.LayoutRes
import android.support.v4.math.MathUtils.clamp
import android.view.*
import android.view.animation.OvershootInterpolator
import com.nextfaze.devfun.annotations.DeveloperLogger
import com.nextfaze.devfun.core.ActivityProvider
import com.nextfaze.devfun.core.ForegroundTracker
import com.nextfaze.devfun.internal.android.*
import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.internal.pref.*
import com.nextfaze.devfun.overlay.VisibilityScope.ALWAYS
import com.nextfaze.devfun.overlay.VisibilityScope.FOREGROUND_ONLY
import java.lang.Math.abs

private const val MIN_ANIMATION_MILLIS = 250L
private const val MAX_ANIMATION_MILLIS = 500L

typealias ClickListener = (View) -> Unit
typealias OverlayReason = () -> CharSequence
typealias VisibilityPredicate = (Context) -> Boolean
typealias VisibilityChangeListener = (Boolean) -> Unit

enum class Dock { TOP, BOTTOM, LEFT, RIGHT, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT }

/**
 * Determines when an overlay can be visible.
 *
 * - [FOREGROUND_ONLY]: Only visible when the app is in the foreground.
 * - [ALWAYS]: Visible even if the app has no active activities. _App cannot be swipe-closed if you have one of these._
 */
enum class VisibilityScope {
    /** Only visible when the app is in the foreground. */
    FOREGROUND_ONLY,
    /** Visible even if the app has no active activities. _App cannot be swipe-closed if you have one of these._ */
    ALWAYS
}

/**
 * Overlay windows are used by DevFun to display the loggers [DeveloperLogger] and DevMenu cog.
 *
 * @see OverlayManager
 * @see OverlayPermissions
 */
interface OverlayWindow {
    /** Preferences file name to store non-default state - must be unique. */
    val prefsName: String

    /** If the overlay is enabled. Setting to false will hide it (but not remove it from the window). */
    var enabled: Boolean

    /**
     * If enabled the overlay will snap to the closest edge of the window. If disabled it can be moved anywhere and will only snap if it
     * leaves the windows's display bounds.
     */
    var snapToEdge: Boolean

    /**
     * Determines when the overlay can be visible.
     *
     * - [FOREGROUND_ONLY]: Only visible when the app is in the foreground.
     * - [ALWAYS]: Visible even if the app has no active activities. _App cannot be swipe-closed if you have one of these._
     */
    var visibilityScope: VisibilityScope

    /**
     * Insets the overlay window. For a docked window, a pos
     *
     * e.g. The DevMenu cog has an inset of half its width (and thus sits half way off the window).
     *
     * For non-docking overlays, this will affect how it sits if you try to drag it off window when it snaps back. _Be careful with this as
     * it could result in the overlay sitting outside the window._
     */
    var inset: Rect

    /** The overlay's view. */
    val view: View

    /** Flag indicating if the overlay is currently visible. */
    val isVisible: Boolean

    /**
     * Rendered only if the user has not granted overlay permissions.
     *
     * @see OverlayPermissions
     */
    val reason: OverlayReason

    /**
     * Add this overlay to the window.
     *
     * @see removeFromWindow
     * @see dispose
     */
    fun addToWindow()

    /**
     * Remove this overlay from the window.
     *
     * @see addToWindow
     */
    fun removeFromWindow()

    /**
     * Reset the position and state to its initial default values and clear its preferences.
     *
     * Please submit an issue if you needed to call this because it was out of bound or misbehaving our something.
     */
    fun resetPositionAndState()

    /**
     * Clean up listeners and callbacks of this window.
     */
    fun dispose()

    /** Callback when user taps the overlay. */
    var onClick: ClickListener?

    /** Callback when user long-presses the overlay. */
    var onLongClick: ClickListener?

    /** Callback when overlay visibility changes. */
    var onVisibilityChange: VisibilityChangeListener?
}

internal class OverlayWindowImpl(
    private val application: Application,
    private val overlays: OverlayManager,
    private val permissions: OverlayPermissions,
    private val activityProvider: ActivityProvider,
    private val foregroundTracker: ForegroundTracker,
    private val displayBoundsTracker: DisplayBoundsTracker,
    @LayoutRes layoutId: Int,
    override val prefsName: String,
    override val reason: OverlayReason,
    override var onClick: ClickListener? = null,
    override var onLongClick: ClickListener? = null,
    override var onVisibilityChange: VisibilityChangeListener? = null,
    private val visibilityPredicate: VisibilityPredicate? = null,
    visibilityScope: VisibilityScope = VisibilityScope.FOREGROUND_ONLY,
    initialDock: Dock = Dock.TOP_LEFT,
    initialDelta: Float = 0f,
    snapToEdge: Boolean = true,
    initialLeft: Float = 0f,
    initialTop: Float = 0f
) : OverlayWindow {
    private val log = logger()
    private val windowManager = application.windowManager
    private val handler = Handler(Looper.getMainLooper())

    private val preferences = KSharedPreferences.named(application, prefsName)
    private var dock by preferences["dock", initialDock]
    private var delta by preferences["delta", initialDelta]
    private var left by preferences["left", initialLeft]
    private var top by preferences["top", initialTop]
    override var snapToEdge: Boolean by preferences["snapToEdge", snapToEdge, { _, _ -> updatePosition(false) }]
    override var enabled: Boolean by preferences["enabled", true, { _, _ -> updateVisibility() }]
    override var visibilityScope: VisibilityScope by preferences["visibilityScope", visibilityScope, { _, _ -> updateVisibility() }]

    override var inset = Rect()

    private val isAdded get() = view.parent != null
    private val params = createOverlayWindowParams()
    private val overlayBounds = Rect()
    private var moving = false

    private var visible: Boolean = true
        set(value) {
            field = value

            val visibility = view.visibility
            val newVisibility = if (value) View.VISIBLE else View.GONE
            if (visibility != newVisibility) {
                view.visibility = newVisibility
                onVisibilityChange?.invoke(value)
            }
        }
    override val isVisible get() = visible

    private val foregroundListener = foregroundTracker.addForegroundChangeListener { updateVisibility() }
    private val boundsListener = displayBoundsTracker.addDisplayBoundsChangeListener { _, bounds -> updateOverlayBounds(bounds) }
    private val fullScreenLockListener = overlays.addFullScreenLockChangeListener { updateVisibility() }

    private var permissionsListener: OverlayPermissionChangeListener? = null
    private var addToWindow = false

    init {
        if (permissionsListener == null) {
            permissions += { if (addToWindow && it) addToWindow() }
        }
    }

    private fun updateOverlayBounds(bounds: Rect, postUpdate: Boolean = true) {
        overlayBounds.set(bounds)
        loadSavedPosition(postUpdate)
    }

    private fun updateVisibility() {
        if (!isAdded) return

        val activity = activityProvider()
        visible = !overlays.isFullScreenLockInUse && enabled &&
                when (visibilityScope) {
                    VisibilityScope.FOREGROUND_ONLY -> {
                        when (activity) {
                            null -> false
                            else -> foregroundTracker.isAppInForeground && visibilityPredicate?.invoke(activity) != false
                        }
                    }
                    VisibilityScope.ALWAYS -> visibilityPredicate?.invoke(activity ?: application) != false
                }
    }

    override val view: View by lazy {
        View.inflate(application, layoutId, null).apply {
            setOnTouchListener(object : View.OnTouchListener {
                private val tapTimeout = ViewConfiguration.getTapTimeout().toLong()
                private val longPressTimeout = ViewConfiguration.getLongPressTimeout().toLong()
                private val pressTimeout get() = if (onLongClick == null) tapTimeout else longPressTimeout

                private val onLongClickRunnable = Runnable {
                    if (onLongClick != null) {
                        longClickPerformed = true
                        performLongClick()
                    }
                }
                private var postedOnLongClickRunnable: Runnable? = null

                private fun postOnLongClick() {
                    if (onLongClick != null) {
                        postedOnLongClickRunnable = onLongClickRunnable
                        handler.postDelayed(postedOnLongClickRunnable, longPressTimeout)
                    }
                }

                private fun removeOnLongClick() {
                    if (onLongClick != null && postedOnLongClickRunnable != null) {
                        handler.removeCallbacks(postedOnLongClickRunnable)
                        postedOnLongClickRunnable = null
                    }
                }

                private var currentPosition = PointF(0f, 0f)
                private var initialPosition = PointF(0f, 0f)
                private var initialWindowPosition = PointF(0f, 0f)

                private var actionDownTime = 0L
                private var longClickPerformed = false

                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    return when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            postOnLongClick()
                            actionDownTime = System.currentTimeMillis()

                            initialPosition.apply {
                                x = event.rawX
                                y = event.rawY
                            }

                            currentPosition.set(initialPosition)

                            initialWindowPosition.apply {
                                x = params.x.toFloat()
                                y = params.y.toFloat()
                            }

                            moving = false

                            true
                        }
                        MotionEvent.ACTION_MOVE -> {
                            if (longClickPerformed) return true

                            val rawX = event.rawX
                            val rawY = event.rawY
                            val dx = abs(rawX - currentPosition.x)
                            val dy = abs(rawY - currentPosition.y)
                            val slop = ViewConfiguration.get(application).scaledTouchSlop
                            val sinceDown = System.currentTimeMillis() - actionDownTime

                            if (moving || dx >= slop || dy >= slop || sinceDown > pressTimeout) {
                                removeOnLongClick()
                                moving = true
                                params.x = (rawX - initialPosition.x + initialWindowPosition.x).toInt()
                                params.y = (rawY - initialPosition.y + initialWindowPosition.y).toInt()

                                currentPosition.apply {
                                    x = rawX
                                    y = rawY
                                }

                                windowManager.updateViewLayout(v, params)
                            }

                            true
                        }
                        MotionEvent.ACTION_UP -> {
                            if (!moving) {
                                val sinceDown = System.currentTimeMillis() - actionDownTime
                                if (sinceDown < longPressTimeout) {
                                    removeOnLongClick()
                                    v.performClick()
                                }
                            } else {
                                updatePosition(true)
                            }

                            moving = false
                            actionDownTime = 0
                            longClickPerformed = false
                            true
                        }
                        else -> false
                    }
                }
            })

            setOnClickListener { onClick?.invoke(it) }
            setOnLongClickListener { onLongClick?.invoke(it); true }

            addOnLayoutChangeListener { _, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
                if (moving) return@addOnLayoutChangeListener

                val prevWidth = oldRight - oldLeft
                val newWidth = right - left
                val prevHeight = oldBottom - oldTop
                val newHeight = bottom - top
                if (prevWidth != newWidth || prevHeight != newHeight) {
                    updatePosition(false)
                }
            }
        }
    }

    private fun updatePosition(updatePrefs: Boolean) {
        if (!snapToEdge && updatePrefs) {
            saveNonSnapPosition()
        }
        adjustPosition(updatePrefs)
    }

    private fun loadSavedPosition(postUpdate: Boolean) {
        val side = dock.takeIf { snapToEdge }
        val displacement = delta

        // initial position
        var x = when (side) {
            null -> (left * overlayBounds.width()).toInt()
            Dock.LEFT, Dock.TOP_LEFT, Dock.BOTTOM_LEFT -> overlayBounds.left
            Dock.RIGHT, Dock.TOP_RIGHT, Dock.BOTTOM_RIGHT -> overlayBounds.right - width
            else -> {
                val xOffset = (displacement * overlayBounds.width()).toInt()
                clamp(overlayBounds.left + xOffset, overlayBounds.left, overlayBounds.right)
            }
        }
        var y = when (side) {
            null -> (top * overlayBounds.height()).toInt()
            Dock.TOP, Dock.TOP_LEFT, Dock.TOP_RIGHT -> overlayBounds.top
            Dock.BOTTOM, Dock.BOTTOM_LEFT, Dock.BOTTOM_RIGHT -> overlayBounds.bottom - height - statusBarHeight
            else -> {
                val yOffset = (displacement * overlayBounds.height()).toInt()
                clamp(overlayBounds.top + yOffset, overlayBounds.top, overlayBounds.bottom)
            }
        }

        // adjust for view inset
        x -= when (side) {
            Dock.LEFT, Dock.TOP_LEFT, Dock.BOTTOM_LEFT -> inset.left
            Dock.RIGHT, Dock.TOP_RIGHT, Dock.BOTTOM_RIGHT -> -inset.right
            else -> 0
        }
        y -= when (side) {
            Dock.TOP, Dock.TOP_LEFT, Dock.TOP_RIGHT -> inset.top
            Dock.BOTTOM, Dock.BOTTOM_LEFT, Dock.BOTTOM_RIGHT -> -inset.bottom
            else -> 0
        }

        updateLayout(x, y)
        adjustPosition(false)

        if (postUpdate && width <= 0 && isAdded && enabled) {
            postUpdateOverlayBounds()
        }
    }

    override fun resetPositionAndState() {
        preferences.clear()
        loadSavedPosition(true)
    }

    override fun addToWindow() {
        addToWindow = true
        if (isAdded || !overlays.canDrawOverlays) return
        windowManager.addView(view, params)
        if (overlayBounds.isEmpty) {
            overlayBounds.set(displayBoundsTracker.displayBounds)
        }
        postUpdateOverlayBounds()
    }

    private fun postUpdateOverlayBounds() {
        // after adding to window for first time or resetting position and state
        // we need to wait to next loop to ensure view has had a layout pass (otherwise width=0) and
        // end up with a 'left' value outside our overlay bounds
        handler.post {
            updateOverlayBounds(overlayBounds, false)
        }
    }

    override fun removeFromWindow() {
        addToWindow = false
        if (!isAdded) return
        try {
            windowManager.removeView(view)
        } catch (t: Throwable) {
            log.w(t) { "Exception while removing window view :: view=$view, this=$this" }
        }
    }

    override fun dispose() {
        foregroundTracker -= foregroundListener
        displayBoundsTracker -= boundsListener
        overlays -= fullScreenLockListener
    }

    private val width get() = view.width
    private val height get() = view.height
    private val windowLeft get() = params.x
    private val windowRight get() = params.x + width
    private val windowTop get() = params.y
    private val windowBottom get() = params.y + height

    private val leftEdge get() = windowLeft
    private val rightEdge get() = overlayBounds.width() - leftEdge - width
    private val topEdge get() = windowTop
    private val bottomEdge get() = overlayBounds.height() - topEdge - height - statusBarHeight

    private val statusBarHeight: Int
        get() = application.resources.let { res ->
            val resourceId = res.getIdentifier("status_bar_height", "dimen", "android")
            // If manufacturer has broken this for some reason, it's not the end of the world to assume 0
            return if (resourceId > 0) res.getDimensionPixelSize(resourceId) else 0
        }

    private fun adjustPosition(updatePrefs: Boolean) {
        // find nearest edge
        val leftEdge = leftEdge
        val rightEdge = rightEdge
        val topEdge = topEdge
        val bottomEdge = bottomEdge

        // don't trigger force-snap for non-snapping mode
        val snapToEdge = snapToEdge
        val edgeValue = if (snapToEdge) 0 else -1

        // force snap edges if out of bounds (preferentially to left/top if out of multiple sides)
        val forceSnapLeft = leftEdge <= edgeValue
        val forceSnapRight = !forceSnapLeft && rightEdge <= edgeValue
        val forceSnapTop = topEdge <= edgeValue
        val forceSnapBottom = !forceSnapTop && bottomEdge <= edgeValue

        // where do we want to snap to (preferentially to left/top if equidistant)
        val snapToLeft = snapToEdge && leftEdge <= rightEdge && leftEdge <= topEdge && leftEdge <= bottomEdge
        val snapToRight = snapToEdge && rightEdge < leftEdge && rightEdge < topEdge && rightEdge < bottomEdge
        val snapToTop = snapToEdge && topEdge < leftEdge && topEdge < rightEdge && topEdge <= bottomEdge
        val snapToBottom = snapToEdge && bottomEdge < leftEdge && bottomEdge < rightEdge && bottomEdge < topEdge

        // where are we snapping to
        val toLeft = forceSnapLeft || snapToLeft
        val toRight = !toLeft && (forceSnapRight || snapToRight)
        val toTop = forceSnapTop || snapToTop
        val toBottom = !toTop && (forceSnapBottom || snapToBottom)

        // how far to move
        val distanceX = when {
            !toLeft && !toRight && !snapToEdge -> leftEdge - (left * overlayBounds.width()).toInt()
            toLeft -> leftEdge + inset.left
            toRight && rightEdge < 0 -> Math.min(-rightEdge, leftEdge) - inset.right // don't go over left of overlay bounds
            toRight -> -rightEdge - inset.right
            else -> 0
        }
        val distanceY = when {
            !toTop && !toBottom && !snapToEdge -> topEdge - (top * overlayBounds.height()).toInt()
            toTop -> topEdge + inset.top
            toBottom && bottomEdge < 0 -> Math.min(-bottomEdge, topEdge) - inset.bottom // don't go over top of overlay bounds
            toBottom -> -bottomEdge - inset.bottom
            else -> 0
        }

        log.d(predicate = false) {
            """adjustPosition(updatePrefs=$updatePrefs):
              |$this
              |snapToEdge: $snapToEdge
              |forceSnap: {l:$forceSnapLeft, r:$forceSnapRight, t:$forceSnapTop, b:$forceSnapBottom}
              |wantSnap: {l:$snapToLeft, r:$snapToRight, t:$snapToTop, b:$snapToBottom}
              |willSnap: {l:$toLeft, r:$toRight, t:$toTop, b:$toBottom}
              |travel: {x:$distanceX, y:$distanceY}""".trimMargin()
        }

        animateToPosition(distanceX, distanceY) { percent ->
            if (updatePrefs && percent >= 1.0f) {
                if (snapToEdge) {
                    dock = when {
                        toLeft && toTop -> Dock.TOP_LEFT
                        toRight && toTop -> Dock.TOP_RIGHT
                        toLeft && toBottom -> Dock.BOTTOM_LEFT
                        toRight && toBottom -> Dock.BOTTOM_RIGHT
                        toLeft -> Dock.LEFT
                        toRight -> Dock.RIGHT
                        toTop -> Dock.TOP
                        toBottom -> Dock.BOTTOM
                        else -> Dock.TOP_LEFT
                    }
                    delta = when (dock) {
                        Dock.LEFT, Dock.RIGHT -> (params.y - overlayBounds.top) / overlayBounds.height().toFloat()
                        Dock.TOP, Dock.BOTTOM -> (params.x - overlayBounds.left) / overlayBounds.width().toFloat()
                        else -> 0f
                    }
                } else {
                    saveNonSnapPosition()
                }
            }
        }
    }

    private fun saveNonSnapPosition() {
        left = (params.x - overlayBounds.left) / overlayBounds.width().toFloat()
        top = (params.y - overlayBounds.top) / overlayBounds.height().toFloat()
    }

    private fun animateToPosition(distanceX: Int, distanceY: Int, onPercentChange: ((Float) -> Unit)? = null) {
        // nothing to see
        if (distanceX == 0 && distanceY == 0) {
            onPercentChange?.invoke(1f)
            return
        }

        val startX = windowLeft
        val startY = windowTop
        ValueAnimator.ofFloat(0f, 1.0f)?.apply {
            duration = run calculateDuration@{
                val proportionX = abs(if (distanceX != 0) distanceX.toFloat() / (overlayBounds.width() / 2f) else 0f)
                val proportionY = abs(if (distanceY != 0) distanceY.toFloat() / (overlayBounds.height() / 2f) else 0f)
                val proportion = Math.min(proportionX + proportionY, 1f)
                Math.max((MAX_ANIMATION_MILLIS * proportion).toLong(), MIN_ANIMATION_MILLIS)
            }
            interpolator = OvershootInterpolator(0.8f)
            addUpdateListener { valueAnimator ->
                val percent = valueAnimator.animatedValue as Float

                params.apply {
                    x = startX - (percent * distanceX).toInt()
                    y = startY - (percent * distanceY).toInt()
                }

                onPercentChange?.invoke(percent)

                updateLayout()
            }
            start()
        }
    }

    private fun updateLayout(x: Int? = null, y: Int? = null, width: Int? = null, height: Int? = null) {
        x?.let { params.x = it }
        y?.let { params.y = it }
        width?.let { params.width = it }
        height?.let { params.height = it }
        view.parent?.let {
            windowManager.updateViewLayout(view, params)
        }
    }

    override fun toString() =
        """
          |overlay: {
          |  name: $prefsName,
          |  isAdded: $isAdded,
          |  prefs: {
          |    dock: $dock,
          |    delta: $delta,
          |    left: $left,
          |    top: $top,
          |    snapToEdge: $snapToEdge,
          |    enabled: $enabled
          |  },
          |  position: {
          |    left: $windowLeft,
          |    right: $windowRight,
          |    top: $windowTop,
          |    bottom: $windowBottom
          |  },
          |  size: {
          |    width: $width,
          |    height: $height
          |  },
          |  inset: {
          |    left: ${inset.left},
          |    right: ${inset.right},
          |    top: ${inset.top},
          |    bottom: ${inset.bottom}
          |  },
          |  edges: {
          |    left: $leftEdge,
          |    right: $rightEdge,
          |    top: $topEdge,
          |    bottom: $bottomEdge
          |  },
          |  view: {w:${view.width}, h:${view.height}, visibility:${Visibility.fromValue(view.visibility)}},
          |  wind: {w:${params.width}, h:${params.height}},
          |  bounds: {l:${overlayBounds.left}, r:${overlayBounds.right}, t:${overlayBounds.top}, b:${overlayBounds.bottom}}
          |}""".trimMargin()

    private enum class Visibility(val value: Int) {
        VISIBLE(0), INVISIBLE(4), GONE(8);

        companion object {
            private val values = values().associateBy { it.value }
            fun fromValue(value: Int) = values[value]
        }
    }
}

internal fun createOverlayWindowParams() =
    WindowManager.LayoutParams().apply {
        type = windowOverlayType
        format = PixelFormat.TRANSLUCENT
        flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        gravity = Gravity.TOP or Gravity.START
    }

@Suppress("DEPRECATION")
private val windowOverlayType by lazy {
    when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        else -> WindowManager.LayoutParams.TYPE_PHONE
    }
}
