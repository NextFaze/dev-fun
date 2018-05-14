package com.nextfaze.devfun.internal.string

import android.graphics.Typeface
import android.support.annotation.ColorInt
import android.text.ParcelableSpan
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.*

typealias Span = Pair<CharSequence, List<ParcelableSpan>>

operator fun Appendable.plusAssign(charSequence: CharSequence) {
    append(charSequence)
}

operator fun SpannableStringBuilder.plusAssign(span: Span) {
    val pos = length
    this += span.first
    span.second.forEach {
        setSpan(it, pos, pos + span.first.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
    }
}

fun u(str: CharSequence) = str to listOf(UnderlineSpan())
fun b(str: CharSequence) = str to listOf(StyleSpan(Typeface.BOLD))
fun i(str: CharSequence) = str to listOf(StyleSpan(Typeface.ITALIC))
fun i(span: Span) = span.first to span.second + StyleSpan(Typeface.ITALIC)
fun pre(str: CharSequence) = str to listOf(TypefaceSpan("monospace"))
fun scale(str: CharSequence, scale: Float) = str to listOf(RelativeSizeSpan(scale))
fun scale(span: Span, scale: Float) = span.first to span.second + RelativeSizeSpan(scale)
fun color(span: Span, @ColorInt color: Int) = span.first to span.second + ForegroundColorSpan(color)
