package com.nextfaze.devfun.demo

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import androidx.test.rule.ActivityTestRule
import com.nextfaze.devfun.demo.kotlin.defaultTag
import kotlin.reflect.KClass

inline fun <reified TActivity : FragmentActivity, reified TFragment : Fragment> fragmentActivityTestRule() =
    FragmentActivityTestRule(TActivity::class, TFragment::class)

class FragmentActivityTestRule<TActivity : FragmentActivity, TFragment : Fragment>(
    activityClass: KClass<TActivity>,
    private val fragmentClass: KClass<TFragment>
) : ActivityTestRule<TActivity>(activityClass.java) {

    @Suppress("UNCHECKED_CAST")
    val fragment
        get() = activity.supportFragmentManager.findFragmentByTag(fragmentClass.defaultTag) as TFragment
}
