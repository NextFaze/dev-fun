package com.nextfaze.devfun.invoke

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.nextfaze.devfun.DebugException
import com.nextfaze.devfun.core.ActivityTracker
import com.nextfaze.devfun.core.DevFun
import com.nextfaze.devfun.core.resumedActivity
import com.nextfaze.devfun.error.ErrorHandler
import com.nextfaze.devfun.function.FunctionItem
import com.nextfaze.devfun.function.InvokeResult
import com.nextfaze.devfun.inject.Constructable
import com.nextfaze.devfun.inject.InstanceProvider
import com.nextfaze.devfun.internal.WithParameters
import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.internal.toSignatureString
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.jvm.kotlinFunction

/** Used to invoke a [FunctionItem] or [UiFunction] and automatically handles parameter injection and errors. */
interface Invoker {
    /**
     * Invokes a function item, using the invocation UI if needed.
     *
     * Any exceptions thrown during invocation should be caught and returned as an [InvokeResult] - exception for
     * [DebugException], which is intended to crash the app and should simply be re-thrown when encountered.
     *
     * @return The result of the invocation and/or any exception thrown (other than [DebugException]).
     *         If the result is `null` then the invocation is pending user interaction.
     */
    fun invoke(item: FunctionItem): InvokeResult?

    /**
     * Invokes a function using the invocation UI.
     *
     * @param function The function description.
     *
     * @return The result of the invocation and/or any exception thrown (other than [DebugException]).
     *         If the result is `null` then the invocation is pending user interaction.
     */
    fun invoke(function: UiFunction): InvokeResult?
}

@Constructable
internal class DefaultInvoker(
    context: Context,
    private val devFun: DevFun,
    private val activityTracker: ActivityTracker,
    private val errorHandler: ErrorHandler
) : Invoker {
    private val log = logger()

    private val application = context.applicationContext

    private data class SimpleInvokeResult(override val value: Any? = null, override val exception: Throwable? = null) : InvokeResult

    override fun invoke(item: FunctionItem): InvokeResult? {
        return try {
            doInvoke(item).apply {
                if (this == null) {
                    log.i { "Invocation of $item is pending user interaction..." }
                } else {
                    // todo show result dialog for non-null non-Unit return values?
                    val exception = exception
                    when (exception) {
                        null -> log.i { "Invocation of $item returned\n$value" }
                        else -> errorHandler.onError(
                            exception,
                            "Invocation Failure",
                            "Something went wrong when trying to invoke the method.",
                            item
                        )
                    }
                }
            }
        } catch (de: DebugException) {
            throw de
        } catch (t: Throwable) {
            errorHandler.onError(t, "Pre-invocation Failure", "Something went wrong when trying to detect/find type instances.", item)
            SimpleInvokeResult(exception = t)
        }
    }

    override fun invoke(function: UiFunction): InvokeResult? {
        val activity = activityTracker.resumedActivity
        when (activity) {
            is FragmentActivity -> InvokingDialogFragment.show(activity, function)
            else -> Toast.makeText(application, "Cannot show UI dialog without a resumed FragmentActivity.", Toast.LENGTH_SHORT).show()
        }
        return null
    }

    private fun doInvoke(item: FunctionItem): InvokeResult? {
        var haveAllInstances = true

        class Checker : InstanceProvider {
            override fun <T : Any> get(clazz: KClass<out T>): T? =
                when {
                    !haveAllInstances -> null
                    else ->
                        try {
                            devFun.instanceOf(clazz)
                        } catch (t: Throwable) {
                            haveAllInstances = false
                            null
                        }
                }
        }

        val instanceProvider = Checker()
        val receiver = item.receiverInstance(instanceProvider)

        val args = if (item.function !is WithParameters) {
            item.parameterInstances(instanceProvider)
        } else {
            (item.function as WithParameters).parameters.map {
                devFun.parameterProviders[it] ?: instanceProvider[it.type.classifier as KClass<*>]
            }
        }

        if (haveAllInstances) {
            return try {
                SimpleInvokeResult(value = item.invoke(receiver, args))
            } catch (de: DebugException) {
                throw de
            } catch (t: Throwable) {
                SimpleInvokeResult(exception = t)
            }
        } else {
            val group = item.group
            val category = item.category.name
            val kFun = item.function.method.kotlinFunction
                    ?: throw RuntimeException(
                        """
                        Could not get Kotlin Function equivalent for ${item.function.method}
                            Try reloading the DevFun definitions:
                            DevFun > Misc > Reload Item Definitions""".trimIndent()
                    )
            val parameters = kFun.parameters.filter { it.kind == KParameter.Kind.VALUE }.map(::NativeParameter)
            val invoke: SimpleInvoke = {
                log.d { "Invoke $item\nwith args: $it" }
                item.invoke(item.receiverInstance(devFun.instanceProviders), it)
            }

            val description = uiFunction(
                title = item.name,
                subtitle = if (group == null) category else "$category - $group",
                signature = kFun.toSignatureString(application.packageName),
                parameters = parameters,
                invoke = invoke
            )

            invoke(description)
        }

        return null
    }
}

private class NativeParameter(
    override val kParameter: KParameter
) : Parameter, WithKParameter {
    override val name = kParameter.name
    override val type: KClass<*> = kParameter.type.classifier as KClass<*>
    override val annotations = kParameter.annotations
}
