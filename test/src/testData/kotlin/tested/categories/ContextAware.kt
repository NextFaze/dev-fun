@file:Suppress("ClassName", "unused", "UNCHECKED_CAST", "PropertyName")

package tested.categories

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.nextfaze.devfun.core.ActivityProvider
import com.nextfaze.devfun.function.DeveloperFunction
import com.nextfaze.devfun.function.DeveloperProperty
import com.nextfaze.devfun.function.FunctionItem
import com.nextfaze.devfun.inject.InstanceProvider
import com.nextfaze.devfun.test.NOPFunctionItem
import com.nextfaze.devfun.test.TestInstanceProviders
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.test.expect

object ContextAware : TestInstanceProviders {
    override val testProviders: List<KClass<out InstanceProvider>> get() = listOf(ActivityProviderInstanceProvider::class)
}

private class ActivityProviderInstanceProvider : InstanceProvider {
    override fun <T : Any> get(clazz: KClass<out T>) = when {
        clazz.isSubclassOf(ActivityProvider::class) ->
            object : ActivityProvider {
                override fun invoke() = ca_MyActivity()
            }
        else -> null
    } as T?
}

class ca_MyActivity : FragmentActivity() {
    @DeveloperProperty
    val testFIFD_Property: String
        get() {
            return ""
        }

    @DeveloperFunction
    fun testFunctionInActiveActivity(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("Context") { functionItem.category.name }
    }

    override fun getSupportFragmentManager(): FragmentManager {
        val caMyFragment = mock<ca_MyFragment> { on { isAdded } doReturn true }
        val caSomeOtherFragment = mock<ca_MyFragment>()
        return mock {
            on { fragments } doReturn listOf<Fragment>(caMyFragment, caSomeOtherFragment)
        }
    }
}

class ca_SomeOtherActivity : Activity() {
    @DeveloperProperty
    val testFI_Property: String
        get() {
            error("Should not be in transformed function items as this::class=${ca_SomeOtherActivity::class} is not current activity!")
        }

    @DeveloperFunction
    fun testFunctionInOtherActivity(functionItem: FunctionItem, activity: Activity) {
        if (functionItem === NOPFunctionItem) return
        error("Should not be in transformed function items as this::class=${ca_SomeOtherActivity::class} != activity=${activity::class}")
    }
}

@DeveloperFunction
fun ca_MyActivity.testExtensionFunctionInActiveActivity(functionItem: FunctionItem) {
    if (functionItem === NOPFunctionItem) return
    expect("Context") { functionItem.category.name }
}

@DeveloperFunction
fun ca_SomeOtherActivity.testExtensionFunctionInOtherActivity(functionItem: FunctionItem, activity: Activity) {
    if (functionItem === NOPFunctionItem) return
    error("Should not be in transformed function items list as this::class=${ca_SomeOtherActivity::class} != activity=${activity::class}")
}

class ca_MyFragment : Fragment() {
    @DeveloperFunction
    fun testFunctionInAddedFragment(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        expect("Context") { functionItem.category.name }
    }
}

class ca_SomeOtherFragment : Fragment() {
    @DeveloperFunction
    fun testFunctionInSomeOtherFragment(functionItem: FunctionItem) {
        if (functionItem === NOPFunctionItem) return
        error("Should not be in transformed function items as this::class=${ca_SomeOtherFragment::class} is not in fragment list!")
    }
}

@DeveloperFunction
fun ca_MyFragment.testExtensionFunctionInActiveFragment(functionItem: FunctionItem) {
    if (functionItem === NOPFunctionItem) return
    expect("Context") { functionItem.category.name }
}

@DeveloperFunction
fun ca_SomeOtherFragment.testExtensionFunctionInOtherFragment(functionItem: FunctionItem) {
    if (functionItem === NOPFunctionItem) return
    error("Should not be in transformed function items list as this::class=${ca_SomeOtherFragment::class} is not in fragment list!")
}
