@file:Suppress("unused", "PackageName", "ClassName")

package tested.custom_names

import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.core.FunctionItem
import com.nextfaze.devfun.test.NOPFunctionItem
import kotlin.test.expect

internal annotation class CategoryNaming

internal class Single {
    @DeveloperFunction
    fun testSingle(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("Single") { functionItem.category.name }
    }
}

class FromCompanionObject {
    companion object {
        @DeveloperFunction
        fun testSomeCompanionFunction(functionItem: FunctionItem) {
            if (functionItem === NOPFunctionItem) return
            expect("From Companion Object") { functionItem.category.name }
        }
    }
}

class JvmStaticInCompanionObject {
    companion object {
        @JvmStatic
        @DeveloperFunction
        fun testSomeCompanionFunction(functionItem: FunctionItem) {
            if (functionItem === NOPFunctionItem) return
            expect("Jvm Static In Companion Object") { functionItem.category.name }
        }
    }
}

internal class single {
    @DeveloperFunction
    fun testSingle(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("Single") { functionItem.category.name }
    }
}

private class AlreadyCapital {
    @DeveloperFunction
    fun testAlreadyCapital(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("Already Capital") { functionItem.category.name }
    }
}

internal class camelCaseName {
    @DeveloperFunction
    fun testCamelCaseName(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("Camel Case Name") { functionItem.category.name }
    }
}

class _camelCaseName {
    @DeveloperFunction
    fun testCamelCaseNameStartWithUnderscore(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("Camel Case Name") { functionItem.category.name }
    }
}

internal class camel123Case4Name {
    @DeveloperFunction
    fun testCamel123Case4Name(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("Camel123 Case4 Name") { functionItem.category.name }
    }
}

internal class CAPS {
    @DeveloperFunction
    fun testCAPS(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("CAPS") { functionItem.category.name }
    }
}

private class ALL_CAPS {
    @DeveloperFunction
    fun testALL_CAPS(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("ALL CAPS") { functionItem.category.name }
    }
}

class _ALL_CAPS {
    @DeveloperFunction
    fun test_ALL_CAPS(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("ALL CAPS") { functionItem.category.name }
    }
}

internal class __someCAPS {
    @DeveloperFunction
    fun test__someCAPS(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("Some CAPS") { functionItem.category.name }
    }
}

@DeveloperCategory("My Custom Name")
internal class cn_customName {
    @DeveloperFunction
    fun testCustomName(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("My Custom Name") { functionItem.category.name }
    }
}

@DeveloperCategory("")
private class cn_blankName {
    @DeveloperFunction
    fun testBlankName(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("") { functionItem.category.name }
    }
}

@DeveloperCategory("      ")
internal class cn_spaces {
    @DeveloperFunction
    fun testSpaces(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("      ") { functionItem.category.name }
    }
}

@DeveloperCategory("     Starts with 5 spaces")
internal class cn_startsWith5Spaces {
    @DeveloperFunction
    fun testStartsWith5Spaces(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("     Starts with 5 spaces") { functionItem.category.name }
    }
}

@DeveloperCategory("Ends with 5 spaces     ")
class cn_endsWith5Spaces {
    @DeveloperFunction
    fun testEndsWith5Spaces(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("Ends with 5 spaces     ") { functionItem.category.name }
    }
}

@DeveloperCategory("Some \"annoying\" quotes")
internal class cn_withQuotes {
    @DeveloperFunction
    fun testWithQuotes(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("Some \"annoying\" quotes") { functionItem.category.name }
    }
}

@DeveloperCategory("With a \$dollar symbol")
private class cn_withDollar {
    @DeveloperFunction
    fun testWithDollar(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("With a \$dollar symbol") { functionItem.category.name }
    }
}

@DeveloperCategory("\"\"More Annoying Quotes\"\"")
class cn_moreAnnoyingQuotes {
    @DeveloperFunction
    fun testMoreAnnoyingQuotes(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("\"\"More Annoying Quotes\"\"") { functionItem.category.name }
    }
}

@DeveloperCategory(
    """""More Annoying
Quotes 2"""""
)
internal class cn_moreAnnoyingQuotes2 {
    @DeveloperFunction
    fun testMoreAnnoyingQuotes2(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("\"\"More Annoying\nQuotes 2\"\"") { functionItem.category.name }
    }
}

@DeveloperCategory(
    """With
new
lines
            and some spaces"""
)
internal class cn_withNewLinesAndSomeSpaces {
    @DeveloperFunction
    fun testWithNewLinesAndSomeSpaces(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("With\nnew\nlines\n            and some spaces") { functionItem.category.name }
    }
}

@DeveloperCategory(
    """With another
${'$'}dollar symbol"""
)
private class cn_withNewLineAndDollar {
    @DeveloperFunction
    fun testWithNewLineAndDollar(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("With another\n\$dollar symbol") { functionItem.category.name }
    }
}
