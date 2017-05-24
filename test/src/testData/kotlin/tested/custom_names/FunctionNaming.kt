@file:Suppress("unused")

package tested.custom_names

import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.test.ExpectedItemCount
import com.nextfaze.devfun.test.SingleItemExpectedNameTest

internal annotation class FunctionNaming

private fun singleItemNamed(name: String) = listOf(ExpectedItemCount(1), SingleItemExpectedNameTest(name))

class AutoNames {
    @DeveloperFunction
    fun single() = singleItemNamed("Single")

    @DeveloperFunction
    fun AlreadyCapital() = singleItemNamed("Already Capital")

    @DeveloperFunction
    fun camelCaseName() = singleItemNamed("Camel Case Name")

    @DeveloperFunction
    fun _camelCaseName() = singleItemNamed("Camel Case Name")

    @DeveloperFunction
    fun camel123Case4Name() = singleItemNamed("Camel123 Case4 Name")

    @DeveloperFunction
    fun CAPS() = singleItemNamed("CAPS")

    @DeveloperFunction
    fun ALL_CAPS() = singleItemNamed("ALL CAPS")

    @DeveloperFunction
    fun _ALL_CAPS() = singleItemNamed("ALL CAPS")

    @DeveloperFunction
    fun __someCAPS() = singleItemNamed("Some CAPS")
}

class CustomNames {
    @DeveloperFunction("My Custom Name")
    fun customName() = singleItemNamed("My Custom Name")

    @DeveloperFunction(value = "")
    fun blankName() = singleItemNamed("")

    @DeveloperFunction(value = "      ")
    fun spaces() = singleItemNamed("      ")

    @DeveloperFunction(value = "     Starts with 5 spaces")
    fun startsWith5Spaces() = singleItemNamed("     Starts with 5 spaces")

    @DeveloperFunction(value = "Ends with 5 spaces     ")
    fun endsWith5Spaces() = singleItemNamed("Ends with 5 spaces     ")

    @DeveloperFunction(value = "Some \"annoying\" quotes")
    fun withQuotes() = singleItemNamed("Some \"annoying\" quotes")

    @DeveloperFunction(value = "With a \$dollar symbol")
    fun withDollar() = singleItemNamed("With a \$dollar symbol")

    @DeveloperFunction(value = "\"\"More Annoying Quotes\"\"")
    fun moreAnnoyingQuotes() = singleItemNamed("\"\"More Annoying Quotes\"\"")

    @DeveloperFunction(value = """""More Annoying
Quotes 2""""")
    fun moreAnnoyingQuotes2() = singleItemNamed("\"\"More Annoying\nQuotes 2\"\"")

    @DeveloperFunction(value = """With
new
lines
            and some spaces""")
    fun withNewLinesAndSomeSpaces() = singleItemNamed("With\nnew\nlines\n            and some spaces")

    @DeveloperFunction(value = """With another
${'$'}dollar symbol""")
    fun withNewLineAndDollar() = singleItemNamed("With another\n\$dollar symbol")
}
