package com.nextfaze.devfun.overlay

import android.app.Application
import android.content.Context
import android.text.SpannableStringBuilder
import androidx.annotation.LayoutRes
import com.nextfaze.devfun.category.DeveloperCategory
import com.nextfaze.devfun.core.ActivityProvider
import com.nextfaze.devfun.core.ForegroundTracker
import com.nextfaze.devfun.core.R
import com.nextfaze.devfun.inject.Constructable
import com.nextfaze.devfun.internal.WindowCallbacks
import com.nextfaze.devfun.internal.android.*
import com.nextfaze.devfun.invoke.Invoker
import com.nextfaze.devfun.invoke.OnClick
import com.nextfaze.devfun.invoke.UiField
import com.nextfaze.devfun.invoke.uiButton
import com.nextfaze.devfun.invoke.uiFunction

/**
 * Handles creation, destruction, and visibility of overlays.
 *
 * @see OverlayWindow
 * @see OverlayPermissions
 */
interface OverlayManager {
    /**
     * Flag indicating if the user has granted Overlay Permissions.
     *
     * @see OverlayPermissions.canDrawOverlays
     * @see OverlayPermissions.requestPermission
     */
    val canDrawOverlays: Boolean

    /**
     * Creates an overlay window.
     *
     * Add the overlay to the screen using [OverlayWindow.addToWindow].
     *
     * When you are done with the overlay by sure to destroy it with [destroyOverlay].
     *
     * @see OverlayWindow
     * @param layoutId A layout resource ID to inflate.
     * @param name A user-friendly name.
     * @param prefsName The name of the preferences file the window will use - __must be unique__!
     * @param reason A function that generates the reason description. Will only be called when we need to request overlays permission.
     * @param onClick Callback when the overlay is clicked.
     * @param onLongClick Callback when the overlay is long-clicked.
     * @param onVisibilityChange Callback when the overlay's visibility is changed.
     * @param onAttachChange Callback when overlay is added/removed to the window.
     * @param visibilityPredicate Predicate that determines if/when the overlay should be visible (e.g. DevMenu uses `context is FragmentActivity`).
     * @param visibilityScope The [VisibilityScope] defaults to `FOREGROUND_ONLY`. i.e. When the app is visible and resumed to the user.
     * @param dock The edge of the screen to dock to (see [snapToEdge]).
     * @param delta The percentage down/across the screen (e.g. An [dock] of `RIGHT` and delta of 0.7 (DevMenu cog) puts it at 70% down the right-hand side of the screen).
     * @param snapToEdge `true` to enable edge docking, `false` to allow it to sit/drag anywhere on the screen.
     * @param left Used when [snapToEdge] is `true` - the left position (percentage of screen).
     * @param top Used when [snapToEdge] is `true` - the top position (percentage of screen).
     */
    fun createOverlay(
        @LayoutRes layoutId: Int,
        name: String,
        prefsName: String,
        reason: OverlayReason,
        onClick: ClickListener? = null,
        onLongClick: ClickListener? = null,
        onVisibilityChange: VisibilityListener? = null,
        onAttachChange: AttachListener? = null,
        visibilityPredicate: VisibilityPredicate? = null,
        visibilityScope: VisibilityScope = VisibilityScope.FOREGROUND_ONLY,
        dock: Dock = Dock.TOP_LEFT,
        delta: Float = 0f,
        snapToEdge: Boolean = true,
        left: Float = 0f,
        top: Float = 0f,
        enabled: Boolean = true
    ): OverlayWindow

    /**
     * Destroy an overlay instance (cleans up any listeners/callbacks/resources/etc).
     *
     * @see createOverlay
     */
    fun destroyOverlay(overlayWindow: OverlayWindow)

    /**
     * Show a configuration dialog for an overlay.
     *
     * @param overlayWindow The overlay to configure.
     * @param additionalOptions Additional options to show in the configuration dialog.
     * @param onResetClick Callback when reset clicked. By default calls the overlay's [OverlayWindow.resetPositionAndState].
     */
    fun configureOverlay(
        overlayWindow: OverlayWindow,
        additionalOptions: List<UiField<*>> = emptyList(),
        onResetClick: OnClick = { overlayWindow.resetPositionAndState() }
    )
}

@Constructable(singleton = true)
@DeveloperCategory("DevFun", group = "Overlays")
internal class OverlayManagerImpl(
    context: Context,
    private val foregroundTracker: ForegroundTracker,
    private val displayBoundsTracker: DisplayBoundsTracker,
    private val windowCallbacks: WindowCallbacks,
    private val activityProvider: ActivityProvider,
    private val permissions: OverlayPermissions,
    private val invoker: Invoker
) : OverlayManager {
    private val application = context.applicationContext as Application

    private val overlaysLock = Any()
    private val overlays = mutableMapOf<String, OverlayWindow>()

    override val canDrawOverlays get() = permissions.canDrawOverlays

    init {
        fun requestPermission() {
            if (permissions.shouldRequestPermission && overlays.isNotEmpty()) {
                val reason = synchronized(overlaysLock) {
                    overlays.values.joinTo(SpannableStringBuilder(), separator = "\n\n") { it.reason() }
                }
                permissions.requestPermission(reason)
            }
        }

        permissions += { requestPermission() }
        context.registerActivityCallbacks(
            onResumed = {
                requestPermission()
            }
        )
    }

    override fun createOverlay(
        @LayoutRes layoutId: Int,
        name: String,
        prefsName: String,
        reason: OverlayReason,
        onClick: ClickListener?,
        onLongClick: ClickListener?,
        onVisibilityChange: VisibilityListener?,
        onAttachChange: AttachListener?,
        visibilityPredicate: VisibilityPredicate?,
        visibilityScope: VisibilityScope,
        dock: Dock,
        delta: Float,
        snapToEdge: Boolean,
        left: Float,
        top: Float,
        enabled: Boolean
    ): OverlayWindow {
        synchronized(overlaysLock) {
            if (overlays.containsKey(prefsName)) {
                throw RuntimeException("Overlay with prefs name $prefsName has already been created!")
            }
        }

        return OverlayWindowImpl(
            application,
            this,
            permissions,
            activityProvider,
            foregroundTracker,
            displayBoundsTracker,
            windowCallbacks,
            layoutId,
            name,
            prefsName,
            reason,
            onClick,
            onLongClick,
            onVisibilityChange,
            onAttachChange,
            visibilityPredicate,
            visibilityScope,
            dock,
            delta,
            snapToEdge,
            left,
            top,
            enabled
        ).apply {
            if (onLongClick == null) {
                this.onLongClickListener = { configureOverlay(this) }
            }
            synchronized(overlaysLock) { overlays[prefsName] = this }
        }
    }

    override fun destroyOverlay(overlayWindow: OverlayWindow) {
        overlays.remove(overlayWindow.prefsName)?.also {
            it.removeFromWindow()
            it.dispose()
        }
    }

    override fun configureOverlay(overlayWindow: OverlayWindow, additionalOptions: List<UiField<*>>, onResetClick: OnClick) {
        val params = overlayWindow.configurationOptions + additionalOptions
        invoker.invoke(
            uiFunction(
                title = application.getString(R.string.df_devfun_overlay_options),
                subtitle = overlayWindow.name,
                parameters = params,
                neutralButton = uiButton(textId = R.string.df_devfun_reset, onClick = onResetClick),
                invoke = {
                    params.asSequence().zip(it.asSequence()).forEach { (param, arg) ->
                        @Suppress("UNCHECKED_CAST")
                        (param as UiField<Any>).setValue(arg!!)
                    }
                }
            )
        )
    }
}
