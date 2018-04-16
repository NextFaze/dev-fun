package com.nextfaze.devfun.demo.inject

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Process
import com.nextfaze.devfun.annotations.Dagger2Component
import dagger.Component
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dagger.multibindings.ElementsIntoSet
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlin.annotation.AnnotationRetention.BINARY
import kotlin.annotation.AnnotationTarget.*

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent : MainInjector {
    fun initializer(): Initializer
    fun retainedComponent(retainedModule: RetainedModule): RetainedComponent
}

typealias Initializer = (application: Application) -> Unit

@Module(includes = [AndroidModule::class, MainModule::class, BuildTypeModule::class])
class ApplicationModule {
    @Provides @ElementsIntoSet internal fun defaultInitializers() = emptySet<Initializer>()
    @Provides @ElementsIntoSet @Early internal fun defaultEarlyInitializers() = emptySet<Initializer>()
    @Provides @ElementsIntoSet internal fun defaultActivityLifecycleCallbacks() = emptySet<Application.ActivityLifecycleCallbacks>()

    @Provides
    internal fun initializer(
        initializers: Lazy<Set<Initializer>>,
        @Early earlyInitializers: Lazy<Set<Initializer>>,
        activityLifecycleCallbacks: Lazy<Set<Application.ActivityLifecycleCallbacks>>
    ): Initializer = { application ->
        earlyInitializers.get().forEach { it(application) }
        initializers.get().forEach { it(application) }
        activityLifecycleCallbacks.get().forEach { application.registerActivityLifecycleCallbacks(it) }
    }
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
    /** The main application component. May throw if called from other than the [main process][isMainProcess]. */
    open val applicationComponent: ApplicationComponent by lazy {
        // Lazily initialized, so that ContentProviders can access the component earlier than onCreate().
        check(isMainProcess) { "Cannot instantiate application component on anything other than the main process" }
        createComponent().apply { initializer()(this@DaggerApplication) }
    }

    /** The [RunningAppProcessInfo] of the current process. */
    protected val currentProcessInfo: ActivityManager.RunningAppProcessInfo by lazy {
        val pid = Process.myPid()
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.runningAppProcesses.orEmpty().first { it.pid == pid }
    }

    /** Indicates if this process is the main process. */
    protected val isMainProcess by lazy { !currentProcessInfo.processName.contains(":") }

    /**
     * Create the application component. Subclasses, as needed for tests for example, can override this to supply an
     * [ApplicationComponent] subclass.
     * @return An application component.
     */
    protected open fun createComponent(): ApplicationComponent =
        DaggerApplicationComponent.builder().androidModule(AndroidModule(this)).build()
}

@Dagger2Component
val Context.applicationComponent: ApplicationComponent?
    get() = (applicationContext as DaggerApplication).applicationComponent
