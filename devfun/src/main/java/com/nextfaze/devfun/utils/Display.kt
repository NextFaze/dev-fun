package com.nextfaze.devfun.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.view.Display
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.nextfaze.devfun.category.DeveloperCategory
import com.nextfaze.devfun.function.DeveloperFunction
import com.nextfaze.devfun.inject.Constructable
import com.nextfaze.devfun.internal.SystemUiFlags
import com.nextfaze.devfun.internal.string.*
import com.nextfaze.devfun.invoke.view.From
import com.nextfaze.devfun.invoke.view.ValueSource
import com.nextfaze.devfun.invoke.view.types.value
import java.util.EnumSet

@DeveloperCategory(order = 9_000)
internal object DisplayUtils {
    private val getRawHeightMethod by lazy { Display::class.java.getMethod("getRawHeight") }
    private val getRawWidthMethod by lazy { Display::class.java.getMethod("getRawWidth") }

    /** TODO quick and gross - do something nicer. */
    @DeveloperFunction
    fun showDisplayInformation(activity: Activity): String {
        val str = StringBuilder()

        var fullWidth: Int
        var fullHeight: Int

        fun DisplayMetrics.toJsonString(name: String): String {
            val densityBucket = Density[densityDpi]
            val prevBucket = densityBucket?.prevBucket
            val nextBucket = densityBucket?.nextBucket
            val prevBucketDpi = if (prevBucket != null) ",\n|  prevBucket: $prevBucket" else ""
            val nextBucketDpi = if (nextBucket != null) ",\n|  nextBucket: $nextBucket" else ""
            return """
                |$name {
                |  widthPixels: $widthPixels,
                |  heightPixels: $heightPixels,
                |  density: $density,
                |  scaledDensity: $scaledDensity,
                |  xdpi: $xdpi,
                |  ydpi: $ydpi,
                |  densityDpi: $densityBucket$prevBucketDpi$nextBucketDpi
                |}""".trimMargin()
        }

        val applicationDisplay = activity.applicationContext.windowManager.defaultDisplay
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val displayMetrics = DisplayMetrics()
            applicationDisplay.getMetrics(displayMetrics)
            try {
                val rawWidth = getRawWidthMethod.invoke(applicationDisplay) as Int
                val rawHeight = getRawHeightMethod.invoke(applicationDisplay) as Int
                str += """
            defaultDisplay {
              displayId: ${applicationDisplay.displayId},
              realMetrics {
                rawWidth: $rawWidth,
                rawHeight: $rawHeight
              },
        """.trimIndent()
                fullWidth = rawWidth
                fullHeight = rawHeight
            } catch (t: Throwable) {
                str += """
            defaultDisplay {
              displayId: ${applicationDisplay.displayId},
              realMetrics {
                exception: "$t"
              },
        """.trimIndent()
                fullWidth = 0
                fullHeight = 0
            }
            str += "\n${displayMetrics.toJsonString("displayMetrics").prependIndent("  ")},"
        } else {
            val realDisplayMetrics = DisplayMetrics()
            applicationDisplay.getRealMetrics(realDisplayMetrics)
            str += """
            |defaultDisplay {
            |  displayId: ${applicationDisplay.displayId},
            |  name: "${applicationDisplay.name}",
            ${realDisplayMetrics.toJsonString("realMetrics").prependIndent("|  ")},
        """.trimMargin()
            fullWidth = realDisplayMetrics.widthPixels
            fullHeight = realDisplayMetrics.heightPixels
        }

        val systemUiFlags = SystemUiFlags.fromValue(activity.window.decorView.systemUiVisibility.toLong())
        str += "\n  systemUiFlags: $systemUiFlags,\n"


        str += "  statusBarHeight: "
        val statusBarResId = try {
            activity.application.resources.getIdentifier("status_bar_height", "dimen", "android")
        } catch (t: Throwable) {
            str += t.toString()
            0
        }

        val statusBarHeight = if (statusBarResId != 0) {
            try {
                activity.application.resources.getDimensionPixelSize(statusBarResId).also {
                    str += "$it"
                }
            } catch (t: Throwable) {
                str += t.toString()
                0
            }
        } else {
            0
        }
        str += ",\n"

        val isStatusBarVisible = SystemUiFlags.FULLSCREEN !in systemUiFlags
        val isNavBarVisible = SystemUiFlags.HIDE_NAVIGATION !in systemUiFlags
        var (width, height) = Rect().also { activity.defaultDisplay.getRectSize(it) }.let {
            it.width() to it.height()
        }
        if (fullWidth == width) { // portrait
            if (!isNavBarVisible) {
                height = fullHeight
            }
            if (isStatusBarVisible) {
                height -= statusBarHeight
            }
            val navigationBarHeight = if (isNavBarVisible) fullHeight - height else 0
            str += """
            |  navigationBarHeight: $navigationBarHeight,
            |  activity {
            |    width: $width,
            |    height: $height
            |  }
        """.trimMargin()
        } else {
            if (isStatusBarVisible) {
                height -= statusBarHeight
            }
            if (!isNavBarVisible) {
                width = fullWidth
            }
            val navigationBarHeight = fullWidth - width
            str += """
            |  navigationBarHeight: $navigationBarHeight,
            |  activity {
            |    width: $width,
            |    height: $height
            |  }
        """.trimMargin()
        }

        str += "\n}\n\n"

        str += "defaultDisplay.toString():\n    \"${activity.defaultDisplay}\""

        AlertDialog.Builder(activity)
            .setTitle("Display Metrics")
            .setMessage(str)
            .create()
            .show()

        return str.toString()
    }

    /**
     * Using the activity like this ensures that rotation, status bar, and other system views are taken into account.
     * Thus for multi-window use it should be properly bounded to the relevant app window area.
     * i.e. This is the space available for the *app* not the *device*
     */
    private val Activity.defaultDisplay: Display get() = (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
    private val Context.windowManager: WindowManager get() = getSystemService(Context.WINDOW_SERVICE) as WindowManager

    @Constructable
    class SystemUiFlagsValueSource(private val activity: Activity) : ValueSource<EnumSet<SystemUiFlags>> {
        override val value: EnumSet<SystemUiFlags>
            get() = SystemUiFlags.fromValue(activity.window.decorView.systemUiVisibility.toLong())
    }

    @DeveloperFunction
    fun setSystemWindowUiFlags(activity: Activity, @From(SystemUiFlagsValueSource::class) systemUiFlags: EnumSet<SystemUiFlags>) {
        activity.window.decorView.systemUiVisibility = systemUiFlags.value.toInt()
    }
}

@SuppressLint("InlinedApi")
internal enum class Density constructor(
    private val value: Int,
    val prevBucket: Density? = null,
    private val nextBucketValue: Int = 0
) {
    DENSITY_LOW(DisplayMetrics.DENSITY_LOW),
    DENSITY_MEDIUM(DisplayMetrics.DENSITY_MEDIUM),
    DENSITY_TV(DisplayMetrics.DENSITY_TV),
    DENSITY_HIGH(DisplayMetrics.DENSITY_HIGH),
    DENSITY_260(DisplayMetrics.DENSITY_260, Density.DENSITY_HIGH, DisplayMetrics.DENSITY_XHIGH),
    DENSITY_280(DisplayMetrics.DENSITY_280, Density.DENSITY_HIGH, DisplayMetrics.DENSITY_XHIGH),
    DENSITY_300(DisplayMetrics.DENSITY_300, Density.DENSITY_HIGH, DisplayMetrics.DENSITY_XHIGH),
    DENSITY_XHIGH(DisplayMetrics.DENSITY_XHIGH),
    DENSITY_340(DisplayMetrics.DENSITY_340, Density.DENSITY_XHIGH, DisplayMetrics.DENSITY_XXHIGH),
    DENSITY_360(DisplayMetrics.DENSITY_360, Density.DENSITY_XHIGH, DisplayMetrics.DENSITY_XXHIGH),
    DENSITY_400(DisplayMetrics.DENSITY_400, Density.DENSITY_XHIGH, DisplayMetrics.DENSITY_XXHIGH),
    DENSITY_420(DisplayMetrics.DENSITY_420, Density.DENSITY_XHIGH, DisplayMetrics.DENSITY_XXHIGH),
    DENSITY_440(DisplayMetrics.DENSITY_440, Density.DENSITY_XHIGH, DisplayMetrics.DENSITY_XXHIGH),
    DENSITY_XXHIGH(DisplayMetrics.DENSITY_XXHIGH),
    DENSITY_560(DisplayMetrics.DENSITY_560, Density.DENSITY_XXHIGH, DisplayMetrics.DENSITY_XXXHIGH),
    DENSITY_XXXHIGH(DisplayMetrics.DENSITY_XXXHIGH);

    companion object {
        private val values by lazy { values().associateBy { it.value } }
        operator fun get(value: Int) = values[value]
    }

    val nextBucket by lazy { Density[nextBucketValue] }

    override fun toString() = "$name($value)"
}
