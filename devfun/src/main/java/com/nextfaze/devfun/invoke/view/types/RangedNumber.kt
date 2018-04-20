package com.nextfaze.devfun.invoke.view.types

import android.content.Context
import android.support.v7.widget.AppCompatSeekBar
import android.util.AttributeSet
import android.view.View
import com.nextfaze.devfun.core.R
import com.nextfaze.devfun.invoke.Parameter
import com.nextfaze.devfun.invoke.ParameterViewFactoryProvider
import com.nextfaze.devfun.invoke.view.Ranged
import com.nextfaze.devfun.invoke.view.WithValue
import com.nextfaze.devfun.view.ViewFactory
import com.nextfaze.devfun.view.inflate
import kotlin.reflect.KClass

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
        val clazz = parameter.type.takeIf { it.isNumber } ?: return null
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

internal inline fun <reified T : Annotation> Iterable<Annotation>.getTypeOrNull(body: ((T) -> Unit) = {}): T? =
    (firstOrNull { it is T } as T?)?.also(body)
