package com.nextfaze.devfun.inject.dagger2

private val isDagger211Minus by lazy {
    try {
        // was added in Dagger 2.12
        Class.forName("dagger.internal.SetBuilder")
        false
    } catch (t: ClassNotFoundException) {
        true
    }
}

private val isDagger213Plus by lazy {
    try {
        // was added in Dagger 2.13
        Class.forName("dagger.internal.MemoizedSentinel")
        true
    } catch (t: ClassNotFoundException) {
        false
    }
}

internal val isDagger212 by lazy { !isDagger211Minus && !isDagger213Plus }
