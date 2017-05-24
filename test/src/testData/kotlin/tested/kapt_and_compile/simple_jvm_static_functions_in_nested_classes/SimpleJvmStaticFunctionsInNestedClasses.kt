@file:Suppress("unused")

package tested.kapt_and_compile.simple_jvm_static_functions_in_nested_classes

import com.nextfaze.devfun.annotations.DeveloperFunction

annotation class SimpleJvmStaticFunctionsInNestedClasses

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

    class SimpleClassPublicNested {
        fun someFun() = Unit
        internal fun someInternalFun() = Unit
        private fun somePrivateFun() = Unit

        class NestedClass {
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
    }

    class SimpleClassInternalNested {
        fun someFun() = Unit
        internal fun someInternalFun() = Unit
        private fun somePrivateFun() = Unit

        internal class NestedClass {
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
    }

    class SimpleClassPrivateNested {
        fun someFun() = Unit
        internal fun someInternalFun() = Unit
        private fun somePrivateFun() = Unit

        private class NestedClass {
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

    internal class InternalSimpleClassPublicNested {
        fun someFun() = Unit
        internal fun someInternalFun() = Unit
        private fun somePrivateFun() = Unit

        class NestedClass {
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
    }

    internal class InternalSimpleClassInternalNested {
        fun someFun() = Unit
        internal fun someInternalFun() = Unit
        private fun somePrivateFun() = Unit

        internal class NestedClass {
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
    }

    internal class InternalSimpleClassPrivateNested {
        fun someFun() = Unit
        internal fun someInternalFun() = Unit
        private fun somePrivateFun() = Unit

        private class NestedClass {
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

    private class PrivateSimpleClassPublicNested {
        fun someFun() = Unit
        internal fun someInternalFun() = Unit
        private fun somePrivateFun() = Unit

        class NestedClass {
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
    }

    private class PrivateSimpleClassInternalNested {
        fun someFun() = Unit
        internal fun someInternalFun() = Unit
        private fun somePrivateFun() = Unit

        internal class NestedClass {
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
    }

    private class PrivateSimpleClassPrivateNested {
        fun someFun() = Unit
        internal fun someInternalFun() = Unit
        private fun somePrivateFun() = Unit

        private class NestedClass {
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
    }
}
