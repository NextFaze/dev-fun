package com.nextfaze.devfun.utils.glide

import android.content.Context
import android.text.format.Formatter
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.engine.cache.MemoryCache
import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.internal.log.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch

/**
 * Utility functions for [Glide](https://github.com/bumptech/glide).
 */
@DeveloperCategory(order = 9_000)
object GlideUtils {
    private val log = logger()

    /** Log Glide's bitmap pool and memory cache size stats to logcat. */
    @DeveloperFunction
    fun showMemoryInfo(context: Context): String {
        fun Long.fileSize() = Formatter.formatFileSize(context, this)
        infix fun Long.pctOf(max: Long) = if (this <= 0) 0f else this * 100f / max

        return try {
            val glide = GlideCompat(context)

            val poolMax = glide.bitmapPoolMaxSize
            val poolCurr = glide.bitmapPoolCurrentSize
            val cacheMax = glide.memoryCacheMaxSize
            val cacheCurr = glide.memoryCacheCurrentSize

            """
            |==== Glide Memory Info ====
            |bitmapPool.maxSize=${poolMax.fileSize()}
            |bitmapPool.currentSize=${poolCurr.fileSize()} (${poolCurr pctOf poolMax}%)
            |memoryCache.maxSize=${cacheMax.fileSize()}
            |memoryCache.currentSize=${cacheCurr.fileSize()} (${cacheCurr pctOf cacheMax}%)
            |""".trimMargin()
        } catch (t: Throwable) {
            log.d(t) { "Exception" }
            "Exception: ${t.message}"
        }.also {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
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

    /**
     * Clear Glide's disk and memory cache.
     *
     * @see clearMemoryCache
     * @see clearDiskCache
     */
    @DeveloperFunction("Clear Disk & Memory Cache")
    fun clearDiskAndMemoryCache(context: Context): String {
        Glide.get(context).clearMemory()
        System.gc()
        launch(CommonPool) {
            Glide.get(context).clearDiskCache()
        }
        return "Glide bitmap pool, memory cache, and disk cache cleared!".also {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }
}

/** Provides support for Glide 3.x and 4.x for [BitmapPool] and [MemoryCache] changed size types ([Int] to [Long]) */
private class GlideCompat(context: Context) {
    companion object {
        private val memoryCacheField by lazy { Glide::class.java.getDeclaredField("memoryCache").apply { isAccessible = true } }
    }

    private val glide = Glide.get(context)

    private val BitmapPool.currentSizeCompat
        get() = this::class.java.getDeclaredField("currentSize").apply { isAccessible = true }.get(this) as Number

    private val BitmapPool.maxSizeCompat
        get() = this::class.java.getDeclaredField("maxSize").apply { isAccessible = true }.get(this) as Number

    private val MemoryCache.currentSizeCompat
        get() = MemoryCache::class.java.getDeclaredMethod("getCurrentSize").apply { isAccessible = true }.invoke(this) as Number

    private val MemoryCache.maxSizeCompat
        get() = MemoryCache::class.java.getDeclaredMethod("getMaxSize").apply { isAccessible = true }.invoke(this) as Number

    private val bitmapPool: BitmapPool by lazy { glide.bitmapPool }
    private val memoryCache by lazy { memoryCacheField.get(glide) as MemoryCache }

    val bitmapPoolCurrentSize get() = bitmapPool.currentSizeCompat.toLong()
    val bitmapPoolMaxSize get() = bitmapPool.maxSizeCompat.toLong()

    val memoryCacheCurrentSize get() = memoryCache.currentSizeCompat.toLong()
    val memoryCacheMaxSize get() = memoryCache.maxSizeCompat.toLong()
}
