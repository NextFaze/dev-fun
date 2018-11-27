package com.nextfaze.devfun.invoke.view.types

import android.content.Context
import android.text.format.DateFormat
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.nextfaze.devfun.core.R
import com.nextfaze.devfun.invoke.Parameter
import com.nextfaze.devfun.invoke.ParameterViewFactoryProvider
import com.nextfaze.devfun.invoke.view.WithValue
import com.nextfaze.devfun.view.ViewFactory
import com.nextfaze.devfun.view.viewFactory
import kotlinx.android.synthetic.main.df_devfun_date_input.view.*
import java.util.Calendar
import java.util.Date

@Suppress("DEPRECATION")
internal class DateTimeInputView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr), WithValue<Date> {

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        timePicker.setIs24HourView(DateFormat.is24HourFormat(context))
    }

    override var value: Date
        set(value) {
            datePicker.updateDate(value.year, value.month, value.day)
            timePicker.currentHour = value.hours
            timePicker.currentMinute = value.minutes
        }
        get() {
            val year = datePicker.year
            val month = datePicker.month
            val day = datePicker.dayOfMonth
            val hour = timePicker.currentHour
            val minute = timePicker.currentMinute
            return Calendar.getInstance().apply { set(year, month, day, hour, minute, 0) }.time
        }
}

internal class DateParameterViewFactoryProvider : ParameterViewFactoryProvider {
    override fun get(parameter: Parameter): ViewFactory<View>? {
        parameter.type.takeIf { it == Date::class } ?: return null
        return viewFactory(R.layout.df_devfun_date_input)
    }
}
