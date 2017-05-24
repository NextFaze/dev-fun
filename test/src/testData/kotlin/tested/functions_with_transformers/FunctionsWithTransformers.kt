@file:Suppress("unused")

package tested.functions_with_transformers

import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.core.*
import com.nextfaze.devfun.test.ExpectedItemCount
import com.nextfaze.devfun.test.ExpectedNamesTest
import com.nextfaze.devfun.test.SingleItemExpectedNameTest

internal annotation class FunctionsWithTransformers

class NullTransformer : FunctionTransformer {
    override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition) = null
}

class NoItemGeneratedTransformer : FunctionTransformer {
    override fun accept(functionDefinition: FunctionDefinition) = true
    override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition) = emptyList<FunctionItem>()
}

class NeverAcceptTransformer : FunctionTransformer {
    override fun accept(functionDefinition: FunctionDefinition) = false
    override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition) =
            listOf(SimpleFunctionItem(functionDefinition, categoryDefinition))
}

class OneToOneTransformer : FunctionTransformer {
    override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition): Collection<SimpleFunctionItem> =
            listOf(object : SimpleFunctionItem(functionDefinition, categoryDefinition) {
                override val name get() = "OneToOneTransformer:${super.name}"
            })
}

class OneToTenTransformer : FunctionTransformer {
    override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition): Collection<FunctionItem>? =
            (0..9).map {
                object : SimpleFunctionItem(functionDefinition, categoryDefinition) {
                    override val name get() = "OneToTenTransformer: 1 🠒 ? no. $it: ${super.name}"
                }
            }
}

private class PrivateInjectableNopTransformer(private val oneToOneTransformer: OneToOneTransformer) : FunctionTransformer {
    override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition) =
            oneToOneTransformer.apply(functionDefinition, categoryDefinition)
}

class DelegatedTransformer(singleFunctionTransformer: SingleFunctionTransformer) : FunctionTransformer by singleFunctionTransformer {
    override fun accept(functionDefinition: FunctionDefinition) = !functionDefinition.method.name.endsWith("1")
}

class FunctionsWithSimpleTransformers {
    @DeveloperFunction(transformer = NullTransformer::class)
    fun nullTransformed() = listOf(ExpectedItemCount(0))

    @DeveloperFunction(transformer = NoItemGeneratedTransformer::class)
    fun noItemGeneratedTransformed() = listOf(ExpectedItemCount(0))

    @DeveloperFunction(transformer = NeverAcceptTransformer::class)
    fun neverAcceptTransformed() = listOf(ExpectedItemCount(0))

    @DeveloperFunction(transformer = OneToOneTransformer::class)
    fun oneToOneTransformed() = listOf(
            ExpectedItemCount(1),
            SingleItemExpectedNameTest("OneToOneTransformer:One To One Transformed")
    )

    @DeveloperFunction(transformer = OneToTenTransformer::class)
    fun oneToTenTransformed() = listOf(
            ExpectedItemCount(10),
            ExpectedNamesTest((0..9).map { "OneToTenTransformer: 1 🠒 ? no. $it: One To Ten Transformed" })
    )

    @DeveloperFunction(transformer = PrivateInjectableNopTransformer::class)
    fun privateInjectableNopTransformed() = listOf(
            ExpectedItemCount(1),
            SingleItemExpectedNameTest("OneToOneTransformer:Private Injectable Nop Transformed")
    )

    @DeveloperFunction(transformer = DelegatedTransformer::class)
    fun delegatedTransformed0() = listOf(
            ExpectedItemCount(1),
            SingleItemExpectedNameTest("Delegated Transformed0")
    )

    @DeveloperFunction(transformer = DelegatedTransformer::class)
    fun delegatedTransformed1() = listOf(ExpectedItemCount(0))

    @DeveloperFunction(value = "Custom Name", transformer = DelegatedTransformer::class)
    fun delegatedTransformed2() = listOf(
            ExpectedItemCount(1),
            SingleItemExpectedNameTest("Custom Name")
    )
}
