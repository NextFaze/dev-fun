package com.nextfaze.devfun.demo.inject

import android.os.Bundle
import com.trello.rxlifecycle.components.support.RxAppCompatDialogFragment
import com.trello.rxlifecycle.components.support.RxFragment

abstract class DaggerFragment : RxFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject((activity as DaggerActivity).fragmentInjector)
    }

    protected abstract fun inject(injector: FragmentInjector)
}

abstract class DaggerDialogFragment : RxAppCompatDialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject((activity as DaggerActivity).dialogFragmentInjector)
    }

    protected abstract fun inject(injector: DialogFragmentInjector)
}
