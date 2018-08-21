@file:Suppress("UNUSED_PARAMETER", "unused", "PackageName", "ClassName")

package tested.kapt_and_compile.interfaces

import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.inject.InstanceProvider
import com.nextfaze.devfun.test.TestInstanceProviders
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

object InterfacesWithDefaults : TestInstanceProviders {
    override val testProviders: List<KClass<out InstanceProvider>> get() = listOf(InterfacesWithDefaultsInstanceProvider::class)
}

@Suppress("UNCHECKED_CAST")
private class InterfacesWithDefaultsInstanceProvider : InstanceProvider {
    override fun <T : Any> get(clazz: KClass<out T>): T? = when {
        clazz.isSubclassOf(iwd_Interface::class) -> iwd_InterfaceImpl()
        else -> null
    } as T?
}

interface iwd_Interface {
    @DeveloperFunction
    fun interfaceFunction(): Any? = Unit
}

class iwd_InterfaceImpl : iwd_Interface {
    fun someFun() = Unit
}
