package com.nextfaze.devfun.demo

import com.nextfaze.devfun.demo.inject.Initializer
import com.squareup.leakcanary.LeakCanary
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
class LeakCanaryModule {
    private val log = logger()

    @Provides @IntoSet @Singleton
    fun initializeLeakCanary(): Initializer = {
        // Don't install LeakCanary when using JRebel
        // It is rarely able to identify the source of the leak due to JRebel (shadow classes etc.) and just gets in the way
        if (!this::class.java.classLoader::class.java.name.contains("jrebel")) {
            LeakCanary.install(it)
            log.d { "LeakCanary installed." }
        } else {
            log.d { "JRebel instance detected - skipping LeakCanary installation." }
        }
    }
}
