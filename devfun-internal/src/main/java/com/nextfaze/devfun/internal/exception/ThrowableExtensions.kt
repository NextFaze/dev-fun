package com.nextfaze.devfun.internal.exception

import java.io.PrintWriter
import java.io.StringWriter

val Throwable.stackTraceAsString get() = StringWriter().apply { printStackTrace(PrintWriter(this)) }.toString()
