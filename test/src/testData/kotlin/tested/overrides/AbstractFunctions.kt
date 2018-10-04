@file:Suppress("ClassName")

package tested.overrides

import com.nextfaze.devfun.function.DeveloperFunction
import com.nextfaze.devfun.inject.InstanceProvider
import com.nextfaze.devfun.test.ExpectedItemCount
import com.nextfaze.devfun.test.TestInstanceProviders
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

object AbstractFunctions : TestInstanceProviders {
    override val testProviders: List<KClass<out InstanceProvider>> get() = listOf(AbstractFunctionsInstanceProvider::class)
}

@Suppress("UNCHECKED_CAST")
private class AbstractFunctionsInstanceProvider : InstanceProvider {
    override fun <T : Any> get(clazz: KClass<out T>): T? = when {
        clazz.isSubclassOf(af_BaseClass::class) -> af_SubClass()
        else -> null
    } as T?
}


abstract class af_BaseClass {
    @DeveloperFunction
    abstract fun overriddenFunction(): Any?

    @DeveloperFunction
    protected abstract fun protectedOverriddenFunction(): Any?
}

class af_SubClass : af_BaseClass() {
    override fun overriddenFunction(): Any? = ExpectedItemCount(1)
    override fun protectedOverriddenFunction(): Any? = ExpectedItemCount(1)
}
