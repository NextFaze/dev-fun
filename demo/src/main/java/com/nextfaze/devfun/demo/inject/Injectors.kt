package com.nextfaze.devfun.demo.inject

import com.nextfaze.devfun.demo.*
import com.nextfaze.devfun.demo.test.DaggerScopesActivity
import com.nextfaze.devfun.demo.test.DaggerScopesFragment

//
// Application Scope
//

interface ApplicationInjector {
    fun inject(application: DemoApplication)
}

interface MainInjector : ApplicationInjector

//
// Retained Scope
//

interface FragmentInjector : BuildTypeFragmentInjector {
    fun inject(welcomeFragment: WelcomeFragment)
    fun inject(registerFragment: RegisterFragment)
    fun inject(authenticateFragment: AuthenticateFragment)
    fun inject(daggerScopesFragment: DaggerScopesFragment)
}

interface DialogFragmentInjector

interface RetainedInjector : FragmentInjector, DialogFragmentInjector

//
// Activity Scope
//

interface ActivityInjector {
    fun inject(splashActivity: SplashActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(daggerScopesActivity: DaggerScopesActivity)
}

interface Injector : ActivityInjector
