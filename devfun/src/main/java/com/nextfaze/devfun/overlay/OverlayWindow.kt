package com.nextfaze.devfun.overlay

import android.animation.ValueAnimator
import android.app.Activity
import android.app.Application
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
import com.nextfaze.devfun.internal.android.*
import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.internal.pref.*
import java.lang.Math.abs

private const val MIN_ANIMATION_MILLIS = 250L
private const val MAX_ANIMATION_MILLIS = 500L

typealias ClickListener = (View) -> Unit
typealias OverlayReason = () -> CharSequence
typealias VisibilityPredicate = (Activity) -> Boolean

enum class Dock { TOP, BOTTOM, LEFT, RIGHT, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT }

class OverlayWindow(
    private val application: Application,
    private val overlays: OverlayManager,
    @LayoutRes layoutId: Int,
    internal val prefsName: String,
    internal val reason: OverlayReason,
    private val onClick: ClickListener? = null,
    private val onLongClick: ClickListener? = null,
    internal val visibilityPredicate: VisibilityPredicate? = null,
    initialDock: Dock = Dock.TOP_LEFT,
    initialDelta: Float = 0f,
    snapToEdge: Boolean = true,
    initialLeft: Float = 0f,
    initialTop: Float = 0f
) {
    private val log = logger()
    private val windowManager = application.windowManager
    private val handler = Handler(Looper.getMainLooper())

    private val preferences = KSharedPreferences.named(application, prefsName)
    private var dock by preferences["dock", initialDock]
    private var delta by preferences["delta", initialDelta]
    private var left by preferences["left", initialLeft]
    private var top by preferences["top", initialTop]
    var snapToEdge: Boolean by preferences["snapToEdge", snapToEdge, { _, _ -> updatePosition(false) }]
    var enabled by preferences["enabled", true, { _, _ -> overlays.updateVisibilities() }]

    var viewInset = Rect()

    val canDrawOverlays get() = overlays.canDrawOverlays

    private val isAdded get() = view.parent != null
    private val params = createOverlayWindowParams()
    private val overlayBounds = Rect()

    fun updateOverlayBounds(bounds: Rect, postUpdate: Boolean = true) {
        overlayBounds.set(bounds)
        loadSavedPosition(postUpdate)
    }

    val view: View by lazy {
        View.inflate(application, layoutId, null).apply {
            var allowOnClick = true

            setOnTouchListener(object : View.OnTouchListener {
                private val onLongClickRunnable = Runnable {
                    onLongClick?.run {
                        allowOnClick = false
                        invoke(view)
                    }
                }

                private var currentPosition = PointF(0f, 0f)
                private var initialPosition = PointF(0f, 0f)
                private var initialWindowPosition = PointF(0f, 0f)

                private var actionDownTime = 0L
                private var moved = false

                override fun onTouch(v: View, event: MotionEvent): Boolean {
                    when {
                        onLongClick != null && event.action == MotionEvent.ACTION_DOWN -> handler.postDelayed(onLongClickRunnable, 1250L)
                        else -> handler.removeCallbacks(onLongClickRunnable)
                    }

                    return when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            allowOnClick = true
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

                            moved = false

                            true
                        }
                        MotionEvent.ACTION_MOVE -> {
                            val rawX = event.rawX
                            val rawY = event.rawY
                            val dx = abs(rawX - currentPosition.x)
                            val dy = abs(rawY - currentPosition.y)
                            val slop = ViewConfiguration.get(application).scaledTouchSlop

                            if (moved || (dx >= slop || dy >= slop) &&
                                System.currentTimeMillis() - actionDownTime > ViewConfiguration.getTapTimeout()) {
                                moved = true
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
                            if (!moved) {
                                v.performClick()
                            } else {
                                updatePosition(true)
                            }

                            moved = false
                            actionDownTime = 0
                            true
                        }
                        else -> false
                    }
                }
            })

            setOnClickListener {
                if (allowOnClick) {
                    onClick?.invoke(this)
                }
            }

            addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
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
            Dock.LEFT, Dock.TOP_LEFT, Dock.BOTTOM_LEFT -> viewInset.left
            Dock.RIGHT, Dock.TOP_RIGHT, Dock.BOTTOM_RIGHT -> -viewInset.right
            else -> 0
        }
        y -= when (side) {
            Dock.TOP, Dock.TOP_LEFT, Dock.TOP_RIGHT -> viewInset.top
            Dock.BOTTOM, Dock.BOTTOM_LEFT, Dock.BOTTOM_RIGHT -> -viewInset.bottom
            else -> 0
        }

        updateLayout(x, y)
        adjustPosition(false)

        if (postUpdate && width <= 0 && isAdded && enabled) {
            postUpdateOverlayBounds()
        }
    }

    fun resetPositionAndState() {
        preferences.clear()
        loadSavedPosition(true)
    }

    fun addToWindow() {
        if (isAdded) return
        windowManager.addView(view, params)
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

    fun removeFromWindow() = view.parent?.let {
        try {
            windowManager.removeView(view)
        } catch (t: Throwable) {
            log.w(t) { "Exception while removing window view :: view=$view" }
        }
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
            toLeft -> leftEdge + viewInset.left
            toRight && rightEdge < 0 -> Math.min(-rightEdge, leftEdge) - viewInset.right // don't go over left of overlay bounds
            toRight -> -rightEdge - viewInset.right
            else -> 0
        }
        val distanceY = when {
            !toTop && !toBottom && !snapToEdge -> topEdge - (top * overlayBounds.height()).toInt()
            toTop -> topEdge + viewInset.top
            toBottom && bottomEdge < 0 -> Math.min(-bottomEdge, topEdge) - viewInset.bottom // don't go over top of overlay bounds
            toBottom -> -bottomEdge - viewInset.bottom
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
          |    left: ${viewInset.left},
          |    right: ${viewInset.right},
          |    top: ${viewInset.top},
          |    bottom: ${viewInset.bottom}
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
