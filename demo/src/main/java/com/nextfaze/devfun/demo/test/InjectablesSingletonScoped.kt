package com.nextfaze.devfun.demo.test

import android.app.Activity
import android.content.Context
import android.support.v7.app.AlertDialog
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

@TestCat
class InjectableSingletonScopedViaProvides {
    @DeveloperFunction
    fun validateSelf(activity: Activity, self: InjectableSingletonScopedViaProvides): AlertDialog =
        AlertDialog.Builder(activity)
            .setMessage(
                """this=$this($activity)
                    |self=$self
                    |this===self: ${this === self}""".trimMargin()
            )
            .show()
}

@TestCat
class InjectableSingletonScopedViaProvidesWithArgs @Inject constructor(private val context: Context) {
    @DeveloperFunction
    fun validateSelf(activity: Activity, self: InjectableSingletonScopedViaProvidesWithArgs): AlertDialog =
        AlertDialog.Builder(activity)
            .setMessage(
                """this=$this($context)
                    |self=$self
                    |this===self: ${this === self}""".trimMargin()
            )
            .show()
}

@Singleton
@TestCat
class InjectableSingletonScopedViaAnnotation @Inject constructor() {
    @DeveloperFunction
    fun validateSelf(activity: Activity, self: InjectableSingletonScopedViaAnnotation): AlertDialog =
        AlertDialog.Builder(activity)
            .setMessage(
                """this=$this($activity)
                    |self=$self
                    |this===self: ${this === self}""".trimMargin()
            )
            .show()
}

@Singleton
@TestCat
class InjectableSingletonScopedViaAnnotationWithArgs @Inject constructor(private val context: Context) {
    @DeveloperFunction
    fun validateSelf(activity: Activity, self: InjectableSingletonScopedViaAnnotationWithArgs): AlertDialog =
        AlertDialog.Builder(activity)
            .setMessage(
                """this=$this($context)
                    |self=$self
                    |this===self: ${this === self}""".trimMargin()
            )
            .show()
}
