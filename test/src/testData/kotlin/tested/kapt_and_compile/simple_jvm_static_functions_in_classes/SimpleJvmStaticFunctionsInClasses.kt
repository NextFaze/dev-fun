@file:Suppress("unused")

package tested.kapt_and_compile.simple_jvm_static_functions_in_classes

import com.nextfaze.devfun.annotations.DeveloperFunction

annotation class SimpleJvmStaticFunctionsInClasses

class SimpleClass {
    fun someFun() = Unit
    internal fun someInternalFun() = Unit
    private fun somePrivateFun() = Unit

    @DeveloperFunction
    fun publicFun() = Unit

    @DeveloperFunction
    internal fun internalFun() = Unit

    @DeveloperFunction
    private fun privateFun() = Unit

    companion object {
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
}

internal class InternalSimpleClass {
    fun someFun() = Unit
    internal fun someInternalFun() = Unit
    private fun somePrivateFun() = Unit

    @DeveloperFunction
    fun publicFun() = Unit

    @DeveloperFunction
    internal fun internalFun() = Unit

    @DeveloperFunction
    private fun privateFun() = Unit

    companion object {
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
}

private class PrivateSimpleClass {
    fun someFun() = Unit
    internal fun someInternalFun() = Unit
    private fun somePrivateFun() = Unit

    @DeveloperFunction
    fun publicFun() = Unit

    @DeveloperFunction
    internal fun internalFun() = Unit

    @DeveloperFunction
    private fun privateFun() = Unit

    companion object {
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
}
