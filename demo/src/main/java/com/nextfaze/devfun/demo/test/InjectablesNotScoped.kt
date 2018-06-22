package com.nextfaze.devfun.demo.test

import android.content.Context
import com.nextfaze.devfun.annotations.DeveloperFunction
import javax.inject.Inject

class InjectableNotScopedWithZeroArgsConstructor @Inject constructor() {
    @DeveloperFunction
    fun validateSelf(self: InjectableNotScopedWithZeroArgsConstructor) {
        println("this=$this")
        println("self=$self")
        println("this===self: ${this === self}")
    }
}

class InjectableNotScopedWithMultipleArgsConstructor @Inject constructor(private val context: Context) {
    @DeveloperFunction
    fun validateSelf(self: InjectableNotScopedWithMultipleArgsConstructor) {
        println("this=$this($context)")
        println("self=$self")
        println("this===self: ${this === self}")
    }
}
