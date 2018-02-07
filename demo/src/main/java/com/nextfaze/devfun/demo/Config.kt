package com.nextfaze.devfun.demo

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.nextfaze.devfun.demo.util.KxSharedPreferences
import com.nextfaze.devfun.demo.util.Optional
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Config @Inject constructor(context: Context) {
    private val prefs = KxSharedPreferences(context.getSharedPreferences("session", MODE_PRIVATE))

    fun signInEnabled() = prefs["signInEnabled", false]
    var signInEnabled by signInEnabled()
        private set

    fun registrationEnabled() = prefs["registrationEnabled", false]
    var registrationEnabled by registrationEnabled()
        private set

    fun welcomeString() = prefs["welcomeString", Optional.None]
    var welcomeString by welcomeString()
        private set
}
