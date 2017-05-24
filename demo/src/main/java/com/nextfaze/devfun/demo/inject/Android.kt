package com.nextfaze.devfun.demo.inject

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AndroidModule(private val application: Application) {
    @Provides internal fun context(): Context = application
    @Provides internal fun application() = application
}
