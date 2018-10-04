@file:Suppress("ClassName", "unused")

package tested.categories

import com.nextfaze.devfun.category.DeveloperCategory
import com.nextfaze.devfun.function.DeveloperFunction
import com.nextfaze.devfun.internal.splitSimpleName
import com.nextfaze.devfun.test.ExpectedCategoryName
import com.nextfaze.devfun.test.ExpectedCategoryOrder

annotation class CategoryOrderOverloading

private const val ORDER_SET_ON_FUNCTION = "Order Set On Function"
private const val MY_ORDER_IS_9 = "My Order is 9 - Before AAAA"

object coo_ExpectedCategoryOrdering {
    val order = listOf(
        // MIN_VALUE
        coo_ClassThatShouldBeFirst::class.splitSimpleName,

        // default=0
        coo_ClassWithoutCategoryAAAAA::class.splitSimpleName,
        coo_ClassWithoutCategoryBBBBB::class.splitSimpleName,

        // 9,10
        MY_ORDER_IS_9,
        coo_ClassWithOrderAAAA::class.splitSimpleName,

        // 50
        ORDER_SET_ON_FUNCTION,

        // 100
        "Custom Name",

        // 350
        "My Own New Category",

        // MAX_VALUE
        coo_ClassThatShouldBeLast::class.splitSimpleName
    )
}

@DeveloperCategory("Custom Name", order = 100)
class coo_ClassWithCustomCategoryNameAndOrder {
    @DeveloperFunction
    fun someFun() = Unit
}

@DeveloperCategory("Custom Name")
class coo_ClassWithCustomCategoryName {
    @DeveloperFunction
    fun someFun() = Unit
}

class coo_ClassWithoutCategoryBBBBB {
    @DeveloperFunction
    fun someFun() = Unit
}

class coo_ClassWithoutCategoryAAAAA {
    @DeveloperFunction
    fun someFun() = Unit
}

@DeveloperCategory(ORDER_SET_ON_FUNCTION)
class coo_ClassWithAnotherCustomCategoryName {
    @DeveloperFunction
    fun someFun() = Unit
}

@DeveloperCategory(order = Int.MIN_VALUE)
class coo_ClassThatShouldBeFirst {
    @DeveloperFunction
    fun someFun() = Unit
}

@DeveloperCategory(order = Int.MAX_VALUE)
class coo_ClassThatShouldBeLast {
    @DeveloperFunction
    fun someFun() = Unit
}

@DeveloperCategory(order = 10)
class coo_ClassWithOrderAAAA {
    @DeveloperFunction
    fun someFun() = Unit
}

@DeveloperCategory(MY_ORDER_IS_9, order = 9)
class coo_ClassWithOrderShouldBeBefore_AAAA {
    @DeveloperFunction
    fun someFun() = Unit
}

@DeveloperCategory(ORDER_SET_ON_FUNCTION)
class coo_ClassWithAnotherCustomCategoryNameAndOrderOnFun {
    @DeveloperFunction(category = DeveloperCategory(order = 50))
    fun someFun() = Unit
}

@DeveloperCategory("Function using different category", order = 200)
class coo_ClassWithOrderWithFunctionUsingDifferentCategory {
    @DeveloperFunction(category = DeveloperCategory(MY_ORDER_IS_9))
    fun someFun() =
        listOf( // the function definition itself will not have the fully composed category
            ExpectedCategoryName(MY_ORDER_IS_9)
        ) to listOf(
            ExpectedCategoryName(MY_ORDER_IS_9),
            ExpectedCategoryOrder(9)
        )
}

@DeveloperCategory("Class Has Unused Own Order", order = 300)
class coo_ClassWithOrderWithFunctionUsingNewCategory {
    @DeveloperFunction(category = DeveloperCategory("My Own New Category", order = 350))
    fun someFun() =
        listOf( // the function definition itself will not have the fully composed category
            ExpectedCategoryName("My Own New Category")
        ) to listOf(
            ExpectedCategoryName("My Own New Category"),
            ExpectedCategoryOrder(350)
        )
}
