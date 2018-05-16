package com.nextfaze.devfun.overlay.logger

import android.os.Handler
import android.os.Looper
import android.widget.TextView
import com.nextfaze.devfun.core.R
import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.invoke.Invoker
import com.nextfaze.devfun.invoke.Parameter
import com.nextfaze.devfun.invoke.WithInitialValue
import com.nextfaze.devfun.invoke.uiFunction
import com.nextfaze.devfun.overlay.OverlayManager
import kotlin.reflect.KClass

typealias UpdateCallback = () -> CharSequence
typealias OnClick = () -> Unit

class OverlayLogger(
    private val overlayManager: OverlayManager,
    private val invoker: Invoker,
    private val prefsName: String,
    private val update: UpdateCallback,
    private val onClick: OnClick? = null
) {
    private val log = logger()
    private val handler = Handler(Looper.getMainLooper())
    private val runnable = Runnable {
        update()
        postUpdate()
    }

    private val overlay = overlayManager.createOverlay(
        layoutId = R.layout.df_devfun_logger_overlay,
        prefsName = prefsName,
        reason = {
            "OverlayLogger"
        },
        onClick = {
            update()
            onClick?.invoke()
        },
        onLongClick = { showConfigDialog() },
        snapToEdge = false
    )

    private val textView by lazy { overlay.view.findViewById<TextView>(R.id.loggerTextView) }

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

    var text: CharSequence
        get() = textView.text
        set(value) {
            textView.text = value
        }

    fun destroy() {
        log.d { "destroy $this" }
        handler.removeCallbacks(runnable)
        overlayManager.destroyOverlay(overlay)
    }

    private fun update() {
        val newText = update.invoke()
        if (text != newText) { // we do this so we don't constantly trigger view redraws etc.
            text = newText
        }
    }

    init {
        postUpdate()
    }

    private fun postUpdate() {
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
            Option("Refresh Rate (ms)", refreshRate) { refreshRate = it }
        )

        invoker.invoke(
            uiFunction(
                title = "Overlay Options",
                subtitle = "Logger Overlay $prefsName",
                parameters = params,
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
