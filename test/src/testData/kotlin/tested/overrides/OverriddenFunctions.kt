@file:Suppress("ClassName")

package tested.overrides

import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.inject.InstanceProvider
import com.nextfaze.devfun.test.ExpectedItemCount
import com.nextfaze.devfun.test.NeverRun
import com.nextfaze.devfun.test.TestInstanceProviders
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

object OverriddenFunctions : TestInstanceProviders {
    override val testProviders: List<KClass<out InstanceProvider>> get() = listOf(OverriddenFunctionsInstanceProvider::class)
}

@Suppress("UNCHECKED_CAST")
private class OverriddenFunctionsInstanceProvider : InstanceProvider {
    @Suppress("IMPLICIT_CAST_TO_ANY")
    override fun <T : Any> get(clazz: KClass<out T>): T? = when {
        clazz.isSubclassOf(of_BaseClass::class) -> of_SubClass()
        clazz.isSubclassOf(of_InternalBaseClass::class) -> of_InternalSubClass()
        clazz.isSubclassOf(of_PrivateBaseClass::class) -> of_PrivateSubClass()
        else -> null
    } as T?
}


open class of_BaseClass {
    @DeveloperFunction
    open fun overriddenFunction(): Any? = NeverRun()

    @DeveloperFunction
    protected open fun protectedOverriddenFunction(): Any? = NeverRun()
}

class of_SubClass : of_BaseClass() {
    override fun overriddenFunction(): Any? = ExpectedItemCount(1)
    override fun protectedOverriddenFunction(): Any? = ExpectedItemCount(1)
}


internal open class of_InternalBaseClass {
    @DeveloperFunction
    open fun overriddenFunction(): Any? = NeverRun()

    @DeveloperFunction
    protected open fun protectedOverriddenFunction(): Any? = NeverRun()
}

internal class of_InternalSubClass : of_InternalBaseClass() {
    override fun overriddenFunction(): Any? = ExpectedItemCount(1)
    override fun protectedOverriddenFunction(): Any? = ExpectedItemCount(1)
}


private open class of_PrivateBaseClass {
    @DeveloperFunction
    open fun overriddenFunction(): Any? = NeverRun()

    @DeveloperFunction
    protected open fun protectedOverriddenFunction(): Any? = NeverRun()
}

private class of_PrivateSubClass : of_PrivateBaseClass() {
    override fun overriddenFunction(): Any? = ExpectedItemCount(1)
    override fun protectedOverriddenFunction(): Any? = ExpectedItemCount(1)
}
