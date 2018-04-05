package com.nextfaze.devfun.invoke.view.simple

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import com.nextfaze.devfun.invoke.view.WithValue
import kotlin.reflect.KClass

/** A simple view that should tell the user if a view could not be injected/rendered. */
interface ErrorParameterView : WithValue<KClass<*>>

internal class ErrorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = android.R.attr.textViewStyle
) : AppCompatTextView(context, attrs, defStyleAttr), ErrorParameterView {

    override var value: KClass<*> = Unit::class
        set(value) {
            this.text = value.qualifiedName
            field = value
        }
}
