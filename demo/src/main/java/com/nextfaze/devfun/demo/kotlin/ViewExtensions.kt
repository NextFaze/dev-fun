package com.nextfaze.devfun.demo.kotlin

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.ViewPropertyAnimator
import android.widget.EditText
import android.widget.TextView

//
// View
//

inline var View.enabled: Boolean
    get() = isEnabled
    set(value) {
        isEnabled = value
    }

inline var View.visible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

//
// TextView
//

fun TextView.clearError() {
    error = null
}

//
// EditText
//

fun EditText.isBlank() = text.isBlank()

//
// ViewPropertyAnimator
//

inline fun ViewPropertyAnimator.onAnimationEnd(crossinline body: () -> Unit): ViewPropertyAnimator =
    setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) = body.invoke()
    })
