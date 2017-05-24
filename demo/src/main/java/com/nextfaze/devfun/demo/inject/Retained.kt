package com.nextfaze.devfun.demo.inject

import dagger.Module
import dagger.Subcomponent
import javax.inject.Scope
import kotlin.annotation.AnnotationRetention.SOURCE
import kotlin.annotation.AnnotationTarget.*

@Scope
@MustBeDocumented
@Retention(SOURCE)
@Target(FIELD, VALUE_PARAMETER, FUNCTION, PROPERTY_GETTER, PROPERTY_SETTER, CLASS, FILE)
annotation class RetainedScope

@Subcomponent(modules = arrayOf(RetainedModule::class))
@RetainedScope
interface RetainedComponent : RetainedInjector {
    fun activityComponent(activityModule: ActivityModule): ActivityComponent
}

@Module
class RetainedModule
