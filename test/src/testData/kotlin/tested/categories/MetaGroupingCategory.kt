@file:Suppress("unused", "ClassName")

package tested.categories

import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.test.ExpectedCategoryName
import com.nextfaze.devfun.test.ExpectedItemGroup

annotation class MetaGroupingCategory


@DeveloperCategory("My Custom Grouping Category")
annotation class CustomGroupingCategory(val group: String = "")

@CustomGroupingCategory
class mgc_SimpleClass {
    @DeveloperFunction
    fun functionInCustomGroupingCat() =
        listOf(
            ExpectedCategoryName("My Custom Grouping Category"),
            ExpectedItemGroup(null)
        )
}

@CustomGroupingCategory("Group1")
class mgc_SimpleGroup1Class {
    @DeveloperFunction
    fun functionInGroupedCustomGroupingCat() =
        listOf(
            ExpectedCategoryName("My Custom Grouping Category"),
            ExpectedItemGroup("Group1")
        )
}

@CustomGroupingCategory("Group2")
class mgc_SimpleClassWithFunGroup {
    @DeveloperFunction(category = DeveloperCategory(group = "SpecialGroup"))
    fun functionInGroupedCustomGroupingCat() =
        listOf(
            ExpectedCategoryName("My Custom Grouping Category"),
            ExpectedItemGroup("SpecialGroup")
        )
}


@DeveloperCategory("My Custom Default Grouping Category")
annotation class CustomDefaultGroupingCategory(val group: String = "DefaultGroup")

@CustomDefaultGroupingCategory
class mgc_DefaultGroupingClass {
    @DeveloperFunction
    fun simpleFunction() =
        listOf(
            ExpectedCategoryName("My Custom Default Grouping Category"),
            ExpectedItemGroup("DefaultGroup")
        )
}

@CustomDefaultGroupingCategory("Grouped")
class mgc_DefaultGroupingClassGrouped {
    @DeveloperFunction
    fun simpleFunction() =
        listOf(
            ExpectedCategoryName("My Custom Default Grouping Category"),
            ExpectedItemGroup("Grouped")
        )
}
