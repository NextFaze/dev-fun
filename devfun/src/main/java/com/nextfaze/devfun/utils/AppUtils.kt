package com.nextfaze.devfun.utils

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.nextfaze.devfun.category.CategoryDefinition
import com.nextfaze.devfun.category.DeveloperCategory
import com.nextfaze.devfun.core.R
import com.nextfaze.devfun.function.DeveloperFunction
import com.nextfaze.devfun.function.FunctionDefinition
import com.nextfaze.devfun.function.FunctionItem
import com.nextfaze.devfun.function.FunctionTransformer
import com.nextfaze.devfun.function.SimpleFunctionItem
import com.nextfaze.devfun.inject.Constructable
import com.nextfaze.devfun.internal.WithSubGroup
import com.nextfaze.devfun.internal.android.*
import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.invoke.Invoker
import com.nextfaze.devfun.invoke.Parameter
import com.nextfaze.devfun.invoke.WithInitialValue
import com.nextfaze.devfun.invoke.WithNullability
import com.nextfaze.devfun.invoke.uiButton
import com.nextfaze.devfun.invoke.uiFunction
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.io.File
import java.util.concurrent.TimeUnit

@Constructable
@DeveloperCategory(order = 9_000, group = "Data")
internal class AppUtils {
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
                GlobalScope.launch {
                    delay(1000L)
                    val component = app.packageManager.getLaunchIntentForPackage(app.packageName)!!.component!!
                    val n = "${component.packageName}/${component.className}"
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
            GlobalScope.launch {
                delay(timeUnit.toMillis(1000L))
                exec()
            }
        } else {
            exec()
        }
    }

    private fun Context.showToast(text: String, length: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, text, length).show()
}

@DeveloperCategory("App Utils", group = "Shared Preferences")
internal object SharedPrefs {
    private val log = logger()

    @DeveloperFunction
    fun deleteAllFiles(context: Context) {
        val prefsDir = File(context.applicationInfo.dataDir, "shared_prefs")
        if (!prefsDir.exists() || !prefsDir.isDirectory) return

        prefsDir.listFiles().filter { it.extension == "xml" }.forEach {
            it.delete()
        }

        AlertDialog.Builder(context)
            .setTitle("Restart Needed")
            .setMessage("Shared preference files deleted.\n\nThis does not trigger listeners or reset memory states!")
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    @DeveloperFunction(transformer = FetchPrefsTransformer::class)
    fun editSharedPreferences(context: Context, invoker: Invoker, file: File) {
        log.d { "file: $file" }

        class Preference(key: String, override var value: Any) : Parameter, WithInitialValue<Any>, WithNullability {
            override val name: String = key
            override val type = value::class
        }

        val prefs = context.getSharedPreferences(file.nameWithoutExtension, Context.MODE_PRIVATE)
        val params = prefs.all.map { (key, value) ->
            Preference(key, value!!)
        }

        invoker.invoke(
            uiFunction(
                title = "Edit SharedPrefs",
                subtitle = file.nameWithoutExtension,
                signature = file.canonicalPath,
                parameters = params,
                neutralButton = uiButton(textId = R.string.df_devfun_reset, onClick = { prefs.edit().clear().apply() }),
                invoke = {
                    val edit = prefs.edit()
                    params.asSequence().zip(it.asSequence()).forEach { (param, arg) ->
                        if (arg == null) {
                            edit.remove(param.name)
                            return@forEach
                        }

                        when (arg) {
                            is Boolean -> edit.putBoolean(param.name, arg)
                            is String -> edit.putString(param.name, arg)
                            is Int -> edit.putInt(param.name, arg)
                            is Long -> edit.putLong(param.name, arg)
                            is Float -> edit.putFloat(param.name, arg)
                            is Set<*> -> @Suppress("UNCHECKED_CAST") edit.putStringSet(param.name, arg as Set<String>)
                            else -> throw RuntimeException("Unexpected type! ${param.type}")
                        }
                    }
                    edit.apply()
                }
            )
        )
    }
}

@Constructable(singleton = true)
internal class FetchPrefsTransformer(context: Context) : FunctionTransformer {
    private val context = context.applicationContext

    override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition): Collection<FunctionItem>? {
        val prefsDir = File(context.applicationInfo.dataDir, "shared_prefs")
        if (!prefsDir.exists() || !prefsDir.isDirectory) return emptyList()

        return prefsDir.listFiles().filter { it.extension == "xml" }.mapNotNull {
            val prefs = context.getSharedPreferences(it.nameWithoutExtension, Context.MODE_PRIVATE)
            if (prefs.all.isEmpty()) {
                null
            } else {
                object : SimpleFunctionItem(functionDefinition, categoryDefinition), WithSubGroup {
                    override val name = it.name
                    override val args = listOf(Unit, Unit, it)
                    override val subGroup = "Files"
                }
            }
        }
    }
}
