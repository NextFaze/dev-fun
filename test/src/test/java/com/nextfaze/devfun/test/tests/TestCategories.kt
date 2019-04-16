package com.nextfaze.devfun.test.tests

import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.internal.splitSimpleName
import com.nextfaze.devfun.test.AbstractKotlinKapt3Tester
import com.nextfaze.devfun.test.TestContext
import com.nextfaze.devfun.test.combine
import com.nextfaze.devfun.test.singleFileTests
import org.testng.annotations.DataProvider
import org.testng.annotations.Test
import tested.categories.CategoriesWithGroups
import tested.categories.CategoryOrderOverloading
import tested.categories.CategoryOrdering
import tested.categories.ContextAware
import tested.categories.FunctionDefinedCategory
import tested.categories.IgnoreEmptyCategories
import tested.categories.co_ExpectedCategoryOrdering
import tested.categories.coo_ExpectedCategoryOrdering
import java.lang.reflect.Method
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.expect

@Test(groups = ["kapt", "compile", "supported", "category"])
class TestCategories : AbstractKotlinKapt3Tester() {
    private val log = logger()

    //
    // IgnoreEmptyCategories
    //

    @DataProvider(name = "testCategoriesIgnoreEmptyData")
    fun testCategoriesIgnoreEmptyData(testMethod: Method) = singleFileTests(testMethod, IgnoreEmptyCategories::class, useSdkInt = 10)

    @Test(dataProvider = "testCategoriesIgnoreEmptyData", enabled = false, groups = ["requiresApi"])
    fun testCategoriesIgnoreEmpty(test: TestContext) {
        test.testInvocations(log)

        // generated
        val funCats = test.funDefs.map { it.clazz }.toSet()
        val catDefNames = test.catDefs.map { it.name }.toSet()
        val allCats = (test.catDefs.map { it.clazz } + funCats).toSet()

        // processed
        val actualCats = test.devFun.categories

        // Loop all generated/possibly generated categories
        allCats.forEach { cat ->
            log.d { "cat=$cat" }

            // Find all function definitions that have this category
            val functionsWithCat = funCats.filter { it == cat }

            // If no functions have this, then it should not be present in the final output
            if (functionsWithCat.isEmpty()) {
                log.d { "Category $cat is not referenced by any function definitions" }
                val simpleName = cat?.splitSimpleName // todo
                assertTrue("Category $cat is not referenced in any generated functions but is seen in final item set") {
                    actualCats.none { it.name == simpleName } &&
                        actualCats.all { ac -> catDefNames.none { it == ac.name } }
                }
            } else {
                log.d { "Category $cat is referenced by ${functionsWithCat.size} function definitions: $functionsWithCat" }

                // TODO...
            }
        }
    }

    //
    // CategoryOrdering
    //

    @DataProvider(name = "testCategoryOrderingData")
    fun testCategoryOrderingData(testMethod: Method) = singleFileTests(testMethod, CategoryOrdering::class)

    @Test(dataProvider = "testCategoryOrderingData")
    fun testCategoryOrdering(test: TestContext) {
        val actualCats = test.devFun.categories.map { it.name }
        actualCats.combine(co_ExpectedCategoryOrdering.order).forEach {
            expect(it.second, "Ordering inconsistent") { it.first }
        }
    }

    //
    // CategoryOrderOverloading
    //

    @DataProvider(name = "testCategoryOrderOverloadingData")
    fun testCategoryOrderOverloadingData(testMethod: Method) = singleFileTests(testMethod, CategoryOrderOverloading::class)

    @Test(dataProvider = "testCategoryOrderOverloadingData")
    fun testCategoryOrderOverloading(test: TestContext) {
        val actualCats = test.devFun.categories.map { it.name }
        val actualCatsAsStrList = actualCats.joinToString("\n")
        val expectedCatsAsStrList = coo_ExpectedCategoryOrdering.order.joinToString("\n")

        assertEquals(
            actualCats.size,
            coo_ExpectedCategoryOrdering.order.size,
            "actualCats:\n$actualCatsAsStrList\n=== vs ===\ncoo_ExpectedCategoryOrdering.order:\n$expectedCatsAsStrList"
        )

        actualCats.combine(coo_ExpectedCategoryOrdering.order).forEach {
            expect(it.second, "Ordering inconsistent:\n$actualCatsAsStrList\n=== vs ===\n$expectedCatsAsStrList") { it.first }
        }

        test.testInvocations(log)
    }

    //
    // Functions with Category
    //

    @DataProvider(name = "testFunctionsWithCategoryData")
    fun testFunctionsWithCategoryData(testMethod: Method) = singleFileTests(testMethod, FunctionDefinedCategory::class)

    @Test(dataProvider = "testFunctionsWithCategoryData")
    fun testFunctionsWithCategory(test: TestContext) = test.testInvocations(log)

    //
    // Categories with Groups
    //

    @DataProvider(name = "testCategoriesWithGroupsData")
    fun testCategoriesWithGroupsData(testMethod: Method) = singleFileTests(testMethod, CategoriesWithGroups::class)

    @Test(dataProvider = "testCategoriesWithGroupsData")
    fun testCategoriesWithGroups(test: TestContext) = test.testInvocations(log)

    //
    // Context-aware Categories
    //

    @DataProvider(name = "testContextAwareData")
    fun testContextAwareData(testMethod: Method) = singleFileTests(testMethod, ContextAware::class)

    @Test(dataProvider = "testContextAwareData")
    fun testContextAwareData(test: TestContext) = test.testInvocations(log)
}
