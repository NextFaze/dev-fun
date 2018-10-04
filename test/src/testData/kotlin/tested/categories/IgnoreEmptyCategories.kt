@file:Suppress("unused", "ClassName")

package tested.categories

import com.nextfaze.devfun.category.DeveloperCategory
import com.nextfaze.devfun.function.DeveloperFunction
import com.nextfaze.devfun.test.ExpectedCategoryName
import com.nextfaze.devfun.test.ExpectedItemCount

annotation class IgnoreEmptyCategories

@DeveloperCategory
class iec_ClassWithoutFunctions {
    fun someFun() = "I'm not annotated"
}

@DeveloperCategory("Custom Name")
class iec_ClassWithoutFunctionsWithCustomName {
    fun someFun() = "I'm not annotated"
}

class iec_ClassWithoutExplicitCategory {
    @DeveloperFunction
    fun someFun() = ExpectedCategoryName("Iec Class Without Explicit Category")
}

class iec_ClassWithRequiresApiFunction {
    @DeveloperFunction(requiresApi = 7)
    fun someFun() = listOf(ExpectedItemCount(1))
}
