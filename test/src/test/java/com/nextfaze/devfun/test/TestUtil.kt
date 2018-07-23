package com.nextfaze.devfun.test

import kotlin.reflect.KClass

annotation class TestUtil

internal val KClass<*>.srcLocation get() = java.protectionDomain.codeSource?.location?.file
