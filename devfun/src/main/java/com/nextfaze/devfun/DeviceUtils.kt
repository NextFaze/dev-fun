package com.nextfaze.devfun

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.inject.Constructable
import com.nextfaze.devfun.internal.*
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.concurrent.TimeUnit

@Constructable
@DeveloperCategory(order = 9_000)
class DeviceUtils {
    private val log = logger()

    @DeveloperFunction
    fun clearDataAndDie(context: Context) {
        exec("pm clear ${context.packageName}", 1000)
        context.showToast("Executing: pm clear ${context.packageName}")
    }

    @DeveloperFunction
    fun clearDataAndTryRestart(app: Application) {
        app.showToast("Hit the HOME button (not back) to clear data and restart.\nFull procedure may take ~5 seconds.", Toast.LENGTH_LONG)
        app.registerActivityLifecycleCallbacks(object : AbstractActivityLifecycleCallbacks() {
            override fun onActivityStopped(activity: Activity) {
                app.showToast("Clearing and restarting...\nFull procedure may take ~5 seconds.", Toast.LENGTH_LONG)
                launch {
                    delay(1000L)
                    val launchIntent = app.packageManager.getLaunchIntentForPackage(app.packageName)
                    val n = "${launchIntent.component.packageName}/${launchIntent.component.className}"
                    exec("am start --user 0 -n $n -f ${Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK}")
                    exec("pm clear ${app.packageName}")
                }
            }
        })
    }

    private fun exec(cmd: String, delayTime: Long = 0, timeUnit: TimeUnit = TimeUnit.MILLISECONDS) {
        fun exec() {
            log.d { "Exec: $cmd" }
            val exitValue = Runtime.getRuntime().exec(cmd).apply {
                errorStream.bufferedReader().readText().takeIf { it.isNotBlank() }?.let { log.e { it } }
                inputStream.bufferedReader().readText().takeIf { it.isNotBlank() }?.let { log.i { it } }
            }.waitFor()
            log.d { "exit($exitValue)" }
        }

        if (delayTime > 0) {
            launch {
                delay(1000L, timeUnit)
                exec()
            }
        } else {
            exec()
        }
    }

    private fun Context.showToast(text: String, length: Int = Toast.LENGTH_SHORT)
            = Toast.makeText(this, text, length).show()
}
