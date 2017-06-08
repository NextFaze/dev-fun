package com.nextfaze.devfun.demo.inject

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Process
import android.support.multidex.MultiDex
import android.util.Log
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.multibindings.ElementsIntoSet
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlin.annotation.AnnotationRetention.BINARY
import kotlin.annotation.AnnotationTarget.*

@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent : MainInjector {
    fun retainedComponent(retainedModule: RetainedModule): RetainedComponent
}

typealias Initializer = (application: Application) -> Unit

@Module(includes = arrayOf(AndroidModule::class, MainModule::class, BuildTypeModule::class))
class ApplicationModule {
    @Provides @ElementsIntoSet internal fun defaultInitializers() = emptySet<Initializer>()
    @Provides @ElementsIntoSet @Early internal fun defaultEarlyInitializers() = emptySet<Initializer>()
    @Provides @ElementsIntoSet internal fun defaultActivityLifecycleCallbacks() = emptySet<Application.ActivityLifecycleCallbacks>()
}

/**
 * Used to inject initializer code that should be run very early in the application lifecycle.
 * Such initializers should have absolute minimal dependencies.
 */
@Qualifier
@Target(FIELD, VALUE_PARAMETER, FUNCTION, PROPERTY_GETTER, PROPERTY_SETTER)
@Retention(BINARY)
annotation class Early

abstract class DaggerApplication : Application() {
    var applicationComponent: ApplicationComponent? = null
        private set

    protected open val isDaggerEnabled = true

    protected val currentProcessInfo: ActivityManager.RunningAppProcessInfo? by lazy {
        val pid = Process.myPid()
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.runningAppProcesses?.firstOrNull { it.pid == pid }
    }

    protected abstract fun inject(injector: ApplicationInjector)

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)

        // Then init main dagger component - need to do this before onCreate() for ContentProviders
        if (isDaggerEnabled) {
            applicationComponent = DaggerApplicationComponent.builder().androidModule(AndroidModule(this)).build()
        } else {
            Log.d("DaggerApplication", "Skipped build of Dagger application component - Dagger not enabled")
        }
    }

    override fun onCreate() {
        super.onCreate()
        applicationComponent?.let { inject(it) }
    }
}

val Context.applicationComponent: ApplicationComponent?
    get() = (applicationContext as DaggerApplication).applicationComponent
