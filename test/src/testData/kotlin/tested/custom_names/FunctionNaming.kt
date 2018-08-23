@file:Suppress("unused", "PackageName", "ClassName", "TestFunctionName")

package tested.custom_names

import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.core.FunctionItem
import com.nextfaze.devfun.test.NOPFunctionItem
import kotlin.test.expect

internal annotation class FunctionNaming

class fn_AutoNames {
    @DeveloperFunction
    fun single(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("Single") { functionItem.name }
    }

    @DeveloperFunction
    fun AlreadyCapital(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("Already Capital") { functionItem.name }
    }

    @DeveloperFunction
    fun camelCaseName(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("Camel Case Name") { functionItem.name }
    }

    @DeveloperFunction
    fun _camelCaseName(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("Camel Case Name") { functionItem.name }
    }

    @DeveloperFunction
    fun camel123Case4Name(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("Camel123 Case4 Name") { functionItem.name }
    }

    @DeveloperFunction
    fun CAPS(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("CAPS") { functionItem.name }
    }

    @DeveloperFunction
    fun ALL_CAPS(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("ALL CAPS") { functionItem.name }
    }

    @DeveloperFunction
    fun _ALL_CAPS(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("ALL CAPS") { functionItem.name }
    }

    @DeveloperFunction
    fun __someCAPS(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("Some CAPS") { functionItem.name }
    }
}

class fn_CustomNames {
    @DeveloperFunction("My Custom Name")
    fun customName(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("My Custom Name") { functionItem.name }
    }

    @DeveloperFunction(value = "")
    fun blankName(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("") { functionItem.name }
    }

    @DeveloperFunction(value = "      ")
    fun spaces(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("      ") { functionItem.name }
    }

    @DeveloperFunction(value = "     Starts with 5 spaces")
    fun startsWith5Spaces(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("     Starts with 5 spaces") { functionItem.name }
    }

    @DeveloperFunction(value = "Ends with 5 spaces     ")
    fun endsWith5Spaces(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("Ends with 5 spaces     ") { functionItem.name }
    }

    @DeveloperFunction(value = "Some \"annoying\" quotes")
    fun withQuotes(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("Some \"annoying\" quotes") { functionItem.name }
    }

    @DeveloperFunction(value = "With a \$dollar symbol")
    fun withDollar(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("With a \$dollar symbol") { functionItem.name }
    }

    @DeveloperFunction(value = "\"\"More Annoying Quotes\"\"")
    fun moreAnnoyingQuotes(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("\"\"More Annoying Quotes\"\"") { functionItem.name }
    }

    @DeveloperFunction(
        value = """""More Annoying
Quotes 2"""""
    )
    fun moreAnnoyingQuotes2(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("\"\"More Annoying\nQuotes 2\"\"") { functionItem.name }
    }

    @DeveloperFunction(
        value = """With
new
lines
            and some spaces"""
    )
    fun withNewLinesAndSomeSpaces(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("With\nnew\nlines\n            and some spaces") { functionItem.name }
    }

    @DeveloperFunction(
        value = """With another
${'$'}dollar symbol"""
    )
    fun withNewLineAndDollar(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("With another\n\$dollar symbol") { functionItem.name }
    }
}
