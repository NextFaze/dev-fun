package com.nextfaze.devfun.internal.string

import kotlin.reflect.KClass

private val SPLIT_REGEX = Regex("(?<=[a-z0-9])(?=[A-Z])|[\\s]")

fun String.splitCamelCase() = this
    .replace('_', ' ')
    .split(SPLIT_REGEX)
    .map { it.trim().capitalize() }
    .filter(String::isNotBlank)
    .joinToString(" ")

inline val KClass<*>.splitSimpleName get() = this.java.simpleName.splitCamelCase()
