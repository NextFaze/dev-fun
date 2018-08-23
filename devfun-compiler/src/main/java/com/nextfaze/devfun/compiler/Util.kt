package com.nextfaze.devfun.compiler

import javax.lang.model.element.Element
import javax.lang.model.element.PackageElement
import javax.lang.model.util.Elements


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

internal interface WithElements {
    val elements: Elements

    val Element.packageElement: PackageElement
        get() = elements.getPackageOf(this)
}

inline fun <T> T.applyIf(predicate: Boolean, block: T.() -> Unit): T {
    if (predicate) {
        block(this)
    }
    return this
}
