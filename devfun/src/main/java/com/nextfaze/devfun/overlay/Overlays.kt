package com.nextfaze.devfun.overlay

import android.app.Activity
import android.app.Application
import android.content.Context
import android.graphics.Rect
import android.support.annotation.LayoutRes
import android.text.SpannableStringBuilder
import android.view.View
import android.view.WindowManager
import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.core.ActivityProvider
import com.nextfaze.devfun.inject.Constructable
import com.nextfaze.devfun.internal.android.*
import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.internal.prop.*

@Constructable(singleton = true)
@DeveloperCategory("DevFun", group = "Overlays")
class OverlayManager(
    context: Context,
    private val activityProvider: ActivityProvider,
    private val permissions: OverlayPermissionsManager
) {
    private val log = logger()

    private val application = context.applicationContext as Application
    private val activity get() = activityProvider()

    private val overlaysLock = Any()
    private val overlays = mutableMapOf<String, OverlayWindow>()

    private val displayBounds = Rect()

    val canDrawOverlays get() = permissions.canDrawOverlays

    init {
        val displayBoundsTmp = Rect()

        /**
         * Using the activity like this ensures that rotation, status bar, and other system views are taken into account.
         * Thus for multi-window use it should be properly bounded to the relevant app window area.
         * i.e. This is the space available for the *app* now the *device*
         */
        fun updateDisplayBounds(activity: Activity): Rect? {
            (activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay?.getRectSize(displayBoundsTmp)
            return when {
                displayBounds.isEmpty || displayBounds != displayBoundsTmp -> { // first time || changed
                    displayBounds.set(displayBoundsTmp)
                    displayBounds
                }
                else -> null // unchanged
            }
        }

        application.registerActivityCallbacks(
            onResumed = {
                if (overlays.isNotEmpty()) {
                    if (!canDrawOverlays) {
                        val reason = synchronized(overlaysLock) {
                            overlays.values.joinTo(SpannableStringBuilder(), separator = "\n\n") { it.reason() }
                        }
                        permissions.requestPermission(reason)
                    } else {
                        updateDisplayBounds(it)?.also { newBounds ->
                            synchronized(overlaysLock) {
                                overlays.forEach { it.value.updateOverlayBounds(newBounds) }
                            }
                        }
                        updateVisibilities()
                    }
                }
            },
            onStopped = { updateVisibilities() }
        )
    }

    private val fullScreenLock = Any()
    private var fullScreenOwner by weak<Any> { null }
    private var fullScreenInUse = false

    fun takeFullScreenLock(who: Any): Boolean {
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
                updateVisibilities()
            }
            return canObtain
        }
    }

    @DeveloperFunction("Release Full Screen Lock\n(needed if DevFun bugged out - please report!)")
    fun releaseFullScreenLock(who: Any) {
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
                updateVisibilities()
            }
        }
    }

    fun createOverlay(
        @LayoutRes layoutId: Int,
        prefsName: String,
        reason: OverlayReason,
        onClick: ClickListener? = null,
        onLongClick: ClickListener? = null,
        visibilityPredicate: VisibilityPredicate? = null,
        initialDock: Dock = Dock.TOP_LEFT,
        initialDelta: Float = 0f,
        snapToEdge: Boolean = true,
        initialLeft: Float = 0f,
        initialTop: Float = 0f
    ): OverlayWindow {
        synchronized(overlaysLock) {
            if (overlays.containsKey(prefsName)) {
                throw RuntimeException("Overlay with prefs name $prefsName has already been created!")
            }
        }

        return OverlayWindow(
            application,
            this,
            layoutId,
            prefsName,
            reason,
            onClick,
            onLongClick,
            visibilityPredicate,
            initialDock,
            initialDelta,
            snapToEdge,
            initialLeft,
            initialTop
        ).apply {
            synchronized(overlaysLock) { overlays[prefsName] = this }
            if (permissions.canDrawOverlays) {
                addToWindow()
                updateOverlayBounds(displayBounds)
                updateVisibility()
            }
        }
    }

    fun destroyOverlay(overlayWindow: OverlayWindow) {
        overlays.remove(overlayWindow.prefsName)?.also { it.removeFromWindow() }
    }

    internal fun updateVisibilities() {
        if (!permissions.canDrawOverlays) return

        val shouldBeVisible = !fullScreenInUse && application.isRunningInForeground
        synchronized(overlaysLock) {
            overlays.forEach {
                it.value.addToWindow()
                it.value.updateVisibility(shouldBeVisible)
            }
        }
    }

    private fun OverlayWindow.updateVisibility(shouldBeVisible: Boolean = !fullScreenInUse && application.isRunningInForeground) {
        val activity = activity
        val isVisible = activity != null && shouldBeVisible && enabled && visibilityPredicate?.invoke(activity) != false
        view.visibility = if (isVisible) View.VISIBLE else View.GONE
    }
}

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
