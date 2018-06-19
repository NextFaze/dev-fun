@file:Suppress("unused")

package tested.categories

import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.internal.splitSimpleName

annotation class CategoryOrdering

object co_ExpectedCategoryOrdering {
    val order = listOf(
        // MIN_VALUE
        co_ClassThatShouldBeFirst::class.splitSimpleName,

        // default=0
        co_ClassWithoutCategoryAAAAA::class.splitSimpleName,
        co_ClassWithoutCategoryBBBBB::class.splitSimpleName,

        // 9,10
        co_ClassWithOrderShouldBeBefore_AAAA::class.splitSimpleName,
        co_ClassWithOrderAAAA::class.splitSimpleName,

        // 50
        co_ClassWithFunctionCategory::class.splitSimpleName,

        // 100
        "My Named Category (order=100 set at fun)",

        // MAX_VALUE
        co_ClassThatShouldBeLast::class.splitSimpleName
    )
}

@DeveloperCategory
class co_ClassWithNoFunctions

@DeveloperCategory("Custom Name")
class co_ClassWithNoFunctionsAndCustomName

class co_ClassWithoutCategoryBBBBB {
    @DeveloperFunction
    fun someFun() = Unit
}

class co_ClassWithoutCategoryAAAAA {
    @DeveloperFunction
    fun someFun() = Unit
}

@DeveloperCategory(order = Int.MIN_VALUE)
class co_ClassThatShouldBeFirst {
    @DeveloperFunction
    fun someFun() = Unit
}

@DeveloperCategory(order = Int.MAX_VALUE)
class co_ClassThatShouldBeLast {
    @DeveloperFunction
    fun someFun() = Unit
}

@DeveloperCategory(order = 10)
class co_ClassWithOrderAAAA {
    @DeveloperFunction
    fun someFun() = Unit
}

@DeveloperCategory(order = 9)
class co_ClassWithOrderShouldBeBefore_AAAA {
    @DeveloperFunction
    fun someFun() = Unit
}

class co_ClassWithFunctionCategory {
    @DeveloperFunction(category = DeveloperCategory(order = 50))
    fun someFun() = Unit
}

@DeveloperCategory("My Named Category (order=100 set at fun)")
class co_ClassWithNamedCategory {
    @DeveloperFunction(category = DeveloperCategory(order = 100))
    fun someFun() = Unit
}

class co_ClassAddingSomeComplexity {
    @DeveloperFunction(category = DeveloperCategory("My Named Category (order=100 set at fun)"))
    fun funRedeclareCategory() = Unit
}
