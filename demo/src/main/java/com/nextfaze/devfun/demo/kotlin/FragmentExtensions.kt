package com.nextfaze.devfun.demo.kotlin

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import kotlin.reflect.KClass

@Suppress("unused")
val Fragment.defaultTag: String
    get() = this::class.java.defaultTag

val KClass<out Fragment>.defaultTag: String
    get() = java.defaultTag

val Class<out Fragment>.defaultTag: String
    get() = name

inline fun <reified T : Fragment> FragmentActivity.findOrCreate(factory: () -> T): T {
    return supportFragmentManager.findFragmentByTag(T::class.defaultTag) as T? ?: factory.invoke()
}
