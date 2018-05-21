package com.nextfaze.devfun.core

import android.app.Activity
import android.os.Build
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.text.SpannableStringBuilder
import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.PropertyTransformer
import com.nextfaze.devfun.inject.Constructable
import com.nextfaze.devfun.inject.RequiringInstanceProvider
import com.nextfaze.devfun.inject.isSubclassOf
import com.nextfaze.devfun.internal.ReflectedProperty
import com.nextfaze.devfun.internal.WithSubGroup
import com.nextfaze.devfun.internal.reflect.*
import com.nextfaze.devfun.internal.splitSimpleName
import com.nextfaze.devfun.internal.string.*
import com.nextfaze.devfun.internal.toReflected
import com.nextfaze.devfun.invoke.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

internal val TRANSFORMERS = listOf(
    RequiresApiTransformer::class,
    ContextTransformer::class,
    CustomProviderTransformer::class,
    SingleFunctionTransformer::class
)

/**
 * Filters items that have a [FunctionDefinition.requiresApi] that is greater than the system.
 *
 * i.e. Items that require an API level later than what the device has will be filtered out.
 */
internal object RequiresApiTransformer : FunctionTransformer {
    override fun accept(functionDefinition: FunctionDefinition) = functionDefinition.requiresApi > 0

    override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition): Collection<FunctionItem>? =
        when {
            functionDefinition.requiresApi > Build.VERSION.SDK_INT -> emptyList()
            else -> null
        }
}

@Constructable(singleton = true)
internal class CustomProviderTransformer(private val instanceProvider: RequiringInstanceProvider) : FunctionTransformer {
    override fun accept(functionDefinition: FunctionDefinition) =
        functionDefinition.transformer != SingleFunctionTransformer::class &&
                instanceProvider[functionDefinition.transformer].accept(functionDefinition)

    override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition) =
        instanceProvider[functionDefinition.transformer].apply(functionDefinition, categoryDefinition)
}

@DeveloperCategory("Context", order = -10_000)
internal data class ContextFunctionItem(private val functionItem: FunctionItem, override val group: CharSequence?) :
    FunctionItem by functionItem, WithSubGroup {
    private object ContextCategory : CategoryDefinition {
        override val name = "Context"
        override val order = -10_000
        override fun toString() = "ContextCategory"
    }

    override val category: CategoryDefinition = ContextCategory
    override val subGroup = if (functionItem is WithSubGroup) functionItem.subGroup else functionItem.group
}

@Constructable(singleton = true)
internal class ContextTransformer(
    private val activityProvider: ActivityProvider,
    private val instanceProvider: RequiringInstanceProvider
) : FunctionTransformer {
    // if the method is static then we don't care about context
    // however we also need to check ifthe method is a "property" (Kotlin creates a synthetic static method to hold annotations)
    // we don't check for Activity/Fragment subclass here though since we'll just need to do it again in apply (to remove it if not in context)
    override fun accept(functionDefinition: FunctionDefinition) =
        !functionDefinition.method.isStatic || functionDefinition.method.isProperty

    override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition): Collection<FunctionItem>? {
        val activity = activityProvider() ?: return emptyList()

        if (Activity::class.java.isAssignableFrom(functionDefinition.clazz.java)) {
            return when {
                activity::class.isSubclassOf(functionDefinition.clazz) -> transformItem(functionDefinition, categoryDefinition)
                else -> emptyList() // remove this item if it's not related to the current activity
            }
        }

        if (functionDefinition.clazz.isSubclassOf<Fragment>()) {
            return if (activity is FragmentActivity) {
                if (activity.supportFragmentManager.fragments.orEmpty().any {
                        // not sure how/why, but under some circumstances some of them are null
                        it != null && it.isAdded && functionDefinition.clazz.java.isAssignableFrom(it::class.java)
                    }) {
                    transformItem(functionDefinition, categoryDefinition)
                } else {
                    // remove fragments that are not added or related to added fragments
                    emptyList()
                }
            } else {
                // remove all fragment items if we're not in a fragment activity
                emptyList()
            }
        }

        return null
    }

    private fun transformItem(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition): Collection<FunctionItem>? =
        with(instanceProvider[functionDefinition.transformer]) {
            when {
                accept(functionDefinition) ->
                    apply(functionDefinition, categoryDefinition)
                        ?.map { ContextFunctionItem(it, functionDefinition.clazz.splitSimpleName) } ?: emptyList()
                else -> emptyList()
            }
        }
}

@Constructable
internal class PropertyTransformerImpl(private val invoker: Invoker) : PropertyTransformer {
    private class Property<out T : Any?>(kProperty: KProperty<T>, override val value: T) : Parameter, WithInitialValue<T>, WithNullability {
        override val name: String = kProperty.name
        override val type = kProperty.returnType.classifier as KClass<*>
        override val isNullable = kProperty.returnType.isMarkedNullable
        override val annotations = kProperty.annotations
    }

    override fun accept(functionDefinition: FunctionDefinition) = functionDefinition.transformer == PropertyTransformer::class

    override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition): Collection<FunctionItem>? {
        val prop = functionDefinition.method.toReflected() as ReflectedProperty
        return listOf(object : SimpleFunctionItem(functionDefinition, categoryDefinition) {
            private val isUninitialized by lazy { prop.isUninitialized }

            override val name by lazy {
                val value = prop.value
                SpannableStringBuilder().apply {
                    this += prop.desc
                    this += " = "
                    when {
                        prop.isLateinit && value == null -> this += i("undefined")
                        isUninitialized -> this += i("uninitialized")
                        prop.type == String::class && value != null -> this += """"$value""""
                        else -> this += "$value"
                    }
                    if (isUninitialized) {
                        this += "\n"
                        this += color(scale(i("\t(tap will initialize)"), 0.85f), 0xFFAAAAAA.toInt())
                    }
                }
            }

            override val invoke: FunctionInvoke = { _, _ ->
                invoker.invoke(
                    uiFunction(
                        title = prop.desc,
                        signature = prop.property.toString(),
                        parameters = listOf(Property(prop.property, prop.value)),
                        invoke = { it.first().also { prop.value = it } }
                    )
                )
            }
        })
    }
}
