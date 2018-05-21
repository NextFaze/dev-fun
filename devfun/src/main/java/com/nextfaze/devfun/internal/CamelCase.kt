package com.nextfaze.devfun.internal

import kotlin.reflect.KClass

private val SPLIT_REGEX = Regex("(?<=[a-z0-9])(?=[A-Z])|[\\s]")

fun String.splitCamelCase() = this
    .replace('_', ' ')
    .split(SPLIT_REGEX)
    .map { it.trim().capitalize() }
    .filter(String::isNotBlank)
    .joinToString(" ")

val KClass<*>.splitSimpleName get() = nameCache.getOrPut(this) { java.simpleName.splitCamelCase() }

private val nameCache = mutableMapOf<KClass<*>, String>()
