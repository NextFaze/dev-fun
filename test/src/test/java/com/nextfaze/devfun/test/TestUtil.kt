package com.nextfaze.devfun.test

import kotlin.reflect.KClass

annotation class TestUtil

@Suppress("unused")
internal val KClass<*>.srcLocation
    get() = java.protectionDomain?.codeSource?.location?.file
