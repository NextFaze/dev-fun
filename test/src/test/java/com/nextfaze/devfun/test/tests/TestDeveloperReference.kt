package com.nextfaze.devfun.test.tests

import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.test.AbstractKotlinKapt3Tester
import com.nextfaze.devfun.test.TestContext
import com.nextfaze.devfun.test.singleFileTests
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import tested.developer_reference.*
import java.lang.reflect.Method
import kotlin.test.assertFalse

@Test(groups = ["kapt", "compile", "compiler", "developerReference"])
class TestDeveloperReference : AbstractKotlinKapt3Tester() {
    private val log = logger()

    @DataProvider(name = "testDeveloperReferencesData")
    fun testDeveloperReferencesData(testMethod: Method) =
        singleFileTests(
            testMethod,
            ExecutableReferences::class,
            TypeReferences::class,
            VarReferences::class
        )

    @Test(dataProvider = "testDeveloperReferencesData")
    fun testDeveloperReferences(test: TestContext) {
        assertFalse(test.devRefs.isEmpty(), "Expected devRefs but was empty!")
        test.testInvocations(log)
    }

    @DataProvider(name = "testDeveloperReferencePropertiesData")
    fun testDeveloperReferencePropertiesData(testMethod: Method) = singleFileTests(testMethod, MetaDevFunAnnotation::class)

    @Test(dataProvider = "testDeveloperReferencePropertiesData")
    fun testDeveloperReferenceProperties(test: TestContext) = test.testInvocations(log)

    @DataProvider(name = "testCustomPropertiesData")
    fun testCustomPropertiesData(testMethod: Method) =
        singleFileTests(
            testMethod,
            CustomPropertiesSimpleTypes::class,
            CustomPropertiesSimpleArrayTypes::class,
            CustomPropertiesStrings::class,
            CustomPropertiesClassTypes::class,
            CustomPropertiesEnums::class,
            CustomPropertiesAnnotationTypes::class
        )

    @Test(dataProvider = "testCustomPropertiesData")
    fun testCustomProperties(test: TestContext) {
        assertFalse(test.devRefs.isEmpty(), "Expected devRefs but was empty!")
        test.testInvocations(log)
    }
}
