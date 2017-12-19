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
import android.support.v4.view.ViewCompat
import android.support.v7.app.AlertDialog
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.*
import android.view.animation.OvershootInterpolator
import android.widget.CheckBox
import android.widget.TextView
import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.core.*
import com.nextfaze.devfun.inject.Constructable
import com.nextfaze.devfun.internal.*
import com.nextfaze.devfun.menu.*
import com.nextfaze.devfun.menu.BuildConfig
import com.nextfaze.devfun.menu.R
import kotlinx.android.synthetic.main.df_menu_cog_overlay.view.cogButton

private const val PREF_TO_LEFT = "toLeft"
private const val PREF_VERTICAL_FACTOR = "verticalFactor"
private const val PREF_VISIBLE = "visible"
private const val PREF_PERMISSIONS = "permissions"
private const val DEFAULT_VISIBILITY = true

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
 */
@DeveloperCategory("DevFun", "Cog Overlay")
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

    private val preferences = context.getSharedPreferences(CogOverlay::class.java.name, Context.MODE_PRIVATE)
    private var cogVisible: Boolean
        get() = preferences.getBoolean(PREF_VISIBLE, DEFAULT_VISIBILITY)
        set(value) = preferences.edit().putBoolean(PREF_VISIBLE, value).apply()
    private var permissions
        get() = OverlayPermissions.fromName(preferences.getString(PREF_PERMISSIONS, null)) ?: OverlayPermissions.NEVER_REQUESTED
        set(value) = preferences.edit().putString(PREF_PERMISSIONS, value.name).apply()

    override fun attach(developerMenu: DeveloperMenu) {
        this.developerMenu = developerMenu

        listener = application.registerOnActivityResumedAndStopped(
                onResumed = {
                    if (it is FragmentActivity) {
                        when {
                            canDrawOverlays -> addOverlay()
                            permissions != OverlayPermissions.NEVER_ASK_AGAIN && !isInstrumentationTest -> managePermissions()
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

    override val title: String get() = application.getString(R.string.df_menu_cog_overlay)
    override val actionDescription: CharSequence?
        get() = mutableListOf<Int>()
                .apply {
                    if (!cogVisible) {
                        this += R.string.df_menu_cog_overlay_hidden_by_user
                    }
                    if (!canDrawOverlays) {
                        this += R.string.df_menu_cog_overlay_no_permissions
                    }
                    if (isEmpty()) {
                        this += R.string.df_menu_cog_tap_to_show
                    }
                }
                .joinTo(SpannableStringBuilder(), "\n") { resId ->
                    SpannableStringBuilder().also {
                        it += " • "
                        it += application.getText(resId)
                    }
                }

    override fun onShown() = setVisible(false)
    override fun onDismissed() = setVisible(true)

    private fun setVisible(visible: Boolean) {
        windowView?.visibility = if (visible && cogVisible && fragmentActivity != null) View.VISIBLE else View.GONE
    }

    private fun addOverlay(force: Boolean = false) {
        val developerMenu = developerMenu ?: return
        val newScreenSize = Point().apply {
            windowManager.defaultDisplay.getSize(this)
            y -= statusBarHeight
        }

        if (!force && ((overlayAdded && newScreenSize == screenSize) || !canDrawOverlays)) return

        screenSize.set(newScreenSize.x, newScreenSize.y)
        removeCurrentWindow()

        val windowView = View.inflate(application, R.layout.df_menu_cog_overlay, null).also { windowView = it } ?: throw RuntimeException("Failed to inflate cog overlay")
        windowView.cogButton.apply {
            ViewCompat.setElevation(this, resources.getDimensionPixelSize(R.dimen.df_menu_cog_elevation).toFloat())
            DrawableCompat.setTintList(DrawableCompat.wrap(background), ContextCompat.getColorStateList(application, R.color.df_menu_cog_background))
        }

        windowView.addOnLayoutChangeListener(object : View.OnLayoutChangeListener {
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
                val iconInset = iconSize / 5

                overlayBounds.set(
                        -iconInset,
                        -iconInset,
                        screenSize.x - iconSize + iconInset,
                        screenSize.y - iconSize + iconInset
                )

                windowView.removeOnLayoutChangeListener(this)

                windowParams.x = if (preferences.getBoolean(PREF_TO_LEFT, true)) overlayBounds.left else overlayBounds.right

                val desiredYOffset = (preferences.getFloat(PREF_VERTICAL_FACTOR, 0f) * ySpan).toInt()
                windowParams.y = clamp(overlayBounds.top + desiredYOffset, overlayBounds.top, overlayBounds.bottom)

                windowManager.updateViewLayout(windowView, windowParams)
            }
        })

        windowView.setOnTouchListener(object : View.OnTouchListener {
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

        windowView.setOnClickListener { fragmentActivity?.let(developerMenu::show) }

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
    private fun managePermissions() = fragmentActivity?.let {
        OverlayPermissionsDialogFragment.show(it, permissions).apply {
            deniedCallback = { neverAskAgain ->
                permissions = if (neverAskAgain) OverlayPermissions.NEVER_ASK_AGAIN else OverlayPermissions.DENIED
            }
        }
    }

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
    private fun resetPositionAndState() {
        preferences.edit().clear().apply()
        cogVisible = DEFAULT_VISIBILITY
        addOverlay(true)
    }

    @Constructable
    private class SetCogVisibilityTransformer(private val cogOverlay: CogOverlay) : FunctionTransformer {
        override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition): List<SimpleFunctionItem> {
            return when {
                cogOverlay.canDrawOverlays -> {
                    listOf(object : SimpleFunctionItem(functionDefinition, categoryDefinition) {
                        override val name = if (cogOverlay.cogVisible) "Hide" else "Show"
                        override val args = listOf(!cogOverlay.cogVisible)
                    })
                }
                else -> emptyList()
            }
        }
    }

    @DeveloperFunction(transformer = SetCogVisibilityTransformer::class)
    private fun setCogVisibility(visible: Boolean) {
        cogVisible = visible
        activity?.let {
            if (!visible) {
                val msg = SpannableStringBuilder().also {
                    it += application.getText(R.string.df_menu_available_controllers)
                    it += "\n\n"
                    it += devFun.devMenu.actionDescription ?: application.getString(R.string.df_menu_no_controllers)
                }
                AlertDialog.Builder(it)
                        .setTitle(R.string.df_menu_cog_overlay_hidden)
                        .setMessage(msg)
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

    private val statusBarHeight: Int
        get() = application.resources.let { res ->
            val resourceId = res.getIdentifier("status_bar_height", "dimen", "android")
            // If manufacturer has broken this for some reason, it's not the end of the world to assume 0
            return if (resourceId > 0) res.getDimensionPixelSize(resourceId) else 0
        }
}

internal enum class OverlayPermissions {
    NEVER_REQUESTED,
    DENIED,
    NEVER_ASK_AGAIN;

    companion object {
        fun fromName(name: String?) = try {
            name?.let { OverlayPermissions.valueOf(name) }
        } catch (t: Throwable) {
            null
        }
    }
}

internal class OverlayPermissionsDialogFragment : DialogFragment() {
    companion object {
        fun show(activity: FragmentActivity, permissions: OverlayPermissions) =
                activity.obtain {
                    OverlayPermissionsDialogFragment().apply {
                        arguments = Bundle().apply {
                            putString(PREF_PERMISSIONS, permissions.name)
                        }
                    }
                }.apply {
                    takeIf { !it.isAdded }?.show(activity.supportFragmentManager)
                }
    }

    var deniedCallback: ((neverAskAgain: Boolean) -> Unit)? = null

    private val permissions by lazy { OverlayPermissions.valueOf(arguments!!.getString(PREF_PERMISSIONS)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = activity!!
        val msg = SpannableStringBuilder().also {
            it += activity.getString(R.string.df_menu_overlay_reason, BuildConfig.VERSION_NAME)
            it += "\n\n"
            it += activity.getText(R.string.df_menu_available_controllers)
            it += "\n\n"
            it += devFun.devMenu.actionDescription ?: activity.getString(R.string.df_menu_no_controllers)
        }

        val (neverAskAgainCheckBox, dialogView) = LayoutInflater.from(context).inflate(R.layout.df_menu_permissions_checkbox, view as ViewGroup?).run {
            findViewById<TextView>(R.id.messageTextView).text = msg
            findViewById<CheckBox>(R.id.neverAskAgainCheckBox).apply {
                isChecked = permissions != OverlayPermissions.NEVER_REQUESTED
            } to this
        }

        return AlertDialog.Builder(activity)
                .setTitle(R.string.df_menu_overlay_request)
                .setView(dialogView)
                .setPositiveButton(R.string.df_menu_allow, { dialog, _ ->
                    val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, "package:${activity.packageName}".toUri())
                    activity.startActivityForResult(intent, 1234)
                    dialog.dismiss()
                })
                .setNegativeButton(R.string.df_menu_deny, { dialog, _ ->
                    deniedCallback?.invoke(neverAskAgainCheckBox.isChecked)
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

internal operator fun Appendable.plusAssign(charSequence: CharSequence) {
    this.append(charSequence)
}
