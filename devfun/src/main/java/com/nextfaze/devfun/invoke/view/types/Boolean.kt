package com.nextfaze.devfun.invoke.view.types

import android.content.Context
import android.support.v7.widget.SwitchCompat
import android.util.AttributeSet
import android.view.View
import com.nextfaze.devfun.core.R
import com.nextfaze.devfun.invoke.Parameter
import com.nextfaze.devfun.invoke.ParameterViewFactoryProvider
import com.nextfaze.devfun.invoke.view.WithLabel
import com.nextfaze.devfun.invoke.view.WithValue
import com.nextfaze.devfun.view.ViewFactory
import com.nextfaze.devfun.view.inflate

internal class SwitchInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.support.v7.appcompat.R.attr.switchStyle
) : SwitchCompat(context, attrs, defStyleAttr), WithValue<Boolean>, WithLabel {

    override var label: CharSequence
        get() = text
        set(value) = run { text = value }

    override var value: Boolean
        get() = isChecked
        set(value) = run { isChecked = value }
}

internal class BooleanParameterViewFactoryProvider : ParameterViewFactoryProvider {
    override fun get(parameter: Parameter): ViewFactory<View>? {
        parameter.clazz.takeIf { it == Boolean::class } ?: return null
        return inflate(R.layout.df_devfun_switch_input)
    }
}
