package com.nextfaze.devfun.demo

import com.google.android.material.textfield.TextInputEditText

val TextInputEditText.value get() = text?.trim() ?: ""
