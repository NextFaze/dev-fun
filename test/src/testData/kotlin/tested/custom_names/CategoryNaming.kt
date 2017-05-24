@file:Suppress("unused")

package tested.custom_names

import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.test.ExpectedCategoryName
import com.nextfaze.devfun.test.ExpectedItemCount

internal annotation class CategoryNaming

private fun itemWithCategoryNamed(name: String) = listOf(ExpectedItemCount(1), ExpectedCategoryName(name))

internal class Single {
    @DeveloperFunction
    fun single() = itemWithCategoryNamed("Single")
}

class FromCompanionObject {
    companion object {
        @DeveloperFunction
        fun someCompanionFunction() = itemWithCategoryNamed("From Companion Object")
    }
}

class JvmStaticInCompanionObject {
    companion object {
        @JvmStatic
        @DeveloperFunction
        fun someCompanionFunction() = itemWithCategoryNamed("Jvm Static In Companion Object")
    }
}

internal class single {
    @DeveloperFunction
    fun single() = itemWithCategoryNamed("Single")
}

private class AlreadyCapital {
    @DeveloperFunction
    fun AlreadyCapital() = itemWithCategoryNamed("Already Capital")
}

internal class camelCaseName {
    @DeveloperFunction
    fun camelCaseName() = itemWithCategoryNamed("Camel Case Name")
}

class _camelCaseName {
    @DeveloperFunction
    fun _camelCaseName() = itemWithCategoryNamed("Camel Case Name")
}

internal class camel123Case4Name {
    @DeveloperFunction
    fun camel123Case4Name() = itemWithCategoryNamed("Camel123 Case4 Name")
}

internal class CAPS {
    @DeveloperFunction
    fun CAPS() = itemWithCategoryNamed("CAPS")
}

private class ALL_CAPS {
    @DeveloperFunction
    fun ALL_CAPS() = itemWithCategoryNamed("ALL CAPS")
}

class _ALL_CAPS {
    @DeveloperFunction
    fun _ALL_CAPS() = itemWithCategoryNamed("ALL CAPS")
}

internal class __someCAPS {
    @DeveloperFunction
    fun __someCAPS() = itemWithCategoryNamed("Some CAPS")
}

@DeveloperCategory("My Custom Name")
internal class customName {
    @DeveloperFunction
    fun customName() = itemWithCategoryNamed("My Custom Name")
}

@DeveloperCategory("")
private class blankName {
    @DeveloperFunction
    fun blankName() = itemWithCategoryNamed("")
}

@DeveloperCategory("      ")
internal class spaces {
    @DeveloperFunction
    fun spaces() = itemWithCategoryNamed("      ")
}

@DeveloperCategory("     Starts with 5 spaces")
internal class startsWith5Spaces {
    @DeveloperFunction
    fun startsWith5Spaces() = itemWithCategoryNamed("     Starts with 5 spaces")
}

@DeveloperCategory("Ends with 5 spaces     ")
class endsWith5Spaces {
    @DeveloperFunction
    fun endsWith5Spaces() = itemWithCategoryNamed("Ends with 5 spaces     ")
}

@DeveloperCategory("Some \"annoying\" quotes")
internal class withQuotes {
    @DeveloperFunction
    fun withQuotes() = itemWithCategoryNamed("Some \"annoying\" quotes")
}

@DeveloperCategory("With a \$dollar symbol")
private class withDollar {
    @DeveloperFunction
    fun withDollar() = itemWithCategoryNamed("With a \$dollar symbol")
}

@DeveloperCategory("\"\"More Annoying Quotes\"\"")
class moreAnnoyingQuotes {
    @DeveloperFunction
    fun moreAnnoyingQuotes() = itemWithCategoryNamed("\"\"More Annoying Quotes\"\"")
}

@DeveloperCategory("""""More Annoying
Quotes 2""""")
internal class moreAnnoyingQuotes2 {
    @DeveloperFunction
    fun moreAnnoyingQuotes2() = itemWithCategoryNamed("\"\"More Annoying\nQuotes 2\"\"")
}

@DeveloperCategory("""With
new
lines
            and some spaces""")
internal class withNewLinesAndSomeSpaces {
    @DeveloperFunction
    fun withNewLinesAndSomeSpaces() = itemWithCategoryNamed("With\nnew\nlines\n            and some spaces")
}

@DeveloperCategory("""With another
${'$'}dollar symbol""")
private class withNewLineAndDollar {
    @DeveloperFunction
    fun withNewLineAndDollar() = itemWithCategoryNamed("With another\n\$dollar symbol")
}
