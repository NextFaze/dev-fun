package com.nextfaze.devfun.demo

import android.content.pm.ActivityInfo
import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import org.hamcrest.Matcher

fun orientationLandscape(): ViewAction = OrientationChangeAction(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
fun orientationPortrait(): ViewAction = OrientationChangeAction(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

private class OrientationChangeAction(private val orientation: Int) : ViewAction {
    override fun getConstraints(): Matcher<View> = isRoot()
    override fun getDescription() = "Change orientation to $orientation"

    override fun perform(uiController: UiController, view: View) {
        uiController.loopMainThreadUntilIdle()
        ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED).single().requestedOrientation = orientation

        uiController.loopMainThreadUntilIdle()
        val resumedActivities = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)
        if (resumedActivities.isEmpty()) {
            throw RuntimeException("Could not change orientation")
        }
    }
}
