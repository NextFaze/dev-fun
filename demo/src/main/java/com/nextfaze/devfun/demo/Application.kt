package com.nextfaze.devfun.demo

import com.nextfaze.devfun.demo.inject.ApplicationInjector
import com.nextfaze.devfun.demo.inject.DaggerApplication
import com.nextfaze.devfun.demo.inject.Early
import com.nextfaze.devfun.demo.inject.Initializer
import com.nextfaze.devfun.demo.kotlin.isNamed
import dagger.Lazy
import javax.inject.Inject

class DemoApplication : DaggerApplication() {

    @Inject @Early lateinit internal var earlyInitializers: Lazy<Set<Initializer>>

    @Inject lateinit internal var initializers: Lazy<Set<Initializer>>
    @Inject lateinit internal var activityLifecycleCallbacks: Lazy<Set<ActivityLifecycleCallbacks>>

    override val isDaggerEnabled: Boolean by lazy { currentProcessInfo?.let { !it.isNamed("leakcanary") } ?: true }

    override fun onCreate() {
        super.onCreate()

        if (isDaggerEnabled) {
            earlyInitializers.get().forEach { it(this) }
            initializers.get().forEach { it(this) }
            activityLifecycleCallbacks.get().forEach { registerActivityLifecycleCallbacks(it) }
        }
    }

    override fun inject(injector: ApplicationInjector) = injector.inject(this)
}
