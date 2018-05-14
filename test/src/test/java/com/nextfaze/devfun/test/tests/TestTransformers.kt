package com.nextfaze.devfun.test.tests

import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.test.AbstractKotlinKapt3Tester
import com.nextfaze.devfun.test.TestContext
import com.nextfaze.devfun.test.singleFileTests
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import tested.functions_with_transformers.FunctionsWithTransformers
import tested.functions_with_transformers.FunctionsWithTransformersAndNullableProvidedArgs
import tested.functions_with_transformers.FunctionsWithTransformersAndProvidedArgs
import java.lang.reflect.Method
import kotlin.test.assertFailsWith

@Test(groups = arrayOf("kapt", "compile", "transformer"))
class TestTransformers : AbstractKotlinKapt3Tester() {
    private val log = logger()

    @DataProvider(name = "testFunctionTransformersData")
    fun testFunctionTransformersData(testMethod: Method) = singleFileTests(
        testMethod,
        FunctionsWithTransformers::class,
        FunctionsWithTransformersAndProvidedArgs::class
    )

    @Test(dataProvider = "testFunctionTransformersData", groups = arrayOf("supported"))
    fun testFunctionTransformers(test: TestContext) = test.testInvocations(log)


    // Nullable types are not not properly supported

    @DataProvider(name = "testFunctionTransformersUnsupportedData")
    fun testFunctionTransformersUnsupportedData(testMethod: Method) = singleFileTests(
        testMethod,
        FunctionsWithTransformersAndNullableProvidedArgs::class
    )

    @Test(dataProvider = "testFunctionTransformersUnsupportedData", groups = arrayOf("unsupported"))
    fun testFunctionTransformersUnsupported(test: TestContext) {
        assertFailsWith<AssertionError> {
            test.testInvocations(log)
        }
    }
}
