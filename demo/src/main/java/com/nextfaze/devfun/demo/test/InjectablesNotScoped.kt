package com.nextfaze.devfun.demo.test

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.nextfaze.devfun.annotations.DeveloperFunction
import javax.inject.Inject

@TestCat
class InjectableNotScopedWithZeroArgsConstructor @Inject constructor() {
    @DeveloperFunction
    fun validateSelf(activity: Activity, self: InjectableNotScopedWithZeroArgsConstructor): AlertDialog =
        AlertDialog.Builder(activity)
            .setMessage(
                """this=$this($activity)
                    |self=$self
                    |this===self: ${this === self}""".trimMargin()
            )
            .show()
}

@TestCat
class InjectableNotScopedWithMultipleArgsConstructor @Inject constructor(private val context: Context) {
    @DeveloperFunction
    fun validateSelf(activity: Activity, self: InjectableNotScopedWithMultipleArgsConstructor): AlertDialog =
        AlertDialog.Builder(activity)
            .setMessage(
                """this=$this($context)
                    |self=$self
                    |this===self: ${this === self}""".trimMargin()
            )
            .show()
}
