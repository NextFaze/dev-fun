package com.nextfaze.devfun

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import kotlin.reflect.KClass

internal abstract class BaseDialogFragment : DialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onDestroyView() {
        dialog?.takeIf { retainInstance }?.setDismissMessage(null) // Fix http:///issuetracker.google.com/17423
        super.onDestroyView()
    }
}

private inline fun <reified T : Fragment> FragmentManager.find() = findFragmentByTag(T::class.defaultTag) as T?
private inline fun <reified T : Fragment> FragmentManager.obtain(factory: () -> T) = find() ?: factory.invoke()
internal inline fun <reified T : Fragment> FragmentActivity.obtain(factory: () -> T) = supportFragmentManager.obtain(factory)

internal fun DialogFragment.show(fragmentManager: FragmentManager) = show(fragmentManager, defaultTag)

private val Fragment.defaultTag: String get() = this::class.java.defaultTag
private val KClass<out Fragment>.defaultTag: String get() = java.defaultTag
private val Class<out Fragment>.defaultTag: String get() = name
