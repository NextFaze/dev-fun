package com.nextfaze.devfun.test.tests

import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.test.AbstractKotlinKapt3Tester
import com.nextfaze.devfun.test.TestContext
import com.nextfaze.devfun.test.singleFileTests
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import tested.developer_annotation.ExecutableReferences
import tested.developer_annotation.MetaDevFunAnnotation
import tested.developer_annotation.UnsupportedElements
import java.lang.reflect.Method
import kotlin.test.assertFailsWith

@Test(groups = ["kapt", "compile", "compiler", "developerAnnotation"])
class TestDeveloperAnnotation : AbstractKotlinKapt3Tester() {
    private val log = logger()

    @DataProvider(name = "testDeveloperAnnotationsData")
    fun testDeveloperAnnotationsData(testMethod: Method) = singleFileTests(testMethod, ExecutableReferences::class)

    @Test(dataProvider = "testDeveloperAnnotationsData")
    fun testDeveloperAnnotations(test: TestContext) = test.testInvocations(log)

    @DataProvider(name = "testDeveloperAnnotationPropertiesData")
    fun testDeveloperAnnotationPropertiesData(testMethod: Method) = singleFileTests(testMethod, MetaDevFunAnnotation::class)

    @Test(dataProvider = "testDeveloperAnnotationPropertiesData")
    fun testDeveloperAnnotationProperties(test: TestContext) = test.testInvocations(log)

    //
    // Currently we only support ExecutableElement types
    //

    @DataProvider(name = "testDeveloperAnnotationsUnsupportedData")
    fun testDeveloperAnnotationsUnsupportedData(testMethod: Method) =
        singleFileTests(
            testMethod,
            UnsupportedElements::class,
            autoKaptAndCompile = false
        )

    @Test(dataProvider = "testDeveloperAnnotationsUnsupportedData", groups = ["unsupported"])
    fun testDeveloperAnnotationsUnsupported(test: TestContext) {
        assertFailsWith<IllegalStateException> {
            test.runKapt(compileClasspath, processors)
        }
    }
}
