@file:Suppress("unused")

package tested.kapt_and_compile.simple_jvm_static_functions_in_nested_objects

import com.nextfaze.devfun.annotations.DeveloperFunction

annotation class SimpleJvmStaticFunctionsInNestedObjects

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

    object SimpleObjectPublicNested {
        fun someFun() = Unit
        internal fun someInternalFun() = Unit
        private fun somePrivateFun() = Unit

        object NestedObject {
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
    }

    object SimpleObjectInternalNested {
        fun someFun() = Unit
        internal fun someInternalFun() = Unit
        private fun somePrivateFun() = Unit

        internal object NestedObject {
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
    }

    object SimpleObjectPrivateNested {
        fun someFun() = Unit
        internal fun someInternalFun() = Unit
        private fun somePrivateFun() = Unit

        private object NestedObject {
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
    }
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

    internal object InternalSimpleObjectPublicNested {
        fun someFun() = Unit
        internal fun someInternalFun() = Unit
        private fun somePrivateFun() = Unit

        object NestedObject {
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
    }

    internal object InternalSimpleObjectInternalNested {
        fun someFun() = Unit
        internal fun someInternalFun() = Unit
        private fun somePrivateFun() = Unit

        internal object NestedObject {
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
    }

    internal object InternalSimpleObjectPrivateNested {
        fun someFun() = Unit
        internal fun someInternalFun() = Unit
        private fun somePrivateFun() = Unit

        private object NestedObject {
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
    }
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

    private object PrivateSimpleObjectPublicNested {
        fun someFun() = Unit
        internal fun someInternalFun() = Unit
        private fun somePrivateFun() = Unit

        object NestedObject {
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
    }

    private object PrivateSimpleObjectInternalNested {
        fun someFun() = Unit
        internal fun someInternalFun() = Unit
        private fun somePrivateFun() = Unit

        internal object NestedObject {
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
    }

    private object PrivateSimpleObjectPrivateNested {
        fun someFun() = Unit
        internal fun someInternalFun() = Unit
        private fun somePrivateFun() = Unit

        private object NestedObject {
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
    }
}
