package com.nextfaze.devfun.internal

import android.os.Build

val isEmulator by lazy(LazyThreadSafetyMode.NONE) {
    Build.PRODUCT == "sdk" ||
            Build.PRODUCT == "google_sdk_x86" ||
            Build.PRODUCT == "sdk_x86" ||
            Build.PRODUCT == "sdk_google_phone_x86" ||
            Build.PRODUCT.contains("genymotion", ignoreCase = true) ||
            Build.DISPLAY.contains("vbox86p", ignoreCase = true)
}
