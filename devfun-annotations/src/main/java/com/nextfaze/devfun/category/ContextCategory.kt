package com.nextfaze.devfun.category

import com.nextfaze.devfun.DeveloperAnnotation

const val CONTEXT_CAT_NAME = "Context"
const val CONTEXT_CAT_ORDER = -10_000

/**
 * [DeveloperCategory] annotation used to declare the "Context" category.
 *
 * By default functions declared within some sort of Android "scope" (Activity/Fragment/View/etc) will have this category.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
@DeveloperAnnotation(developerCategory = true)
annotation class ContextCategory(
    val value: String = CONTEXT_CAT_NAME,
    val group: String = "",
    val order: Int = CONTEXT_CAT_ORDER
)
