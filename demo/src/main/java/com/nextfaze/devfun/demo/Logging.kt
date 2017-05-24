@file:Suppress("unused")

package com.nextfaze.devfun.demo

import org.slf4j.Logger
import org.slf4j.LoggerFactory

inline fun <reified T : Any> T.logger(): Logger = LoggerFactory.getLogger(T::class.java.name)

fun logger(name: String): Logger = LoggerFactory.getLogger(name)

inline fun Logger.t(t: Throwable? = null, predicate: Boolean = true, body: () -> String) {
    if (predicate && isTraceEnabled) {
        trace(body.invoke(), t)
    }
}

inline fun Logger.d(t: Throwable? = null, predicate: Boolean = true, body: () -> String) {
    if (predicate && isDebugEnabled) {
        debug(body.invoke(), t)
    }
}

inline fun Logger.i(t: Throwable? = null, predicate: Boolean = true, body: () -> String) {
    if (predicate && isInfoEnabled) {
        info(body.invoke(), t)
    }
}

inline fun Logger.w(t: Throwable? = null, predicate: Boolean = true, body: () -> String) {
    if (predicate && isWarnEnabled) {
        warn(body.invoke(), t)
    }
}

inline fun Logger.e(t: Throwable? = null, predicate: Boolean = true, body: () -> String) {
    if (predicate && isErrorEnabled) {
        error(body.invoke(), t)
    }
}
