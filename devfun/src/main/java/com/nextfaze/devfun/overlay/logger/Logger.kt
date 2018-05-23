package com.nextfaze.devfun.overlay.logger

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.nextfaze.devfun.annotations.DeveloperLogger
import com.nextfaze.devfun.annotations.DeveloperProperty
import com.nextfaze.devfun.core.R
import com.nextfaze.devfun.core.devFun
import com.nextfaze.devfun.error.ErrorHandler
import com.nextfaze.devfun.internal.pref.*
import com.nextfaze.devfun.invoke.*
import com.nextfaze.devfun.overlay.OverlayManager
import com.nextfaze.devfun.overlay.OverlayPermissions
import com.nextfaze.devfun.overlay.OverlayWindow
import com.nextfaze.devfun.overlay.VisibilityScope
import com.nextfaze.devfun.overlay.VisibilityScope.ALWAYS
import com.nextfaze.devfun.overlay.VisibilityScope.FOREGROUND_ONLY
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0

typealias UpdateCallback = () -> CharSequence
typealias OnClick = () -> Unit

/**
 * An overlay logger is a floating semi-transparent `TextView` that will display the `.toString()` of a function or property.
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

    /** Show the config dialog for this logger. */
    fun showConfigDialog()
}

class OverlayLoggerImpl(
    context: Context,
    override val overlay: OverlayWindow,
    private val invoker: Invoker,
    private val name: String,
    private val update: UpdateCallback,
    private val onLoggerClick: OnClick? = null
) : OverlayLogger {
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = Runnable {
        update()
        postUpdate()
    }

    init {
        overlay.apply {
            onClick = {
                if (errored) {
                    errored = false
                    runnable.run()
                } else {
                    update()
                    onLoggerClick?.invoke()
                }
            }
            onLongClick = { showConfigDialog() }
            onVisibilityChange = {
                handler.removeCallbacks(runnable)
                if (it) runnable.run()
            }
        }
    }

    private var errored = false

    private val preferences = KSharedPreferences.named(context.applicationContext, overlay.prefsName)
    override var refreshRate by preferences["refreshRate", 1000L, { _, value ->
        handler.removeCallbacks(runnable)
        if (value > 0) {
            runnable.run()
        }
    }]

    override var enabled by overlay::enabled

    override var visibilityScope: VisibilityScope
        get() = overlay.visibilityScope
        set(value) {
            overlay.visibilityScope = value
            handler.removeCallbacks(runnable)
            postUpdate()
        }

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

    private fun update() {
        if (errored || !overlay.isVisible) return
        try {
            val newText = update.invoke()
            if (text != newText) { // we do this so we don't constantly trigger view redraws etc.
                text = newText
            }
        } catch (t: Throwable) {
            errored = true
            devFun.get<ErrorHandler>()
                .onError(t, "Overlay Logger", "Exception while updating overlay $this.\nTap overlay to re-enable.")
        }
    }

    private fun postUpdate() {
        if (errored || !overlay.isVisible) return
        val refreshRate = refreshRate
        if (refreshRate > 0) {
            handler.postDelayed(runnable, refreshRate)
        }
    }

    private class Option<T : Any>(
        override val name: String,
        override var value: T,
        override val type: KClass<out T> = value::class,
        val setValue: (T) -> Unit
    ) : Parameter, WithInitialValue<T>

    override fun showConfigDialog() {
        val params = listOf(
            Option("Enabled", overlay.enabled) { overlay.enabled = it },
            Option("Snap to Edges", overlay.snapToEdge) { overlay.snapToEdge = it },
            Option("Refresh Rate (ms)", refreshRate) { refreshRate = it },
            Option("Visibility Scope", visibilityScope) { visibilityScope = it }
        )

        invoker.invoke(
            uiFunction(
                title = "Overlay Options",
                subtitle = "Logger Overlay $name",
                parameters = params,
                neutralButton = uiButton(textId = R.string.df_devfun_reset, onClick = { overlay.resetPositionAndState() }),
                invoke = {
                    params.asSequence().zip(it.asSequence()).forEach { (param, arg) ->
                        @Suppress("UNCHECKED_CAST")
                        (param as Option<Any>).setValue(arg!!)
                    }
                }
            )
        )
    }

    override fun toString() = "OverlayLogger(prefsName='${overlay.prefsName}', enabled=$enabled, refreshRate=$refreshRate)"
}

private operator fun <R> KProperty0<R>.getValue(instance: Any?, metadata: KProperty<*>): R = get()
private operator fun <R> KMutableProperty0<R>.setValue(instance: Any?, metadata: KProperty<*>, value: R) = set(value)
