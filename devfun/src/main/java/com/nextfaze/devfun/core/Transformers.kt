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
import com.nextfaze.devfun.internal.*
import com.nextfaze.devfun.invoke.*
import kotlin.reflect.*
import kotlin.reflect.full.IllegalPropertyDelegateAccessException
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaMethod
import kotlin.reflect.jvm.kotlinProperty

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

@Constructable
internal class CustomProviderTransformer(private val instanceProvider: RequiringInstanceProvider) : FunctionTransformer {
    override fun accept(functionDefinition: FunctionDefinition) =
        functionDefinition.transformer != SingleFunctionTransformer::class &&
                instanceProvider[functionDefinition.transformer].accept(functionDefinition)

    override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition) =
        instanceProvider[functionDefinition.transformer].apply(functionDefinition, categoryDefinition)
}

@DeveloperCategory("Context", order = -10_000)
internal data class ContextFunctionItem(private val functionItem: FunctionItem, override val group: String) : FunctionItem by functionItem {
    private object ContextCategory : CategoryDefinition {
        override val name get() = "Context"
        override val order get() = -10_000
        override fun toString() = "ContextCategory"
    }

    override val category: CategoryDefinition = ContextCategory
}

@Constructable
internal class ContextTransformer(
    private val activity: Activity,
    private val instanceProvider: RequiringInstanceProvider
) : FunctionTransformer {
    // if the method is static then we don't care about context
    // however we also need to check ifthe method is a "property" (Kotlin creates a synthetic static method to hold annotations)
    // we don't check for Activity/Fragment subclass here though since we'll just need to do it again in apply (to remove it if not in context)
    override fun accept(functionDefinition: FunctionDefinition) =
        !functionDefinition.method.isStatic || functionDefinition.method.isProperty

    override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition): Collection<FunctionItem>? {
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
internal class PropertyTransformerImpl(
    private val instanceProvider: RequiringInstanceProvider,
    private val invoker: Invoker
) : PropertyTransformer {
    private val log = logger()

    private class Property<out T : Any?>(kProperty: KProperty<T>, override val value: T) : Parameter, WithInitialValue<T>, WithNullability {
        override val name: String = kProperty.name
        override val type = kProperty.returnType.classifier as KClass<*>
        override val isNullable = kProperty.returnType.isMarkedNullable
        override val annotations = kProperty.annotations
    }

    override fun accept(functionDefinition: FunctionDefinition) = functionDefinition.transformer == PropertyTransformer::class

    override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition): Collection<FunctionItem>? {
        val fieldName = functionDefinition.method.name.substringBefore('$')
        val propertyField = try {
            functionDefinition.clazz.java.getDeclaredField(fieldName).apply { isAccessible = true }
        } catch (ignore: NoSuchFieldException) {
            null // is property without backing field (i.e. has custom getter/setter)
        }
        val property = when {
            propertyField != null -> propertyField.kotlinProperty!!
            else -> functionDefinition.clazz.declaredMemberProperties.first { it.name == fieldName }
        }.apply { isAccessible = true }


        // Kotlin reflection has weird accessibility issues when invoking get/set/getter/setter .call()
        // it only seems to work the first time with subsequent calls failing with illegal access exceptions and the like
        val getter = property.getter.javaMethod?.apply { isAccessible = true }
        val setter = if (property is KMutableProperty<*>) property.setter.javaMethod?.apply { isAccessible = true } else null

        val propertyDesc = run {
            val lateInit = if (property.isLateinit) "lateinit " else ""
            val varType = if (property is KMutableProperty<*>) "var" else "val"
            "$lateInit$varType $fieldName: ${property.simpleName}"
        }

        return listOf(object : SimpleFunctionItem(functionDefinition, categoryDefinition) {
            private val receiver by lazy { functionDefinition.receiverInstance(instanceProvider) }
            private val isUninitialized by lazy { property.isUninitialized }

            private var value: Any?
                get() = when {
                    getter != null -> getter.invoke(receiver)
                    else -> propertyField?.get(receiver)
                }
                set(value) {
                    when {
                        setter != null -> setter.invoke(receiver, value)
                        else -> propertyField?.set(receiver, value)
                    }
                }
            override val name by lazy {
                SpannableStringBuilder().apply {
                    this += propertyDesc
                    this += " = "
                    when {
                        property.isLateinit && value == null -> this += i("undefined")
                        isUninitialized -> this += i("uninitialized")
                        property.type == String::class && value != null -> this += """"$value""""
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
                        title = propertyDesc,
                        signature = property.toString(),
                        parameters = listOf(Property(property, value)),
                        invoke = { it.first().also { value = it } }
                    )
                )
            }

            @Suppress("UNCHECKED_CAST")
            private val KProperty<*>.isUninitialized: Boolean
                get() {
                    fun tryGet() = try {
                        when (this) {
                            is KProperty0<*> -> (getDelegate() as? Lazy<*>)?.isInitialized() == false
                            is KProperty1<*, *> -> ((this as KProperty1<Any?, Any>).getDelegate(receiver) as? Lazy<*>)?.isInitialized() == false
                            else -> false
                        }
                    } catch (t: IllegalPropertyDelegateAccessException) {
                        log.d(t) { "Kotlin Bug: Ignoring IllegalPropertyDelegateAccessException" }
                        null
                    }

                    isAccessible = true
                    return tryGet() ?: tryGet() ?: false // try it again if it first fails
                }
        })
    }

    private val KProperty<*>.simpleName get() = "${type.simpleName}${if (returnType.isMarkedNullable) "?" else ""}"
    private val KProperty<*>.type get() = returnType.classifier as KClass<*>
}
