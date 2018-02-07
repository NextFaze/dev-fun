package com.nextfaze.devfun.core

private val SPLIT_REGEX = Regex("(?<=[a-z0-9])(?=[A-Z])|[\\s]")

internal fun String.splitCamelCase() = this
    .replace('_', ' ')
    .split(SPLIT_REGEX)
    .map { it.trim().capitalize() }
    .filter(String::isNotBlank)
    .joinToString(" ")
