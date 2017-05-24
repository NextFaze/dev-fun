package com.nextfaze.devfun.demo.devfun

import android.app.Application
import com.nextfaze.devfun.core.devFun
import com.nextfaze.devfun.demo.inject.DaggerActivity
import com.nextfaze.devfun.demo.inject.Initializer
import com.nextfaze.devfun.demo.inject.applicationComponent
import com.nextfaze.devfun.inject.InstanceProvider
import com.nextfaze.devfun.inject.dagger2.tryGetInstanceFromComponent
import com.nextfaze.devfun.inject.dagger2.useAutomaticDagger2Injector
import com.nextfaze.devfun.internal.*
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import javax.inject.Singleton
import kotlin.reflect.KClass

@Module
class DevFunModule {
    init {
        useAutomaticDagger2Injector = false
    }

    @Provides @IntoSet @Singleton
    fun initializeDevFun(application: Application): Initializer = {
        //DevFun().initialize(application, DevMenu(), DevHttpD(), DevHttpIndex(), DevStetho(), useServiceLoader = false)
        devFun += onInitialized@ {
            instanceProviders += DemoInstanceProvider(application, devFun.get<ActivityProvider>())
        }
    }
}

private class DemoInstanceProvider(private val application: Application, private val activityProvider: ActivityProvider) : InstanceProvider {
    private val applicationComponent by lazy { application.applicationComponent!! }

    override fun <T : Any> get(clazz: KClass<out T>): T? {
        tryGetInstanceFromComponent(applicationComponent, clazz)?.let { return it }

        activityProvider()?.let { activity ->
            if (activity is DaggerActivity) {
                tryGetInstanceFromComponent(activity.retainedComponent, clazz)?.let { return it }
                tryGetInstanceFromComponent(activity.activityComponent, clazz)?.let { return it }
            }
        }

        return null
    }
}
