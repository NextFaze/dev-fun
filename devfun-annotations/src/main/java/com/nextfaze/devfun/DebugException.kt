package com.nextfaze.devfun

/**
 * This will not be caught by the standard DevFun error handler.
 *
 * i.e. Under most conditions, if this is thrown it will crash your app.
 */
class DebugException(message: String = "Debug") : Throwable(message)
