package com.nextfaze.devfun.invoke

import com.nextfaze.devfun.core.DebugException
import com.nextfaze.devfun.core.DevFun
import com.nextfaze.devfun.core.FunctionItem
import com.nextfaze.devfun.core.InvokeResult
import com.nextfaze.devfun.inject.ClassInstanceNotFoundException
import com.nextfaze.devfun.inject.Constructable
import com.nextfaze.devfun.inject.InstanceProvider
import com.nextfaze.devfun.internal.i
import com.nextfaze.devfun.internal.logger
import com.nextfaze.devfun.internal.w
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
internal class DefaultInvoker(private val devFun: DevFun) : Invoker {
    private val log = logger()

    private data class SimpleInvokeResult(override val value: Any? = null, override val exception: Throwable? = null) : InvokeResult

    override fun invoke(item: FunctionItem): InvokeResult? {
        return try {
            doInvoke(item).apply {
                if (this == null) {
                    log.i { "Invocation of $item is pending user interaction..." }
                } else {
                    // todo show result dialog for non-null non-Unit return values?
                    when (exception) {
                        null -> log.i { "Invocation of $item returned\n$value" }
                        else -> log.w(exception) { "Exception thrown during invocation of $item." }
                    }
                }
            }
        } catch (de: DebugException) {
            throw de
        } catch (t: Throwable) {
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
            try {
                return SimpleInvokeResult(value = item.function.invoke(receiver, args))
            } catch (de: DebugException) {
                throw de
            } catch (t: Throwable) {
                return SimpleInvokeResult(exception = t)
            }
        } else {
            return SimpleInvokeResult(exception = RuntimeException("Could not inject all instances!"))
        }
    }
}

internal fun <T : Any> DevFun.tryGetInstanceOf(clazz: KClass<T>) =
    try {
        instanceOf(clazz)
    } catch (ignore: ClassInstanceNotFoundException) {
        null
    }
