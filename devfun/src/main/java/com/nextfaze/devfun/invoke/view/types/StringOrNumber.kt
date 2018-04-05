package com.nextfaze.devfun.invoke.view.types

import android.content.Context
import android.support.design.widget.TextInputLayout
import android.util.AttributeSet
import android.view.View
import android.view.inputmethod.EditorInfo.*
import com.nextfaze.devfun.core.R
import com.nextfaze.devfun.invoke.Parameter
import com.nextfaze.devfun.invoke.ParameterViewFactoryProvider
import com.nextfaze.devfun.invoke.view.WithLabel
import com.nextfaze.devfun.invoke.view.WithValue
import com.nextfaze.devfun.view.ViewFactory
import com.nextfaze.devfun.view.inflate
import kotlin.reflect.KClass

internal class StringOrNumberTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr), WithValue<Any>, WithLabel {

    override var label: CharSequence
        get() = hint ?: ""
        set(value) = run { hint = value }

    override var value: Any
        get() = editText!!.text.let { v ->
            // TODO could throw number format exception
            return when (type) {
                CharSequence::class -> v
                String::class -> v.toString()
                Byte::class -> v.toString().toByte()
                Short::class -> v.toString().toShort()
                Int::class -> v.toString().toInt()
                Long::class -> v.toString().toLong()
                Float::class -> v.toString().toFloat()
                Double::class -> v.toString().toDouble()
                else -> v
            }
        }
        set(value) = editText!!.setText(value.toString())

    internal var type: KClass<*> = String::class
}

internal class StringOrNumberParameterViewFactoryProvider : ParameterViewFactoryProvider {
    override fun get(parameter: Parameter): ViewFactory<View>? {
        fun inflateEditText(inputType: Int) =
            inflate<StringOrNumberTextView>(R.layout.df_devfun_edit_text_input) {
                type = parameter.clazz
                editText!!.inputType = inputType
            }

        return when (parameter.clazz) {
            String::class -> inflateEditText(TYPE_CLASS_TEXT)
            Byte::class, Short::class, Int::class, Long::class -> inflateEditText(TYPE_CLASS_NUMBER or TYPE_NUMBER_FLAG_SIGNED)
            Float::class, Double::class -> inflateEditText(TYPE_CLASS_NUMBER or TYPE_NUMBER_FLAG_DECIMAL or TYPE_NUMBER_FLAG_SIGNED)
            else -> null
        }
    }
}
