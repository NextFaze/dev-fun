package com.nextfaze.devfun.utils.leakcanary

import android.app.Application
import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.squareup.leakcanary.internal.DisplayLeakActivity

/**
 * Utility functions for [Leak Canary](https://github.com/square/leakcanary).
 */
@DeveloperCategory(order = 9_000)
object LeakCanaryUtils {

    /**
     * Starts the Leak Canary display leaks activity.
     *
     * @see DisplayLeakActivity
     */
    @DeveloperFunction
    fun startLeakCanaryActivity(application: Application): Unit =
            DisplayLeakActivity.createPendingIntent(application).send()
}
