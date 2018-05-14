package tested.categories

import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.internal.string.*

annotation class CategoryOrderOverloading

object coo_ExpectedCategoryOrdering {
    val order = listOf(
        // MIN_VALUE
        coo_ClassThatShouldBeFirst::class.splitSimpleName,

        // default=0
        coo_ClassWithoutCategoryAAAAA::class.splitSimpleName,
        coo_ClassWithoutCategoryBBBBB::class.splitSimpleName,

        // 9,10
        coo_ClassWithOrderShouldBeBefore_AAAA::class.splitSimpleName,
        coo_ClassWithOrderAAAA::class.splitSimpleName,

        // 50
        "Order Set On Function",

        // 100
        "Custom Name",

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

@DeveloperCategory("Order Set On Function")
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

@DeveloperCategory(order = 9)
class coo_ClassWithOrderShouldBeBefore_AAAA {
    @DeveloperFunction
    fun someFun() = Unit
}

@DeveloperCategory("Order Set On Function")
class coo_ClassWithAnotherCustomCategoryNameAndOrderOnFun {
    @DeveloperFunction(category = DeveloperCategory(order = 50))
    fun someFun() = Unit
}
