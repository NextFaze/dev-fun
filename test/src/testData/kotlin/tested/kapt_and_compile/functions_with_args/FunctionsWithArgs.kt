@file:Suppress("UNUSED_PARAMETER", "unused", "PackageName")

package tested.kapt_and_compile.functions_with_args

import com.nextfaze.devfun.function.DeveloperFunction
import com.nextfaze.devfun.inject.InstanceProvider
import com.nextfaze.devfun.test.TestInstanceProviders
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

object FunctionsWithArgs : TestInstanceProviders {
    override val testProviders: List<KClass<out InstanceProvider>> get() = listOf(FunctionsWithArgsInstanceProvider::class)
}

@Suppress("UNCHECKED_CAST")
private class FunctionsWithArgsInstanceProvider : InstanceProvider {
    override fun <T : Any> get(clazz: KClass<out T>) = when {
        clazz.isSubclassOf(MyPublicInterface::class) -> MyPrivateObject as T
        clazz.isSubclassOf(MyInternalInterface::class) -> MyPublicObject as T
        clazz.isSubclassOf(MyPrivateInterface::class) -> MyInternalObject as T
        else -> null
    }
}

class MyPublicClass
internal class MyInternalClass
private class MyPrivateClass

object MyPublicObject : MyInternalInterface
internal object MyInternalObject : MyPrivateInterface
private object MyPrivateObject : MyPublicInterface

interface MyPublicInterface
internal interface MyInternalInterface
private interface MyPrivateInterface

class SimpleClass {
    fun someFun() = Unit
    internal fun someInternalFun() = Unit
    private fun somePrivateFun() = Unit

    @DeveloperFunction
    fun publicFun(arg1: MyPublicClass, arg2: MyPublicObject, arg3: MyPublicInterface) = Unit

    @DeveloperFunction
    internal fun internalFun(arg1: MyInternalClass, arg2: MyPublicObject, arg3: MyInternalObject) = Unit

    @DeveloperFunction
    private fun privateFun(arg1: MyPrivateClass, arg2: MyInternalObject, arg3: MyPublicObject) = Unit
}

internal class InternalSimpleClass {
    fun someFun() = Unit
    internal fun someInternalFun() = Unit
    private fun somePrivateFun() = Unit

    @DeveloperFunction
    fun publicFun(arg1: MyInternalClass, arg2: MyPublicInterface, arg3: MyInternalObject) = Unit

    @DeveloperFunction
    internal fun internalFun(arg1: MyPublicClass, arg2: MyPublicInterface, arg3: MyInternalInterface) = Unit

    @DeveloperFunction
    private fun privateFun(arg1: MyPrivateClass, arg2: MyPrivateInterface, arg3: MyPrivateObject) = Unit
}

private class PrivateSimpleClass {
    fun someFun() = Unit
    internal fun someInternalFun() = Unit
    private fun somePrivateFun() = Unit

    @DeveloperFunction
    fun publicFun(arg1: MyInternalClass, arg2: MyPrivateInterface, arg3: MyPublicObject) = Unit

    @DeveloperFunction
    internal fun internalFun(arg1: MyPrivateClass, arg2: MyPublicInterface, arg3: MyInternalObject) = Unit

    @DeveloperFunction
    private fun privateFun(arg1: MyPublicClass, arg2: MyPublicInterface, arg3: MyPrivateObject) = Unit
}
