package com.nextfaze.devfun.menu.internal

import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.app.Application
import android.graphics.PixelFormat
import android.graphics.PointF
import android.graphics.Rect
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.support.annotation.LayoutRes
import android.support.v4.math.MathUtils.clamp
import android.view.*
import android.view.animation.OvershootInterpolator
import com.nextfaze.devfun.internal.*
import java.lang.Math.abs

private const val MIN_ANIMATION_MILLIS = 250L
private const val MAX_ANIMATION_MILLIS = 500L

internal class OverlayWindow(
    private val application: Application,
    @LayoutRes layoutId: Int,
    private val prefsName: String,
    initialDock: Dock = Dock.TOP_LEFT,
    initialDelta: Float = 0f
) {
    private val log = logger()
    private val windowManager = application.windowManager
    private val handler = Handler(Looper.getMainLooper())

    private val preferences = KSharedPreferences.named(application, prefsName)
    private var dock by preferences["dock", initialDock]
    private var delta by preferences["delta", initialDelta]

    var onClick: ((View) -> Unit)? = null

    val isAdded get() = view.parent != null

    var viewInset = Rect()

    val canDrawOverlays: Boolean
        get() = when {
            Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(application) -> true
            Build.VERSION.SDK_INT == Build.VERSION_CODES.O -> forceCheckPermissionsEnabled()
            else -> false
        }

    private val params = createOverlayWindowParams()
    private val overlayBounds = Rect()

    fun updateOverlayBounds(bounds: Rect) {
        overlayBounds.set(bounds)
        loadSavedPosition()
    }

    val view: View by lazy {
        View.inflate(application, layoutId, null).apply {
            setOnTouchListener(object : View.OnTouchListener {
                private var currentPosition = PointF(0f, 0f)
                private var initialPosition = PointF(0f, 0f)
                private var initialWindowPosition = PointF(0f, 0f)

                private var actionDownTime = 0L
                private var moved = false

                override fun onTouch(v: View, event: MotionEvent?): Boolean {
                    return when (event?.action) {
                        MotionEvent.ACTION_DOWN -> {
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
                                snapToEdge(true)
                            }

                            moved = false
                            actionDownTime = 0
                            true
                        }
                        else -> false
                    }
                }
            })

            setOnClickListener { onClick?.invoke(this) }
        }
    }

    private fun loadSavedPosition() {
        val side = dock
        val displacement = delta

        // initial position
        var x = when (side) {
            Dock.LEFT, Dock.TOP_LEFT, Dock.BOTTOM_LEFT -> overlayBounds.left
            Dock.RIGHT, Dock.TOP_RIGHT, Dock.BOTTOM_RIGHT -> overlayBounds.right - width
            else -> {
                val xOffset = (displacement * overlayBounds.width()).toInt()
                clamp(overlayBounds.left + xOffset, overlayBounds.left, overlayBounds.right)
            }
        }
        var y = when (side) {
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
        snapToEdge(false)

        if (width <= 0 && isAdded) {
            postUpdateOverlayBounds()
        }
    }

    fun resetPositionAndState() {
        preferences.clear()
        loadSavedPosition()
    }

    fun addToWindow() {
        windowManager.addView(view, params)
        postUpdateOverlayBounds()
    }

    private fun postUpdateOverlayBounds() {
        // after adding to window for first time or resetting position and state
        // we need to wait to next loop to ensure view has had a layout pass (otherwise width=0) and
        // end up with a 'left' value outside our overlay bounds
        handler.post {
            updateOverlayBounds(overlayBounds)
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

    fun removeFromWindow() = view.parent?.let {
        try {
            windowManager.removeView(view)
        } catch (t: Throwable) {
            log.w(t) { "Exception while removing window view :: view=$view" }
        }
    }

    private val width get() = view.width
    private val height get() = view.height
    private val left get() = params.x
    private val right get() = params.x + width
    private val top get() = params.y
    private val bottom get() = params.y + height

    private val leftEdge get() = left
    private val rightEdge get() = overlayBounds.width() - leftEdge - width
    private val topEdge get() = top
    private val bottomEdge get() = overlayBounds.height() - topEdge - height - statusBarHeight

    private val statusBarHeight: Int
        get() = application.resources.let { res ->
            val resourceId = res.getIdentifier("status_bar_height", "dimen", "android")
            // If manufacturer has broken this for some reason, it's not the end of the world to assume 0
            return if (resourceId > 0) res.getDimensionPixelSize(resourceId) else 0
        }

    private fun snapToEdge(updatePrefs: Boolean) {
        // find nearest edge
        val leftEdge = leftEdge
        val rightEdge = rightEdge
        val topEdge = topEdge
        val bottomEdge = bottomEdge

        // force snap edges if out of bounds (preferentially to left/top if out of multiple sides)
        val forceSnapLeft = leftEdge <= 0
        val forceSnapRight = !forceSnapLeft && rightEdge <= 0
        val forceSnapTop = topEdge <= 0
        val forceSnapBottom = !forceSnapTop && bottomEdge <= 0

        // where do we want to snap to (preferentially to left/top if equidistant)
        val snapToLeft = leftEdge <= rightEdge && leftEdge <= topEdge && leftEdge <= bottomEdge
        val snapToRight = rightEdge < leftEdge && rightEdge < topEdge && rightEdge < bottomEdge
        val snapToTop = topEdge < leftEdge && topEdge < rightEdge && topEdge <= bottomEdge
        val snapToBottom = bottomEdge < leftEdge && bottomEdge < rightEdge && bottomEdge < topEdge

        // where are we snapping to
        val toLeft = forceSnapLeft || snapToLeft
        val toRight = !toLeft && (forceSnapRight || snapToRight)
        val toTop = forceSnapTop || snapToTop
        val toBottom = !toTop && (forceSnapBottom || snapToBottom)

        // how far to move
        val distanceX = when {
            toLeft -> leftEdge + viewInset.left
            toRight && rightEdge < 0 -> Math.min(-rightEdge, leftEdge) - viewInset.right // don't go over left of overlay bounds
            toRight -> -rightEdge - viewInset.right
            else -> 0
        }
        val distanceY = when {
            toTop -> topEdge + viewInset.top
            toBottom && bottomEdge < 0 -> Math.min(-bottomEdge, topEdge) - viewInset.bottom // don't go over top of overlay bounds
            toBottom -> -bottomEdge - viewInset.bottom
            else -> 0
        }

        log.t(predicate = false) {
            """snapToEdge:
              |$this
              |forceSnap: {l:$forceSnapLeft, r:$forceSnapRight, t:$forceSnapTop, b:$forceSnapBottom}
              |wantSnap: {l:$snapToLeft, r:$snapToRight, t:$snapToTop, b:$snapToBottom}
              |willSnap: {l:$toLeft, r:$toRight, t:$toTop, b:$toBottom}
              |travel: {x:$distanceX, y:$distanceY}""".trimMargin()
        }

        // nothing to see
        if (distanceX == 0 && distanceY == 0) {
            return
        }

        val startX = left
        val startY = top
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

                if (updatePrefs && percent >= 1.0f) {
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
                }

                updateLayout()
            }
            start()
        }
    }

    /**
     * Forcefully check if we have permissions on SDK 26
     *
     * See
     * - https://stackoverflow.com/questions/46187625/settings-candrawoverlayscontext-returns-false-on-android-oreo
     * - https://stackoverflow.com/questions/46173460/why-in-android-o-method-settings-candrawoverlays-returns-false-when-user-has
     * - https://issuetracker.google.com/issues/66072795
     */
    @TargetApi(Build.VERSION_CODES.O)
    private fun forceCheckPermissionsEnabled() =
        try {
            val params = createOverlayWindowParams()
            val view = View(application).apply { layoutParams = params }
            windowManager.addView(view, params)
            windowManager.removeView(view)
            log.d { "Overlay permissions check hack for SDK 26 success!" }
            true
        } catch (ignore: Throwable) {
            log.d(ignore) { "Overlay permissions check hack for SDK 26 failed!" }
            false
        }

    override fun toString() =
        """
          |overlay: {
          |  name: $prefsName,
          |  isAdded: $isAdded,
          |  prefs: {
          |    dock: $dock,
          |    delta: $delta
          |  },
          |  position: {
          |    left: $left,
          |    right: $right,
          |    top: $top,
          |    bottom: $bottom
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
          |  view: {w:${view.width}, h:${view.height}},
          |  wind: {w:${params.width}, h:${params.height}},
          |  bounds: {l:${overlayBounds.left}, r:${overlayBounds.right}, t:${overlayBounds.top}, b:${overlayBounds.bottom}}
          |}""".trimMargin()
}

internal enum class Dock { TOP, BOTTOM, LEFT, RIGHT, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT }

private fun createOverlayWindowParams() =
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
