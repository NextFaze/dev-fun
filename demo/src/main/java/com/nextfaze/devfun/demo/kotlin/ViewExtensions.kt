package com.nextfaze.devfun.demo.kotlin

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.ViewPropertyAnimator
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.TextView

//
// View
//

inline var View.enabled: Boolean
    get() = this.isEnabled
    set(value) {
        this.isEnabled = value
    }

inline var View.visible: Boolean
    get() = this.visibility == View.VISIBLE
    set(value) {
        this.visibility = if (value) View.VISIBLE else View.GONE
    }

//
// TextView
//

fun TextView.clearError() {
    this.error = null
}

//
// EditText
//

fun EditText.isBlank() = this.text.isBlank()

//
// ViewPropertyAnimator
//

inline fun ViewPropertyAnimator.onAnimationEnd(crossinline body: () -> Unit): ViewPropertyAnimator =
        this.setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) = body.invoke()
        })
