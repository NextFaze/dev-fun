package com.nextfaze.devfun.invoke.view.types

import android.content.Context
import android.support.v7.widget.AppCompatSeekBar
import android.util.AttributeSet
import android.view.View
import android.widget.SeekBar
import com.nextfaze.devfun.core.R
import com.nextfaze.devfun.invoke.Parameter
import com.nextfaze.devfun.invoke.ParameterViewFactoryProvider
import com.nextfaze.devfun.invoke.view.WithValue
import com.nextfaze.devfun.view.ViewFactory
import com.nextfaze.devfun.view.inflate
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER
import kotlin.reflect.KClass

/**
 * Used to restrict the range of a [Number] for user input. Using this will render a slider rather than a text view.
 *
 * Behind the scenes this is scaling the value range within `0 → 100` (via [SeekBar]).
 * Thus if you want a small range (e.g. 0 → 1 for say a color value), then you should use `to = 255.0` and then normalize it.
 *
 * e.g.
 * ```kotlin
 * @DeveloperFunction
 * fun setRed(@Ranged(from = 0.0, to = 255.0) red: Int) {
 *     val redPct = red / 255f
 *     val someRedColor = Color.rgb(redPct, 0, 0) // pretend rgb() can't take ints...
 *     ...
 * }
 * ```
 *
 * Using this on anything other than a [Number] will do nothing.
 */
@Retention(RUNTIME)
@Target(VALUE_PARAMETER)
annotation class Ranged(
    /** Minimum value _(inclusive)_. */
    val from: Double = 0.0,

    /** Maximum value _(inclusive)_. */
    val to: Double = 100.0
)

internal class RangedNumberInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.support.v7.appcompat.R.attr.seekBarStyle
) : AppCompatSeekBar(context, attrs, defStyleAttr), WithValue<Number> {

    override var value: Number
        get() {
            val v = (progress.toDouble() * range / 100.0 + minValue)
            return when (type) {
                Byte::class -> v.toByte()
                Short::class -> v.toShort()
                Int::class -> v.toInt()
                Long::class -> v.toLong()
                Float::class -> v.toFloat()
                Double::class -> v
                else -> v.toInt()
            }
        }
        set(value) = super.setProgress(((value.toDouble().coerceIn(minValue, maxValue) - minValue) * 100.0 / range).toInt())

    internal var type: KClass<*> = Int::class
    internal var minValue: Double = 0.0
    internal var maxValue: Double = 0.0

    private val range get() = maxValue - minValue
}

internal class RangedNumberParameterViewFactoryProvider : ParameterViewFactoryProvider {
    override fun get(parameter: Parameter): ViewFactory<View>? {
        val clazz = parameter.clazz.takeIf { it.isNumber } ?: return null
        val annotation = parameter.annotations.getTypeOrNull<Ranged>() ?: return null

        return inflate<RangedNumberInputView>(R.layout.df_devfun_range_number_input) {
            type = clazz
            minValue = annotation.from
            maxValue = annotation.to
        }
    }

    private val KClass<*>.isNumber get() = isDecimalNumber || isWholeNumber
    private val KClass<*>.isDecimalNumber get() = this == Double::class || this == Float::class
    private val KClass<*>.isWholeNumber get() = this == Long::class || this == Int::class || this == Short::class || this == Byte::class
}

private inline fun <reified T : Annotation> Iterable<Annotation>.getTypeOrNull(body: ((T) -> Unit) = {}): T? =
    (firstOrNull { it is T } as T?)?.also(body)
