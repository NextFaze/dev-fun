package com.nextfaze.devfun.demo.test

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AlertDialog
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

@TestCat
class InjectableActivityScopedViaProvides {
    @DeveloperFunction
    fun validateSelf(activity: Activity, self: InjectableActivityScopedViaProvides): AlertDialog =
        AlertDialog.Builder(activity)
            .setMessage(
                """this=$this($activity)
                    |self=$self
                    |this===self: ${this === self}""".trimMargin()
            )
            .show()
}

@TestCat
class InjectableActivityScopedViaProvidesWithArgs(private val context: Context) {
    @DeveloperFunction
    fun validateSelf(activity: Activity, self: InjectableActivityScopedViaProvidesWithArgs): AlertDialog =
        AlertDialog.Builder(activity)
            .setMessage(
                """this=$this($context)
                    |self=$self
                    |this===self: ${this === self}""".trimMargin()
            )
            .show()
}

@ActivityScope
@TestCat
class InjectableActivityScopedViaAnnotation @Inject constructor() {
    @DeveloperFunction
    fun validateSelf(activity: Activity, self: InjectableActivityScopedViaAnnotation): AlertDialog =
        AlertDialog.Builder(activity)
            .setMessage(
                """this=$this($activity)
                    |self=$self
                    |this===self: ${this === self}""".trimMargin()
            )
            .show()
}

// Dagger treats non-singleton no-arg types differently (doesn't use a Provides since it can be instantiated on the fly)
@ActivityScope
@TestCat
class InjectableActivityScopedViaAnnotationWithArgs @Inject constructor(private val context: Context) {
    @DeveloperFunction
    fun validateSelf(activity: Activity, self: InjectableActivityScopedViaAnnotationWithArgs): AlertDialog =
        AlertDialog.Builder(activity)
            .setMessage(
                """this=$this($context)
                    |self=$self
                    |this===self: ${this === self}""".trimMargin()
            )
            .show()
}
