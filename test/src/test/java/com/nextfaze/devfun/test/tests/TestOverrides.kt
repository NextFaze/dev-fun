package com.nextfaze.devfun.test.tests

import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.test.AbstractKotlinKapt3Tester
import com.nextfaze.devfun.test.TestContext
import com.nextfaze.devfun.test.singleFileTests
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import tested.overrides.AbstractFunctions
import tested.overrides.OverriddenFunctions
import java.lang.reflect.Method

class TestOverrides : AbstractKotlinKapt3Tester() {
    private val log = logger()

    @DataProvider(name = "testOverridesData")
    fun testOverridesData(testMethod: Method) = singleFileTests(
        testMethod,
        OverriddenFunctions::class,
        AbstractFunctions::class
    )

    @Test(dataProvider = "testOverridesData", groups = ["kapt", "compile", "supported"])
    fun testOverrides(test: TestContext) = test.testInvocations(log)
}
