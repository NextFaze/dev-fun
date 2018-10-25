@file:Suppress("unused")

package com.nextfaze.devfun.internal.android

import android.app.ActivityManager
import android.app.Application
import android.app.KeyguardManager
import android.content.ClipboardManager
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

val Context.activityManager: ActivityManager by lazier { service<ActivityManager>(Context.ACTIVITY_SERVICE) }
val Context.keyguardManager: KeyguardManager by lazier { service<KeyguardManager>(Context.KEYGUARD_SERVICE) }
val Context.wifiManager: WifiManager by lazier { service<WifiManager>(Context.WIFI_SERVICE) }
val Context.windowManager: WindowManager by lazier { service<WindowManager>(Context.WINDOW_SERVICE) }
val Context.connectivityManager: ConnectivityManager by lazier { service<ConnectivityManager>(Context.CONNECTIVITY_SERVICE) }
val Context.clipboardManager: ClipboardManager by lazier { service<ClipboardManager>(Context.CLIPBOARD_SERVICE) }

private fun <T : Any, R> lazier(initializer: T.() -> R) = UnsafeLazyImpl(initializer)
private inline fun <reified T : Any> Context.service(name: String) = applicationContext.getSystemService(name) as T

@Suppress("ClassName")
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
