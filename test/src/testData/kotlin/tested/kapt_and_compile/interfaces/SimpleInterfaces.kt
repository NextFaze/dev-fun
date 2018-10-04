@file:Suppress("UNUSED_PARAMETER", "unused", "PackageName", "ClassName")

package tested.kapt_and_compile.interfaces

import com.nextfaze.devfun.function.DeveloperFunction
import com.nextfaze.devfun.inject.InstanceProvider
import com.nextfaze.devfun.test.ExpectedItemCount
import com.nextfaze.devfun.test.TestInstanceProviders
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

object SimpleInterfaces : TestInstanceProviders {
    override val testProviders: List<KClass<out InstanceProvider>> get() = listOf(SimpleInterfacesInstanceProvider::class)
}

@Suppress("UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY")
private class SimpleInterfacesInstanceProvider : InstanceProvider {
    override fun <T : Any> get(clazz: KClass<out T>): T? = when {
        clazz.isSubclassOf(si_Interface::class) -> si_InterfaceImpl()
        clazz.isSubclassOf(si_InternalInterface::class) -> si_InternalInterfaceImpl()
        clazz.isSubclassOf(si_PrivateInterface::class) -> si_PrivateInterfaceImpl()
        else -> null
    } as T?
}


interface si_Interface {
    @DeveloperFunction fun interfaceFunction(): Any?
}

class si_InterfaceImpl : si_Interface {
    override fun interfaceFunction(): Any? = ExpectedItemCount(1)
}


internal interface si_InternalInterface {
    @DeveloperFunction fun interfaceFunction(): Any?
}

internal class si_InternalInterfaceImpl : si_InternalInterface {
    override fun interfaceFunction(): Any? = ExpectedItemCount(1)
}


private interface si_PrivateInterface {
    @DeveloperFunction fun interfaceFunction(): Any?
}

private class si_PrivateInterfaceImpl : si_PrivateInterface {
    override fun interfaceFunction(): Any? = ExpectedItemCount(1)
}
