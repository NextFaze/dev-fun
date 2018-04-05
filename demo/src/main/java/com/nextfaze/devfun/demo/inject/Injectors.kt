package com.nextfaze.devfun.demo.inject

import com.nextfaze.devfun.demo.*

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

interface FragmentInjector {
    fun inject(welcomeFragment: WelcomeFragment)
    fun inject(registerFragment: RegisterFragment)
    fun inject(authenticateFragment: AuthenticateFragment)
}

interface DialogFragmentInjector

interface RetainedInjector : FragmentInjector, DialogFragmentInjector

//
// Activity Scope
//

interface ActivityInjector {
    fun inject(splashActivity: SplashActivity)
    fun inject(mainActivity: MainActivity)
}

interface Injector : ActivityInjector
