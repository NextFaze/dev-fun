package com.nextfaze.devfun.demo

import android.content.Context
import android.support.multidex.MultiDex
import com.nextfaze.devfun.demo.inject.DaggerApplication
import com.nextfaze.devfun.demo.util.KxSharedPreferences

class DemoApplication : DaggerApplication() {
    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)

        // TODO add properties to DeveloperLogger to specify defaults?
        // In general probably not needed though? (just wanted for demo purposes?)
        fun prefs(name: String) = KxSharedPreferences(getSharedPreferences(name, Context.MODE_PRIVATE))

        var firstTime by prefs("demo")["firstTime", true]
        if (firstTime) {
            @Suppress("UNUSED_VALUE")
            firstTime = false
            prefs("OverlayLogger_Config").apply {
                this["snapToEdge", false].value = true
                this["dock", ""].value = "RIGHT"
                this["delta", 0f].value = 0.27f
            }
            prefs("OverlayLogger_WelcomeFragment.createAccountButtonEnabled").apply {
                this["top", 0f].value = 0.104f
            }
            prefs("OverlayLogger_WelcomeFragment.signInButtonEnabled").apply {
                this["top", 0f].value = 0.137f
            }
        }
    }
}
