package com.nextfaze.devfun.demo.test

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.runner.AndroidJUnit4
import com.nextfaze.devfun.core.call
import com.nextfaze.devfun.core.devFun
import com.nextfaze.devfun.demo.R
import com.nextfaze.devfun.demo.fragmentActivityTestRule
import com.nextfaze.devfun.demo.orientationLandscape
import com.nextfaze.devfun.demo.orientationPortrait
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.reflect.KClass
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
            // Check that DevFun can resolve them correctly
            with(activity) {
                assertFailsWith<AssertionError> {
                    // dagger does not hold a reference to fetch for non-scoped types
                    // this should fail here, but succeed when injected in-context
                    assertEquals(injectableNotScopedWithZeroArgsConstructor, devFun.get())
                }
                assertFailsWith<AssertionError> {
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
                    // dagger does not hold a reference to fetch for non-scoped types
                    assertEquals(injectablePackagePrivateNotScoped, devFun.get())
                }
                assertFailsWith<AssertionError> {
                    // dagger does not hold a reference to fetch for non-scoped types
                    assertEquals(injectablePackagePrivateNotScopedWithArgs, devFun.get())
                }
                assertEquals(injectablePackagePrivateActivityViaAnnotation, devFun.get())
                assertEquals(injectablePackagePrivateRetainedViaAnnotation, devFun.get())
                assertEquals(injectablePackagePrivateSingletonViaAnnotation, devFun.get())
            }
            with(fragment) {
                assertFailsWith<AssertionError> {
                    // dagger does not hold a reference to fetch for non-scoped types
                    assertEquals(injectableNotScopedWithZeroArgsConstructor, devFun.get())
                }
                assertFailsWith<AssertionError> {
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
                    // dagger does not hold a reference to fetch for non-scoped types
                    assertEquals(injectablePackagePrivateNotScoped, devFun.get())
                }
                assertFailsWith<AssertionError> {
                    // dagger does not hold a reference to fetch for non-scoped types
                    assertEquals(injectablePackagePrivateNotScopedWithArgs, devFun.get())
                }
                assertEquals(injectablePackagePrivateRetainedViaAnnotation, devFun.get())
                assertEquals(injectablePackagePrivateSingletonViaAnnotation, devFun.get())
            }

            // TODO? Correct injection would get type from fragment itself rather than searching components
            // Now check that call-aware injection can grab it based on where it's called.
            // i.e. if we're calling a function in the Fragment then we should be able to inject the non-scoped instances to the call.
            // We can't get the correct non-scoped instance when we don't know the context as there could be more than one instance etc.
            val testDevFunctions = listOf(
//                    DaggerScopesActivity::testInjectableNotScopedWithZeroArgsConstructor,
//                    DaggerScopesActivity::testInjectableNotScopedWithMultipleArgsConstructor,
//                    DaggerScopesActivity::testInjectablePackagePrivateNotScoped,
//                    DaggerScopesActivity::testInjectablePackagePrivateNotScopedWithArgs,
                DaggerScopesActivity::testInjectablePackagePrivateRetainedViaAnnotation,
//                    DaggerScopesFragment::testInjectableNotScopedWithZeroArgsConstructor,
//                    DaggerScopesFragment::testInjectableNotScopedWithMultipleArgsConstructor,
//                    DaggerScopesFragment::testInjectablePackagePrivateNotScoped,
//                    DaggerScopesFragment::testInjectablePackagePrivateNotScopedWithArgs,
                DaggerScopesFragment::testInjectablePackagePrivateRetainedViaAnnotation
            )
            val items = devFun.categories.flatMap { it.items }
            testDevFunctions.forEach { devFunFunc ->
                val clazz = devFunFunc.parameters.first().type.classifier as KClass<*>
                val name = devFunFunc.name
                val item = items.single { it.function.clazz == clazz && it.function.method.name.startsWith(name) }
                item.call()!!.exception?.run { throw this }
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
