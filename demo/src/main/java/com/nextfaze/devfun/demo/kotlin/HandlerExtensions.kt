package com.nextfaze.devfun.demo.kotlin

import android.os.Handler
import java.util.concurrent.TimeUnit

internal fun Handler.post(delay: Long = 0,
                          timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
                          body: () -> Unit)
        = if (delay > 0) this.postDelayed(body, timeUnit.toMillis(delay)) else this.post(body)
