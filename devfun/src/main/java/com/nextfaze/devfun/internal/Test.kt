package com.nextfaze.devfun.internal

import android.util.Log
import com.nextfaze.devfun.annotations.*
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

// TODO don't generate interfaces for annotations types with @DeveloperAnnotation from other project/libs?
/**
 * We do this here for now as we want the interfaces generated but we can't run the annotation processor on the
 * annotations lib (cyclic task/project dependency tree)
 */
@DeveloperAnnotation(developerReference = true)
internal annotation class DummyAnnotation(
    val developerFunction: DeveloperFunction,
    val developerCategory: DeveloperCategory,
    val developerLogger: DeveloperLogger,
    val dagger2Component: Dagger2Component,
    val developerProperty: DeveloperProperty,
    val developerArguments: DeveloperArguments,
    val developerReference: DeveloperReference
)
