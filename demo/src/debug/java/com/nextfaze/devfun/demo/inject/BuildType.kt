package com.nextfaze.devfun.demo.inject

import com.nextfaze.devfun.demo.KotlinBugFragment
import com.nextfaze.devfun.demo.LeakCanaryModule
import com.nextfaze.devfun.demo.devfun.DevFunModule
import dagger.Module

@Module(
    includes = [
        DevFunModule::class,
        LeakCanaryModule::class
    ]
)
class BuildTypeModule

interface BuildTypeFragmentInjector {
    fun inject(kotlinBugFragment: KotlinBugFragment)
}
