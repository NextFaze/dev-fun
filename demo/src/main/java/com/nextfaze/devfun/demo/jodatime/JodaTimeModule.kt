package com.nextfaze.devfun.demo.jodatime

import com.nextfaze.devfun.demo.inject.Early
import com.nextfaze.devfun.demo.inject.Initializer
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import net.danlew.android.joda.JodaTimeAndroid
import javax.inject.Singleton

@Module
class JodaTimeModule {
    @Provides @IntoSet @Singleton @Early
    internal fun initJodaTime(): Initializer = { JodaTimeAndroid.init(it) }
}
