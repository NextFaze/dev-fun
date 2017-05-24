@file:Suppress("unused")

package tested.kapt_and_compile.simple_jvm_static_functions_in_objects

import com.nextfaze.devfun.annotations.DeveloperFunction

annotation class SimpleJvmStaticFunctionsInObjects

object SimpleObject {
    fun someFun() = Unit
    internal fun someInternalFun() = Unit
    private fun somePrivateFun() = Unit

    @DeveloperFunction
    fun publicFun() = Unit

    @DeveloperFunction
    internal fun internalFun() = Unit

    @DeveloperFunction
    private fun privateFun() = Unit

    @JvmStatic
    @DeveloperFunction
    fun jvmStaticPublicFun() = Unit

    @JvmStatic
    @DeveloperFunction
    internal fun jvmStaticInternalFun() = Unit

    @JvmStatic
    @DeveloperFunction
    private fun jvmStaticPrivateFun() = Unit
}

internal object InternalSimpleObject {
    fun someFun() = Unit
    internal fun someInternalFun() = Unit
    private fun somePrivateFun() = Unit

    @DeveloperFunction
    fun publicFun() = Unit

    @DeveloperFunction
    internal fun internalFun() = Unit

    @DeveloperFunction
    private fun privateFun() = Unit

    @JvmStatic
    @DeveloperFunction
    fun jvmStaticPublicFun() = Unit

    @JvmStatic
    @DeveloperFunction
    internal fun jvmStaticInternalFun() = Unit

    @JvmStatic
    @DeveloperFunction
    private fun jvmStaticPrivateFun() = Unit
}

private object PrivateSimpleObject {
    fun someFun() = Unit
    internal fun someInternalFun() = Unit
    private fun somePrivateFun() = Unit

    @DeveloperFunction
    fun publicFun() = Unit

    @DeveloperFunction
    internal fun internalFun() = Unit

    @DeveloperFunction
    private fun privateFun() = Unit

    @JvmStatic
    @DeveloperFunction
    fun jvmStaticPublicFun() = Unit

    @JvmStatic
    @DeveloperFunction
    internal fun jvmStaticInternalFun() = Unit

    @JvmStatic
    @DeveloperFunction
    private fun jvmStaticPrivateFun() = Unit
}
