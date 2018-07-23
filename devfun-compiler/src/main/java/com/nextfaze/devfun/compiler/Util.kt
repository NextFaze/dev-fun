package com.nextfaze.devfun.compiler


/** Same as `Iterable.joinToString` but returns and empty string if [List] is empty. (i.e. wont include [prefix] or [postfix]). */
internal fun <T> List<T>.joiner(
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    limit: Int = -1,
    truncated: CharSequence = "...",
    transform: ((T) -> CharSequence)? = null
) =
    when {
        this.isEmpty() -> ""
        else -> joinTo(StringBuilder(), separator, prefix, postfix, limit, truncated, transform).toString()
    }
