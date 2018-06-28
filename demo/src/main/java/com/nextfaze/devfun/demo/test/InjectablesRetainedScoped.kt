package com.nextfaze.devfun.demo.test

import android.app.Activity
import android.content.Context
import android.support.v7.app.AlertDialog
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

@TestCat
class InjectableRetainedScopedViaProvides {
    @DeveloperFunction
    fun validateSelf(activity: Activity, self: InjectableRetainedScopedViaProvides): AlertDialog =
        AlertDialog.Builder(activity)
            .setMessage(
                """this=$this($activity)
                    |self=$self
                    |this===self: ${this === self}""".trimMargin()
            )
            .show()
}

@TestCat
class InjectableRetainedScopedViaProvidesWithArgs(private val context: Context) {
    @DeveloperFunction
    fun validateSelf(activity: Activity, self: InjectableRetainedScopedViaProvidesWithArgs): AlertDialog =
        AlertDialog.Builder(activity)
            .setMessage(
                """this=$this($context)
                    |self=$self
                    |this===self: ${this === self}""".trimMargin()
            )
            .show()
}

@RetainedScope
@TestCat
class InjectableRetainedScopedViaAnnotation @Inject constructor() {
    @DeveloperFunction
    fun validateSelf(activity: Activity, self: InjectableRetainedScopedViaAnnotation): AlertDialog =
        AlertDialog.Builder(activity)
            .setMessage(
                """this=$this($activity)
                    |self=$self
                    |this===self: ${this === self}""".trimMargin()
            )
            .show()
}

// Dagger treats non-singleton no-arg types differently (doesn't use a Provides since it can be instantiated on the fly)
@RetainedScope
@TestCat
class InjectableRetainedScopedViaAnnotationWithArgs @Inject constructor(private val context: Context) {
    @DeveloperFunction
    fun validateSelf(activity: Activity, self: InjectableRetainedScopedViaAnnotationWithArgs): AlertDialog =
        AlertDialog.Builder(activity)
            .setMessage(
                """this=$this($context)
                    |self=$self
                    |this===self: ${this === self}""".trimMargin()
            )
            .show()
}
