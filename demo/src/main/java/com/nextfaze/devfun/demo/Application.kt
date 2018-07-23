package com.nextfaze.devfun.demo

import android.content.Context
import android.support.multidex.MultiDex
import com.nextfaze.devfun.demo.inject.DaggerApplication

class DemoApplication : DaggerApplication() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}
