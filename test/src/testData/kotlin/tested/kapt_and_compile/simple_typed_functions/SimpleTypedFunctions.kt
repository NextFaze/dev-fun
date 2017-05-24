@file:Suppress("unused")

package tested.kapt_and_compile.simple_typed_functions

import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.inject.InstanceProvider
import com.nextfaze.devfun.test.TestInstanceProviders
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

object SimpleTypedFunctions : TestInstanceProviders {
    override val testProviders: List<KClass<out InstanceProvider>> get() = listOf(FunctionsWithArgsInstanceProvider::class)
}

@Suppress("UNCHECKED_CAST")
private class FunctionsWithArgsInstanceProvider : InstanceProvider {
    override fun <T : Any> get(clazz: KClass<out T>) = when {
        clazz.isSubclassOf(MyPublicTypeInterface::class) -> MyPublicTypeClass() as T
        clazz.isSubclassOf(MyInternalTypeInterface::class) -> MyInternalTypeClass() as T
        clazz.isSubclassOf(MyPrivateTypeInterface::class) -> MyPrivateTypeClass() as T
        else -> null
    }
}

class MyPublicTypeClass : MyPublicTypeInterface
internal class MyInternalTypeClass : MyInternalTypeInterface
private class MyPrivateTypeClass : MyPrivateTypeInterface

interface MyPublicTypeInterface
internal interface MyInternalTypeInterface
private interface MyPrivateTypeInterface

class SimpleClass {
    fun someFun() = Unit
    internal fun someInternalFun() = Unit
    private fun somePrivateFun() = Unit

    @DeveloperFunction
    fun <T : MyPublicTypeInterface> publicFun() = Unit

    @DeveloperFunction
    internal fun <T : Any?> internalFun() = Unit

    @DeveloperFunction
    private fun <T : MyPrivateTypeInterface> privateFun() = Unit
}

object SimpleObject {
    fun someFun() = Unit
    internal fun someInternalFun() = Unit
    private fun somePrivateFun() = Unit

    @DeveloperFunction
    fun <T : Number> publicFun() = Unit

    @DeveloperFunction
    internal fun <T : MyPrivateTypeInterface> internalFun() = Unit

    @DeveloperFunction
    private fun <T : MyPrivateTypeInterface> privateFun() = Unit
}

internal class InternalSimpleClass {
    fun someFun() = Unit
    internal fun someInternalFun() = Unit
    private fun somePrivateFun() = Unit

    @DeveloperFunction
    fun <T : MyPublicTypeInterface> publicFun() = Unit

    @DeveloperFunction
    internal fun <T> internalFun() = Unit

    @DeveloperFunction
    private fun <T : Any> privateFun() = Unit
}

internal object InternalSimpleObject {
    fun someFun() = Unit
    internal fun someInternalFun() = Unit
    private fun somePrivateFun() = Unit

    @DeveloperFunction
    fun <T : MyPublicTypeInterface, U : MyInternalTypeInterface> publicFun() = Unit

    @DeveloperFunction
    internal fun <T : MyInternalTypeInterface> internalFun() = Unit

    @DeveloperFunction
    private fun <T : MyPrivateTypeInterface> privateFun() = Unit
}

private class PrivateSimpleClass {
    fun someFun() = Unit
    internal fun someInternalFun() = Unit
    private fun somePrivateFun() = Unit

    @DeveloperFunction
    fun <T : MyPublicTypeInterface> publicFun() = Unit

    @DeveloperFunction
    internal fun <T : MyInternalTypeInterface> internalFun() = Unit

    @DeveloperFunction
    private fun <T : MyPrivateTypeInterface> privateFun() = Unit
}

private class PrivateSimpleObject {
    fun someFun() = Unit
    internal fun someInternalFun() = Unit
    private fun somePrivateFun() = Unit

    @DeveloperFunction
    fun <T : MyPublicTypeInterface> publicFun() = Unit

    @DeveloperFunction
    internal fun <T : MyInternalTypeInterface, U : MyPrivateTypeInterface, V : MyPublicTypeInterface> internalFun() = Unit

    @DeveloperFunction
    private fun <T : MyPrivateTypeInterface> privateFun() = Unit
}
