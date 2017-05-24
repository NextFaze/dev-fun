package tested.categories

import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.test.ExpectedCategoryName

annotation class FunctionDefinedCategory

@DeveloperCategory("Top Level Category")
class fdc_SimpleClass {
    @DeveloperFunction(category = DeveloperCategory("A category defined at the function"))
    fun functionWithCustomCategory() = ExpectedCategoryName("A category defined at the function")

    @DeveloperFunction(category = DeveloperCategory(order = 0x5A2E))
    fun functionWithCustomCategoryOrderOnly() = ExpectedCategoryName("Top Level Category")
}
