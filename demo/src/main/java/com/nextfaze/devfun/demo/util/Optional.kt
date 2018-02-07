package com.nextfaze.devfun.demo.util

import com.nextfaze.devfun.demo.util.Optional.Some
import io.reactivex.Observable

sealed class Optional<out T : Any> {
    abstract val isPresent: Boolean

    object None : Optional<Nothing>() {
        override val isPresent: Boolean = false
    }

    data class Some<out T : Any>(val value: T) : Optional<T>() {
        override val isPresent: Boolean = true
    }
}

val <T : Any> Optional<T>.value: T?
    get() = when (this) {
        is Optional.Some -> value
        else -> null
    }

fun <T : Any> T?.toOptional(): Optional<T> = this?.let(::Some) ?: Optional.None

fun <T : Any> Observable<Optional<T>>.filterPresent(): Observable<T> = ofType<Optional.Some<T>>().map { it.value }

private inline fun <reified R : Any> Observable<*>.ofType(): Observable<R> = ofType(R::class.java)
