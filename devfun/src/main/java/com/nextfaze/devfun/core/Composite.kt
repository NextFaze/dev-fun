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
    private val components = ArrayDeque<T>()

    internal fun clear() = components.clear()

    override fun iterator(): Iterator<T> = components.descendingIterator()

    override operator fun plusAssign(other: T) {
        components += other
    }

    override operator fun minusAssign(other: T) {
        components -= other
    }
}
