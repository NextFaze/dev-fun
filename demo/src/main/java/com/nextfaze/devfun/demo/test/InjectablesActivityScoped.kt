package com.nextfaze.devfun.demo.test

import android.content.Context
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.demo.inject.ActivityScope
import dagger.Module
import dagger.Provides
import javax.inject.Inject

@Module
class ActivityScopedTestModule {
    @Provides
    @ActivityScope
    fun scoped() = InjectableActivityScopedViaProvides()

    @Provides
    @ActivityScope
    fun scopedWithArgs(context: Context) = InjectableActivityScopedViaProvidesWithArgs(context)
}

class InjectableActivityScopedViaProvides {
    @DeveloperFunction
    fun validateSelf(self: InjectableActivityScopedViaProvides) {
        println("this=$this")
        println("self=$self")
        println("this===self: ${this === self}")
    }
}

class InjectableActivityScopedViaProvidesWithArgs(private val context: Context) {
    @DeveloperFunction
    fun validateSelf(self: InjectableActivityScopedViaProvidesWithArgs) {
        println("this=$this($context)")
        println("self=$self")
        println("this===self: ${this === self}")
    }
}

@ActivityScope
class InjectableActivityScopedViaAnnotation @Inject constructor() {
    @DeveloperFunction
    fun validateSelf(self: InjectableActivityScopedViaAnnotation) {
        println("this=$this")
        println("self=$self")
        println("this===self: ${this === self}")
    }
}

// Dagger treats non-singleton no-arg types differently (doesn't use a Provides since it can be instantiated on the fly)
@ActivityScope
class InjectableActivityScopedViaAnnotationWithArgs @Inject constructor(private val context: Context) {
    @DeveloperFunction
    fun validateSelf(self: InjectableActivityScopedViaAnnotationWithArgs) {
        println("this=$this($context)")
        println("self=$self")
        println("this===self: ${this === self}")
    }
}
