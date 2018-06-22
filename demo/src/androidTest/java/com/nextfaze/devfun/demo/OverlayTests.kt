package com.nextfaze.devfun.demo

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OverlayTests {
    @Rule
    @JvmField
    val activityRule = ActivityTestRule(WelcomeActivity::class.java)

    @Test
    fun permissionsDialogShouldNotBeShownDuringTests() {
        onView(withId(R.id.signInButton)).perform(click())
    }
}
