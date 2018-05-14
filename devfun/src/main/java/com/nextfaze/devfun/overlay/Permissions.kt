package com.nextfaze.devfun.overlay

import android.annotation.TargetApi
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.core.ActivityProvider
import com.nextfaze.devfun.core.BuildConfig
import com.nextfaze.devfun.core.R
import com.nextfaze.devfun.inject.Constructable
import com.nextfaze.devfun.internal.android.*
import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.internal.pref.*
import com.nextfaze.devfun.internal.string.*
import com.nextfaze.devfun.obtain
import com.nextfaze.devfun.show

internal enum class OverlayPermissions { NEVER_REQUESTED, DENIED, NEVER_ASK_AGAIN }

@Constructable(singleton = true)
@DeveloperCategory("DevFun", group = "Overlays")
class OverlayPermissionsManager(
    context: Context,
    private val activityProvider: ActivityProvider
) {
    private val log = logger()

    private val application = context.applicationContext as Application
    private val windowManager = application.windowManager

    private val activity get() = activityProvider()
    private val fragmentActivity get() = activity as? FragmentActivity

    private val preferences = KSharedPreferences.named(application, "OverlayPermissions")
    private var permissions by preferences["permissionsState", OverlayPermissions.NEVER_REQUESTED]

    val canDrawOverlays: Boolean
        get() = when {
            Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(application) -> true
            Build.VERSION.SDK_INT == Build.VERSION_CODES.O -> forceCheckPermissionsEnabled()
            else -> false
        }

    fun requestPermission(reason: CharSequence? = null) {
        if (permissions != OverlayPermissions.NEVER_ASK_AGAIN && !isInstrumentationTest) {
            showPermissionsDialog(reason)
        }
    }

    @DeveloperFunction(requiresApi = Build.VERSION_CODES.M)
    private fun managePermissions() = showPermissionsDialog()

    private fun showPermissionsDialog(reason: CharSequence? = null) =
        fragmentActivity?.let {
            OverlayPermissionsDialogFragment.show(it, permissions, reason).apply {
                deniedCallback = { neverAskAgain ->
                    permissions = if (neverAskAgain) OverlayPermissions.NEVER_ASK_AGAIN else OverlayPermissions.DENIED
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
}

private const val PERMISSIONS = "PERMISSIONS"
private const val REASON = "REASON"

internal class OverlayPermissionsDialogFragment : DialogFragment() {
    companion object {
        fun show(activity: FragmentActivity, permissions: OverlayPermissions, reason: CharSequence? = null) = activity
            .obtain {
                OverlayPermissionsDialogFragment().apply {
                    arguments = Bundle().apply {
                        putString(PERMISSIONS, permissions.name)
                        putCharSequence(REASON, reason)
                    }
                }
            }
            .apply {
                takeIf { !it.isAdded }?.show(activity.supportFragmentManager)
            }
    }

    var deniedCallback: ((neverAskAgain: Boolean) -> Unit)? = null

    private val permissions by lazy { OverlayPermissions.valueOf(arguments!!.getString(PERMISSIONS)) }
    private val reason by lazy { arguments!!.getCharSequence(REASON) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val activity = activity!!
        val msg = SpannableStringBuilder().also {
            it += activity.getString(R.string.df_devfun_overlay_reason, BuildConfig.VERSION_NAME)
            if (reason != null) {
                it += "\n\n"
                it += reason
            }
        }

        val (neverAskAgainCheckBox, dialogView) = LayoutInflater.from(context)
            .inflate(R.layout.df_devfun_permissions_checkbox, view as ViewGroup?)
            .run {
                findViewById<TextView>(R.id.messageTextView).text = msg
                findViewById<CheckBox>(R.id.neverAskAgainCheckBox).apply {
                    isChecked = permissions != OverlayPermissions.NEVER_REQUESTED
                } to this
            }

        return AlertDialog.Builder(activity)
            .setTitle(R.string.df_devfun_overlay_request)
            .setView(dialogView)
            .setPositiveButton(R.string.df_devfun_allow, { dialog, _ ->
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, "package:${activity.packageName}".toUri())
                activity.startActivityForResult(intent, 1234)
                dialog.dismiss()
            })
            .setNegativeButton(R.string.df_devfun_deny, { dialog, _ ->
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
