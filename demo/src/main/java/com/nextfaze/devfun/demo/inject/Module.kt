package com.nextfaze.devfun.demo.inject

import com.nextfaze.devfun.demo.jodatime.JodaTimeModule
import com.nextfaze.devfun.demo.util.ActivityTracker
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet

@Module(includes = [JodaTimeModule::class])
class MainModule {
    @Provides @IntoSet
    internal fun activityTracker(activityTracker: ActivityTracker): Initializer = activityTracker
}
