@file:Suppress("unused")

package com.nextfaze.devfun.internal

import android.support.annotation.RestrictTo
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Controls trace-level logging. Disabled (`false`) by default.
 *
 * Enabled automatically when library is a `-SNAPSHOT` build.
 */
var devFunVerbose = false

/**
 * Creates a new logger instance using the containing class' qualified name.
 *
 * @internal Intended for use across DevFun libraries.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
inline fun <reified T : Any> T.logger(): Logger = LoggerFactory.getLogger(T::class.java.name)

/**
 * Creates a new logger instance using [name].
 *
 * @internal Intended for use across DevFun libraries.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun logger(name: String): Logger = LoggerFactory.getLogger(name)

/**
 * Log `trace`.
 *
 * @internal Intended for use across DevFun libraries.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
inline fun Logger.t(t: Throwable? = null, predicate: Boolean = true, body: () -> String) {
    if (devFunVerbose && predicate && isTraceEnabled) {
        trace(body.invoke(), t)
    }
}

/**
 * Log `debug`.
 *
 * @internal Intended for use across DevFun libraries.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
inline fun Logger.d(t: Throwable? = null, predicate: Boolean = true, body: () -> String) {
    if (predicate && isDebugEnabled) {
        debug(body.invoke(), t)
    }
}

/**
 * Log `info`.
 *
 * @internal Intended for use across DevFun libraries.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
inline fun Logger.i(t: Throwable? = null, predicate: Boolean = true, body: () -> String) {
    if (predicate && isInfoEnabled) {
        info(body.invoke(), t)
    }
}

/**
 * Log `warn`.
 *
 * @internal Intended for use across DevFun libraries.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
inline fun Logger.w(t: Throwable? = null, predicate: Boolean = true, body: () -> String) {
    if (predicate && isWarnEnabled) {
        warn(body.invoke(), t)
    }
}

/**
 * Log `error`.
 *
 * @internal Intended for use across DevFun libraries.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
inline fun Logger.e(t: Throwable? = null, predicate: Boolean = true, body: () -> String) {
    if (predicate && isErrorEnabled) {
        error(body.invoke(), t)
    }
}
