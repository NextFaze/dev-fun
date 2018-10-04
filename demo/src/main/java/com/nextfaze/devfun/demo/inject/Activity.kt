package com.nextfaze.devfun.demo.inject

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nextfaze.devfun.demo.test.ActivityScopedTestModule
import com.nextfaze.devfun.reference.Dagger2Component
import com.nextfaze.devfun.reference.Dagger2Scope
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Scope
import kotlin.annotation.AnnotationRetention.SOURCE
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.annotation.AnnotationTarget.FILE
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.PROPERTY_GETTER
import kotlin.annotation.AnnotationTarget.PROPERTY_SETTER
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER

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
    @Dagger2Component(Dagger2Scope.RETAINED_FRAGMENT) // on property
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
