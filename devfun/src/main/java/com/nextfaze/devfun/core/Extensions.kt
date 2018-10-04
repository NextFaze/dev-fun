package com.nextfaze.devfun.core

import com.nextfaze.devfun.DebugException
import com.nextfaze.devfun.function.FunctionItem
import com.nextfaze.devfun.function.InvokeResult
import com.nextfaze.devfun.invoke.Invoker

/**
 * Convenience function for invoking a [FunctionItem] using the current [devFun] instance.
 *
 * Invocation exceptions (excluding [DebugException] will be wrapped to [InvokeResult.exception].
 *
 * Invocations pending user interaction will return `null`.
 *
 * @return See [Invoker.invoke].
 */
fun FunctionItem.call(invoker: Invoker = devFun.get()): InvokeResult? = invoker.invoke(this)
