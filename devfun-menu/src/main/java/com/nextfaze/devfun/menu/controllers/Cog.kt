package com.nextfaze.devfun.menu.controllers

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.graphics.Point
import android.graphics.PointF
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v4.math.MathUtils.clamp
import android.support.v7.app.AlertDialog
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import android.view.animation.OvershootInterpolator
import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.core.CategoryDefinition
import com.nextfaze.devfun.core.FunctionDefinition
import com.nextfaze.devfun.core.FunctionTransformer
import com.nextfaze.devfun.core.SimpleFunctionItem
import com.nextfaze.devfun.inject.Constructable
import com.nextfaze.devfun.internal.*
import com.nextfaze.devfun.menu.*

/**
 * Controls the floating cog overlay.
 *
 * Manages/requests permissions as needed, and hides/shows when app view context changes.
 *
 * Background color/tint of the cog can be changed by declaring (overriding) a color resource `df_menu_cog_background_color`
 *
 * e.g.
 * ```xml
 *     <color name="df_menu_cog_background_color">#FF0000</color> <!-- red -->
 * ```
 *
 */

private const val PREF_TO_LEFT = "toLeft"
private const val PREF_VERTICAL_FACTOR = "verticalFactor"

@DeveloperCategory("DevFun", "Developer Menu")
class CogOverlay constructor(
        context: Context,
        private val activityProvider: ActivityProvider
) : MenuController {

    private val log = logger()
    private val application = context.applicationContext as Application
    private val windowManager = application.windowManager

    private val activity get() = activityProvider()
    private val fragmentActivity get() = activity as? FragmentActivity

    private val overlayBounds = Rect(0, 0, 0, 0)
    private val screenSize = Point(0, 0)

    private val ySpan: Int
        get() = overlayBounds.bottom - overlayBounds.top

    private var windowParams = newLayoutParams()

    private val canDrawOverlays: Boolean
        get() {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(application)) {
                return true
            }
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
                return forceCheckPermissionsEnabled()
            }
            return false
        }
    private var overlayAdded = false

    private var windowView: View? = null

    private var listener: Application.ActivityLifecycleCallbacks? = null
    private var developerMenu: DeveloperMenu? = null

    private var cogVisible = true

    private var preferences = context.getSharedPreferences(CogOverlay::class.java.name, Context.MODE_PRIVATE)

    override fun attach(developerMenu: DeveloperMenu) {
        this.developerMenu = developerMenu

        listener = application.registerOnActivityResumedAndStopped(
                onResumed = {
                    if (it is FragmentActivity) {
                        when {
                            canDrawOverlays -> addOverlay()
                            !permissionsDenied && !isInstrumentationTest -> manageOverlayPermission()
                        }
                        setVisible(it.isRunningInForeground)
                    } else {
                        setVisible(false)
                    }
                },
                onStopped = {
                    if (it === activity) {
                        setVisible(false)
                    }
                }
        )
    }

    override fun detach() {
        listener?.unregister(application).also { listener = null }
        developerMenu = null
    }

    override fun onShown() = setVisible(false)
    override fun onDismissed() = setVisible(true)

    private fun setVisible(visible: Boolean) {
        windowView?.visible = visible && cogVisible && fragmentActivity != null
    }

    private fun addOverlay() {
        log.d { "addOverlay" }
        val developerMenu = developerMenu ?: return

        val metrics = DisplayMetrics().apply { windowManager.defaultDisplay.getMetrics(this) }
        val newScreenSize = Point(metrics.widthPixels, metrics.heightPixels)

        if ((overlayAdded && newScreenSize == screenSize) || !canDrawOverlays) return

        screenSize.set(newScreenSize.x, newScreenSize.y)
        removeCurrentWindow()

        val windowView = View.inflate(application, R.layout.df_menu_cog_overlay, null).also { windowView = it } ?: throw RuntimeException("Failed to inflate cog overlay")
        val cog = windowView.apply {
            DrawableCompat.setTintList(background, ContextCompat.getColorStateList(application, R.color.df_menu_cog_background))
        }

        cog.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
            override fun onLayoutChange(
                    v: View,
                    left: Int,
                    top: Int,
                    right: Int,
                    bottom: Int,
                    oldLeft: Int,
                    oldTop: Int,
                    oldRight: Int,
                    oldBottom: Int
            ) {
                if (v.width <= 0) return

                val iconSize = v.width
                val iconInset = iconSize / 10

                overlayBounds.set(
                        -iconInset,
                        0,
                        screenSize.x - iconSize + iconInset,
                        screenSize.y - iconSize
                )

                cog.removeOnLayoutChangeListener(this)

                windowParams.x = if (preferences.getBoolean(PREF_TO_LEFT, true)) overlayBounds.left else overlayBounds.right

                val desiredYOffset = (preferences.getFloat(PREF_VERTICAL_FACTOR, 0f) * ySpan).toInt()
                windowParams.y = clamp(overlayBounds.top + desiredYOffset, overlayBounds.top, overlayBounds.bottom)

                windowManager.updateViewLayout(windowView, windowParams)
            }
        })

        cog.setOnTouchListener(object : View.OnTouchListener {

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
                            x = windowParams.x.toFloat()
                            y = windowParams.y.toFloat()
                        }

                        moved = false

                        true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val dx = Math.abs(event.rawX - currentPosition.x)
                        val dy = Math.abs(event.rawY - currentPosition.y)
                        val slop = ViewConfiguration.get(application).scaledTouchSlop

                        if (moved || (dx >= slop || dy >= slop) && System.currentTimeMillis() - actionDownTime > ViewConfiguration.getTapTimeout()) {
                            moved = true
                            windowParams.x = Math.min(Math.max((event.rawX - initialPosition.x + initialWindowPosition.x).toInt(), overlayBounds.left), overlayBounds.right)
                            windowParams.y = (event.rawY - initialPosition.y + initialWindowPosition.y).toInt()

                            currentPosition.apply {
                                x = event.rawX
                                y = event.rawY
                            }

                            windowManager.updateViewLayout(v, windowParams)
                        }

                        true
                    }
                    MotionEvent.ACTION_UP -> {
                        if (!moved) {
                            v.performClick()
                        } else {
                            animateRelease()
                        }

                        moved = false
                        actionDownTime = 0
                        true
                    }
                    else -> false
                }
            }
        })

        cog.setOnClickListener { fragmentActivity?.let(developerMenu::show) }

        windowManager.addView(windowView, windowParams)
        overlayAdded = true
    }

    private fun animateRelease() {
        val startX = windowParams.x
        val startY = windowParams.y

        val toLeft = startX + windowView!!.width / 2 <= screenSize.x / 2

        val distanceX = when {
            toLeft -> startX - overlayBounds.left
            else -> startX - overlayBounds.right
        }

        val distanceY = when {
            startY < overlayBounds.top -> startY - overlayBounds.top
            startY > overlayBounds.bottom -> startY - overlayBounds.bottom
            else -> 0
        }

        val proportionX = distanceX.toFloat() / (screenSize.x / 2f)
        val duration = Math.max((750f * proportionX).toLong(), 250)

        val animator = ValueAnimator.ofFloat(0f, 1.0f)
        animator.addUpdateListener { valueAnimator ->
            val percent = valueAnimator.animatedValue as Float

            windowParams.apply {
                x = startX - (percent * distanceX).toInt()
                y = startY - (percent * distanceY).toInt()
            }

            if (percent == 1.0f) {
                // Release animation has finished, save current position
                preferences.edit().apply {
                    putBoolean(PREF_TO_LEFT, toLeft)
                    putFloat(PREF_VERTICAL_FACTOR, (windowParams.y - overlayBounds.top) / ySpan.toFloat())
                }.apply()
            }

            windowManager.updateViewLayout(windowView, windowParams)
        }

        animator.duration = duration
        animator.interpolator = OvershootInterpolator(0.9f)
        animator.start()
    }

    @DeveloperFunction(requiresApi = Build.VERSION_CODES.M)
    private fun manageOverlayPermission() = fragmentActivity?.show<OverlayPermissionsDialogFragment>()

    private val isInstrumentationTest by lazy {
        when {
            Thread.currentThread().contextClassLoader?.toString().orEmpty().contains("android.test.runner.jar") -> true
            else -> Log.getStackTraceString(Throwable()).contains("android.support.test.runner.MonitoringInstrumentation")
        }.also {
            if (it) {
                log.d { "Instance detected as instrumentation test - debug cog overlay disabled." }
            }
        }
    }

    private fun removeCurrentWindow() = windowView?.let { view ->
        view.parent?.let {
            try {
                windowManager.removeView(view)
            } catch (t: Throwable) {
                log.w(t) { "Exception while removing window view :: view=$view" }
            }
        }
    }

    private fun newLayoutParams() =
            WindowManager.LayoutParams().apply {
                type = windowOverlayType
                format = PixelFormat.TRANSLUCENT
                flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                width = ViewGroup.LayoutParams.WRAP_CONTENT
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                gravity = Gravity.TOP or Gravity.START
            }

    @DeveloperFunction
    private fun resetOverlayPosition() {
        if (overlayAdded) {
            windowParams = newLayoutParams()
            windowManager.updateViewLayout(windowView, windowParams)
        }
    }

    @Constructable
    private class SetCogVisibilityTransformer(private val cogOverlay: CogOverlay) : FunctionTransformer {
        override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition) =
                listOf(object : SimpleFunctionItem(functionDefinition, categoryDefinition) {
                    override val name = if (cogOverlay.cogVisible) "Hide cog overlay" else "Show cog overlay"
                    override val group = "Cog Overlay"
                    override val args = listOf(!cogOverlay.cogVisible)
                })
    }

    @DeveloperFunction(transformer = SetCogVisibilityTransformer::class)
    private fun setCogVisibility(visible: Boolean) {
        cogVisible = visible
        activity?.let {
            if (!visible) {
                AlertDialog.Builder(it)
                        .setTitle("Cog Overlay Hidden")
                        .setMessage("Show menu again using volume button sequence:\n\"down,down,up,down\"")
                        .show()
            }
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
    private fun forceCheckPermissionsEnabled() =
            try {
                val params = newLayoutParams()
                val view = View(application).apply { layoutParams = params }
                windowManager.addView(view, params)
                windowManager.removeView(view)
                log.d { "permissionCheckHack success!" }
                true
            } catch (ignore: Throwable) {
                log.d(ignore) { "permissionCheckHack failed!" }
                false
            }

    @Suppress("DEPRECATION")
    private val windowOverlayType
        @SuppressLint("InlinedApi")
        get() = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else -> WindowManager.LayoutParams.TYPE_PHONE
        }
}

private var permissionsDenied = false

internal class OverlayPermissionsDialogFragment : DialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity)
                .setTitle(R.string.df_menu_overlay_request)
                .setMessage(activity.getString(R.string.df_menu_overlay_reason, BuildConfig.VERSION_NAME))
                .setPositiveButton(android.R.string.yes, { dialog, _ ->
                    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, "package:${activity.packageName}".toUri())
                    activity.startActivityForResult(intent, 1234)
                    dialog.dismiss()
                })
                .setNegativeButton(android.R.string.no, { dialog, _ ->
                    permissionsDenied = true
                    dialog.dismiss()
                })
                .show()
    }

    override fun onDestroyView() {
        // Fix http:///issuetracker.google.com/17423
        dialog?.takeIf { retainInstance }?.setDismissMessage(null)
        super.onDestroyView()
    }
}

private var View.visible: Boolean
    get() = this.visibility == View.VISIBLE
    set(value) = when {
        value -> this.visibility = View.VISIBLE
        else -> this.visibility = View.GONE
    }

private fun String.toUri(): Uri = Uri.parse(this)

private val Context.isRunningInForeground: Boolean
    get() {
        @Suppress("DEPRECATION")
        val tasks = activityManager.getRunningTasks(1)
        if (tasks.isEmpty()) {
            return false
        }
        val topActivityName = tasks[0].topActivity.packageName
        return topActivityName.equals(packageName, ignoreCase = true)
    }
