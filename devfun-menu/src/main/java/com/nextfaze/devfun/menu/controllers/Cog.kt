package com.nextfaze.devfun.menu.controllers

import android.annotation.SuppressLint
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.*
import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.internal.*
import com.nextfaze.devfun.menu.*
import kotlinx.android.synthetic.main.df_menu_cog_overlay.view.cogButton

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
@DeveloperCategory("DevFun", "Developer Menu")
class CogOverlay constructor(context: Context,
                             private val activityProvider: ActivityProvider) : MenuController {
    private val log = logger()
    private val application = context.applicationContext as Application
    private val windowManager = application.windowManager

    private val activity get() = activityProvider()
    private val fragmentActivity get() = activity as? FragmentActivity

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

    private val dragObject = Any()
    private var windowView: View? = null
    private var moved = false
    private var lastXPos = 0
    private var lastYPos = 0

    private var listener: Application.ActivityLifecycleCallbacks? = null
    private var developerMenu: DeveloperMenu? = null

    override fun attach(developerMenu: DeveloperMenu) {
        this.developerMenu = developerMenu

        listener = application.registerOnActivityResumedAndStopped(
                onResumed = {
                    if (it is FragmentActivity) {
                        when {
                            canDrawOverlays -> addOverlay()
                            !permissionsDenied -> manageOverlayPermission()
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
        windowView?.visible = visible && fragmentActivity != null
    }

    private fun addOverlay() {
        log.d { "addOverlay" }
        val developerMenu = developerMenu ?: return
        if (overlayAdded || !canDrawOverlays) return

        removeCurrentWindow()
        val windowView = View.inflate(application, R.layout.df_menu_cog_overlay, null).also { windowView = it } ?: throw RuntimeException("Failed to inflate cog overlay")
        val cog = windowView.cogButton.apply {
            DrawableCompat.setTintList(background, ContextCompat.getColorStateList(application, R.color.df_menu_cog_background))
        }

        windowView.setOnDragListener(object : View.OnDragListener {
            private var lastX = 0f
            private var lastY = 0f
            private var alpha = 0f

            override fun onDrag(v: View, event: DragEvent): Boolean {
                if (event.localState !== dragObject) return false

                when (event.action) {
                    DragEvent.ACTION_DRAG_STARTED -> {
                        moved = true

                        alpha = cog.alpha
                        cog.alpha = 0f // setVisibility(INVISIBLE); doesn't work properly when using window manager?

                        val lpStart = newLayoutParams().apply {
                            width = ViewGroup.LayoutParams.MATCH_PARENT
                            height = ViewGroup.LayoutParams.MATCH_PARENT
                            gravity = Gravity.NO_GRAVITY
                            x = lastXPos
                            y = lastYPos
                        }
                        windowManager.updateViewLayout(windowView, lpStart)
                        return true
                    }

                    DragEvent.ACTION_DRAG_ENDED -> {
                        lastXPos = (lastX - 0.5f * cog.width).toInt()
                        lastYPos = (lastY - 0.5f * cog.height).toInt()
                        windowManager.updateViewLayout(windowView, newLayoutParams())

                        cog.alpha = alpha // setVisibility(VISIBLE); doesn't work properly when using window manager?
                        return true
                    }

                    DragEvent.ACTION_DRAG_LOCATION -> {
                        lastX = event.x
                        lastY = event.y
                        return true
                    }

                    else -> return false
                }
            }
        })

        cog.setOnClickListener { fragmentActivity?.let(developerMenu::show) }

        cog.setOnLongClickListener { v ->
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                @Suppress("DEPRECATION")
                v.startDrag(null, View.DragShadowBuilder(cog), dragObject, 0)
            } else {
                v.startDragAndDrop(null, View.DragShadowBuilder(cog), dragObject, 0)
            }
            true
        }

        windowManager.addView(windowView, newLayoutParams())
        overlayAdded = true
    }

    @DeveloperFunction(requiresApi = Build.VERSION_CODES.M)
    private fun manageOverlayPermission() =
            if (!isInstrumentationTest) fragmentActivity?.show<OverlayPermissionsDialogFragment>() else null

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
                flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                width = ViewGroup.LayoutParams.WRAP_CONTENT
                height = ViewGroup.LayoutParams.WRAP_CONTENT
                gravity = Gravity.TOP or Gravity.START

                if (moved) {
                    x = lastXPos
                    y = lastYPos
                } else {
                    x = 200
                }
            }

    @DeveloperFunction
    private fun resetOverlayPosition() {
        if (overlayAdded) {
            moved = false
            windowManager.updateViewLayout(windowView, newLayoutParams())
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
