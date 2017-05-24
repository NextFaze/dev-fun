package com.nextfaze.devfun.utils.glide

import android.content.Context
import android.text.format.Formatter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.engine.cache.MemoryCache
import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.internal.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch

/**
 * Utility functions for [Glide](https://github.com/bumptech/glide).
 */
@DeveloperCategory(order = 9_000)
object GlideUtils {
    private val log = logger()

    /**
     * Log Glide's bitmap pool and memory cache size stats to logcat.
     */
    @DeveloperFunction
    fun logMemoryInfo(context: Context): String {
        val b = StringBuilder("==== Glide Memory Info ====")
        try {
            val bitmapPool = Glide.get(context).bitmapPool
            val memoryCache = Glide.get(context).memoryCache

            val poolMax = bitmapPool.maxSize
            val poolCurr = bitmapPool.currentSize
            val cacheMax = memoryCache.maxSize
            val cacheCurr = memoryCache.currentSize

            b.append("\nGlide.bitmapPool.maxSize=").append(poolMax.fileSize(context))
            b.append("\nGlide.bitmapPool.currentSize=").append(poolCurr.fileSize(context)).append(" (").append(poolCurr pctOf poolMax).append("%)")
            b.append("\nGlide.memoryCache.maxSize=").append(cacheMax.fileSize(context))
            b.append("\nGlide.memoryCache.currentSize=").append(cacheCurr.fileSize(context)).append(" (").append(cacheCurr pctOf cacheMax).append("%)")
        } catch (t: Throwable) {
            log.d(t) { "Exception" }
            b.append("Exception: ").append(t.message)
        }
        b.append("\n\n")

        return b.toString().also { log.i { it } }
    }

    /**
     * Clear Glide's memory cache.
     *
     * @see MemoryCache
     */
    @DeveloperFunction
    fun clearMemoryCache(context: Context): String {
        Glide.get(context).clearMemory()
        System.gc()
        return "Glide bitmap pool and memory cache cleared!".also {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Clear Glide's disk cache.
     */
    @DeveloperFunction
    fun clearDiskCache(context: Context): String {
        launch(CommonPool) {
            Glide.get(context).clearDiskCache()
        }
        return "Glide disk cache cleared!".also {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }
}

private val Glide.memoryCache get() = this::class.java.getDeclaredField("memoryCache").apply { isAccessible = true }.get(this) as MemoryCache
private val BitmapPool.currentSize get() = this::class.java.getDeclaredField("currentSize").apply { isAccessible = true }.get(this) as Int
private infix fun Int.pctOf(max: Int) = if (this <= 0) 0f else this * 100f / max
private fun Int.fileSize(context: Context) = Formatter.formatFileSize(context, this.toLong())
