package com.nextfaze.devfun.overlay

import android.annotation.TargetApi
import android.app.Application
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.core.ActivityProvider
import com.nextfaze.devfun.core.BuildConfig
import com.nextfaze.devfun.core.R
import com.nextfaze.devfun.inject.Constructable
import com.nextfaze.devfun.internal.android.*
import com.nextfaze.devfun.internal.isInstrumentationTest
import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.internal.pref.*
import com.nextfaze.devfun.internal.string.*
import java.util.concurrent.CopyOnWriteArrayList

typealias OverlayPermissionListener = (havePermission: Boolean) -> Unit

/**
 * Handles overlay permissions.
 *
 * @see OverlayManager
 * @see OverlayWindow
 */
interface OverlayPermissions {
    /**
     * Flag indicating if the user has granted overlay permissions.
     *
     * @see requestPermission
     */
    val canDrawOverlays: Boolean

    /**
     * Flag indicating if we should request overlay permissions - i.e. we don't have them and the user has not denied them.
     *
     * Calling [requestPermission] will only show the request if the user has not denied them already. This flag is here to allow you to
     * know if you should generate/render the reasoning string etc, and as to whether or not a resumed FragmentActivity is present.
     * Calling [requestPermission] when this is false will do nothing.
     *
     * @see canDrawOverlays
     * @see requestPermission
     */
    val shouldRequestPermission: Boolean

    /**
     * Signal a request to the user that we want permission for overlays.
     *
     * Calling this when permissions are already granted will do nothing.
     *
     * @param reason Optional reason to show the user.
     * @see canDrawOverlays
     */
    fun requestPermission(reason: CharSequence? = null)

    /**
     * Add a listener for when overlay permissions have changed.
     *
     * @see plusAssign
     */
    fun addOverlayPermissionListener(listener: OverlayPermissionListener): OverlayPermissionListener

    /**
     * Add a listener for when overlay permissions have changed.
     *
     * @see addOverlayPermissionListener
     */
    operator fun plusAssign(listener: OverlayPermissionListener) {
        addOverlayPermissionListener(listener)
    }

    /**
     * Remove a listener for when overlay permissions have changed.
     *
     * @see minusAssign
     */
    fun removeOverlayPermissionListener(listener: OverlayPermissionListener): OverlayPermissionListener

    /**
     * Remove a listener for when overlay permissions have changed.
     *
     * @see removeOverlayPermissionListener
     */
    operator fun minusAssign(listener: OverlayPermissionListener) {
        removeOverlayPermissionListener(listener)
    }
}

internal enum class PermissionState { NEVER_REQUESTED, DENIED, NEVER_ASK_AGAIN }

@Constructable(singleton = true)
@DeveloperCategory("DevFun", group = "Overlays")
internal class OverlayPermissionsImpl(
    context: Context,
    private val activityProvider: ActivityProvider
) : OverlayPermissions {
    private val log = logger()

    private val application = context.applicationContext as Application
    private val windowManager = application.windowManager
    private val handler by lazy { Handler(Looper.getMainLooper()) }

    private val activity get() = activityProvider()
    private val fragmentActivity get() = activity as? FragmentActivity

    private val preferences = KSharedPreferences.named(application, "OverlayPermissions")
    private var permission by preferences["permissionState", PermissionState.NEVER_REQUESTED]

    private val listeners = CopyOnWriteArrayList<OverlayPermissionListener>()

    private var havePermission: Boolean? = null

    init {
        context.registerActivityCallbacks(
            onResumed = {
                val canDrawOverlays = canDrawOverlays
                if (canDrawOverlays != havePermission) {
                    havePermission = canDrawOverlays
                    listeners.forEach { it(canDrawOverlays) }
                }
            }
        )
    }

    override val canDrawOverlays: Boolean
        get() = when {
            Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(application) -> true
            Build.VERSION.SDK_INT == Build.VERSION_CODES.O -> forceCheckPermissionsEnabled()
            else -> false
        }

    override val shouldRequestPermission: Boolean get() = permission != PermissionState.NEVER_ASK_AGAIN && fragmentActivity != null && !canDrawOverlays

    override fun requestPermission(reason: CharSequence?) {
        if (permission != PermissionState.NEVER_ASK_AGAIN && !isInstrumentationTest && !canDrawOverlays) {
            showPermissionsDialog(reason)
        }
    }

    override fun addOverlayPermissionListener(listener: OverlayPermissionListener): OverlayPermissionListener {
        listeners += listener
        return listener
    }

    override fun removeOverlayPermissionListener(listener: OverlayPermissionListener): OverlayPermissionListener {
        listeners -= listener
        return listener
    }

    @DeveloperFunction(requiresApi = Build.VERSION_CODES.M)
    private fun managePermissions() = showPermissionsDialog()

    private fun showPermissionsDialog(reason: CharSequence? = null) {
        handler.post {
            fragmentActivity?.let {
                OverlayPermissionsDialogFragment.show(it, permission, reason).apply {
                    deniedCallback = { neverAskAgain ->
                        permission = if (neverAskAgain) PermissionState.NEVER_ASK_AGAIN else PermissionState.DENIED
                    }
                }
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
}

private const val PERMISSIONS = "PERMISSIONS"
private const val REASON = "REASON"

internal class OverlayPermissionsDialogFragment : DialogFragment() {
    companion object {
        fun show(activity: FragmentActivity, permissions: PermissionState, reason: CharSequence? = null) = activity
            .obtain {
                OverlayPermissionsDialogFragment().apply {
                    arguments = Bundle().apply {
                        putString(PERMISSIONS, permissions.name)
                        putCharSequence(REASON, reason)
                    }
                }
            }
            .apply { takeIf { !it.isAdded }?.showNow(activity.supportFragmentManager) }
    }

    var deniedCallback: ((neverAskAgain: Boolean) -> Unit)? = null

    private val permissions by lazy { PermissionState.valueOf(arguments!!.getString(PERMISSIONS)!!) }
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
                    isChecked = permissions != PermissionState.NEVER_REQUESTED
                } to this
            }

        return AlertDialog.Builder(activity)
            .setTitle(R.string.df_devfun_overlay_request)
            .setView(dialogView)
            .setPositiveButton(R.string.df_devfun_allow) { dialog, _ ->
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, "package:${activity.packageName}".toUri())
                activity.startActivityForResult(intent, 1234)
                dialog.dismiss()
            }
            .setNegativeButton(R.string.df_devfun_deny) { dialog, _ ->
                deniedCallback?.invoke(neverAskAgainCheckBox.isChecked)
                dialog.dismiss()
            }
            .show()
    }

    override fun onDestroyView() {
        // Fix http:///issuetracker.google.com/17423
        dialog?.takeIf { retainInstance }?.setDismissMessage(null)
        super.onDestroyView()
    }
}

private fun String.toUri(): Uri = Uri.parse(this)
