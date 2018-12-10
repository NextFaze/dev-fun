package com.nextfaze.devfun.invoke.view.types

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import com.nextfaze.devfun.core.DevFun
import com.nextfaze.devfun.core.R
import com.nextfaze.devfun.invoke.Parameter
import com.nextfaze.devfun.invoke.ParameterViewFactoryProvider
import com.nextfaze.devfun.invoke.view.Values
import com.nextfaze.devfun.invoke.view.WithValue
import com.nextfaze.devfun.view.ViewFactory
import com.nextfaze.devfun.view.viewFactory

internal class SpinnerInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.spinnerStyle
) : AppCompatSpinner(context, attrs, defStyleAttr), WithValue<Any> {

    override var value: Any
        get() = selectedItem
        set(value) {
            valueOptions.indexOf(value).takeIf { it >= 0 }?.let { setSelection(it) }
        }

    internal var valueOptions: List<Any?> = listOf()
        set(value) {
            field = value
            adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, value).apply {
                setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
            }
        }
}

internal class EnumParameterViewFactoryProvider : ParameterViewFactoryProvider {
    override fun get(parameter: Parameter): ViewFactory<View>? {
        val classifier = parameter.type.takeIf { it.java.isEnum } ?: return null
        return viewFactory<SpinnerInputView>(R.layout.df_devfun_spinner_input) {
            valueOptions = classifier.java.enumConstants.toList()
        }
    }
}

internal class IterableParameterViewFactoryProvider(private val devFun: DevFun) : ParameterViewFactoryProvider {
    override fun get(parameter: Parameter): ViewFactory<View>? {
        val annotation = parameter.annotations.getTypeOrNull<Values>() ?: return null
        return viewFactory<SpinnerInputView>(R.layout.df_devfun_spinner_input) {
            valueOptions = devFun.instanceOf(annotation.source).value.toList()
        }
    }
}
