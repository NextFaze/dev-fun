package com.nextfaze.devfun.demo.kotlin

import android.app.ActivityManager

fun ActivityManager.RunningAppProcessInfo.isNamed(name: String) = this.processName.endsWith(":$name")
