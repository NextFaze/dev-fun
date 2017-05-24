package tested.categories

import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.test.ExpectedCategoryName
import com.nextfaze.devfun.test.ExpectedItemGroup

annotation class CategoriesWithGroups

@DeveloperCategory("My Category", group = "Group Fun")
class cwg_SimpleClass {
    @DeveloperFunction
    fun someFun() = listOf(
            ExpectedCategoryName("My Category"),
            ExpectedItemGroup("Group Fun")
    )

    @DeveloperFunction(category = DeveloperCategory("Custom Category 1"))
    fun functionWithCustomCategory() = listOf(
            ExpectedCategoryName("Custom Category 1"),
            ExpectedItemGroup("Group Fun")
    )

    @DeveloperFunction(category = DeveloperCategory("Custom Category 2", group = ""))
    fun functionWithCustomCategoryRemovingGroup() = listOf(
            ExpectedCategoryName("Custom Category 2"),
            ExpectedItemGroup(null)
    )

    @DeveloperFunction(category = DeveloperCategory(group = "I'm Special"))
    fun functionWithCustomGroup() = listOf(
            ExpectedCategoryName("My Category"),
            ExpectedItemGroup("I'm Special")
    )

    @DeveloperFunction(category = DeveloperCategory("Custom Category 3", group = "Custom Group"))
    fun functionWithCustomCategoryAndGroup() = listOf(
            ExpectedCategoryName("Custom Category 3"),
            ExpectedItemGroup("Custom Group")
    )
}
