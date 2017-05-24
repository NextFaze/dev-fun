package com.nextfaze.devfun.stetho

import java.io.PrintWriter
import java.io.StringWriter

internal val Throwable.stackTraceAsString get() = StringWriter().apply { printStackTrace(PrintWriter(this)) }.toString()
