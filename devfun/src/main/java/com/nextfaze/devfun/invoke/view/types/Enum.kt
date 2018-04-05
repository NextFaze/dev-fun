package com.nextfaze.devfun.invoke.view.types

import android.content.Context
import android.support.v7.widget.AppCompatSpinner
import android.util.AttributeSet
import android.view.View
import android.widget.ArrayAdapter
import com.nextfaze.devfun.core.R
import com.nextfaze.devfun.invoke.Parameter
import com.nextfaze.devfun.invoke.ParameterViewFactoryProvider
import com.nextfaze.devfun.invoke.view.WithValue
import com.nextfaze.devfun.view.ViewFactory
import com.nextfaze.devfun.view.inflate

internal class SpinnerInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.support.v7.appcompat.R.attr.spinnerStyle
) : AppCompatSpinner(context, attrs, defStyleAttr), WithValue<Any> {

    override var value: Any
        get() = selectedItem
        set(value) {
            valueOptions.indexOf(value).takeIf { it >= 0 }?.let { setSelection(it) }
        }

    internal var valueOptions: List<Any> = listOf()
        set(value) {
            field = value
            adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, value).apply {
                setDropDownViewResource(android.support.v7.appcompat.R.layout.support_simple_spinner_dropdown_item)
            }
        }
}

internal class EnumParameterViewFactoryProvider : ParameterViewFactoryProvider {
    override fun get(parameter: Parameter): ViewFactory<View>? {
        val classifier = parameter.clazz.takeIf { it.java.isEnum } ?: return null
        return inflate<SpinnerInputView>(R.layout.df_devfun_spinner_input) {
            valueOptions = classifier.java.enumConstants.toList()
        }
    }
}
