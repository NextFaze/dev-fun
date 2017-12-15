package com.nextfaze.devfun.menu

import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import kotlin.reflect.KClass

internal val Fragment.defaultTag get() = this::class.java.defaultTag
internal val KClass<out Fragment>.defaultTag get() = java.defaultTag
internal val Class<out Fragment>.defaultTag get() = name

internal inline fun <reified T : Fragment> FragmentManager.find() = findFragmentByTag(T::class.defaultTag) as T?
internal inline fun <reified T : Fragment> FragmentManager.obtain(factory: () -> T) = find() ?: factory.invoke()
internal inline fun <reified T : Fragment> FragmentActivity.obtain(factory: () -> T) = supportFragmentManager.obtain(factory)

internal fun DialogFragment.show(fragmentManager: FragmentManager) = show(fragmentManager, defaultTag)
