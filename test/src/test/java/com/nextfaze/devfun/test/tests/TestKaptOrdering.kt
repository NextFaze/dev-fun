package com.nextfaze.devfun.test.tests

import com.nextfaze.devfun.generated.DevFunGenerated
import com.nextfaze.devfun.test.AbstractKotlinKapt3Tester
import com.nextfaze.devfun.test.TestContext
import com.nextfaze.devfun.test.combine
import com.nextfaze.devfun.test.singleFileTests
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import tested.generation_order.GenerationOrder
import java.lang.reflect.Method
import java.util.ServiceLoader
import kotlin.test.expect

@Test(groups = arrayOf("kapt", "compile", "supported", "compiler"))
class TestKaptOrdering : AbstractKotlinKapt3Tester() {

    @DataProvider(name = "testGenerationOrderData")
    fun testGenerationOrderData(testMethod: Method) = singleFileTests(testMethod, GenerationOrder::class)

    private lateinit var firstPass: List<String>

    @Suppress("UNUSED_PARAMETER")
    @Test(dataProvider = "testGenerationOrderData")
    fun testGenerationOrderFirstPass(test: TestContext) {
        val devFun = ServiceLoader.load(DevFunGenerated::class.java).single()
        firstPass = devFun.functionDefinitions.map { it.method.toString().substringBefore('$') }
    }

    @Suppress("UNUSED_PARAMETER")
    @Test(dataProvider = "testGenerationOrderData", dependsOnMethods = arrayOf("testGenerationOrderFirstPass"))
    fun testGenerationOrder(test: TestContext) {
        val devFun = ServiceLoader.load(DevFunGenerated::class.java).single()
        val secondPass = devFun.functionDefinitions.map { it.method.toString().substringBefore('$') }
        firstPass.combine(secondPass).forEach {
            expect(it.second, "Method ordering inconsistent") { it.first }
        }
    }
}
