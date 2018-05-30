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
import android.widget.Toast.LENGTH_LONG
import com.nextfaze.devfun.core.DebugException
import com.nextfaze.devfun.core.R
import com.nextfaze.devfun.error.ErrorHandler
import com.nextfaze.devfun.internal.android.*
import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.internal.splitCamelCase
import com.nextfaze.devfun.invoke.view.From
import com.nextfaze.devfun.invoke.view.InvokeParameterView
import com.nextfaze.devfun.invoke.view.WithValue
import com.nextfaze.devfun.invoke.view.simple.ErrorParameterView
import com.nextfaze.devfun.invoke.view.simple.InjectedParameterView
import com.nextfaze.devfun.invoke.view.types.getTypeOrNull
import com.nextfaze.devfun.overlay.OverlayManager
import kotlinx.android.synthetic.main.df_devfun_invoker_dialog_fragment.*

internal class InvokingDialogFragment : BaseDialogFragment() {
    companion object {
        fun show(activity: FragmentActivity, function: UiFunction) =
            showNow(activity) {
                InvokingDialogFragment().apply {
                    this.function = function
                }
            }
    }

    private val log = logger()

    private lateinit var function: UiFunction

    private val devFun = com.nextfaze.devfun.core.devFun
    private val errorHandler by lazy { devFun.get<ErrorHandler>() }
    private val overlays by lazy { devFun.get<OverlayManager>() }

    private var canExecute = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            Toast.makeText(context, "Invocation dialog does not support recreation from a saved state at this time.", LENGTH_LONG).show()
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

    override fun onStart() {
        super.onStart()
        overlays.notifyUsingFullScreen(this)
    }

    override fun onPerformDismiss() {
        overlays.notifyFinishUsingFullScreen(this)
    }

    private fun performOnViewCreated() {
        titleText.text = function.title

        subtitleText.text = function.subtitle
        subtitleText.visibility = if (function.subtitle == null) View.GONE else View.VISIBLE

        functionSignatureText.text = function.signature
        functionSignatureText.visibility = if (function.signature == null) View.GONE else View.VISIBLE

        function.parameters.forEach { buildParameterView(it) }

        fun Button.setUiButton(uiButton: UiButton) {
            visibility = View.VISIBLE
            uiButton.text?.also { text = it }
            uiButton.textId?.also { text = getText(it) }
            setOnClickListener {
                val invoke = uiButton.invoke
                when {
                    invoke != null -> onInvokeButtonClick(invoke)
                    else -> uiButton.onClick?.invoke()
                }
                dialog.dismiss()
            }
            if (!canExecute && uiButton.invoke != null) {
                isEnabled = false
            }
        }

        (function.negativeButton ?: uiButton()).also { negativeButton.setUiButton(it) }
        function.neutralButton?.also { neutralButton.setUiButton(it) }
        (function.positiveButton ?: uiButton()).also { positiveButton.setUiButton(it) }

        if (!canExecute) {
            (0 until inputsList.childCount).forEach {
                inputsList.getChildAt(it).also { view ->
                    view as InvokeParameterView
                    val paramView = view.view
                    // we want to leave these enabled so we can click to show error details
                    if (paramView !is ErrorParameterView) {
                        view.isEnabled = false
                    }
                }
            }
            errorMessageText.visibility = View.VISIBLE
        }
    }

    private fun onInvokeButtonClick(simpleInvoke: SimpleInvoke) {
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
            simpleInvoke(params)
        } catch (de: DebugException) {
            throw de
        } catch (t: Throwable) {
            errorHandler.onError(t, "Invocation Failure", "Something went wrong when trying to execute requested method for $function.")
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun buildParameterView(parameter: Parameter) {
        val paramView = layoutInflater.inflate(R.layout.df_devfun_parameter, inputsList, false) as ViewGroup
        paramView as InvokeParameterView
        paramView.label = parameter.displayName
        paramView.nullable = if (parameter is WithNullability) parameter.isNullable else false

        val injectException =
            try {
                devFun.instanceOf(parameter.type).let { null }
            } catch (t: Throwable) {
                t
            }

        if (injectException == null) {
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
                    value = parameter.type
                    paramView.attributes = getText(R.string.df_devfun_missing)
                    paramView.view = this
                    paramView.setOnClickListener {
                        devFun.get<ErrorHandler>()
                            .onError(injectException, "Instance Not Found", getString(R.string.df_devfun_cannot_execute))
                    }
                }
            }
        }

        inputsList.addView(paramView)
    }
}
