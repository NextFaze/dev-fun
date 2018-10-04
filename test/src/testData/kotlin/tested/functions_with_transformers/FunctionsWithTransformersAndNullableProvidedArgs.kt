@file:Suppress("unused", "ClassName", "PackageName")

package tested.functions_with_transformers

import com.nextfaze.devfun.category.CategoryDefinition
import com.nextfaze.devfun.function.DeveloperFunction
import com.nextfaze.devfun.function.FunctionDefinition
import com.nextfaze.devfun.function.FunctionItem
import com.nextfaze.devfun.function.FunctionTransformer
import com.nextfaze.devfun.function.SimpleFunctionItem
import com.nextfaze.devfun.test.ExpectedArgs
import com.nextfaze.devfun.test.ExpectedItemCount

internal annotation class FunctionsWithTransformersAndNullableProvidedArgs

class ftn_1IsProvided_2IsInjected_3IsProvidedNull_4IsInjected_Transformer : FunctionTransformer {
    override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition): Set<FunctionItem>? {
        return setOf(object : SimpleFunctionItem(functionDefinition, categoryDefinition) {
            override val args get() = listOf("1IsProvided_2IsInjected_3IsProvidedNull_4IsInjected", Unit, null)
        })
    }
}

object ftn_SomePublicObject

class ftn_FunctionsWithTransformersAndNullableArgs {
    @DeveloperFunction(transformer = ftn_1IsProvided_2IsInjected_3IsProvidedNull_4IsInjected_Transformer::class)
    fun thirdArgShouldBeNull(
        providedArg1: String,
        injectedArg1: ftn_SomePublicObject,
        providedArg2: Float?,
        injectedArg2: Boolean
    ) = listOf(ExpectedItemCount(1)) to listOf(
        ExpectedArgs(
            listOf(
                providedArg1 to "1IsProvided_2IsInjected_3IsProvidedNull_4IsInjected",
                injectedArg1 to ftn_SomePublicObject,
                providedArg2 to null,
                injectedArg2 to false
            )
        )
    )
}
