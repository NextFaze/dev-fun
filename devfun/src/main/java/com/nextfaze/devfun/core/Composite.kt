package com.nextfaze.devfun.core

import java.util.ArrayDeque

interface Composite<T : Any> : Iterable<T> {
    operator fun plusAssign(other: T)
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
