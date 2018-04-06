package com.nextfaze.devfun.invoke.view.colorpicker

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.google.auto.service.AutoService
import com.nextfaze.devfun.core.AbstractDevFunModule
import com.nextfaze.devfun.core.DevFunModule
import com.nextfaze.devfun.invoke.Parameter
import com.nextfaze.devfun.invoke.ParameterViewFactoryProvider
import com.nextfaze.devfun.invoke.view.ColorPicker
import com.nextfaze.devfun.invoke.view.WithValue
import com.nextfaze.devfun.view.ViewFactory
import com.nextfaze.devfun.view.inflate
import com.rarepebble.colorpicker.ColorPickerView

@AutoService(DevFunModule::class)
internal class DevInvokeViewColorPicker : AbstractDevFunModule() {
    override fun init(context: Context) {
        devFun.parameterViewFactories += ColorPickerParameterViewProvider
    }

    override fun dispose() {
        devFun.parameterViewFactories -= ColorPickerParameterViewProvider
    }
}

private object ColorPickerParameterViewProvider : ParameterViewFactoryProvider {
    override fun get(parameter: Parameter): ViewFactory<View>? =
        when {
            parameter.clazz == Int::class && parameter.annotations.any { it is ColorPicker } -> inflate(R.layout.df_invoke_view_colorpicker)
            else -> null
        }
}

private class ParameterColorPickerViewForInt @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    ColorPickerView(context, attrs),
    WithValue<Int> {

    override var value: Int
        get() = this.color
        set(value) {
            color = value
        }
}
