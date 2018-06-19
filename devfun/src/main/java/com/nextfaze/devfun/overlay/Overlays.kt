package com.nextfaze.devfun.overlay

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.support.annotation.LayoutRes
import android.text.SpannableStringBuilder
import android.view.Display
import android.view.WindowManager
import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.core.ActivityProvider
import com.nextfaze.devfun.core.ForegroundTracker
import com.nextfaze.devfun.core.R
import com.nextfaze.devfun.core.devFun
import com.nextfaze.devfun.inject.Constructable
import com.nextfaze.devfun.internal.android.*
import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.internal.string.*
import com.nextfaze.devfun.invoke.*
import java.util.Collections
import java.util.WeakHashMap
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.LazyThreadSafetyMode.NONE

/**
 * Function signature of a listener for the current full-screen status. This callback may be called multiple times with the same value.
 *
 * @see OverlayManager.addFullScreenUsageStateListener
 * @see OverlayManager.plusAssign
 * @see OverlayManager.isFullScreenInUse
 */
typealias FullScreenUsageStateListener = (Boolean) -> Unit

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
     * @param visibilityPredicate Predicate that determines if/when the overlay should be visible (e.g. DevMenu uses `context is FragmentActivity`).
     * @param visibilityScope The [VisibilityScope] defaults to `FOREGROUND_ONLY`. i.e. When the app is visible and resumed to the user.
     * @param initialDock The initial edge of the screen to dock to (see [snapToEdge]).
     * @param initialDelta The percentage down/across the screen (e.g. An [initialDock] of `RIGHT` and delta of 0.7 (DevMenu cog) puts it at 70% down the right-hand side of the screen).
     * @param snapToEdge `true` to enable edge docking, `false` to allow it to sit/drag anywhere on the screen.
     * @param initialLeft Used when [snapToEdge] is `true` - the initial left position (percentage of screen).
     * @param initialTop Used when [snapToEdge] is `true` - the initial top position (percentage of screen).
     */
    fun createOverlay(
        @LayoutRes layoutId: Int,
        name: String,
        prefsName: String,
        reason: OverlayReason,
        onClick: ClickListener? = null,
        onLongClick: ClickListener? = null,
        onVisibilityChange: VisibilityChangeListener? = null,
        visibilityPredicate: VisibilityPredicate? = null,
        visibilityScope: VisibilityScope = VisibilityScope.FOREGROUND_ONLY,
        initialDock: Dock = Dock.TOP_LEFT,
        initialDelta: Float = 0f,
        snapToEdge: Boolean = true,
        initialLeft: Float = 0f,
        initialTop: Float = 0f
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

    /**
     * Flag indicating if the full-screen is in use for the current display.
     *
     * Android creates a new [Display] for each activity. Thus this flag effectively represents the current activity.
     *
     * @see notifyUsingFullScreen
     * @see addFullScreenUsageStateListener
     */
    val isFullScreenInUse: Boolean

    /**
     * Notify DevFun that you want to present a full-screen/dialog to signal that you want overlays to be hidden.
     *
     * Calling this effectively associates [who] with the current display - Android creates a new [Display] for each activity.
     * Multiple calls to this does not require multiple finish calls.
     *
     * Be sure to notify when you're done with [notifyFinishUsingFullScreen].
     *
     * @param who A reference to the user of the notification (a weak reference will be held).
     * @return `true` if the notification was taken successfully (requires there be an Activity present), or `false` if no Activity present.
     *
     * @see removeFullScreenUsageStateListener
     * @see isFullScreenInUse
     * @see addFullScreenUsageStateListener
     * @see removeFullScreenUsageStateListener
     */
    fun notifyUsingFullScreen(who: Any): Boolean

    /**
     * Notify that you are done with your full-screen/dialog usage to signal the overlays they can be visible (if nothing else is using it).
     *
     * @param who The same reference as passed in with [notifyUsingFullScreen].
     *
     * @see notifyUsingFullScreen
     * @see isFullScreenInUse
     * @see addFullScreenUsageStateListener
     * @see removeFullScreenUsageStateListener
     */
    fun notifyFinishUsingFullScreen(who: Any)

    /**
     * Add a listener for the current full-screen status. This callback may be called multiple times with the same value.
     *
     * Will be called upon each Activity onResume and each time something notifies their usage.
     *
     * @return The listener you passed in for assigning/chaining purposes.
     *
     * @see removeFullScreenUsageStateListener
     * @see notifyUsingFullScreen
     * @see isFullScreenInUse
     */
    fun addFullScreenUsageStateListener(listener: FullScreenUsageStateListener): FullScreenUsageStateListener

    /**
     * Remove a listener for the current full-screen status.
     *
     * @return The listener you passed in for assigning/chaining purposes.
     *
     * @see addFullScreenUsageStateListener
     * @see notifyUsingFullScreen
     * @see isFullScreenInUse
     */
    fun removeFullScreenUsageStateListener(listener: FullScreenUsageStateListener): FullScreenUsageStateListener

    /**
     * Add a listener for the current full-screen status. This callback may be called multiple times with the same value.
     *
     * Will be called upon each Activity onResume and each time something notifies their usage.
     *
     * @see addFullScreenUsageStateListener
     * @see minusAssign
     */
    operator fun plusAssign(listener: FullScreenUsageStateListener) {
        addFullScreenUsageStateListener(listener)
    }

    /**
     * Add a listener for the current full-screen status.
     *
     * @see removeFullScreenUsageStateListener
     * @see plusAssign
     */
    operator fun minusAssign(listener: FullScreenUsageStateListener) {
        removeFullScreenUsageStateListener(listener)
    }
}

@Constructable(singleton = true)
@DeveloperCategory("DevFun", group = "Overlays")
internal class OverlayManagerImpl(
    context: Context,
    private val foregroundTracker: ForegroundTracker,
    private val displayBoundsTracker: DisplayBoundsTracker,
    private val activityProvider: ActivityProvider,
    private val permissions: OverlayPermissions,
    private val invoker: Invoker
) : OverlayManager {
    private val application = context.applicationContext as Application
    private val handler by lazy { Handler(Looper.getMainLooper()) }

    private val overlaysLock = Any()
    private val overlays = mutableMapOf<String, OverlayWindow>()

    override val isFullScreenInUse get() = fullScreenUsage.isInUse
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
                notifyCurrentFullScreenUsage()
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
        onVisibilityChange: VisibilityChangeListener?,
        visibilityPredicate: VisibilityPredicate?,
        visibilityScope: VisibilityScope,
        initialDock: Dock,
        initialDelta: Float,
        snapToEdge: Boolean,
        initialLeft: Float,
        initialTop: Float
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
            layoutId,
            name,
            prefsName,
            reason,
            onClick,
            onLongClick,
            onVisibilityChange,
            visibilityPredicate,
            visibilityScope,
            initialDock,
            initialDelta,
            snapToEdge,
            initialLeft,
            initialTop
        ).apply {
            if (onLongClick == null) {
                this.onLongClick = { configureOverlay(this) }
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

    private val fullScreenUsage = devFun.get<FullScreenUsageTracker>()
    private val fullScreenUseStateListeners = CopyOnWriteArrayList<FullScreenUsageStateListener>()

    override fun notifyUsingFullScreen(who: Any): Boolean {
        if (fullScreenUsage.notifyUsingFullScreen(who)) {
            notifyCurrentFullScreenUsage()
            return true
        }
        return false
    }

    override fun notifyFinishUsingFullScreen(who: Any) {
        if (fullScreenUsage.notifyFinishUsingFullScreen(who)) {
            notifyCurrentFullScreenUsage()
        }
    }

    override fun addFullScreenUsageStateListener(listener: FullScreenUsageStateListener): FullScreenUsageStateListener {
        fullScreenUseStateListeners += listener
        return listener
    }

    override fun removeFullScreenUsageStateListener(listener: FullScreenUsageStateListener): FullScreenUsageStateListener {
        fullScreenUseStateListeners -= listener
        return listener
    }

    private fun notifyCurrentFullScreenUsage() {
        handler.post {
            val inUse by lazy(NONE) { fullScreenUsage.isInUse }
            fullScreenUseStateListeners.forEach { it(inUse) }
        }
    }

    @DeveloperFunction("Clear full-screen usages\n(needed if DevFun bugged out - please report!)")
    private fun clearUsages() {
        fullScreenUsage.clearUsages()
        notifyCurrentFullScreenUsage()
    }
}

@Constructable(singleton = true)
private class FullScreenUsageTracker(private val activityProvider: ActivityProvider) {
    private val log = logger()

    private val usagesLock = Any()
    private val usages = WeakHashMap<Display, MutableSet<Any>>()

    val isInUse: Boolean
        get() {
            val display = activityProvider()?.defaultDisplay ?: return false
            synchronized(usagesLock) {
                return usages[display]?.takeIf { it.isNotEmpty() } != null
            }
        }

    fun notifyUsingFullScreen(who: Any): Boolean {
        val display = activityProvider()?.defaultDisplay ?: return false
        log.t {
            """notifying usage {
                |  who: $who
                |  display: $display
                |  others: ${synchronized(usagesLock) { usages[display] }}
                }
                """.trimMargin()
        }
        synchronized(usagesLock) {
            usages.getOrPut(display) { Collections.newSetFromMap(WeakHashMap<Any, Boolean>()) }.apply { this += who }
        }
        return true
    }

    fun notifyFinishUsingFullScreen(who: Any): Boolean {
        val display = activityProvider()?.defaultDisplay ?: return true
        val nowUnused = synchronized(usagesLock) {
            usages[display]?.let {
                it.remove(who)
                it.isEmpty().also { if (it) usages.remove(display) }
            } ?: true
        }
        log.t {
            """notifying stop usage {
                |  who: $who,
                |  display: $display,
                |  nowUnused: $nowUnused,
                |  others: ${synchronized(usagesLock) { usages[display] }}
                }
                """.trimMargin()
        }
        return nowUnused
    }

    fun clearUsages() = synchronized(usagesLock) { usages.clear() }

    private val Context.defaultDisplay: Display get() = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
}
