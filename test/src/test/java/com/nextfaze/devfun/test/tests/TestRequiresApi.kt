package com.nextfaze.devfun.test.tests

import com.nextfaze.devfun.internal.logger
import com.nextfaze.devfun.test.AbstractKotlinKapt3Tester
import com.nextfaze.devfun.test.TestContext
import com.nextfaze.devfun.test.singleFileTests
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import tested.requires_api.FunctionsWithRequiresApi
import java.lang.reflect.Method

@Test(groups = arrayOf("kapt", "compile", "supported", "function", "requiresApi"))
class TestRequiresApi : AbstractKotlinKapt3Tester() {
    private val log = logger()

    @DataProvider(name = "testFunctionRequiresApiData")
    fun testFunctionRequiresApiData(testMethod: Method) = singleFileTests(testMethod, FunctionsWithRequiresApi::class, useSdkInt = 10)

    @Test(dataProvider = "testFunctionRequiresApiData")
    fun testFunctionRequiresApi(test: TestContext) = test.testInvocations(log)
}
