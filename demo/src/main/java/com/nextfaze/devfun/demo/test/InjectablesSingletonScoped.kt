package com.nextfaze.devfun.demo.test

import android.content.Context
import com.nextfaze.devfun.annotations.DeveloperFunction
import dagger.Module
import dagger.Provides
import javax.inject.Inject
import javax.inject.Singleton

@Module
class SingletonScopedTestModule {
    @Provides
    @Singleton
    fun scoped() = InjectableSingletonScopedViaProvides()

    @Provides
    @Singleton
    fun scopedWithArgs(context: Context) = InjectableSingletonScopedViaProvidesWithArgs(context)
}

class InjectableSingletonScopedViaProvides {
    @DeveloperFunction
    fun validateSelf(self: InjectableSingletonScopedViaProvides) {
        println("this=$this")
        println("self=$self")
        println("this===self: ${this === self}")
    }
}

class InjectableSingletonScopedViaProvidesWithArgs @Inject constructor(private val context: Context) {
    @DeveloperFunction
    fun validateSelf(self: InjectableSingletonScopedViaProvidesWithArgs) {
        println("this=$this($context)")
        println("self=$self")
        println("this===self: ${this === self}")
    }
}

@Singleton
class InjectableSingletonScopedViaAnnotation @Inject constructor() {
    @DeveloperFunction
    fun validateSelf(self: InjectableSingletonScopedViaAnnotation) {
        println("this=$this")
        println("self=$self")
        println("this===self: ${this === self}")
    }
}

@Singleton
class InjectableSingletonScopedViaAnnotationWithArgs @Inject constructor(private val context: Context) {
    @DeveloperFunction
    fun validateSelf(self: InjectableSingletonScopedViaAnnotationWithArgs) {
        println("this=$this($context)")
        println("self=$self")
        println("this===self: ${this === self}")
    }
}
