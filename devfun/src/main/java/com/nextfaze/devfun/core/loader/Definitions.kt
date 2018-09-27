package com.nextfaze.devfun.core.loader

import android.content.Context
import androidx.annotation.Keep
import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.core.*
import com.nextfaze.devfun.generated.DevFunGenerated
import com.nextfaze.devfun.inject.Constructable
import java.lang.reflect.Method
import java.util.ServiceLoader
import kotlin.reflect.jvm.javaMethod

@DeveloperCategory("DevFun")
internal class DefinitionsLoader {
    var definitions: List<DevFunGenerated> = listOf()
        private set
        get() {
            if (field.isNotEmpty()) return field

            field = ServiceLoader.load(DevFunGenerated::class.java).run {
                reload()
                toList()
            }

            return if (field.isEmpty()) listOf(NoGeneratedDefinitionsFound) else field
        }

    @DeveloperFunction
    fun reloadItemDefinitions() {
        definitions = listOf()
    }
}

private object NoGeneratedDefinitionsFound : DevFunGenerated {
    override val categoryDefinitions: List<CategoryDefinition> = listOf()
    override val functionDefinitions: List<FunctionDefinition> = listOf(EmptyFunctionDefinition)
}

@Constructable
private class EmptyFunctionTransformer(private val context: Context) : FunctionTransformer {
    override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition) = listOf(
        object : FunctionItem {
            override val function = EmptyFunctionDefinition
            override val category = EmptyCategoryDefinition
            override val group = context.getText(R.string.df_devfun_no_generated_definitions_found)
            override val name = context.getText(R.string.df_devfun_no_generated_definitions_found_reasons)
        }
    )
}

private object EmptyFunctionDefinition : FunctionDefinition {
    override val transformer = EmptyFunctionTransformer::class
    override val method: Method = this::empty.javaMethod!!
    override val invoke: FunctionInvoke = { _, _ -> EmptyInvokeResult }
    @Keep fun empty() = Unit
}

private object EmptyCategoryDefinition : CategoryDefinition

private object EmptyInvokeResult : InvokeResult {
    override val value: Nothing? = null
    override val exception: Nothing? = null
}
