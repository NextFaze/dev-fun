package com.nextfaze.devfun.test.tests

import com.nextfaze.devfun.generated.DevFunGenerated
import com.nextfaze.devfun.test.AbstractKotlinKapt3Tester
import com.nextfaze.devfun.test.TestContext
import com.nextfaze.devfun.test.singleFileTests
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import tested.no_annotations.NoAnnotations
import java.lang.reflect.Method
import java.util.ServiceLoader
import kotlin.test.assertTrue

class TestNoAnnotations : AbstractKotlinKapt3Tester() {
    @DataProvider(name = "testNoAnnotationsData")
    fun testNoAnnotationsData(testMethod: Method) = singleFileTests(testMethod, NoAnnotations::class)

    @Suppress("UNUSED_PARAMETER")
    @Test(dataProvider = "testNoAnnotationsData")
    fun testNoAnnotations(test: TestContext) {
        val generated = ServiceLoader.load(DevFunGenerated::class.java)
        assertTrue("Generated list was not empty!") { generated.count() == 0 }
    }
}
