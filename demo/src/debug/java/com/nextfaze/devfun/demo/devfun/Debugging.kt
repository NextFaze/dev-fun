package com.nextfaze.devfun.demo.devfun

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Process
import androidx.appcompat.app.AlertDialog
import com.nextfaze.devfun.function.DeveloperFunction

object Debugging {
    // DevFun automatically logs the return value of function invocations
    @DeveloperFunction fun logActivityManagerInfo(activity: Activity) = "Activity Manager:\n${getActivityManagerInfo(activity)}"

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
        val memoryInfo = context.activityManager.getProcessMemoryInfo(processIds)
        memoryInfo.firstOrNull()?.run {
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

private val Context.activityManager: ActivityManager get() = applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
