package com.nextfaze.devfun.demo

import android.os.Handler
import com.nextfaze.devfun.demo.inject.ActivityInjector
import javax.inject.Inject

/**
 * Here to test how the cog overlay permissions request interacts with a transient splash screen.
 */
class SplashActivity : BaseActivity() {
    @Inject lateinit var config: Config

    private val handler = Handler()
    private val continueToWelcomeActivity = Runnable {
        WelcomeActivity.start(this)
        finish()
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(continueToWelcomeActivity, config.splashDuration)
    }

    override fun onPause() {
        handler.removeCallbacks(continueToWelcomeActivity)
        super.onPause()
    }

    override fun inject(injector: ActivityInjector) = injector.inject(this)
}
