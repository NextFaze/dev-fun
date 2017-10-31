package com.nextfaze.devfun.demo

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.filters.LargeTest
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class SimpleInstrumentedTest {
    @Rule
    @JvmField
    val activityRule = ActivityTestRule(WelcomeActivity::class.java)

    @Test
    fun clickSignInButton() {
        onView(withId(R.id.signInButton)).perform(click())
    }
}
