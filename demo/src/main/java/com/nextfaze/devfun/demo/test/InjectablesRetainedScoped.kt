package com.nextfaze.devfun.demo.test

import android.content.Context
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.demo.inject.RetainedScope
import dagger.Module
import dagger.Provides
import javax.inject.Inject

@Module
class RetainedScopedTestModule {
    @Provides
    @RetainedScope
    fun scoped() = InjectableRetainedScopedViaProvides()

    @Provides
    @RetainedScope
    fun scopedWithArgs(context: Context) = InjectableRetainedScopedViaProvidesWithArgs(context)
}

class InjectableRetainedScopedViaProvides {
    @DeveloperFunction
    fun validateSelf(self: InjectableRetainedScopedViaProvides) {
        println("this=$this")
        println("self=$self")
        println("this===self: ${this === self}")
    }
}

class InjectableRetainedScopedViaProvidesWithArgs(private val context: Context) {
    @DeveloperFunction
    fun validateSelf(self: InjectableRetainedScopedViaProvidesWithArgs) {
        println("this=$this($context)")
        println("self=$self")
        println("this===self: ${this === self}")
    }
}

@RetainedScope
class InjectableRetainedScopedViaAnnotation @Inject constructor() {
    @DeveloperFunction
    fun validateSelf(self: InjectableRetainedScopedViaAnnotation) {
        println("this=$this")
        println("self=$self")
        println("this===self: ${this === self}")
    }
}

// Dagger treats non-singleton no-arg types differently (doesn't use a Provides since it can be instantiated on the fly)
@RetainedScope
class InjectableRetainedScopedViaAnnotationWithArgs @Inject constructor(private val context: Context) {
    @DeveloperFunction
    fun validateSelf(self: InjectableRetainedScopedViaAnnotationWithArgs) {
        println("this=$this($context)")
        println("self=$self")
        println("this===self: ${this === self}")
    }
}
