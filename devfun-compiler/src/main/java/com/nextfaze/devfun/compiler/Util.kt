package com.nextfaze.devfun.compiler

import com.nextfaze.devfun.compiler.processing.KElements
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.joinToCode
import javax.lang.model.element.Element
import javax.lang.model.element.PackageElement
import javax.lang.model.util.Elements


/** Same as `Iterable.joinToString` but returns and empty string if [List] is empty. (i.e. wont include [prefix] or [postfix]). */
internal fun <T> Collection<T>.joiner(
    separator: CharSequence = ", ",
    prefix: CharSequence = "",
    postfix: CharSequence = "",
    limit: Int = -1,
    truncated: CharSequence = "...",
    ifEmpty: String = "",
    transform: ((T) -> CharSequence)? = null
) =
    when {
        isEmpty() -> ifEmpty
        else -> joinTo(StringBuilder(), separator, prefix, postfix, limit, truncated, transform).toString()
    }

internal fun List<CodeBlock>.joiner(
    prefix: CharSequence = "",
    suffix: CharSequence = ""
): CodeBlock = when {
    isEmpty() -> CodeBlock.of("")
    else -> joinToCode(prefix = prefix, suffix = suffix)
}

internal interface WithElements {
    val elements: Elements

    val Element.packageElement: PackageElement
        get() = if (this is KElements.ClassElement) elements.getPackageOf(element) else elements.getPackageOf(this)
}

internal inline fun <T : Any, V : Any> T.applyNotNull(value: V?, block: T.(V) -> Unit): T {
    if (value != null) {
        block(value)
    }
    return this
}
