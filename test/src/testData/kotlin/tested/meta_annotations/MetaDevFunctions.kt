@file:Suppress("unused", "ClassName", "PackageName")

package tested.meta_annotations

import com.nextfaze.devfun.annotations.DeveloperAnnotation
import com.nextfaze.devfun.core.CategoryItem
import com.nextfaze.devfun.core.FunctionItem
import com.nextfaze.devfun.test.NOPCategoryItem
import com.nextfaze.devfun.test.NOPFunctionItem
import kotlin.annotation.AnnotationRetention.BINARY
import kotlin.test.expect

annotation class MetaDevFunctions

/*
TODO Add something like these to DevFun?
 */

/** Example of meta categories with `BINARY` retention for better proguard compatibility. */
@Retention(BINARY)
@DeveloperAnnotation(developerCategory = true)
annotation class DevCategory(
    val value: String = "",
    val group: String = "",
    val order: Int = 0
)

/** Example of meta dev. function with `BINARY` retention for better proguard compatibility. */
@Retention(BINARY)
@DeveloperAnnotation(developerFunction = true)
annotation class DevFunction(
    val value: String = "",
    val category: DevCategory = DevCategory()
)

@DevCategory
class mdf_BinaryRetainedCustomDevCategoryAndDevFunction {
    @DevFunction
    fun testAllDefaults(functionItem: FunctionItem, categoryItem: CategoryItem) {
        if (functionItem === NOPFunctionItem || categoryItem === NOPCategoryItem) return

        expect("Mdf Binary Retained Custom Dev Category And Dev Function") { categoryItem.name }
        expect(null) { functionItem.group }
        expect(0) { categoryItem.order }
    }

    @DevFunction(category = DevCategory(group = "Different Group - So Meta!"))
    fun testFunctionDefinedGroup(functionItem: FunctionItem, categoryItem: CategoryItem) {
        if (functionItem === NOPFunctionItem || categoryItem === NOPCategoryItem) return

        expect("Mdf Binary Retained Custom Dev Category And Dev Function") { categoryItem.name }
        expect("Different Group - So Meta!") { functionItem.group }
        expect(0) { categoryItem.order }
    }

    @DevFunction(category = DevCategory("Function Cat", group = "FunGroup!", order = -1_098))
    fun testAllFunctionDefined(functionItem: FunctionItem, categoryItem: CategoryItem) {
        if (functionItem === NOPFunctionItem || categoryItem === NOPCategoryItem) return

        expect("Function Cat") { categoryItem.name }
        expect("FunGroup!") { functionItem.group }
        expect(-1_098) { categoryItem.order }
    }
}

@DevCategory("Custom Name")
class mdf_CategoryDefinedOnClass {
    @DevFunction
    fun testAllDefaults(functionItem: FunctionItem, categoryItem: CategoryItem) {
        if (functionItem === NOPFunctionItem || categoryItem === NOPCategoryItem) return

        expect("Custom Name") { categoryItem.name }
        expect(null) { functionItem.group }
        expect(0) { categoryItem.order }
    }

    @DevFunction(category = DevCategory(group = "Different Group - So Meta!"))
    fun testFunctionDefinedGroup(functionItem: FunctionItem, categoryItem: CategoryItem) {
        if (functionItem === NOPFunctionItem || categoryItem === NOPCategoryItem) return

        expect("Custom Name") { categoryItem.name }
        expect("Different Group - So Meta!") { functionItem.group }
        expect(0) { categoryItem.order }
    }

    @DevFunction(category = DevCategory("Function Cat", group = "FunGroup!", order = -1_098))
    fun testAllFunctionDefined(functionItem: FunctionItem, categoryItem: CategoryItem) {
        if (functionItem === NOPFunctionItem || categoryItem === NOPCategoryItem) return

        expect("Function Cat") { categoryItem.name }
        expect("FunGroup!") { functionItem.group }
        expect(-1_098) { categoryItem.order }
    }
}
