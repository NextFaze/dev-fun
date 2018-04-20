package com.nextfaze.devfun.invoke.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.nextfaze.devfun.core.R
import kotlinx.android.synthetic.main.df_devfun_parameter_view.view.*

/**
 * Views used with the Invoke UI should implement this to ensure the correct value is obtainable upon invocation.
 *
 * If absent, DevFun will attempt to grab the value based on the view type (e.g. if TextView it will use `.text`)
 *
 * Currently `null` types are not supported due to limitations/complications of KAPT and reflection. It is intended to
 * be permissible in the future.
 */
interface WithValue<T : Any> {
    /** The value of this view to be passed to the function for invocation. */
    var value: T
}

/**
 * View used with the Invoke UI will automatically be wrapped and be given a label unless they provide their own with this interface.
 *
 * For example, the Switch view has its own text value and thus is used instead of the one provided by DevFun.
 */
interface WithLabel {
    /** The label/title for this parameter. */
    var label: CharSequence
}

/**
 * Parameter views rendered for the Invoke UI will be wrapped with this type (to provide a label etc.).
 *
 * Be careful if using this as a custom implementation has not been tested (though _should_ work - create an issue if not).
 */
interface InvokeParameterView : WithLabel {
    /** Attributes of the parameter this view represents (e.g. if it is injected or missing etc.). */
    var attributes: CharSequence

    /** The wrapped view the represents the parameter. */
    var view: View?

    /** Can this value be nullable. */
    var nullable: Boolean

    /** Is this value `null`. */
    val isNull: Boolean get() = false
}

internal class SimpleParameterView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr), InvokeParameterView {

    init {
        orientation = VERTICAL
        View.inflate(context, R.layout.df_devfun_parameter_view, this)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        getParamView()?.isEnabled = enabled
    }

    override var label: CharSequence
        get() {
            getParamView()?.let {
                if (it is WithLabel) {
                    return it.label
                }
            }
            return fieldName.text
        }
        set(value) {
            fieldName.text = value
            (getParamView() as? WithLabel)?.let { it.label = value }
        }

    override var attributes: CharSequence
        get() = attributesTextView.text
        set(value) = run { attributesTextView.text = value }

    override var view: View?
        get() = paramView.getChildAt(0)
        set(value) {
            paramView.removeAllViews()
            if (value is WithLabel) {
                labelView.visibility = View.GONE
                value.label = label
            } else {
                labelView.visibility = View.VISIBLE
            }
            value?.let { paramView.addView(it) }
        }

    private fun getParamView(): View? = paramView.getChildAt(0)

    override var nullable: Boolean = false
        set(value) {
            nullCheckBox.visibility = if (value) View.VISIBLE else View.GONE
        }

    override val isNull get() = nullCheckBox.isChecked
}
