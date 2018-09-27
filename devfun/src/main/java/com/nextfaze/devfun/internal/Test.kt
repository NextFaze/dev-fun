package com.nextfaze.devfun.internal

import android.util.Log
import com.nextfaze.devfun.internal.log.*

private val log = logger("${com.nextfaze.devfun.core.BuildConfig.APPLICATION_ID}.test")

val isInstrumentationTest by lazy {
    when {
        Thread.currentThread().contextClassLoader?.toString().orEmpty().contains("android.test.runner.jar") -> true
        else -> Log.getStackTraceString(Throwable()).let {
            it.contains("android.support.test.runner.MonitoringInstrumentation") ||
                    it.contains("androidx.test.runner.MonitoringInstrumentation")
        }
    }.also {
        if (it) {
            log.d { "Instance detected as instrumentation test - debug cog overlay disabled." }
        }
    }
}
