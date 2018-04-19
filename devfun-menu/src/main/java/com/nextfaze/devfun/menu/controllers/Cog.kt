package com.nextfaze.devfun.menu.controllers

import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
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
import android.widget.ImageView
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
 * Background color/tint of the cog can be changed by declaring (overriding) a color resource `df_menu_cog_tint`
 *
 * e.g.
 * ```xml
 * <color name="df_menu_cog_tint">#77FF0000</color> <!-- red -->
 * ```
 */
@DeveloperCategory("DevFun", "Cog Overlay")
class CogOverlay constructor(
    context: Context,
    private val activityProvider: ActivityProvider
) : MenuController {
    private val log = logger()
    private val application = context.applicationContext as Application

    private val activity get() = activityProvider()
    private val fragmentActivity get() = activity as? FragmentActivity

    private val overlay = OverlayWindow(application, R.layout.df_menu_cog_overlay, "DevFunCog", Dock.RIGHT, 0.7f)
        .apply {
            val padding = application.resources.getDimension(R.dimen.df_menu_cog_padding).toInt()
            val halfSize = ((padding * 2 + application.resources.getDimension(R.dimen.df_menu_cog_size)) / 2).toInt()
            viewInset = Rect(halfSize, halfSize, halfSize, padding)
            onClick = { fragmentActivity?.let { developerMenu?.show(it) } }
        }

    private var listener: Application.ActivityLifecycleCallbacks? = null
    private var developerMenu: DeveloperMenu? = null
    private var menuVisible = false

    private val preferences = KSharedPreferences.named(context, "DevFunCog")
    private var cogVisible by preferences["cogVisible", true]
    private val cogColorPref = preferences["cogColor", defaultTint]
    private var cogColor by cogColorPref
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
                        overlay.canDrawOverlays -> addOverlay(it)
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
                if (!overlay.canDrawOverlays) {
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

    override fun onShown() {
        menuVisible = true
        setVisible(false)
    }

    override fun onDismissed() {
        menuVisible = false
        setVisible(true)
    }

    private fun setVisible(visible: Boolean) {
        overlay.view.visibility = if (visible && !menuVisible && cogVisible && fragmentActivity != null) View.VISIBLE else View.GONE
    }

    private fun addOverlay(activity: Activity, force: Boolean = false) {
        if (!force && (overlay.isAdded || !overlay.canDrawOverlays)) return

        overlay.apply {
            removeFromWindow()
            onClick = ::onOverlayClick
            view.apply {
                findViewById<View>(R.id.cogButton).apply {
                    ViewCompat.setElevation(this, resources.getDimensionPixelSize(R.dimen.df_menu_cog_elevation).toFloat())
                    tintOverlayView(resolveTint(activity))
                }
            }
            addToWindow()
        }
    }

    /**
     * This function was extracted like this as the activity parameter is leaked from with the original code:
     * ```kotlin
     * val developerMenu = developerMenu ?: return
     * onClick = { fragmentActivity?.let(developerMenu::show) }
     * ```
     * (bug has been reported - the onClick lambda is incorrectly generated)
     * https://youtrack.jetbrains.com/issue/KT-23881
     */
    private fun onOverlayClick(@Suppress("UNUSED_PARAMETER") view: View) {
        developerMenu?.also { menu ->
            fragmentActivity?.also { activity ->
                menu.show(activity)
            }
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
    private fun resetPositionAndState(activity: Activity) {
        preferences.clear()
        overlay.resetPositionAndState()
        addOverlay(activity, true)
    }

    @DeveloperFunction
    private fun resetColor(activity: Activity) {
        cogColorPref.delete()
        tintOverlayView(resolveTint(activity))
    }

    @Constructable
    private inner class CurrentVisibility : ValueSource<Boolean> {
        override val value get() = cogVisible
    }

    @DeveloperFunction
    private fun setVisibility(@From(CurrentVisibility::class) visible: Boolean) {
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
    private inner class CurrentColor(private val activity: Activity) : ValueSource<Int> {
        override val value get() = resolveTint(activity)
    }

    @DeveloperFunction
    private fun setColor(@ColorPicker @From(CurrentColor::class) color: Int) {
        cogColor = color
        tintOverlayView(cogColor)
    }

    @Constructable
    private inner class CurrentAlpha(private val activity: Activity) : ValueSource<Int> {
        override val value get() = Color.alpha(resolveTint(activity))
    }

    @DeveloperFunction
    private fun setAlpha(activity: Activity, @Ranged(to = 255.0) @From(CurrentAlpha::class) alpha: Int) {
        val c = resolveTint(activity)
        cogColor = Color.argb(alpha, Color.red(c), Color.green(c), Color.blue(c))
        tintOverlayView(cogColor)
    }

    private fun tintOverlayView(color: Int) {
        val alpha = Color.alpha(color)
        val noAlphaColor = Color.rgb(Color.red(color), Color.green(color), Color.blue(color))

        if (Build.VERSION.SDK_INT >= 21) {
            // we need to separate it out otherwise the img gets a weird alpha shadow effect
            overlay.view.alpha = alpha / 255f
            overlay.view.findViewById<View>(R.id.cogButton).apply {
                DrawableCompat.setTint(DrawableCompat.wrap(background), noAlphaColor)
            }
        } else {
            // older SDKs don't support setting the alpha value on a ViewGroup
            // so we do it to the ImageView instead (doesn't look as nice though)
            overlay.view.findViewById<ImageView>(R.id.cogButton).apply {
                DrawableCompat.setTint(DrawableCompat.wrap(background), color)
                when {
                    Build.VERSION.SDK_INT >= 16 -> imageAlpha = alpha
                    else -> @Suppress("DEPRECATION") setAlpha(alpha)
                }
            }
        }

        // required for older devices
        overlay.view.invalidate()
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

    private fun resolveTint(activity: Activity) =
        try {
            if (cogColorPref.isSet) {
                // user has overridden value manually
                cogColor.also {
                    log.d { "Using custom tint set by user: ${it.toColorStruct()}" }
                }
            } else {
                // if the use has defined their own tint
                ContextCompat.getColor(activity, R.color.df_menu_cog_tint).also {
                    log.d { "Using user defined resource 'df_menu_cog_tint' for cog overlay tint: ${it.toColorStruct()}" }
                }
            }
        } catch (ignore: Resources.NotFoundException) {
            log.d { "Override resource tint 'df_menu_cog_tint' not defined. Trying theme accent color..." }

            try {
                // otherwise use app accent color if defined
                val ta = activity.theme.obtainStyledAttributes(intArrayOf(android.support.v7.appcompat.R.attr.colorAccent))
                try {
                    if (ta.hasValue(0)) {
                        ta.getColor(0, defaultTint).let {
                            when {
                                Color.alpha(it) == 0xFF -> (it.toLong() and 0x77FFFFFF).toInt()
                                else -> it
                            }
                        }.also { log.d { "Using primary color attribute for tint: ${it.toColorStruct()}" } }
                    } else {
                        defaultTint.also {
                            log.d { "No appcompat 'colorPrimary' attribute defined. Using default tint value: ${it.toColorStruct()}" }
                        }
                    }
                } finally {
                    ta.recycle()
                }
            } catch (t: Throwable) {
                log.w(t) { "Unexpected error when trying to resolve cog overlay tint - falling back to default tint value: ${defaultTint.toColorStruct()}" }
                // else fall back to default tint
                defaultTint
            }
        }

    private fun Int.toColorStruct(): String {
        fun Int.toHexString() = Integer.toHexString(this)

        val a = Color.alpha(this)
        val (r, g, b) = Triple(Color.red(this), Color.green(this), Color.blue(this))
        return """$this as color:
            |color {
            |  components {
            |    alpha: $a ($${a.toHexString()})
            |    red: $r ($${r.toHexString()})
            |    green: $g ($${g.toHexString()})
            |    blue: $b ($${b.toHexString()})
            |  }
            |  code: #${this.toHexString()}
            |}""".trimMargin()
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

private val defaultTint = Color.argb(0x77, 0xEE, 0x41, 0x36)
