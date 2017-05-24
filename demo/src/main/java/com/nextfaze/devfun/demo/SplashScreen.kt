package com.nextfaze.devfun.demo

import android.os.Handler

/**
 * Here to test how the cog overlay permissions request interacts with a transient splash screen.
 */
class SplashActivity : BaseActivity() {
    private val handler = Handler()
    private val continueToWelcomeActivity = Runnable {
        WelcomeActivity.start(this)
        finish()
    }

    override fun onResume() {
        super.onResume()
        handler.postDelayed(continueToWelcomeActivity, 1000)
    }

    override fun onPause() {
        handler.removeCallbacks(continueToWelcomeActivity)
        super.onPause()
    }
}
