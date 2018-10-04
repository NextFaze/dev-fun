@file:Suppress("UNUSED_PARAMETER", "unused", "PackageName")

package tested.kapt_and_compile.functions_with_primitive_args

import com.nextfaze.devfun.function.DeveloperFunction

annotation class FunctionsWithPrimitiveArgs

class SimpleClass {
    fun someFun() = Unit
    internal fun someInternalFun() = Unit
    private fun somePrivateFun() = Unit

    @DeveloperFunction
    fun publicFun(arg1: Short) = Unit

    @DeveloperFunction
    internal fun internalFun(arg1: Int) = Unit

    @DeveloperFunction
    private fun privateFun(arg1: Long) = Unit
}

internal class InternalSimpleClass {
    fun someFun() = Unit
    internal fun someInternalFun() = Unit
    private fun somePrivateFun() = Unit

    @DeveloperFunction
    fun publicFun(arg1: Byte) = Unit

    @DeveloperFunction
    internal fun internalFun(arg1: Char) = Unit

    @DeveloperFunction
    private fun privateFun(arg1: Boolean) = Unit
}

private class PrivateSimpleClass {
    fun someFun() = Unit
    internal fun someInternalFun() = Unit
    private fun somePrivateFun() = Unit

    @DeveloperFunction
    fun publicFun(arg1: Float) = Unit

    @DeveloperFunction
    internal fun internalFun(arg1: Double) = Unit

    @DeveloperFunction
    private fun privateFun(arg1: Short, arg2: Int, arg3: Long, arg4: Byte, arg5: Char, arg6: Boolean, arg7: Float, arg8: Double) = Unit
}
