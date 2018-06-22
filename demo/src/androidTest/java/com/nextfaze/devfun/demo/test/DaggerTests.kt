package com.nextfaze.devfun.demo.test

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.runner.AndroidJUnit4
import com.nextfaze.devfun.core.devFun
import com.nextfaze.devfun.demo.R
import com.nextfaze.devfun.demo.fragmentActivityTestRule
import com.nextfaze.devfun.demo.orientationLandscape
import com.nextfaze.devfun.demo.orientationPortrait
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotSame

@RunWith(AndroidJUnit4::class)
class DaggerInjectionTests {
    @Rule
    @JvmField
    val activityRule = fragmentActivityTestRule<DaggerScopesActivity, DaggerScopesFragment>()

    @Test
    fun validateStandardDaggerBehaviour() {
        onView(withId(R.id.daggerScopesFragmentRoot)).check(matches(isCompletelyDisplayed()))

        // Sanity checks
        fun sanityChecks(activity: DaggerScopesActivity, fragment: DaggerScopesFragment) {
            assertNotSame(activity.injectableNotScopedWithZeroArgsConstructor, fragment.injectableNotScopedWithZeroArgsConstructor)
            assertNotSame(activity.injectableNotScopedWithMultipleArgsConstructor, fragment.injectableNotScopedWithMultipleArgsConstructor)

            assertEquals(activity.injectableRetainedScopedViaAnnotation, fragment.injectableRetainedScopedViaAnnotation)
            assertEquals(activity.injectableRetainedScopedViaAnnotationWithArgs, fragment.injectableRetainedScopedViaAnnotationWithArgs)
            assertEquals(activity.injectableRetainedScopedViaProvides, fragment.injectableRetainedScopedViaProvides)
            assertEquals(activity.injectableRetainedScopedViaProvidesWithArgs, fragment.injectableRetainedScopedViaProvidesWithArgs)

            assertEquals(activity.injectableSingletonScopedViaAnnotation, fragment.injectableSingletonScopedViaAnnotation)
            assertEquals(activity.injectableSingletonScopedViaAnnotationWithArgs, fragment.injectableSingletonScopedViaAnnotationWithArgs)
            assertEquals(activity.injectableSingletonScopedViaProvides, fragment.injectableSingletonScopedViaProvides)
            assertEquals(activity.injectableSingletonScopedViaProvidesWithArgs, fragment.injectableSingletonScopedViaProvidesWithArgs)

            // package private to test accessibility issues (not Dagger/scoping per se)
            assertNotSame(activity.injectablePackagePrivateNotScoped, fragment.injectablePackagePrivateNotScoped)
            assertNotSame(activity.injectablePackagePrivateNotScopedWithArgs, fragment.injectablePackagePrivateNotScopedWithArgs)
            assertEquals(activity.injectablePackagePrivateRetainedViaAnnotation, fragment.injectablePackagePrivateRetainedViaAnnotation)
            assertEquals(activity.injectablePackagePrivateSingletonViaAnnotation, fragment.injectablePackagePrivateSingletonViaAnnotation)
        }

        fun testDevFunGet(activity: DaggerScopesActivity, fragment: DaggerScopesFragment) {
            // Now check that DevFun can resolve them correctly
            with(activity) {
                assertFailsWith<AssertionError> {
                    // TODO? Correct injection would get type from activity itself rather than searching components
                    // dagger does not hold a reference to fetch for non-scoped types
                    assertEquals(injectableNotScopedWithZeroArgsConstructor, devFun.get())
                }
                assertFailsWith<AssertionError> {
                    // TODO? Correct injection would get type from activity itself rather than searching components
                    // dagger does not hold a reference to fetch for non-scoped types
                    assertEquals(injectableNotScopedWithMultipleArgsConstructor, devFun.get())
                }

                assertEquals(injectableActivityScopedViaAnnotation, devFun.get())
                assertEquals(injectableActivityScopedViaAnnotationWithArgs, devFun.get())
                assertEquals(injectableActivityScopedViaProvides, devFun.get())
                assertEquals(injectableActivityScopedViaProvidesWithArgs, devFun.get())

                assertEquals(injectableRetainedScopedViaAnnotation, devFun.get())
                assertEquals(injectableRetainedScopedViaAnnotationWithArgs, devFun.get())
                assertEquals(injectableRetainedScopedViaProvides, devFun.get())
                assertEquals(injectableRetainedScopedViaProvidesWithArgs, devFun.get())

                assertEquals(injectableSingletonScopedViaAnnotation, devFun.get())
                assertEquals(injectableSingletonScopedViaAnnotationWithArgs, devFun.get())
                assertEquals(injectableSingletonScopedViaProvides, devFun.get())
                assertEquals(injectableSingletonScopedViaProvidesWithArgs, devFun.get())

                assertFailsWith<AssertionError> {
                    // TODO? Correct injection would get type from activity itself rather than searching components
                    // dagger does not hold a reference to fetch for non-scoped types
                    assertEquals(injectablePackagePrivateNotScoped, devFun.get())
                }
                assertFailsWith<AssertionError> {
                    // TODO? Correct injection would get type from activity itself rather than searching components
                    // dagger does not hold a reference to fetch for non-scoped types
                    assertEquals(injectablePackagePrivateNotScopedWithArgs, devFun.get())
                }
                assertEquals(injectablePackagePrivateActivityViaAnnotation, devFun.get())
                assertEquals(injectablePackagePrivateRetainedViaAnnotation, devFun.get())
                assertEquals(injectablePackagePrivateSingletonViaAnnotation, devFun.get())
            }
            with(fragment) {
                assertFailsWith<AssertionError> {
                    // TODO? Correct injection would get type from fragment itself rather than searching components
                    // dagger does not hold a reference to fetch for non-scoped types
                    assertEquals(injectableNotScopedWithZeroArgsConstructor, devFun.get())
                }
                assertFailsWith<AssertionError> {
                    // TODO? Correct injection would get type from fragment itself rather than searching components
                    // dagger does not hold a reference to fetch for non-scoped types
                    assertEquals(injectableNotScopedWithMultipleArgsConstructor, devFun.get())
                }

                assertEquals(injectableRetainedScopedViaAnnotation, devFun.get())
                assertEquals(injectableRetainedScopedViaAnnotationWithArgs, devFun.get())
                assertEquals(injectableRetainedScopedViaProvides, devFun.get())
                assertEquals(injectableRetainedScopedViaProvidesWithArgs, devFun.get())

                assertEquals(injectableSingletonScopedViaAnnotation, devFun.get())
                assertEquals(injectableSingletonScopedViaAnnotationWithArgs, devFun.get())
                assertEquals(injectableSingletonScopedViaProvides, devFun.get())
                assertEquals(injectableSingletonScopedViaProvidesWithArgs, devFun.get())

                assertFailsWith<AssertionError> {
                    // TODO? Correct injection would get type from fragment itself rather than searching components
                    // dagger does not hold a reference to fetch for non-scoped types
                    assertEquals(injectablePackagePrivateNotScoped, devFun.get())
                }
                assertFailsWith<AssertionError> {
                    // TODO? Correct injection would get type from fragment itself rather than searching components
                    // dagger does not hold a reference to fetch for non-scoped types
                    assertEquals(injectablePackagePrivateNotScopedWithArgs, devFun.get())
                }
                assertEquals(injectablePackagePrivateRetainedViaAnnotation, devFun.get())
                assertEquals(injectablePackagePrivateSingletonViaAnnotation, devFun.get())
            }
        }

        val activity = activityRule.activity
        val fragment = activityRule.fragment
        sanityChecks(activity, fragment)
        testDevFunGet(activity, fragment)

        onView(isRoot()).perform(orientationLandscape())

        try {
            val activity2 = activityRule.activity
            assertNotSame(activity, activity2)
            assertEquals(fragment, activityRule.fragment)

            sanityChecks(activity2, fragment)
            testDevFunGet(activity2, fragment)
        } finally {
            onView(isRoot()).perform(orientationPortrait())
        }
    }
}
