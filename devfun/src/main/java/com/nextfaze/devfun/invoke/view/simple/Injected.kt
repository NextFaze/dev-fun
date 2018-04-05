package com.nextfaze.devfun.invoke.view.simple

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import com.nextfaze.devfun.invoke.view.WithValue
import kotlin.reflect.KClass

/** A simple view that should tell the user if a parameter is being injected. */
interface InjectedParameterView : WithValue<KClass<*>>

internal class InjectedView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr), InjectedParameterView {

    override var value: KClass<*> = Unit::class
        set(value) {
            this.text = value.qualifiedName
            field = value
        }
}
