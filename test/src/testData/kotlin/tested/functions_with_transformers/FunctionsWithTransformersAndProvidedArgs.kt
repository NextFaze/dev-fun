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
import com.nextfaze.devfun.test.ExpectedNamesTest
import com.nextfaze.devfun.test.SingleItemExpectedNameTest
import com.nextfaze.devfun.test.TestableTest
import kotlin.test.assertNotNull
import kotlin.test.expect

internal annotation class FunctionsWithTransformersAndProvidedArgs

class fta_OneToOneWithOneArgTransformer : FunctionTransformer {
    override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition): Set<FunctionItem>? {
        return setOf(object : SimpleFunctionItem(functionDefinition, categoryDefinition) {
            override val args get() = listOf("Single Arg")
        })
    }
}

class fta_OneToOneWithMultipleArgsTransformer : FunctionTransformer {
    override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition): Set<FunctionItem>? {
        return setOf(object : SimpleFunctionItem(functionDefinition, categoryDefinition) {
            override val args get() = listOf("First Arg", 2, "My 3rd arg is a CharSequence")
        })
    }
}

class fta_OneToFiveTransformerWithArgs : FunctionTransformer {
    override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition): Set<FunctionItem>? {
        return (0..4).map {
            object : SimpleFunctionItem(functionDefinition, categoryDefinition) {
                override val name get() = "OneToFiveTransformerWithArgs: 1 🠒 ? no. $it: ${super.name}"
                override val args get() = listOf(this, it + 1)
            }
        }.toSet()
    }
}

private class fta_OneToOneWithProvidedAndInjectedArgsTransformer : FunctionTransformer {
    override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition): Set<FunctionItem>? {
        return setOf(object : SimpleFunctionItem(functionDefinition, categoryDefinition) {
            override val args get() = listOf("Provided arg1", 2.0f)
        })
    }

    override fun equals(other: Any?) = other is fta_OneToOneWithProvidedAndInjectedArgsTransformer
    override fun hashCode(): Int = javaClass.hashCode()
}

internal class fta_1IsProvided_2IsInjected_3IsProvided_4IsInjected_Transformer : FunctionTransformer {
    internal object NestedObject

    override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition): Set<FunctionItem>? {
        return setOf(object : SimpleFunctionItem(functionDefinition, categoryDefinition) {
            override val args get() = listOf("1IsProvided_2IsInjected_3IsProvided_4IsInjected", Unit, 2.1f, Unit, Unit)
        })
    }
}

object fta_SomePublicObject
private object fta_SomePrivateObject

class fta_FunctionsWithTransformersAndArgs {
    @DeveloperFunction(transformer = fta_OneToOneWithOneArgTransformer::class)
    fun oneToOneWithOneArgTransformed(arg1: String) = listOf(
        ExpectedItemCount(1)
    ) to listOf(
        ExpectedArgs(listOf(arg1 to "Single Arg"))
    )

    @DeveloperFunction(transformer = fta_OneToOneWithMultipleArgsTransformer::class)
    private fun oneToOneWithMultipleArgsTransformed(arg1: String, arg2: Int, arg3: CharSequence) = listOf(
        ExpectedItemCount(1)
    ) to listOf(
        ExpectedArgs(listOf(arg1 to "First Arg", arg2 to 2, arg3 to "My 3rd arg is a CharSequence"))
    )

    @DeveloperFunction(transformer = fta_OneToFiveTransformerWithArgs::class)
    fun oneToFiveTransformedWithArgs(item: FunctionItem, number: Int) = listOf(
        ExpectedItemCount(5),
        ExpectedNamesTest((0..4).map { "OneToFiveTransformerWithArgs: 1 🠒 ? no. $it: One To Five Transformed With Args" })
    ) to listOf(
        TestableTest { _, _ ->
            val args = item.args
            assertNotNull(args, "No args provided"); args!!
            expect(2, "Only two args expected") { args.size }
            val arg = args.last() as? Int
            assertNotNull(arg, "Arg as not an Int")
            expect(arg, "Arg value did not match item arg") { number }
            "Item was supplied with $item and $number"
        }
    )

    @DeveloperFunction(value = "My Custom Name", transformer = fta_OneToOneWithProvidedAndInjectedArgsTransformer::class)
    private fun oneToOneWithProvidedAndInjectedArgsTransformed(
        providedArg1: String,
        providedArg2: Float,
        injectedArg1: fta_SomePrivateObject,
        injectedArg2: fta_OneToOneWithProvidedAndInjectedArgsTransformer
    ) = listOf(
        ExpectedItemCount(1),
        SingleItemExpectedNameTest("My Custom Name")
    ) to listOf(
        ExpectedArgs(
            listOf(
                providedArg1 to "Provided arg1",
                providedArg2 to 2.0f,
                injectedArg1 to fta_SomePrivateObject,
                injectedArg2 to fta_OneToOneWithProvidedAndInjectedArgsTransformer()
            )
        )
    )

    @DeveloperFunction(transformer = fta_1IsProvided_2IsInjected_3IsProvided_4IsInjected_Transformer::class)
    internal fun secondArgShouldBeInjected(
        providedArg1: String,
        injectedArg1: fta_1IsProvided_2IsInjected_3IsProvided_4IsInjected_Transformer.NestedObject,
        providedArg2: Float,
        injectedArg2: fta_SomePublicObject
    ) = listOf(
        ExpectedItemCount(1)
    ) to listOf(
        ExpectedArgs(
            listOf(
                providedArg1 to "1IsProvided_2IsInjected_3IsProvided_4IsInjected",
                injectedArg1 to fta_1IsProvided_2IsInjected_3IsProvided_4IsInjected_Transformer.NestedObject,
                providedArg2 to 2.1f,
                injectedArg2 to fta_SomePublicObject
            )
        )
    )
}
