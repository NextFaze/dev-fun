package com.nextfaze.devfun.demo

import com.nextfaze.devfun.demo.inject.ActivityInjector
import com.nextfaze.devfun.demo.inject.DaggerActivity
import com.nextfaze.devfun.demo.inject.DaggerFragment

abstract class BaseActivity : DaggerActivity() {
    override fun inject(injector: ActivityInjector) = Unit
}

abstract class BaseFragment : DaggerFragment()
