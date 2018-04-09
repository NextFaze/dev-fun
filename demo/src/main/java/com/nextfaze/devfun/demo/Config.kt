package com.nextfaze.devfun.demo

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.widget.Toast
import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.demo.util.KxSharedPreferences
import com.nextfaze.devfun.demo.util.toOptional
import com.nextfaze.devfun.inject.Constructable
import com.nextfaze.devfun.invoke.view.From
import com.nextfaze.devfun.invoke.view.Ranged
import com.nextfaze.devfun.invoke.view.ValueSource
import javax.inject.Inject
import javax.inject.Singleton

/*
 * This file demonstrates various construction and injection techniques.
 * For simplicity everything is kept in this one file, but in practice you'd have all your debug code in the debug tree where possible.
 */

@Singleton
class Config @Inject constructor(context: Context) {
    private val prefs = KxSharedPreferences(context.getSharedPreferences("session", MODE_PRIVATE))

    fun splashDuration() = prefs["splashDuration", 1000L]
    var splashDuration by splashDuration()
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

    /**
     * These can be inner classes or static classes with injected constructors, etc.
     * For these two, technically behind the scenes they result in effectively the same byte code implementation.
     */
    @Constructable
    private inner class CurrentSignInEnabled : ValueSource<Boolean> {
        override val value get() = signInEnabled
    }

    @Constructable
    private class CurrentRegistrationEnabled(private val config: Config) : ValueSource<Boolean> {
        override val value get() = config.registrationEnabled
    }

    @DeveloperFunction
    private fun editValues(
        config: Config, // injecting for UI demonstration purposes
        @From(CurrentSplashDuration::class) splashDurationMillis: Long,
        @From(CurrentSignInEnabled::class) signInEnabled: Boolean,
        @From(CurrentRegistrationEnabled::class) registrationEnabled: Boolean,
        @From(CurrentWelcomeMessage::class) welcomeString: String
    ) {
        config.splashDuration = splashDurationMillis
        config.signInEnabled = signInEnabled
        config.registrationEnabled = registrationEnabled
        config.welcomeString = welcomeString
    }

    @DeveloperFunction
    private fun reset() {
        // using preferences.edit().clear() does not trigger key changes (which we want for RX etc).
        splashDuration().delete()
        signInEnabled().delete()
        registrationEnabled().delete()
        welcomeString().delete()
    }
}

/** They can be inner classes or whatever - they are treated like any other injectable class. */
@Constructable
private class CurrentSplashDuration(private val config: Config) : ValueSource<Long> {
    override val value get() = config.splashDuration
}

@Constructable
private class CurrentWelcomeMessage(private val config: Config) : ValueSource<String> {
    override val value get() = config.welcomeString ?: ""
}

/** This class could be in the debug source tree, but it left here/public for demo purposes. */
@Constructable
@DeveloperCategory("Config") // Set it to 'Config' to merge it with
class ConfigDebugClass(private val config: Config) {
    @Constructable
    private inner class CurrentSplashScreenDurationSec : ValueSource<Double> {
        override val value get() = config.splashDuration / 1000.0
    }

    @DeveloperFunction
    private fun setSplashScreenDuration(@From(CurrentSplashScreenDurationSec::class) @Ranged(to = 10.0) durationSeconds: Double) {
        config.splashDuration().value = (durationSeconds * 1000).toLong()
    }
}

/**
 * This object could be in the debug source tree, but it left here/public for demo purposes.
 *
 * You don't need `@Constructable` for `object` types.
 * A limitation of this however is that all injection must occur at the function level.
 */
@DeveloperCategory("Config")
object ConfigDebugObject {
    @DeveloperFunction
    fun setWelcomeMessage(config: Config, @From(CurrentWelcomeMessage::class) welcomeString: String) {
        config.welcomeString().value = welcomeString
    }

    enum class ToastLength(val value: Int) { SHORT(Toast.LENGTH_SHORT), LONG(Toast.LENGTH_LONG) }

    /**
     * Since [length] is not injectable, but is supported via the invoke UI (all simple types and enums are), DevFun
     * will render a UI w/ a spinner to allow you to provide the value manually.
     *
     * If we wanted to change the default, we could either put the [ToastLength.LONG] first (as its based on
     * their ordinal number), or we could add an @[From] that always returns [ToastLength.LONG].
     */
    @DeveloperFunction
    fun showToastOfValues(context: Context, config: Config, length: ToastLength) =
        Toast.makeText(
            context,
            """
                |splashDuration=${config.splashDuration}
                |signInEnabled=${config.signInEnabled}
                |registrationEnabled=${config.registrationEnabled}
                |welcomeString='${config.welcomeString}'""".trimMargin(),
            length.value
        ).show()
}
