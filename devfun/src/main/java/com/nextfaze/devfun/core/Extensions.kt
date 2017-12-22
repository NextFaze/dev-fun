package com.nextfaze.devfun.core

import com.nextfaze.devfun.inject.InstanceProvider
import com.nextfaze.devfun.internal.*
import org.slf4j.Logger

/**
 * Convenience function for invoking a [FunctionItem] using the current [devFun] instance.
 *
 * Invocation exceptions (excluding [DebugException] will be in [InvokeResult.exception].
 */
fun FunctionItem.call(instanceProvider: InstanceProvider = devFun.instanceProviders) = invoke(instanceProvider, args)

/**
 * Convenience function to quickly log the result of a [FunctionItem.invoke].
 *
 * @return `this` for chaining.
 */
fun InvokeResult.log(logger: Logger = log, title: CharSequence = "Invocation returned"): InvokeResult {
    when (exception) {
        null -> logger.i { "$title\n$value" }
        else -> logger.w(exception) { "Exception thrown during invocation." }
    }
    return this
}

/**
 * Convenience function for invoking and logging a [FunctionItem.invoke].
 *
 * This is equivalent to [FunctionItem.call] and [InvokeResult.log].
 */
fun FunctionItem.callAndLog(instanceProvider: InstanceProvider = devFun.instanceProviders, logger: Logger = log)
        = this.call(instanceProvider).log(logger, this.name)

private val log = logger("com.nextfaze.devfun.core.InvokeResult")
