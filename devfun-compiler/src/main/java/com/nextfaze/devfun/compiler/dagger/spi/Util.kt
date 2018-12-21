package com.nextfaze.devfun.compiler.dagger.spi

import java.util.Optional

internal val <T : Any> Optional<T>.valueOrNull: T?
    get() =
        when {
            isPresent -> get()
            else -> null
        }
