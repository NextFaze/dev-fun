package com.nextfaze.devfun.core

import android.support.annotation.Keep
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.reflect.Method
import kotlin.reflect.jvm.javaMethod

internal val Throwable.stackTraceAsString get() = StringWriter().apply { printStackTrace(PrintWriter(this)) }.toString()

private object ExceptionInvokeResult : InvokeResult {
    override val value = null
    override val exception = null
}

private object ExceptionFunctionDefinition : FunctionDefinition {
    override val method: Method = this::exception.javaMethod!!
    override val invoke: FunctionInvoke get() = { _, _ -> ExceptionInvokeResult }
    @Keep fun exception() = Unit
}

private object ExceptionCategoryDefinition : CategoryDefinition {
    override val name = "Exception"
}

private class ExceptionFunctionItem(ex: String) : FunctionItem {
    override val function = ExceptionFunctionDefinition
    override val category = ExceptionCategoryDefinition
    override val name = ex
}

internal class ExceptionCategoryItem(ex: String) : CategoryItem {
    override val name = "Exception"
    override val items = listOf<FunctionItem>(ExceptionFunctionItem(ex))
}
