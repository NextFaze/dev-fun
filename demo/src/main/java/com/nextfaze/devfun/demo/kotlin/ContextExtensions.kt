package com.nextfaze.devfun.demo.kotlin

import android.app.Activity
import android.content.Context
import android.content.Intent

inline fun <reified T : Activity> Context.startActivity(flags: Int = 0) =
        this.startActivity(Intent(this, T::class.java).addFlags(flags))
