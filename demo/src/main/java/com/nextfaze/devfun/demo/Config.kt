package com.nextfaze.devfun.demo

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.demo.util.KxSharedPreferences
import com.nextfaze.devfun.demo.util.toOptional
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Config @Inject constructor(context: Context) {
    private val prefs = KxSharedPreferences(context.getSharedPreferences("session", MODE_PRIVATE))

    var splashDuration by prefs["splashDuration", 1000L]
        private set

    fun signInEnabled() = prefs["signInEnabled", false]
    var signInEnabled by signInEnabled()
        private set

    fun registrationEnabled() = prefs["registrationEnabled", false]
    var registrationEnabled by registrationEnabled()
        private set

    fun welcomeString() = prefs["welcomeString", "Welcome!\n\nSet my value via\nDevFun > Config > Set Welcome Message".toOptional()]
    var welcomeString by welcomeString()
        private set

    @DeveloperFunction
    private fun setConfigValues(
        splashDuration: Long,
        signInEnabled: Boolean,
        registrationEnabled: Boolean,
        welcomeString: String
    ) {
        this.splashDuration = splashDuration
        this.signInEnabled = signInEnabled
        this.registrationEnabled = registrationEnabled
        this.welcomeString = welcomeString
    }

    @DeveloperFunction
    private fun setWelcomeMessage(welcomeString: String) {
        this.welcomeString = welcomeString
    }

    @DeveloperFunction
    private fun setSplashScreenDuration(durationMillis: Long) {
        splashDuration = durationMillis
    }
}
