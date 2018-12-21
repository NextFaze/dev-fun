@file:Suppress("PropertyName", "unused")

package com.nextfaze.devfun.demo.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nextfaze.devfun.DeveloperAnnotation
import com.nextfaze.devfun.demo.BaseActivity
import com.nextfaze.devfun.demo.BaseFragment
import com.nextfaze.devfun.demo.R
import com.nextfaze.devfun.demo.inject.ActivityInjector
import com.nextfaze.devfun.demo.inject.FragmentInjector
import com.nextfaze.devfun.function.DeveloperFunction
import javax.inject.Inject
import kotlin.annotation.AnnotationRetention.SOURCE
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class DaggerScopesActivity : BaseActivity() {
    @Inject lateinit var injectableNotScopedWithZeroArgsConstructor: InjectableNotScopedWithZeroArgsConstructor
    @Inject lateinit var injectableNotScopedWithMultipleArgsConstructor: InjectableNotScopedWithMultipleArgsConstructor

    @Inject lateinit var injectableActivityScopedViaAnnotation: InjectableActivityScopedViaAnnotation
    @Inject lateinit var injectableActivityScopedViaAnnotationWithArgs: InjectableActivityScopedViaAnnotationWithArgs
    @Inject lateinit var injectableActivityScopedViaProvides: InjectableActivityScopedViaProvides
    @Inject lateinit var injectableActivityScopedViaProvidesWithArgs: InjectableActivityScopedViaProvidesWithArgs

    @Inject lateinit var injectableRetainedScopedViaAnnotation: InjectableRetainedScopedViaAnnotation
    @Inject lateinit var injectableRetainedScopedViaAnnotationWithArgs: InjectableRetainedScopedViaAnnotationWithArgs
    @Inject lateinit var injectableRetainedScopedViaProvides: InjectableRetainedScopedViaProvides
    @Inject lateinit var injectableRetainedScopedViaProvidesWithArgs: InjectableRetainedScopedViaProvidesWithArgs

    @Inject lateinit var injectableSingletonScopedViaAnnotation: InjectableSingletonScopedViaAnnotation
    @Inject lateinit var injectableSingletonScopedViaAnnotationWithArgs: InjectableSingletonScopedViaAnnotationWithArgs
    @Inject lateinit var injectableSingletonScopedViaProvides: InjectableSingletonScopedViaProvides
    @Inject lateinit var injectableSingletonScopedViaProvidesWithArgs: InjectableSingletonScopedViaProvidesWithArgs

    @Inject internal lateinit var injectablePackagePrivateNotScoped: InjectablePackagePrivateNotScoped
    @Inject internal lateinit var injectablePackagePrivateNotScopedWithArgs: InjectablePackagePrivateNotScopedWithArgs
    @Inject internal lateinit var injectablePackagePrivateActivityViaAnnotation: InjectablePackagePrivateActivityViaAnnotation
    @Inject internal lateinit var injectablePackagePrivateRetainedViaAnnotation: InjectablePackagePrivateRetainedViaAnnotation
    @Inject internal lateinit var injectablePackagePrivateSingletonViaAnnotation: InjectablePackagePrivateSingletonViaAnnotation

//    @Inject @field:QualifiedString1 lateinit var qualifiedString1: String
//    @Inject @field:QualifiedString2 lateinit var qualifiedString2: String

    @field:MyQualified("some_name1")
    @Inject internal lateinit var someQualifiedClass1: SomeQualifiedClass

    @field:MyQualified("some_name1", 123)
    @Inject internal lateinit var someQualifiedClass1_123: SomeQualifiedClass

    @field:MyQualified("some_name1", 1234)
    @Inject internal lateinit var someQualifiedClass1_1234: SomeQualifiedClass

    @field:MyQualified("some_name2", 456)
    @Inject internal lateinit var someQualifiedClass2_456: SomeQualifiedClass

    @field:MyQualified("double_set", 999)
    @Inject internal lateinit var qualifiedDoubleSet_999: Set<Double>

    @Inject internal lateinit var unqualifiedDoubleSet: Set<Double>

    override fun inject(injector: ActivityInjector) = injector.inject(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_activity)

        if (savedInstanceState == null) {
            setContentFragment { DaggerScopesFragment() }
        }
    }

//    @DeveloperFunction
//    fun testQualifiedString1(@QualifiedString1 instance: String) =
//        assertEquals(qualifiedString1, instance, "qualifiedString1")
//
//    @DeveloperFunction
//    fun testQualifiedString2(@QualifiedString2 instance: String) =
//        assertEquals(qualifiedString2, instance, "qualifiedString2")

    @DeveloperFunction
    fun testQualifiedClasses(
        @MyQualified("some_name1") arg_someQualifiedClass1: SomeQualifiedClass,
        @MyQualified("some_name1", 123) arg_someQualifiedClass1_123: SomeQualifiedClass,
        @MyQualified("some_name1", 1234) arg_someQualifiedClass1_1234: SomeQualifiedClass,
        @MyQualified("some_name2", 456) arg_someQualifiedClass2_456: SomeQualifiedClass
    ) {
        assertEquals(someQualifiedClass1, arg_someQualifiedClass1, "arg_someQualifiedClass1")
        assertEquals(someQualifiedClass1_123, arg_someQualifiedClass1_123, "arg_someQualifiedClass1_123")
        assertEquals(someQualifiedClass1_1234, arg_someQualifiedClass1_1234, "arg_someQualifiedClass1_1234")
        assertEquals(someQualifiedClass2_456, arg_someQualifiedClass1_1234, "arg_someQualifiedClass2_456")

        assertEquals(someQualifiedClass1, arg_someQualifiedClass1_123, "arg_someQualifiedClass1_123")

        assertNotEquals(someQualifiedClass1, arg_someQualifiedClass1_1234, "someQualifiedClass1 === arg_someQualifiedClass1_1234")
        assertNotEquals(someQualifiedClass1, arg_someQualifiedClass2_456, "someQualifiedClass1 === arg_someQualifiedClass2_456")
    }

    @DeveloperFunction
    fun testMultiset(
        @MyQualified("double_set", 999) arg_qualifiedDoubleSet_999: Set<Double>,
        arg_unqualifiedDoubleSet: Set<Double>
    ) {
        assertEquals(qualifiedDoubleSet_999, arg_qualifiedDoubleSet_999, "arg_qualifiedDoubleSet_999")
        assertEquals(unqualifiedDoubleSet, arg_unqualifiedDoubleSet, "arg_unqualifiedDoubleSet")
        assertNotEquals(qualifiedDoubleSet_999, arg_unqualifiedDoubleSet, "qualifiedDoubleSet_999 === arg_unqualifiedDoubleSet")
        assertTrue(arg_qualifiedDoubleSet_999.isNotEmpty(), "arg_qualifiedDoubleSet_999.isNotEmpty()")
        assertTrue(arg_unqualifiedDoubleSet.isNotEmpty(), "arg_unqualifiedDoubleSet.isNotEmpty()")

        assertEquals(arg_qualifiedDoubleSet_999.size, 4, "arg_unqualifiedDoubleSet.size == 1")
        assertTrue(
            arg_qualifiedDoubleSet_999.containsAll(listOf(1.23, 12.3, 123.4, 9.876)),
            "arg_qualifiedDoubleSet_999.containsAll(listOf(1.23, 12.3, 123.4, 9.876))"
        )

        assertEquals(arg_unqualifiedDoubleSet.size, 1, "arg_unqualifiedDoubleSet.size == 1")
        assertTrue(arg_unqualifiedDoubleSet.contains(4.56), "arg_unqualifiedDoubleSet.contains(4.56)")
    }

    @DeveloperFunction
    fun testInjectableNotScopedWithZeroArgsConstructor(instance: InjectableNotScopedWithZeroArgsConstructor) =
        assertEquals(injectableNotScopedWithZeroArgsConstructor, instance, "injectableNotScopedWithZeroArgsConstructor")

    @DeveloperFunction
    fun testInjectableNotScopedWithMultipleArgsConstructor(instance: InjectableNotScopedWithMultipleArgsConstructor) =
        assertEquals(injectableNotScopedWithMultipleArgsConstructor, instance, "injectableNotScopedWithMultipleArgsConstructor")

    @DeveloperFunction
    internal fun testInjectablePackagePrivateNotScoped(instance: InjectablePackagePrivateNotScoped) =
        assertEquals(injectablePackagePrivateNotScoped, instance, "injectablePackagePrivateNotScoped")

    @DeveloperFunction
    internal fun testInjectablePackagePrivateNotScopedWithArgs(instance: InjectablePackagePrivateNotScopedWithArgs) =
        assertEquals(injectablePackagePrivateNotScopedWithArgs, instance, "injectablePackagePrivateNotScopedWithArgs")

    @DeveloperFunction
    internal fun testInjectablePackagePrivateRetainedViaAnnotation(instance: InjectablePackagePrivateRetainedViaAnnotation) =
        assertEquals(injectablePackagePrivateRetainedViaAnnotation, instance, "injectablePackagePrivateRetainedViaAnnotation")
}

class DaggerScopesFragment : BaseFragment() {
    @Inject lateinit var injectableNotScopedWithZeroArgsConstructor: InjectableNotScopedWithZeroArgsConstructor
    @Inject lateinit var injectableNotScopedWithMultipleArgsConstructor: InjectableNotScopedWithMultipleArgsConstructor

    @Inject lateinit var injectableRetainedScopedViaAnnotation: InjectableRetainedScopedViaAnnotation
    @Inject lateinit var injectableRetainedScopedViaAnnotationWithArgs: InjectableRetainedScopedViaAnnotationWithArgs
    @Inject lateinit var injectableRetainedScopedViaProvides: InjectableRetainedScopedViaProvides
    @Inject lateinit var injectableRetainedScopedViaProvidesWithArgs: InjectableRetainedScopedViaProvidesWithArgs

    @Inject lateinit var injectableSingletonScopedViaAnnotation: InjectableSingletonScopedViaAnnotation
    @Inject lateinit var injectableSingletonScopedViaAnnotationWithArgs: InjectableSingletonScopedViaAnnotationWithArgs
    @Inject lateinit var injectableSingletonScopedViaProvides: InjectableSingletonScopedViaProvides
    @Inject lateinit var injectableSingletonScopedViaProvidesWithArgs: InjectableSingletonScopedViaProvidesWithArgs

    @Inject internal lateinit var injectablePackagePrivateNotScoped: InjectablePackagePrivateNotScoped
    @Inject internal lateinit var injectablePackagePrivateNotScopedWithArgs: InjectablePackagePrivateNotScopedWithArgs
    @Inject internal lateinit var injectablePackagePrivateRetainedViaAnnotation: InjectablePackagePrivateRetainedViaAnnotation
    @Inject internal lateinit var injectablePackagePrivateSingletonViaAnnotation: InjectablePackagePrivateSingletonViaAnnotation

    override fun inject(injector: FragmentInjector) = injector.inject(this)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.dagger_scopes_fragment, container, false)

    @DeveloperFunction
    fun testInjectableNotScopedWithZeroArgsConstructor(instance: InjectableNotScopedWithZeroArgsConstructor) =
        assertEquals(injectableNotScopedWithZeroArgsConstructor, instance, "injectableNotScopedWithZeroArgsConstructor")

    @DeveloperFunction
    fun testInjectableNotScopedWithMultipleArgsConstructor(instance: InjectableNotScopedWithMultipleArgsConstructor) =
        assertEquals(injectableNotScopedWithMultipleArgsConstructor, instance, "injectableNotScopedWithMultipleArgsConstructor")

    @DeveloperFunction
    internal fun testInjectablePackagePrivateNotScoped(instance: InjectablePackagePrivateNotScoped) =
        assertEquals(injectablePackagePrivateNotScoped, instance, "injectablePackagePrivateNotScoped")

    @DeveloperFunction
    internal fun testInjectablePackagePrivateNotScopedWithArgs(instance: InjectablePackagePrivateNotScopedWithArgs) =
        assertEquals(injectablePackagePrivateNotScopedWithArgs, instance, "injectablePackagePrivateNotScopedWithArgs")

    @DeveloperFunction
    internal fun testInjectablePackagePrivateRetainedViaAnnotation(instance: InjectablePackagePrivateRetainedViaAnnotation) =
        assertEquals(injectablePackagePrivateRetainedViaAnnotation, instance, "injectablePackagePrivateRetainedViaAnnotation")
}

@Target(CLASS)
@Retention(SOURCE)
@DeveloperAnnotation(developerCategory = true)
annotation class TestCat(
    val value: String = "Testing & Debugging",
    val group: String = "%CLASS_SN%",
    val order: Int = 200_000
)
