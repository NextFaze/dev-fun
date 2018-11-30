package com.nextfaze.devfun.invoke.view.types

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.nextfaze.devfun.core.R
import com.nextfaze.devfun.internal.android.*
import com.nextfaze.devfun.invoke.Parameter
import com.nextfaze.devfun.invoke.ParameterViewFactoryProvider
import com.nextfaze.devfun.invoke.WithKParameter
import com.nextfaze.devfun.invoke.view.WithValue
import com.nextfaze.devfun.view.ViewFactory
import com.nextfaze.devfun.view.viewFactory
import kotlinx.android.synthetic.main.df_devfun_enum_set_input.view.*
import kotlinx.android.synthetic.main.df_devfun_enum_set_item.view.*
import java.util.EnumSet
import kotlin.reflect.KClass

/**
 * This is an experimental feature.
 * TODO? Could inline classes be a cleaner way of doing this given most of Android's flags are Int/Long values?
 */
interface WithMask {
    val mask: Long
}

/** This is an experimental feature. */
val Collection<WithMask>.value: Long
    get() {
        var value = 0L
        forEach {
            value = value or it.mask
        }
        return value
    }

@Suppress("UNCHECKED_CAST")
internal class EnumSetInputView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr), WithValue<EnumSet<*>> {

    override var value: EnumSet<*>
        get() {
            val enumSet = EnumSet.noneOf<Nothing>(valueOptions.first()::class.java as Class<Nothing>)
            val checked = flagsLayout.children<EnumSetItemView>().mapNotNull { view ->
                view.enum?.takeIf { view.checked }
            }
            enumSet.addAll(checked as Collection<Nothing>)
            return enumSet
        }
        set(enumSet) {
            flagsLayout.children<EnumSetItemView>().forEach {
                it.checked = it.enum in enumSet
            }
            updateCurrentValue()
        }

    internal var valueOptions: List<Any> = listOf()
        set(values) {
            field = values
            flagsLayout.removeAllViews()

            val enums = values.filterIsInstance<Enum<*>>().sortedBy { if (it is WithMask) it.mask else it.ordinal.toLong() }
            enums.forEach { e ->
                val flagView = inflateView<EnumSetItemView>(R.layout.df_devfun_enum_set_item) {
                    enum = e
                    onClick = ::updateCurrentValue
                }

                flagsLayout.addView(flagView)
            }
        }

    @SuppressLint("SetTextI18n")
    private fun updateCurrentValue() {
        var value = 0L
        flagsLayout.children<EnumSetItemView>().forEach {
            if (it.checked) {
                value = value or it.getMask()
            }
        }
        currentBinaryValueTextView.text = value.toBinary()
        currentHexValueTextView.text = value.toHex()
    }
}

internal class EnumSetItemView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    internal var enum: Enum<*>? = null
        set(value) {
            field = value
            nameTextView.text = value?.let { "${it.name}(${it.ordinal})" }
            valueTextView.text = value?.let { getMask() }?.let { "${it.toBinary()} : ${it.toHex()}" }
        }

    internal var onClick: (() -> Unit)? = null

    init {
        setOnClickListener {
            checked = !checked
            onClick?.invoke()
        }
    }

    internal var checked
        get() = enabledCheckBox.isChecked
        set(value) {
            enabledCheckBox.isChecked = value
        }

    fun getMask(): Long {
        val enum = enum
        return if (enum is WithMask) enum.mask else 1L shl (enum?.ordinal ?: 0)
    }
}

internal class EnumSetParameterViewFactoryProvider : ParameterViewFactoryProvider {
    override fun get(parameter: Parameter): ViewFactory<View>? {
        if (parameter.type != EnumSet::class) return null
        return viewFactory<EnumSetInputView>(R.layout.df_devfun_enum_set_input) {
            val type = (parameter as WithKParameter).kParameter.type.arguments.first().type!!.classifier as KClass<*>
            valueOptions = type.java.enumConstants.toList()
        }
    }
}

private fun Long.toBinary(): String {
    val maskBinary = toString(2)
    val padded = "0".repeat(4 - maskBinary.length.rem(4)) + maskBinary
    return padded.windowed(4, 4).joinToString(" ")
}

private fun Long.toHex(): String {
    val maskHex = toString(16).toUpperCase()
    return if (maskHex.length.rem(2) != 0) "0x0$maskHex" else "0x$maskHex"
}
