package com.nextfaze.devfun.menu.controllers

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.view.WindowCallbackWrapper
import android.view.KeyEvent
import com.nextfaze.devfun.internal.ActivityProvider
import com.nextfaze.devfun.internal.logger
import com.nextfaze.devfun.internal.registerOnActivityCreated
import com.nextfaze.devfun.internal.t
import com.nextfaze.devfun.menu.DeveloperMenu
import com.nextfaze.devfun.menu.MenuController

private val DEFAULT_KEY_SEQUENCE = intArrayOf(
        KeyEvent.KEYCODE_VOLUME_DOWN,
        KeyEvent.KEYCODE_VOLUME_DOWN,
        KeyEvent.KEYCODE_VOLUME_UP,
        KeyEvent.KEYCODE_VOLUME_DOWN
)

class KeySequence(context: Context,
                  private val activityProvider: ActivityProvider,
                  private val keySequence: IntArray = DEFAULT_KEY_SEQUENCE) : MenuController {
    private val log = logger()
    private val application = context.applicationContext as Application

    private var listener: Application.ActivityLifecycleCallbacks? = null
    private var developerMenu: DeveloperMenu? = null

    override fun attach(developerMenu: DeveloperMenu) {
        this.developerMenu = developerMenu
        listener = application.registerOnActivityCreated(this::onActivityCreated)
    }

    override fun detach() {
        application.unregisterActivityLifecycleCallbacks(listener)
        listener = null
        developerMenu = null
    }

    private fun onActivityCreated(activity: Activity, @Suppress("UNUSED_PARAMETER") savedInstanceState: Bundle?) {
        activity.window?.let {
            it.callback = object : WindowCallbackWrapper(it.callback) {
                override fun dispatchKeyEvent(event: KeyEvent): Boolean {
                    onKeyEvent(event.action, event.keyCode)
                    return super.dispatchKeyEvent(event)
                }
            }
        }
    }

    private var sequenceIdx = 0
    private fun onKeyEvent(action: Int, code: Int) {
        if (developerMenu?.isVisible ?: true || action != KeyEvent.ACTION_DOWN) return

        if (code == keySequence[sequenceIdx]) {
            sequenceIdx++
            log.t { "Matched code=$code or $sequenceIdx/${keySequence.size}" }
        } else {
            sequenceIdx = 0
            log.t { "Sequence reset" }
        }

        if (sequenceIdx == keySequence.size) {
            sequenceIdx = 0
            (activityProvider() as? FragmentActivity)?.let { developerMenu?.show(it) }
        }
    }

    override fun onShown() {
        sequenceIdx = 0
    }

    override fun onDismissed() {
        sequenceIdx = 0
    }
}
