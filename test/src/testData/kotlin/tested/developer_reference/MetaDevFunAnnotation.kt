@file:Suppress("PackageName", "unused")

package tested.developer_reference

import com.nextfaze.devfun.DeveloperAnnotation
import com.nextfaze.devfun.category.DeveloperCategory
import com.nextfaze.devfun.test.ExpectedCategoryName
import com.nextfaze.devfun.test.ExpectedItemGroup
import com.nextfaze.devfun.test.ExpectedNamesTest

annotation class MetaDevFunAnnotation

/*****************************/

@DeveloperAnnotation(developerFunction = true)
annotation class DevPropertyOnlyValueFieldTest(val value: String = "")

class ClassWithProperties {
    @DevPropertyOnlyValueFieldTest
    var defaultNamed = ExpectedNamesTest(listOf("Default Named"))

    @DevPropertyOnlyValueFieldTest("Forced Name")
    var customName = ExpectedNamesTest(listOf("Forced Name"))
}

@DeveloperCategory(group = "Some Group")
class ClassWithGroup {
    @DevPropertyOnlyValueFieldTest
    var prop = ExpectedItemGroup("Some Group")
}

/*****************************/

@DeveloperAnnotation(developerFunction = true)
annotation class DevPropertyOnlyValueFieldWithDefaultTest(val value: String = "Custom Default")

class TestingDefaultValues {
    @DevPropertyOnlyValueFieldWithDefaultTest
    var defaultNamed = ExpectedNamesTest(listOf("Custom Default"))

    @DevPropertyOnlyValueFieldWithDefaultTest("Custom Name")
    var customName = ExpectedNamesTest(listOf("Custom Name"))

    @DevPropertyOnlyValueFieldWithDefaultTest("")
    fun blankName() = ExpectedNamesTest(listOf(""))
}

/*****************************/

@DeveloperAnnotation(developerFunction = true)
annotation class DevPropertyWithValueAndCategoryTest(
    val value: String = "",
    val category: DeveloperCategory = DeveloperCategory(group = "Custom Default Group")
)

class ClassWithItemsThatHaveGroups {
    @DevPropertyWithValueAndCategoryTest
    var defaultGroup = ExpectedItemGroup("Custom Default Group")

    @DevPropertyWithValueAndCategoryTest(category = DeveloperCategory(group = "Custom Group"))
    var customGroup = ExpectedItemGroup("Custom Group")
}

@DeveloperCategory(group = "Some Group")
class ClassWithTopLevelGroupClassWithItemsThatHaveGroups {
    @DevPropertyWithValueAndCategoryTest
    var defaultGroup = ExpectedItemGroup("Custom Default Group")

    @DevPropertyWithValueAndCategoryTest(category = DeveloperCategory(group = "Custom Group"))
    var customGroup = ExpectedItemGroup("Custom Group")
}

/*****************************/

@DeveloperAnnotation(developerFunction = true)
annotation class DevPropertyWithValueAndCategoryWithCustomNameAndGroupTest(
    val value: String = "",
    val category: DeveloperCategory = DeveloperCategory(value = "Custom Default Category", group = "Custom Default Group")
)

class ClassWithItemsThatHaveCatNamesAndGroups {
    @DevPropertyWithValueAndCategoryWithCustomNameAndGroupTest
    var defaultGroup = listOf(
        ExpectedCategoryName("Custom Default Category"),
        ExpectedItemGroup("Custom Default Group")
    )

    @DevPropertyWithValueAndCategoryWithCustomNameAndGroupTest(category = DeveloperCategory(group = "Custom Group"))
    var customGroup = listOf(
        ExpectedCategoryName("Custom Default Category"),
        ExpectedItemGroup("Custom Group")
    )
}

@DeveloperCategory(value = "Some Category", group = "Some Group")
class ClassWithTopLevelGroupClassWithItemsThatHaveCatNamesAndGroups {
    @DevPropertyWithValueAndCategoryWithCustomNameAndGroupTest
    var defaultGroup = listOf(
        ExpectedCategoryName("Custom Default Category"),
        ExpectedItemGroup("Custom Default Group")
    )

    @DevPropertyWithValueAndCategoryWithCustomNameAndGroupTest(category = DeveloperCategory(value = "Custom Category"))
    var customCategory = listOf(
        ExpectedCategoryName("Custom Category"),
        ExpectedItemGroup("Custom Default Group")
    )

    @DevPropertyWithValueAndCategoryWithCustomNameAndGroupTest(
        category = DeveloperCategory(value = "Custom Category", group = "Custom Group")
    )
    var customCatAndGroup = listOf(
        ExpectedCategoryName("Custom Category"),
        ExpectedItemGroup("Custom Group")
    )
}
