package com.nextfaze.devfun.demo.devfun

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Process
import android.support.annotation.RequiresApi
import android.support.v7.app.AlertDialog
import android.util.DisplayMetrics
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.core.DebugException
import com.nextfaze.devfun.demo.logger
import com.nextfaze.devfun.demo.t
import com.nextfaze.devfun.internal.*

object Debugging {
    private val log = logger()

    @DeveloperFunction fun crashApp() {
        log.d { "throw DebugException()" }
        throw DebugException()
    }

    @DeveloperFunction("Force GC") fun forceGarbageCollection() = System.gc()
    @DeveloperFunction("Throw NPE") fun throwNullPointerException(): Unit = throw NullPointerException("Debug")

    @DeveloperFunction fun logDisplayMetrics(activity: Activity) = "Display Metrics:\n${getDisplayMetrics(activity)}".also { log.t { it } }
    @DeveloperFunction fun logActivityManagerInfo(activity: Activity) = "Activity Manager:\n${getActivityManagerInfo(activity)}".also { log.t { it } }

    @DeveloperFunction
    fun showDisplayMetrics(activity: Activity) {
        AlertDialog.Builder(activity)
                .setTitle("Display Metrics")
                .setMessage(getDisplayMetrics(activity))
                .setPositiveButton(android.R.string.ok, null)
                .show()
    }

    private fun getDisplayMetrics(activity: Activity) = with(DisplayMetrics()) displayMetrics@ {
        val display = activity.windowManager.defaultDisplay.apply {
            getMetrics(this@displayMetrics)
        }

        "%s, dpi=%s, dpWidth=%.2f, dpHeight=%.2f".format(
                display, Density[densityDpi].name,
                widthPixels / density,
                heightPixels / density).replace(", ", "\n")
    }

    @DeveloperFunction
    fun showActivityManagerInfo(activity: Activity) {
        AlertDialog.Builder(activity)
                .setTitle("Activity Manager Info")
                .setMessage(getActivityManagerInfo(activity))
                .setPositiveButton(android.R.string.ok, null)
                .show()
    }

    @SuppressLint("NewApi")
    private fun getActivityManagerInfo(context: Context) = StringBuilder().apply {
        context.activityManager.run {
            append("getDeviceConfigurationInfo=").append(deviceConfigurationInfo)
            append("\nmemoryClass=").append(memoryClass)
            append("\nlargeMemoryClass=").append(largeMemoryClass)
            append("\nlauncherLargeIconDensity=").append(launcherLargeIconDensity)
            append("\nlauncherLargeIconSize=").append(launcherLargeIconSize)
            append("\nisLowRamDevice=").append(kitKat { isLowRamDevice })
        }

        val processIds = intArrayOf(Process.myPid())
        val memoryInfos = context.activityManager.getProcessMemoryInfo(processIds)
        memoryInfos.firstOrNull()?.run {
            append("\nMemoryInfo.dalvikPss=").append(dalvikPss)
            append("\nMemoryInfo.dalvikPrivateDirty=").append(dalvikPrivateDirty)
            append("\nMemoryInfo.dalvikSharedDirty=").append(dalvikSharedDirty)
            append("\nMemoryInfo.nativePss=").append(nativePss)
            append("\nMemoryInfo.nativePrivateDirty=").append(nativePrivateDirty)
            append("\nMemoryInfo.nativeSharedDirty=").append(nativeSharedDirty)
            append("\nMemoryInfo.otherPss=").append(otherPss)
            append("\nMemoryInfo.otherPrivateDirty=").append(otherPrivateDirty)
            append("\nMemoryInfo.otherSharedDirty=").append(otherSharedDirty)
            append("\nMemoryInfo.getTotalPss=").append(totalPss)
            append("\nMemoryInfo.getTotalSwappablePss=").append(kitKat { totalSwappablePss })
            append("\nMemoryInfo.getTotalPrivateDirty=").append(totalPrivateDirty)
            append("\nMemoryInfo.getTotalSharedDirty=").append(totalSharedDirty)
            append("\nMemoryInfo.getTotalPrivateClean=").append(kitKat { totalPrivateClean })
            append("\nMemoryInfo.getTotalSharedClean=").append(kitKat { totalSharedClean })
            append("\nMemoryInfo.describeContents=").append(describeContents())
        }
    }.toString()
}

private inline fun kitKat(body: () -> Any) = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT -> body.invoke()
    else -> "MIN_SDK:KITKAT"
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP) // doesn't matter as they're static final int values
private enum class Density constructor(private val value: Int) {
    DENSITY_LOW(DisplayMetrics.DENSITY_LOW),
    DENSITY_MEDIUM(DisplayMetrics.DENSITY_MEDIUM),
    DENSITY_TV(DisplayMetrics.DENSITY_TV),
    DENSITY_HIGH(DisplayMetrics.DENSITY_HIGH),
    DENSITY_280(DisplayMetrics.DENSITY_280),
    DENSITY_XHIGH(DisplayMetrics.DENSITY_XHIGH),
    DENSITY_400(DisplayMetrics.DENSITY_400),
    DENSITY_XXHIGH(DisplayMetrics.DENSITY_XXHIGH),
    DENSITY_560(DisplayMetrics.DENSITY_560),
    DENSITY_XXXHIGH(DisplayMetrics.DENSITY_XXXHIGH),
    UNKNOWN(0);

    companion object {
        private val values by lazy { values().associateBy { it.value } }
        operator fun get(value: Int) = values[value] ?: UNKNOWN
    }
}
