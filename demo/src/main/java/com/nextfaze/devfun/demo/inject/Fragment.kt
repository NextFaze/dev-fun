package com.nextfaze.devfun.demo.inject

import android.os.Bundle
import android.support.v4.app.Fragment

abstract class DaggerFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject((activity as DaggerActivity).fragmentInjector)
    }

    protected abstract fun inject(injector: FragmentInjector)
}
