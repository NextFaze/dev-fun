package com.nextfaze.devfun.test.tests

import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.test.AbstractKotlinKapt3Tester
import com.nextfaze.devfun.test.TestContext
import com.nextfaze.devfun.test.singleFileTests
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import tested.meta_annotations.MetaCategories
import tested.meta_annotations.MetaDevFunctions
import tested.meta_annotations.MetaGroupingCategory
import java.lang.reflect.Method

@Test(groups = ["kapt", "compile", "supported", "meta", "category"])
class TestMetaAnnotations : AbstractKotlinKapt3Tester() {
    private val log = logger()

    @DataProvider(name = "testMetaAnnotationsData")
    fun testMetaAnnotationsData(testMethod: Method) =
        singleFileTests(
            testMethod,
            MetaCategories::class,
            MetaGroupingCategory::class,
            MetaDevFunctions::class
        )

    @Test(dataProvider = "testMetaAnnotationsData")
    fun testMetaAnnotations(test: TestContext) = test.testInvocations(log)
}
