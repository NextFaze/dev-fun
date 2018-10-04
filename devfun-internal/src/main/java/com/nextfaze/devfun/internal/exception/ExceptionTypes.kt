package com.nextfaze.devfun.internal.exception

import androidx.annotation.Keep
import com.nextfaze.devfun.category.CategoryDefinition
import com.nextfaze.devfun.category.CategoryItem
import com.nextfaze.devfun.function.FunctionDefinition
import com.nextfaze.devfun.function.FunctionInvoke
import com.nextfaze.devfun.function.FunctionItem
import com.nextfaze.devfun.function.InvokeResult
import java.lang.reflect.Method

object ExceptionInvokeResult : InvokeResult {
    override val value: Nothing? = null
    override val exception: Nothing? = null
}

object ExceptionFunctionDefinition : FunctionDefinition {
    override val method: Method by lazy { this.javaClass.getMethod("exception") }
    override val invoke: FunctionInvoke get() = { _, _ -> ExceptionInvokeResult }
    @Keep fun exception() = Unit
}

object ExceptionCategoryDefinition : CategoryDefinition {
    override val name = "Exception"
}

class ExceptionFunctionItem(ex: String) : FunctionItem {
    override val function = ExceptionFunctionDefinition
    override val category = ExceptionCategoryDefinition
    override val name = ex
}

class ExceptionCategoryItem(ex: String) : CategoryItem {
    override val name = "Exception"
    override val items = listOf<FunctionItem>(ExceptionFunctionItem(ex))
}
