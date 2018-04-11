package com.nextfaze.devfun.internal

import android.graphics.Typeface
import android.text.ParcelableSpan
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.text.style.TypefaceSpan
import android.text.style.UnderlineSpan

operator fun Appendable.plusAssign(charSequence: CharSequence) {
    append(charSequence)
}

operator fun SpannableStringBuilder.plusAssign(span: Pair<CharSequence, ParcelableSpan>) {
    val pos = length
    this += span.first
    setSpan(span.second, pos, pos + span.first.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
}

fun u(str: CharSequence) = str to UnderlineSpan()
fun bold(str: CharSequence) = str to StyleSpan(Typeface.BOLD)
fun pre(str: CharSequence) = str to TypefaceSpan("monospace")
