package com.nextfaze.devfun.internal.android

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.view.WindowManager
import kotlin.reflect.KProperty

fun Context.registerActivityLifecycleCallbacks(activityLifecycleCallbacks: Application.ActivityLifecycleCallbacks) =
    activityLifecycleCallbacks.apply { (applicationContext as Application).registerActivityLifecycleCallbacks(activityLifecycleCallbacks) }

internal fun Context.unregisterActivityCallbacks(callbacks: Application.ActivityLifecycleCallbacks?) {
    callbacks?.let {
        (this.applicationContext as Application).unregisterActivityLifecycleCallbacks(callbacks)
    }
}

val Context.activityManager: ActivityManager by lazier { applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager }
val Context.wifiManager: WifiManager by lazier { applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager }
val Context.windowManager: WindowManager by lazier { applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager }
val Context.connectivityManager: ConnectivityManager by lazier {
    applicationContext.getSystemService(
        Context.CONNECTIVITY_SERVICE
    ) as ConnectivityManager
}

private fun <T : Any, R> lazier(initializer: T.() -> R) = UnsafeLazyImpl(initializer)

private object UNINITIALIZED_VALUE

private class UnsafeLazyImpl<in T, out R>(initializer: T.() -> R) {
    private var initializer: (T.() -> R)? = initializer
    private var _value: Any? = UNINITIALIZED_VALUE

    operator fun getValue(thisRef: T, property: KProperty<*>): R {
        if (_value === UNINITIALIZED_VALUE) {
            _value = initializer!!(thisRef)
            initializer = null
        }
        @Suppress("UNCHECKED_CAST")
        return _value as R
    }

    private val isInitialized get() = _value !== UNINITIALIZED_VALUE

    override fun toString(): String = if (isInitialized) _value.toString() else "Lazy value not initialized yet."
}
