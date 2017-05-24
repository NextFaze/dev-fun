package com.nextfaze.devfun.compiler

import java.io.File
import java.io.PrintWriter
import java.io.StringWriter


/**
 * Same as `Iterable.joinToString` but returns and empty string if [List] is empty. (i.e. wont include [prefix] or [postfix])
 */
internal fun <T> List<T>.joiner(separator: CharSequence = ", ",
                                prefix: CharSequence = "",
                                postfix: CharSequence = "",
                                limit: Int = -1,
                                truncated: CharSequence = "...",
                                transform: ((T) -> CharSequence)? = null) =
        when {
            this.isEmpty() -> ""
            else -> joinTo(StringBuilder(), separator, prefix, postfix, limit, truncated, transform).toString()
        }

internal val Throwable.stackTraceAsString get() = StringWriter().apply { printStackTrace(PrintWriter(this)) }.toString()

internal inline fun File.filterRecursively(predicate: (File) -> Boolean) = filterRecursivelyTo(ArrayList<File>(), predicate)

internal inline fun <C : MutableCollection<in File>> File.filterRecursivelyTo(destination: C, predicate: (File) -> Boolean): C {
    val directories = ArrayList<File>()
    if (isDirectory) {
        directories.add(this)
    } else {
        if (predicate(this)) destination.add(this)
        return destination
    }

    var i = 0
    while (i < directories.size) {
        directories[i++].listFiles().orEmpty().forEach {
            if (predicate(it)) {
                destination.add(it)
            }
            if (it.isDirectory) {
                directories.add(it)
            }
        }
    }

    return destination
}

internal inline fun <T, R> T.runIf(condition: Boolean, block: T.() -> R) {
    if (condition) block()
}
