package com.nextfaze.devfun.overlay

import android.app.Application
import android.content.Context
import android.support.annotation.LayoutRes
import android.text.SpannableStringBuilder
import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.core.ActivityProvider
import com.nextfaze.devfun.core.ForegroundTracker
import com.nextfaze.devfun.inject.Constructable
import com.nextfaze.devfun.internal.android.*
import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.internal.prop.*
import java.util.concurrent.CopyOnWriteArrayList

typealias FullScreenLockChangeListener = (Boolean) -> Unit

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
     * Flag indicating if the full-screen lock is in use.
     *
     * @see takeFullScreenLock
     * @see addFullScreenLockChangeListener
     */
    val isFullScreenLockInUse: Boolean

    /**
     * Request to take full-screen use.
     *
     * Use when you want to tell the overlays that they should be hidden (such as when the DevMenu is shown).
     *
     * Be sure to release it when you're done with [releaseFullScreenLock].
     *
     * @param who A reference to the user of the lock (a weak reference will be held).
     * @return `true` if the lock was taken successfully, or `false` if already in use.
     *
     * @see addFullScreenLockChangeListener
     * @see removeFullScreenLockChangeListener
     */
    fun takeFullScreenLock(who: Any): Boolean

    /**
     * Release your full-screen lock.
     *
     * The lock will also be released if the previous "taker" has been garbage collected.
     *
     * @param who The same reference as passed in with [takeFullScreenLock].
     */
    fun releaseFullScreenLock(who: Any)

    /**
     * Add a listener for when the full-screen is being used by something else (e.g. DevMenu).
     *
     * @see plusAssign
     */
    fun addFullScreenLockChangeListener(listener: FullScreenLockChangeListener): FullScreenLockChangeListener

    /**
     * Add a listener for when the full-screen is being used by something else (e.g. DevMenu).
     *
     * @see addFullScreenLockChangeListener
     */
    operator fun plusAssign(listener: FullScreenLockChangeListener) {
        addFullScreenLockChangeListener(listener)
    }

    /**
     * Remove a listener for when the full-screen is being used by something else (e.g. DevMenu).
     *
     * @see minusAssign
     */
    fun removeFullScreenLockChangeListener(listener: FullScreenLockChangeListener): FullScreenLockChangeListener

    /**
     * Remove a listener for when the full-screen is being used by something else (e.g. DevMenu).
     *
     * @see removeFullScreenLockChangeListener
     */
    operator fun minusAssign(listener: FullScreenLockChangeListener) {
        removeFullScreenLockChangeListener(listener)
    }
}

@Constructable(singleton = true)
@DeveloperCategory("DevFun", group = "Overlays")
internal class OverlayManagerImpl(
    context: Context,
    private val foregroundTracker: ForegroundTracker,
    private val displayBoundsTracker: DisplayBoundsTracker,
    private val activityProvider: ActivityProvider,
    private val permissions: OverlayPermissions
) : OverlayManager {
    private val log = logger()

    private val application = context.applicationContext as Application

    private val overlaysLock = Any()
    private val overlays = mutableMapOf<String, OverlayWindow>()

    override val isFullScreenLockInUse get() = fullScreenInUse
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
            onResumed = { requestPermission() }
        )
    }

    private val fullScreenLock = Any()
    private var fullScreenOwner by weak<Any> { null }
    private var fullScreenInUse = false
        private set(value) {
            if (field != value) {
                field = value
                fullScreenLockChangeListeners.forEach { it(value) }
            }
        }

    override fun takeFullScreenLock(who: Any): Boolean {
        synchronized(fullScreenLock) {
            val isCurrentOwner = who === fullScreenOwner
            val canObtain = fullScreenOwner == null || isCurrentOwner
            log.t {
                """$who wants to take full-screen lock {
                currentOwner: $fullScreenOwner,
                isCurrentOwner: $isCurrentOwner,
                fullScreenInUse: $fullScreenInUse,
                willObtain: $canObtain
            }
            """.trimMargin()
            }
            if (canObtain && !isCurrentOwner) {
                fullScreenOwner = who
                fullScreenInUse = true
            }
            return canObtain
        }
    }

    @DeveloperFunction("Release Full Screen Lock\n(needed if DevFun bugged out - please report!)")
    override fun releaseFullScreenLock(who: Any) {
        synchronized(fullScreenLock) {
            val isCurrentOwner = who === fullScreenOwner
            val canRelease = fullScreenOwner == null || isCurrentOwner
            log.t {
                """$who wants to release full-screen lock {
                currentOwner: $fullScreenOwner,
                isCurrentOwner: $isCurrentOwner,
                fullScreenInUse: $fullScreenInUse,
                willRelease: $canRelease
            }
            """.trimMargin()
            }
            if (canRelease) {
                fullScreenOwner = null
                fullScreenInUse = false
            }
        }
    }

    override fun createOverlay(
        @LayoutRes layoutId: Int,
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
        ).apply { synchronized(overlaysLock) { overlays[prefsName] = this } }
    }

    override fun destroyOverlay(overlayWindow: OverlayWindow) {
        overlays.remove(overlayWindow.prefsName)?.also {
            it.removeFromWindow()
            it.dispose()
        }
    }

    private val fullScreenLockChangeListeners = CopyOnWriteArrayList<FullScreenLockChangeListener>()

    override fun addFullScreenLockChangeListener(listener: FullScreenLockChangeListener): FullScreenLockChangeListener {
        fullScreenLockChangeListeners += listener
        return listener
    }

    override fun removeFullScreenLockChangeListener(listener: FullScreenLockChangeListener): FullScreenLockChangeListener {
        fullScreenLockChangeListeners -= listener
        return listener
    }
}
