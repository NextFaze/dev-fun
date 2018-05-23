package com.nextfaze.devfun.internal.android

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatDialogFragment
import kotlin.reflect.KClass

abstract class BaseDialogFragment : AppCompatDialogFragment() {
    companion object {
        inline fun <reified T : DialogFragment> show(activity: FragmentActivity, obtain: () -> T) =
            activity.obtain { obtain() }.apply { takeIf { !it.isAdded }?.show(activity.supportFragmentManager) }

        inline fun <reified T : DialogFragment> showNow(activity: FragmentActivity, obtain: () -> T) =
            activity.obtain { obtain() }.apply { takeIf { !it.isAdded }?.showNow(activity.supportFragmentManager) }

        inline fun <reified T : DialogFragment> dismiss(activity: FragmentActivity) =
            activity.supportFragmentManager.find<T>()?.dismiss() != null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onDestroyView() {
        dialog?.takeIf { retainInstance }?.setDismissMessage(null) // Fix http:///issuetracker.google.com/17423
        super.onDestroyView()
    }
}

inline fun <reified T : Fragment> FragmentManager.find() = findFragmentByTag(T::class.defaultTag) as T?
inline fun <reified T : Fragment> FragmentManager.obtain(factory: () -> T) = find() ?: factory.invoke()
inline fun <reified T : Fragment> FragmentActivity.obtain(factory: () -> T) = supportFragmentManager.obtain(factory)

fun DialogFragment.show(fragmentManager: FragmentManager) = show(fragmentManager, defaultTag)
fun DialogFragment.showNow(fragmentManager: FragmentManager) = showNow(fragmentManager, defaultTag)

val Fragment.defaultTag: String get() = this::class.java.defaultTag
val KClass<out Fragment>.defaultTag: String get() = java.defaultTag
val Class<out Fragment>.defaultTag: String get() = name
