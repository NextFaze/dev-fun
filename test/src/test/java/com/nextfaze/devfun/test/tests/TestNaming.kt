package com.nextfaze.devfun.test.tests

import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.test.AbstractKotlinKapt3Tester
import com.nextfaze.devfun.test.TestContext
import com.nextfaze.devfun.test.singleFileTests
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import tested.custom_names.CategoryNaming
import tested.custom_names.FunctionNaming
import java.lang.reflect.Method

class TestNaming : AbstractKotlinKapt3Tester() {
    private val log = logger()

    @DataProvider(name = "testNamingData")
    fun testNamingData(testMethod: Method) = singleFileTests(
        testMethod,
        FunctionNaming::class,
        CategoryNaming::class
    ).last().let { arrayOf(it) }

    @Test(dataProvider = "testNamingData", groups = ["kapt", "compile", "supported", "naming", "function", "category"])
    fun testNaming(test: TestContext) = test.testInvocations(log)
}
