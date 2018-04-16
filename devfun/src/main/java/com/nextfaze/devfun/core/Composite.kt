package com.nextfaze.devfun.core

import java.util.ArrayDeque

/**
 * Use by providers to facilitate user provided types [T] to the composting provider.
 *
 * In general additions are checked in reverse order. i.e. newest added are checked first.
 *
 * Users should remove their type when disposed of or out of scope.
 */
interface Composite<T : Any> : Iterable<T> {
    /** Add to this [Composite]. */
    operator fun plusAssign(other: T)

    /** Remove from this [Composite] */
    operator fun minusAssign(other: T)
}

internal abstract class Composited<T : Any> : Composite<T> {
    private var components = ArrayDeque<T>()

    internal fun clear() = components.clear()

    override fun iterator(): MutableIterator<T> = components.descendingIterator()

    internal fun remove(other: T) {
        components = components.clone().also { it.remove(other) }
    }

    override operator fun plusAssign(other: T) {
        components.add(other)
    }

    override operator fun minusAssign(other: T) {
        components.remove(other)
    }
}
