package com.nextfaze.devfun.invoke

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TextInputLayout
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.nextfaze.devfun.BaseDialogFragment
import com.nextfaze.devfun.core.DebugException
import com.nextfaze.devfun.core.R
import com.nextfaze.devfun.error.ErrorHandler
import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.internal.string.*
import com.nextfaze.devfun.invoke.view.From
import com.nextfaze.devfun.invoke.view.InvokeParameterView
import com.nextfaze.devfun.invoke.view.WithValue
import com.nextfaze.devfun.invoke.view.simple.ErrorParameterView
import com.nextfaze.devfun.invoke.view.simple.InjectedParameterView
import com.nextfaze.devfun.invoke.view.types.getTypeOrNull
import com.nextfaze.devfun.obtain
import com.nextfaze.devfun.show
import kotlinx.android.synthetic.main.df_devfun_invoker_dialog_fragment.*
import kotlin.reflect.KClass

internal class InvokingDialogFragment : BaseDialogFragment() {
    companion object {
        fun show(activity: FragmentActivity, function: UiFunction) = activity
            .obtain {
                InvokingDialogFragment().apply {
                    this.function = function
                }
            }
            .apply { takeIf { !it.isAdded }?.show(activity.supportFragmentManager) }
    }

    private val log = logger()

    private lateinit var function: UiFunction
    private val devFun = com.nextfaze.devfun.core.devFun
    private val errorHandler get() = devFun.get<ErrorHandler>()

    private var canExecute = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            Toast.makeText(context, "Invocation dialog does not support recreation from a saved state at this time.", Toast.LENGTH_LONG)
                .show()
            dismissAllowingStateLoss()
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        super.onCreateDialog(savedInstanceState).apply { requestWindowFeature(Window.FEATURE_NO_TITLE) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.df_devfun_invoker_dialog_fragment, container, false)

    private val Parameter.displayName: CharSequence
        get() = name.let { name ->
            when (name) {
                is String -> name.capitalize().splitCamelCase()
                else -> name ?: "Unknown"
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {
            canExecute = true
            performOnViewCreated()
        } catch (t: Throwable) {
            errorHandler.onError(
                t,
                "View Creation Failure",
                "Something when wrong when trying to create the invocation dialog view for $function."
            )
            Handler().post { dismissAllowingStateLoss() }
        }
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun performOnViewCreated() {
        titleText.text = function.title

        subtitleText.text = function.subtitle
        subtitleText.visibility = if (function.subtitle == null) View.GONE else View.VISIBLE

        functionSignatureText.text = function.signature
        functionSignatureText.visibility = if (function.signature == null) View.GONE else View.VISIBLE

        function.parameters.forEach { buildParameterView(it) }

        cancelButton.setOnClickListener { dialog.dismiss() }
        executeButton.setOnClickListener {
            try {
                fun View?.getValue(): Any? {
                    return when (this) {
                        is InvokeParameterView -> if (isNull) null else view.getValue()
                        is InjectedParameterView -> devFun.instanceOf(value)
                        is WithValue<*> -> value
                        is TextInputLayout -> editText!!.text
                        is Switch -> isChecked
                        is Spinner -> selectedItem
                        is ProgressBar -> progress
                        is TextView -> text
                        else -> throw RuntimeException("Unexpected view type $this. If this is a custom view, add the WithValue interface. If this is a standard platform view, create an issue to have it handled.")
                    }
                }

                val params = (0 until inputsList.childCount).map {
                    inputsList.getChildAt(it).getValue()
                }

                log.d { "Invoke $function\nwith params: $params" }
                function.invoke(params)
            } catch (de: DebugException) {
                throw de
            } catch (t: Throwable) {
                errorHandler.onError(t, "Invocation Failure", "Something went wrong when trying to execute requested method for $function.")
            }
            dialog.dismiss()
        }

        if (!canExecute) {
            (0 until inputsList.childCount).forEach {
                inputsList.getChildAt(it).isEnabled = false
            }
            executeButton.isEnabled = false
            errorMessageText.visibility = View.VISIBLE
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun buildParameterView(parameter: Parameter) {
        val paramView = layoutInflater.inflate(R.layout.df_devfun_parameter, inputsList, false) as ViewGroup
        paramView as InvokeParameterView
        paramView.label = parameter.displayName
        paramView.nullable = if (parameter is WithNullability) parameter.isNullable else false

        if (parameter.type.isInjectable) {
            paramView.view = devFun.viewFactories[InjectedParameterView::class]?.inflate(layoutInflater, inputsList)?.apply {
                if (this is InjectedParameterView) {
                    value = parameter.type
                    paramView.attributes = getText(R.string.df_devfun_injected)
                }
            }
        } else {
            val inputViewFactory = devFun.parameterViewFactories[parameter]
            if (inputViewFactory != null) {
                paramView.view = inputViewFactory.inflate(layoutInflater, inputsList).apply {
                    if (this is WithValue<*>) {
                        if (parameter is WithInitialValue<*>) {
                            val value = parameter.value
                            when (value) {
                                null -> paramView.isNull = paramView.nullable
                                else -> (this as WithValue<Any>).value = value
                            }
                        } else {
                            parameter.annotations.getTypeOrNull<From> {
                                (this as WithValue<Any>).value = devFun.instanceOf(it.source).value
                            }
                        }
                    }
                }
            } else {
                canExecute = false
                devFun.viewFactories[ErrorParameterView::class]?.inflate(layoutInflater, inputsList)?.apply {
                    this as ErrorParameterView
                    this.value = parameter.type
                    paramView.attributes = getText(R.string.df_devfun_missing)
                    paramView.view = this
                }
            }
        }

        inputsList.addView(paramView)
    }

    private val <T : Any> KClass<T>.isInjectable get() = devFun.tryGetInstanceOf(this) != null
}
