package com.nextfaze.devfun.menu.controllers

import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
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
import android.support.v4.view.ViewCompat
import android.support.v7.app.AlertDialog
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.TextView
import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.core.ActivityProvider
import com.nextfaze.devfun.core.devFun
import com.nextfaze.devfun.inject.Constructable
import com.nextfaze.devfun.internal.*
import com.nextfaze.devfun.invoke.view.ColorPicker
import com.nextfaze.devfun.invoke.view.From
import com.nextfaze.devfun.invoke.view.Ranged
import com.nextfaze.devfun.invoke.view.ValueSource
import com.nextfaze.devfun.menu.*
import com.nextfaze.devfun.menu.BuildConfig
import com.nextfaze.devfun.menu.R
import com.nextfaze.devfun.menu.internal.Dock
import com.nextfaze.devfun.menu.internal.KSharedPreferences
import com.nextfaze.devfun.menu.internal.OverlayWindow

private const val PREF_PERMISSIONS = "permissions"

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

    private val overlay = OverlayWindow(application, R.layout.df_menu_cog_overlay, "DevFunCog", Dock.RIGHT, 0.7f)
        .apply {
            val padding = application.resources.getDimension(R.dimen.df_menu_cog_padding).toInt()
            val halfSize = ((padding * 2 + application.resources.getDimension(R.dimen.df_menu_cog_size)) / 2).toInt()
            viewInset = Rect(halfSize, halfSize, halfSize, padding)
            onClick = { fragmentActivity?.let { developerMenu?.show(it) } }
        }

    private var listener: Application.ActivityLifecycleCallbacks? = null
    private var developerMenu: DeveloperMenu? = null

    private val preferences = KSharedPreferences.named(context, "DevFunCog")
    private var cogVisible by preferences["cogVisible", true]
    private var cogColor by preferences["cogColor", ContextCompat.getColor(context, R.color.df_menu_cog_background)]
    private var permissions by preferences["permissionsState", OverlayPermissions.NEVER_REQUESTED]

    override fun attach(developerMenu: DeveloperMenu) {
        this.developerMenu = developerMenu

        /**
         * Using the activity like this ensures that rotation, status bar, and other system views are taken into account.
         * Thus for multi-window use it should be properly bounded to the relevant app window area.
         * i.e. This is the space available for the *app* now the *device*
         */
        fun updateDisplayBounds(activity: Activity) {
            val displayBounds = Rect(0, 0, 0, 0)
            (activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay?.getRectSize(displayBounds)
            overlay.updateOverlayBounds(displayBounds)
        }

        listener = application.registerActivityCallbacks(
            onCreated = { activity, _ ->
                updateDisplayBounds(activity)
            },
            onResumed = {
                updateDisplayBounds(it)
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
        overlay.view.visibility = if (visible && cogVisible && fragmentActivity != null) View.VISIBLE else View.GONE
    }

    private fun addOverlay(force: Boolean = false) {
        val developerMenu = developerMenu ?: return
        if (!force && (overlay.isAdded || !canDrawOverlays)) return

        overlay.apply {
            removeFromWindow()
            onClick = { fragmentActivity?.let(developerMenu::show) }
            view.apply {
                findViewById<View>(R.id.cogButton).apply {
                    ViewCompat.setElevation(this, resources.getDimensionPixelSize(R.dimen.df_menu_cog_elevation).toFloat())
                    setColor(cogColor)
                }
            }
            addToWindow()
        }
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

    @DeveloperFunction
    private fun resetPositionAndState() {
        preferences.clear()
        overlay.resetPositionAndState()
        addOverlay(true)
    }

    @Constructable
    private inner class CurrentVisibility : ValueSource<Boolean> {
        override val value get() = cogVisible
    }

    @DeveloperFunction
    private fun setCogVisibility(@From(CurrentVisibility::class) visible: Boolean) {
        cogVisible = visible
        setVisible(visible)
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

    @Constructable
    private inner class CurrentColor : ValueSource<Int> {
        override val value get() = cogColor
    }

    @DeveloperFunction
    private fun setColor(@ColorPicker @From(CurrentColor::class) color: Int) {
        cogColor = color

        // we need to separate it out otherwise the img gets a weird alpha shadow effect
        val alpha = Color.alpha(color)
        val noAlphaColor = Color.rgb(Color.red(color), Color.green(color), Color.blue(color))

        overlay.view.findViewById<View>(R.id.cogButton).apply {
            DrawableCompat.setTint(DrawableCompat.wrap(background), noAlphaColor)
        }
        setAlpha(alpha)
    }

    @Constructable
    private inner class CurrentAlpha : ValueSource<Int> {
        override val value get() = Color.alpha(cogColor)
    }

    @DeveloperFunction
    private fun setAlpha(@Ranged(to = 255.0) @From(CurrentAlpha::class) alpha: Int) {
        val c = cogColor
        cogColor = Color.argb(alpha, Color.red(c), Color.green(c), Color.blue(c))
        overlay.view.alpha = alpha / 255f
    }

    @DeveloperFunction
    private fun showOverlayState(activity: Activity) =
        overlay.toString()
            .also {
                AlertDialog.Builder(activity)
                    .setTitle("Overlay State")
                    .setMessage(
                        SpannableStringBuilder().apply {
                            this += pre(it)
                        }
                    )
                    .show()
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
            val params = OverlayWindow.newParams()
            val view = View(application).apply { layoutParams = params }
            windowManager.addView(view, params)
            windowManager.removeView(view)
            log.d { "permissionCheckHack success!" }
            true
        } catch (ignore: Throwable) {
            log.d(ignore) { "permissionCheckHack failed!" }
            false
        }
}

internal enum class OverlayPermissions { NEVER_REQUESTED, DENIED, NEVER_ASK_AGAIN }

internal class OverlayPermissionsDialogFragment : DialogFragment() {
    companion object {
        fun show(activity: FragmentActivity, permissions: OverlayPermissions) = activity
            .obtain {
                OverlayPermissionsDialogFragment().apply {
                    arguments = Bundle().apply {
                        putString(PREF_PERMISSIONS, permissions.name)
                    }
                }
            }
            .apply {
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

        val (neverAskAgainCheckBox, dialogView) = LayoutInflater.from(context)
            .inflate(R.layout.df_menu_permissions_checkbox, view as ViewGroup?)
            .run {
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
