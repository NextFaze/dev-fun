package com.nextfaze.devfun.invoke.view.types

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.SwitchCompat
import com.nextfaze.devfun.core.R
import com.nextfaze.devfun.invoke.Parameter
import com.nextfaze.devfun.invoke.ParameterViewFactoryProvider
import com.nextfaze.devfun.invoke.view.WithLabel
import com.nextfaze.devfun.invoke.view.WithValue
import com.nextfaze.devfun.view.ViewFactory
import com.nextfaze.devfun.view.viewFactory

internal class SwitchInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.switchStyle
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
        parameter.type.takeIf { it == Boolean::class } ?: return null
        return viewFactory(R.layout.df_devfun_switch_input)
    }
}
