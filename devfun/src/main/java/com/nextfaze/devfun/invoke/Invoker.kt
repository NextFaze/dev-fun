package com.nextfaze.devfun.invoke

import com.nextfaze.devfun.core.DebugException
import com.nextfaze.devfun.core.DevFun
import com.nextfaze.devfun.core.FunctionItem
import com.nextfaze.devfun.core.InvokeResult
import com.nextfaze.devfun.error.ErrorHandler
import com.nextfaze.devfun.inject.ClassInstanceNotFoundException
import com.nextfaze.devfun.inject.Constructable
import com.nextfaze.devfun.inject.InstanceProvider
import com.nextfaze.devfun.internal.*
import kotlin.reflect.KClass

/** Used to invoke a [FunctionItem] and automatically handles parameter injection and errors. */
interface Invoker {
    /**
     * Invokes the provided [FunctionItem].
     *
     * Any exceptions thrown during invocation should be caught and returned as an [InvokeResult] - exception for
     * [DebugException], which is intended to crash the app and should simply be re-thrown when encountered.
     *
     * @return The result of the invocation and/or any exception thrown (other than [DebugException]).
     *         If the result is `null` then the invocation is pending user interaction.
     */
    fun invoke(item: FunctionItem): InvokeResult?
}

@Constructable
internal class DefaultInvoker(private val devFun: DevFun, private val errorHandler: ErrorHandler) : Invoker {
    private val log = logger()

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

    private fun doInvoke(item: FunctionItem): InvokeResult? {
        var haveAllInstances = true

        class Checker : InstanceProvider {
            override fun <T : Any> get(clazz: KClass<out T>): T? {
                return devFun.tryGetInstanceOf(clazz).also {
                    if (it == null) {
                        haveAllInstances = false
                    }
                }
            }
        }

        val instanceProvider = Checker()
        val receiver = item.receiverInstance(instanceProvider)
        val args = item.parameterInstances(instanceProvider)

        if (haveAllInstances) {
            return try {
                SimpleInvokeResult(value = item.function.invoke(receiver, args))
            } catch (de: DebugException) {
                throw de
            } catch (t: Throwable) {
                SimpleInvokeResult(exception = t)
            }
        } else {
            InvokingDialogFragment.show(devFun.get(), item)
        }

        return null
    }
}

internal fun <T : Any> DevFun.tryGetInstanceOf(clazz: KClass<T>) =
    try {
        instanceOf(clazz)
    } catch (ignore: ClassInstanceNotFoundException) {
        null
    }
