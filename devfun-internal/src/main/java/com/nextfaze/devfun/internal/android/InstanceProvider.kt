package com.nextfaze.devfun.internal.android

import android.app.Activity
import com.nextfaze.devfun.inject.InstanceProvider

interface AndroidInstanceProviderInternal : InstanceProvider {
    val activity: Activity?
}
