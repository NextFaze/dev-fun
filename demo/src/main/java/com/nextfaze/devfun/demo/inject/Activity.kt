package com.nextfaze.devfun.demo.inject

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nextfaze.devfun.annotations.Dagger2Component
import com.nextfaze.devfun.annotations.Dagger2Scope.RETAINED_FRAGMENT
import com.nextfaze.devfun.demo.test.ActivityScopedTestModule
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Scope
import kotlin.annotation.AnnotationRetention.SOURCE
import kotlin.annotation.AnnotationTarget.*

@Scope
@MustBeDocumented
@Retention(SOURCE)
@Target(FIELD, VALUE_PARAMETER, FUNCTION, PROPERTY_GETTER, PROPERTY_SETTER, CLASS, FILE)
annotation class ActivityScope

@Subcomponent(modules = [ActivityModule::class])
@ActivityScope
interface ActivityComponent : Injector

@Module(includes = [ActivityScopedTestModule::class])
class ActivityModule(private val activity: AppCompatActivity) {
    @Provides internal fun activity(): Activity = activity
}


abstract class DaggerActivity : AppCompatActivity() {
    @Dagger2Component(RETAINED_FRAGMENT) // on property
    lateinit var retainedComponent: RetainedComponent
        private set

    @get:Dagger2Component // or on getter explicitly
    lateinit var activityComponent: ActivityComponent
        private set

    val activityInjector: ActivityInjector get() = activityComponent
    val fragmentInjector: FragmentInjector get() = retainedComponent
    val dialogFragmentInjector: DialogFragmentInjector get() = retainedComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        retainedComponent = (lastCustomNonConfigurationInstance as? RetainedComponent) ?:
                applicationComponent!!.retainedComponent(RetainedModule())

        activityComponent = retainedComponent.activityComponent(ActivityModule(this))

        inject(activityComponent)
        super.onCreate(savedInstanceState)
    }

    override fun onRetainCustomNonConfigurationInstance(): Any = retainedComponent

    protected abstract fun inject(injector: ActivityInjector)
}
