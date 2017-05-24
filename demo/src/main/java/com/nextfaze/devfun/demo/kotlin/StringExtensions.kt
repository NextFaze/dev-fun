package com.nextfaze.devfun.demo.kotlin

import android.text.SpannableStringBuilder

operator fun CharSequence?.plus(other: CharSequence?): CharSequence = SpannableStringBuilder(this ?: "").append(other ?: "")
