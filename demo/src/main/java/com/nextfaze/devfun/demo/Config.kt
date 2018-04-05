package com.nextfaze.devfun.demo

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.demo.util.KxSharedPreferences
import com.nextfaze.devfun.demo.util.toOptional
import com.nextfaze.devfun.inject.Constructable
import com.nextfaze.devfun.invoke.view.From
import com.nextfaze.devfun.invoke.view.ValueSource
import com.nextfaze.devfun.invoke.view.types.Ranged
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

    @Constructable
    private inner class CurrentSignInEnabled : ValueSource<Boolean> {
        override val value get() = signInEnabled
    }

    @Constructable
    private inner class CurrentRegistrationEnabled : ValueSource<Boolean> {
        override val value get() = registrationEnabled
    }

    @DeveloperFunction
    private fun setConfigValues(
        @From(CurrentSplashDuration::class) splashDurationMillis: Long,
        @From(CurrentSignInEnabled::class) signInEnabled: Boolean,
        @From(CurrentRegistrationEnabled::class) registrationEnabled: Boolean,
        @From(CurrentWelcomeMessage::class) welcomeString: String
    ) {
        this.splashDuration = splashDurationMillis
        this.signInEnabled = signInEnabled
        this.registrationEnabled = registrationEnabled
        this.welcomeString = welcomeString
    }

    @Constructable
    private inner class CurrentWelcomeMessage : ValueSource<String> {
        override val value get() = welcomeString ?: ""
    }

    @DeveloperFunction
    private fun setWelcomeMessage(@From(CurrentWelcomeMessage::class) welcomeString: String) {
        this.welcomeString = welcomeString
    }

    @Constructable
    private inner class CurrentSplashScreenDurationSec : ValueSource<Double> {
        override val value get() = splashDuration / 1000.0
    }

    @DeveloperFunction
    private fun setSplashScreenDuration(@From(CurrentSplashScreenDurationSec::class) @Ranged(to = 10.0) durationSeconds: Double) {
        splashDuration = (durationSeconds * 1000).toLong()
    }
}

/**
 * They can be inner classes or whatever - they are treated like any other injectable class.
 */
@Constructable
private class CurrentSplashDuration(private val config: Config) : ValueSource<Long> {
    override val value get() = config.splashDuration
}
