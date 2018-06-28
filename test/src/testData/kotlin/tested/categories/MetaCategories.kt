@file:Suppress("unused", "ClassName")

package tested.categories

import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.test.ExpectedCategoryName
import com.nextfaze.devfun.test.ExpectedCategoryOrder

annotation class MetaCategories

@DeveloperCategory("My Custom Category", order = 5_000)
annotation class CustomCategory

@DeveloperCategory("Context", order = -10_000)
annotation class CategoryContext

@CustomCategory
class ma_SimpleClass {
    @DeveloperFunction
    fun simpleFunction() = ExpectedCategoryName("My Custom Category")
}

@CategoryContext
class ma_ContextClass {
    @DeveloperFunction
    fun functionInContextCat() = listOf(
        ExpectedCategoryName("Context"),
        ExpectedCategoryOrder(-10_000)
    )

    @DeveloperFunction(category = DeveloperCategory("Different Category"))
    fun functionInContextCatClassWithCustomCat() =
        listOf( // the function definition itself will not have the fully composed category
            ExpectedCategoryName("Different Category")
        ) to listOf(
            ExpectedCategoryName("Different Category"),
            ExpectedCategoryOrder(null)
        )

    @DeveloperFunction(category = DeveloperCategory("My Custom Category"))
    fun functionInContextCatClassWithAnotherCat() =
        listOf( // the function definition itself will not have the fully composed category
            ExpectedCategoryName("My Custom Category")
        ) to listOf(
            ExpectedCategoryName("My Custom Category"),
            ExpectedCategoryOrder(5_000)
        )
}
