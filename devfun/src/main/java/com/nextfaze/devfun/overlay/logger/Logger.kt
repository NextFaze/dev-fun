package com.nextfaze.devfun.overlay.logger

import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.nextfaze.devfun.core.R
import com.nextfaze.devfun.core.devFun
import com.nextfaze.devfun.error.ErrorHandler
import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.invoke.*
import com.nextfaze.devfun.overlay.OverlayManager
import com.nextfaze.devfun.overlay.VisibilityScope
import kotlin.reflect.KClass

typealias UpdateCallback = () -> CharSequence
typealias OnClick = () -> Unit

class OverlayLogger(
    private val overlayManager: OverlayManager,
    private val invoker: Invoker,
    private val name: String,
    private val update: UpdateCallback,
    private val onClick: OnClick? = null
) {
    private val log = logger()
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = Runnable {
        update()
        postUpdate()
    }
    private val prefsName = "OverlayLogger_$name"

    private val overlay = overlayManager.createOverlay(
        layoutId = R.layout.df_devfun_logger_overlay,
        prefsName = prefsName,
        reason = { "Show overlay logger for $prefsName" },
        onClick = {
            if (errored) {
                errored = false
                runnable.run()
            } else {
                update()
                onClick?.invoke()
            }
        },
        onLongClick = { showConfigDialog() },
        snapToEdge = false
    )

    private val textView by lazy { overlay.view.findViewById<TextView>(R.id.loggerTextView) }

    private var errored = false

    var enabled
        get() = overlay.enabled
        set(value) {
            overlay.enabled = value
        }

    var refreshRate = 1000L
        set(value) {
            handler.removeCallbacks(runnable)
            if (value > 0) {
                runnable.run()
            }
            field = value
        }

    var visibilityScope: VisibilityScope
        get() = overlay.visibilityScope
        set(value) {
            overlay.visibilityScope = value
            handler.removeCallbacks(runnable)
            postUpdate()
        }

    var text: CharSequence
        get() = textView.text
        set(value) {
            textView.text = value
        }

    fun destroy() {
        log.t { "destroy $this" }
        handler.removeCallbacks(runnable)
        overlayManager.destroyOverlay(overlay)
    }

    private fun update() {
        try {
            val newText = update.invoke()
            if (text != newText) { // we do this so we don't constantly trigger view redraws etc.
                text = newText
            }
        } catch (t: Throwable) {
            errored = true
            devFun.get<ErrorHandler>().onError(t, "Overlay Logger", "Exception while updating overlay $this.\nTap overlay to re-enable.")
        }
    }

    init {
        postUpdate()
    }

    private fun postUpdate() {
        if (errored) return
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

    fun showConfigDialog() {
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

    override fun toString() =
        "OverlayLogger(prefsName='$prefsName', enabled=$enabled, refreshRate=$refreshRate)"
}
