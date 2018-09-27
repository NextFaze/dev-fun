package com.nextfaze.devfun.demo

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import com.nextfaze.devfun.demo.inject.ActivityInjector
import com.nextfaze.devfun.demo.inject.DaggerActivity
import com.nextfaze.devfun.demo.inject.DaggerFragment
import com.nextfaze.devfun.demo.kotlin.defaultTag
import com.nextfaze.devfun.demo.kotlin.findOrCreate

abstract class BaseActivity : DaggerActivity() {
    override fun inject(injector: ActivityInjector) = Unit

    inline fun <reified T : Fragment> setContentFragment(@IdRes containerViewId: Int = R.id.activity_fragment_view, factory: () -> T) {
        supportFragmentManager.beginTransaction().replace(containerViewId, findOrCreate { factory() }, T::class.defaultTag).commit()
    }
}

abstract class BaseFragment : DaggerFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }
}
