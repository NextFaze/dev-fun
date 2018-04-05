@file:Suppress("UNUSED_PARAMETER", "unused")

package tested.kapt_and_compile.top_level_functions

import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.inject.Constructable

annotation class TopLevelFunctions


@DeveloperFunction
fun someTopLevelFunction() = Unit

@DeveloperFunction
internal fun internalTopLevelFunction() = Unit

@DeveloperFunction
private fun privateTopLevelFunction() = Unit


@DeveloperFunction
fun topLevelFunctionWithArgs(arg0: String, arg1: Float) = Unit

@DeveloperFunction
internal fun internalTopLevelFunctionWithArgs(arg0: String, arg1: Int) = Unit

@DeveloperFunction
private fun privateTopLevelFunctionWithArgs(arg0: String, arg1: Float) = Unit


@Constructable
private class SomeClass(val someValue: String)

@DeveloperFunction
fun String.someTopLevelFunctionToasted() = Unit

@DeveloperFunction
private fun SomeClass.privateTopLevelFunction() = Unit
