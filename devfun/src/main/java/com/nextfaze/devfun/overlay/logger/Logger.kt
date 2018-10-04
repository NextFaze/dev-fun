package com.nextfaze.devfun.overlay.logger

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.nextfaze.devfun.core.R
import com.nextfaze.devfun.core.devFun
import com.nextfaze.devfun.error.ErrorHandler
import com.nextfaze.devfun.function.DeveloperProperty
import com.nextfaze.devfun.internal.pref.*
import com.nextfaze.devfun.invoke.UiField
import com.nextfaze.devfun.invoke.uiField
import com.nextfaze.devfun.overlay.OverlayManager
import com.nextfaze.devfun.overlay.OverlayPermissions
import com.nextfaze.devfun.overlay.OverlayWindow
import com.nextfaze.devfun.overlay.VisibilityScope
import com.nextfaze.devfun.overlay.VisibilityScope.ALWAYS
import com.nextfaze.devfun.overlay.VisibilityScope.FOREGROUND_ONLY
import com.nextfaze.devfun.reference.DeveloperLogger
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0

typealias UpdateCallback = () -> CharSequence
typealias OnClick = () -> Unit

/**
 * An overlay logger is a floating semi-transparent `TextView` that will display the `.toString()` of a function, property, or class.
 *
 * They are contextually aware if created via @[DeveloperLogger] (managed by [OverlayLogging]) and will appear/disappear when in context (if
 * the annotation is within an Activity or Fragment).
 *
 * - By default it will update every second (see [refreshRate]).
 * - If the annotation is on a property also annotated with @[DeveloperProperty] then a single click will allow you to edit the value (opens
 * the standard DevFun invoker dialog).
 * - The overlay's floating behaviour can be configured with a long click (snapping, refresh rate, etc.)
 * - Overlays can also be configured via. DevFun > Overlays
 *
 * @see OverlayManager
 * @see OverlayPermissions
 */
interface OverlayLogger {
    /** Enabled state of this logger. Delegates to [OverlayWindow.enabled]. */
    var enabled: Boolean

    /**
     * How frequently this overlay should update (in milliseconds). _(default: `1000`)_
     *
     * Be aware: Low/quick refresh rates may slow down the app due to heavy reflection (depending on what/where the logging occurs).
     */
    var refreshRate: Long

    /**
     * Determines when an overlay can be visible. Delegates to [OverlayWindow.visibilityScope].
     *
     * - [FOREGROUND_ONLY]: Only visible when the app is in the foreground.
     * - [ALWAYS]: Visible even if the app has no active activities. _App cannot be swipe-closed if you have one of these._
     */
    var visibilityScope: VisibilityScope

    /** The overlay instance used by this logger. */
    val overlay: OverlayWindow

    /**
     * Add this logger to the window and start updating.
     *
     * @see stop
     */
    fun start()

    /**
     * Stop this logger and remove it from the window.
     *
     * @see start
     */
    fun stop()

    /** Additional configuration options for this overlay. */
    val configurationOptions: List<UiField<*>>

    /**
     * Reset the position and state to its initial default values and clear its preferences.
     *
     * Please submit an issue if you needed to call this because it was out of bound or misbehaving our something.
     */
    fun resetPositionAndState()
}

/*
TODO resize overlay when text changes
 */
internal class OverlayLoggerImpl(
    private val context: Context,
    override val overlay: OverlayWindow,
    private val update: UpdateCallback,
    private val onLoggerClick: OnClick? = null,
    initialRefreshRate: Long = 1000L
) : OverlayLogger {
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = Runnable {
        update()
        postUpdate()
    }

    init {
        overlay.apply {
            onClickListener = {
                if (errored) {
                    errored = false
                    runnable.run()
                } else {
                    update()
                    onLoggerClick?.invoke()
                }
            }
            onVisibilityListener = {
                handler.removeCallbacks(runnable)
                if (it) runnable.run()
            }
            onAttachListener = {
                handler.removeCallbacks(runnable)
                if (it) runnable.run()
            }
        }
    }

    private var errored = false

    private val preferences = KSharedPreferences.named(context.applicationContext, overlay.prefsName)
    override var refreshRate by preferences["refreshRate", initialRefreshRate, { _, value ->
        handler.removeCallbacks(runnable)
        if (value > 0) {
            runnable.run()
        }
    }]

    override var enabled by overlay::enabled
    override var visibilityScope by overlay::visibilityScope

    private val textView by lazy { overlay.view.findViewById<TextView>(R.id.loggerTextView) }
    var text: CharSequence
        get() = textView.text
        set(value) {
            textView.text = value
        }

    override fun start() {
        overlay.addToWindow()
        handler.post(runnable)
    }

    override fun stop() {
        handler.removeCallbacks(runnable)
        overlay.removeFromWindow()
    }

    override val configurationOptions: List<UiField<*>>
        get() = listOf(
            uiField(context.getString(R.string.df_devfun_refresh_rate), refreshRate) { refreshRate = it }
        )

    override fun resetPositionAndState() = overlay.resetPositionAndState()

    private fun update() {
        if (errored || !overlay.isVisible) return
        try {
            val newText = update.invoke()
            if (text != newText) { // we do this so we don't constantly trigger view redraws etc.
                text = newText
            }
        } catch (t: Throwable) {
            errored = true
            text = "Exception while updating overlay $this.\nTap overlay to re-enable/try again.\n\nException: $t"
            devFun.get<ErrorHandler>().onError(t, "Overlay Logger", "Exception while updating overlay $this.")
        }
    }

    private fun postUpdate() {
        if (errored || !overlay.isVisible) return
        val refreshRate = refreshRate
        if (refreshRate > 0) {
            handler.postDelayed(runnable, refreshRate)
        }
    }

    override fun toString() = "OverlayLogger(prefsName='${overlay.prefsName}', enabled=$enabled, refreshRate=$refreshRate)"
}

private operator fun <R> KProperty0<R>.getValue(instance: Any?, metadata: KProperty<*>): R = get()
private operator fun <R> KMutableProperty0<R>.setValue(instance: Any?, metadata: KProperty<*>, value: R) = set(value)
