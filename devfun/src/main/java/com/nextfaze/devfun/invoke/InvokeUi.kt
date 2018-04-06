package com.nextfaze.devfun.invoke

import android.os.Bundle
import android.os.Handler
import android.support.design.widget.TextInputLayout
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.nextfaze.devfun.BaseDialogFragment
import com.nextfaze.devfun.core.DebugException
import com.nextfaze.devfun.core.FunctionItem
import com.nextfaze.devfun.core.R
import com.nextfaze.devfun.error.ErrorHandler
import com.nextfaze.devfun.internal.*
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
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.kotlinFunction

internal class InvokingDialogFragment : BaseDialogFragment() {
    companion object {
        fun show(activity: FragmentActivity, functionItem: FunctionItem) = activity
            .obtain {
                InvokingDialogFragment().apply {
                    this.functionItem = functionItem
                }
            }
            .apply { takeIf { !it.isAdded }?.show(activity.supportFragmentManager) }
    }

    private val log = logger()

    private lateinit var functionItem: FunctionItem
    private val devFun = com.nextfaze.devfun.core.devFun
    private val errorHandler get() = devFun.get<ErrorHandler>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            Toast.makeText(context, "Invocation dialog does not support recreation from a saved state at this time.", Toast.LENGTH_LONG)
                .show()
            dismissAllowingStateLoss()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.df_devfun_invoker_dialog_fragment, container, false)

    private val Parameter.displayName
        get() = (name?.capitalize() ?: "Unknown").splitCamelCase()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        try {
            performOnViewCreated()
        } catch (t: Throwable) {
            errorHandler.onError(
                functionItem,
                t,
                "View Creation Failure",
                "Something when wrong when trying to create the invocation dialog view."
            )
            Handler().post { dismissAllowingStateLoss() }
        }
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun performOnViewCreated() {
        val category = functionItem.category.name
        val group = functionItem.category.group
        subtitleText.text = if (group == null) category else "$category - $group"
        titleText.text = functionItem.name

        val kFun = functionItem.function.method.kotlinFunction
                ?: throw RuntimeException(
                    """
                        Could not get Kotlin Function equivalent for ${functionItem.function.method}
                            Try reloading the DevFun definitions:
                            DevFun > Misc > Reload Item Definitions""".trimIndent()
                )
        functionSignatureText.text = kFun.toString()

        var canExecute = true
        kFun.parameters.asSequence()
            .filter { it.kind == KParameter.Kind.VALUE }
            .map { SimpleParameter(it) }
            .forEach {
                val paramView = layoutInflater.inflate(R.layout.df_devfun_parameter, inputsList, false) as ViewGroup
                paramView as InvokeParameterView
                paramView.label = it.displayName

                if (it.clazz.isInjectable) {
                    paramView.view = devFun.viewFactories[InjectedParameterView::class]?.inflate(layoutInflater, inputsList)?.apply {
                        if (this is InjectedParameterView) {
                            value = it.clazz
                            paramView.attributes = getText(R.string.df_devfun_injected)
                        }
                    }
                } else {
                    val inputViewFactory = devFun.parameterViewFactories[it]
                    if (inputViewFactory != null) {
                        paramView.view = inputViewFactory.inflate(layoutInflater, inputsList).apply {
                            if (this is WithValue<*>) {
                                it.annotations.getTypeOrNull<From> {
                                    @Suppress("UNCHECKED_CAST")
                                    (this as WithValue<Any>).value = devFun.instanceOf(it.source).value
                                }
                            }
                        }
                    } else {
                        canExecute = false
                        devFun.viewFactories[ErrorParameterView::class]?.inflate(layoutInflater, inputsList)?.apply {
                            this as ErrorParameterView
                            this.value = it.clazz
                            paramView.attributes = getText(R.string.df_devfun_missing)
                            paramView.view = this
                        }
                    }
                }

                inputsList.addView(paramView)
            }

        cancelButton.setOnClickListener { dialog.dismiss() }
        executeButton.setOnClickListener {
            try {
                fun View?.getValue(): Any {
                    return when (this) {
                        is InjectedParameterView -> devFun.instanceOf(value)
                        is InvokeParameterView -> view.getValue()
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

                log.d { "Invoke $functionItem\nwith params: $params" }
                functionItem.function.invoke(functionItem.receiverInstance(devFun.instanceProviders), params)
            } catch (de: DebugException) {
                throw de
            } catch (t: Throwable) {
                errorHandler.onError(functionItem, t, "Invocation Failure", "Something went wrong when trying to execute requested method.")
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

    private val <T : Any> KClass<T>.isInjectable get() = devFun.tryGetInstanceOf(this) != null
}

private class SimpleParameter(override val kParameter: KParameter) : Parameter
