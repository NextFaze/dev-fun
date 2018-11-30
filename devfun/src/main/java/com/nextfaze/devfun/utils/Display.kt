package com.nextfaze.devfun.utils

import android.app.Activity
import com.nextfaze.devfun.function.DeveloperFunction
import com.nextfaze.devfun.inject.Constructable
import com.nextfaze.devfun.internal.SystemUiFlags
import com.nextfaze.devfun.invoke.view.From
import com.nextfaze.devfun.invoke.view.ValueSource
import com.nextfaze.devfun.invoke.view.types.value
import java.util.EnumSet

object DisplayUtils {
    @Constructable
    class SystemUiFlagsValueSource(private val activity: Activity) : ValueSource<EnumSet<SystemUiFlags>> {
        override val value: EnumSet<SystemUiFlags>
            get() = SystemUiFlags.fromValue(activity.window.decorView.systemUiVisibility.toLong())
    }

    @DeveloperFunction
    fun setSystemUiFlags(activity: Activity, @From(SystemUiFlagsValueSource::class) systemUiFlags: EnumSet<SystemUiFlags>) {
        activity.window.decorView.systemUiVisibility = systemUiFlags.value.toInt()
    }

}
