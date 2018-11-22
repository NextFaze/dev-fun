package com.nextfaze.devfun.core

import android.app.Activity
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.nextfaze.devfun.category.CONTEXT_CAT_NAME
import com.nextfaze.devfun.category.CONTEXT_CAT_ORDER
import com.nextfaze.devfun.category.CategoryDefinition
import com.nextfaze.devfun.category.ContextCategory
import com.nextfaze.devfun.function.ArgumentsTransformer
import com.nextfaze.devfun.function.DeveloperArgumentsProperties
import com.nextfaze.devfun.function.FunctionDefinition
import com.nextfaze.devfun.function.FunctionInvoke
import com.nextfaze.devfun.function.FunctionItem
import com.nextfaze.devfun.function.FunctionTransformer
import com.nextfaze.devfun.function.PropertyTransformer
import com.nextfaze.devfun.function.SimpleFunctionItem
import com.nextfaze.devfun.function.SingleFunctionTransformer
import com.nextfaze.devfun.inject.Constructable
import com.nextfaze.devfun.inject.ThrowingInstanceProvider
import com.nextfaze.devfun.inject.isSubclassOf
import com.nextfaze.devfun.internal.ReflectedProperty
import com.nextfaze.devfun.internal.WithSubGroup
import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.internal.reflect.*
import com.nextfaze.devfun.internal.splitCamelCase
import com.nextfaze.devfun.internal.splitSimpleName
import com.nextfaze.devfun.internal.toReflected
import com.nextfaze.devfun.internal.toStringRepresentation
import com.nextfaze.devfun.invoke.Invoker
import com.nextfaze.devfun.invoke.Parameter
import com.nextfaze.devfun.invoke.WithInitialValue
import com.nextfaze.devfun.invoke.WithNullability
import com.nextfaze.devfun.invoke.uiFunction
import com.nextfaze.devfun.reference.ReferenceDefinition
import com.nextfaze.devfun.reference.withProperties
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
internal class CustomProviderTransformer(private val instanceProvider: ThrowingInstanceProvider) : FunctionTransformer {
    override fun accept(functionDefinition: FunctionDefinition) =
        functionDefinition.transformer != SingleFunctionTransformer::class &&
                instanceProvider[functionDefinition.transformer].accept(functionDefinition)

    override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition) =
        instanceProvider[functionDefinition.transformer].apply(functionDefinition, categoryDefinition)
}

@ContextCategory
internal data class ContextFunctionItem(private val functionItem: FunctionItem, override val group: CharSequence?) :
    FunctionItem by functionItem, WithSubGroup {
    private object ContextCategory : CategoryDefinition {
        override val name = CONTEXT_CAT_NAME
        override val order = CONTEXT_CAT_ORDER
        override fun toString() = "ContextCategory"
    }

    override val category: CategoryDefinition = ContextCategory
    override val subGroup = if (functionItem is WithSubGroup) functionItem.subGroup else functionItem.group
}

@Constructable(singleton = true)
internal class ContextTransformer(
    private val activityProvider: ActivityProvider,
    private val instanceProvider: ThrowingInstanceProvider
) : FunctionTransformer {
    // if the method is static then we don't care about context
    // however we also need to check if the method is a "property" (Kotlin creates a synthetic static method to hold annotations)
    // we don't check for Activity/Fragment subclass here though since we'll just need to do it again in apply (to remove it if not in context)
    override fun accept(functionDefinition: FunctionDefinition) =
        !functionDefinition.method.isStatic || functionDefinition.method.isProperty

    override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition): Collection<FunctionItem>? {
        if (Activity::class.java.isAssignableFrom(functionDefinition.clazz.java)) {
            val activity = activityProvider() ?: return emptyList()
            return when {
                activity::class.isSubclassOf(functionDefinition.clazz) -> transformItem(functionDefinition, categoryDefinition)
                else -> emptyList() // remove this item if it's not related to the current activity
            }
        }

        if (functionDefinition.clazz.isSubclassOf<Fragment>()) {
            val activity = activityProvider() ?: return emptyList()
            return if (activity is FragmentActivity) {
                if (activity.supportFragmentManager.fragments.any {
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
            override val name by lazy { prop.toStringRepresentation() }
            override val invoke: FunctionInvoke = { _, _ ->
                invoker.invoke(
                    uiFunction(
                        title = prop.desc,
                        signature = prop.property.toString(),
                        parameters = listOf(Property(prop.property, prop.value)),
                        invoke = { args -> args.first().also { prop.value = it } }
                    )
                )
            }
        })
    }
}

@Constructable
internal class ArgumentsTransformerImpl : ArgumentsTransformer {
    private val log = logger()

    companion object {
        private val argIndexRegex = Regex("""%\d+""")
    }

    override fun accept(functionDefinition: FunctionDefinition) = functionDefinition.transformer == ArgumentsTransformer::class

    @Suppress("UNCHECKED_CAST")
    override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition): Collection<FunctionItem>? {
        if (functionDefinition !is ReferenceDefinition) {
            log.w { "Got a function definition $functionDefinition that was not a DeveloperReference - ignoring." }
            return null
        }

        val annotation = functionDefinition.withProperties<DeveloperArgumentsProperties>()
        val values = annotation?.args?.map { it.value }
        if (values == null || values.isEmpty()) {
            log.w { "Got a DeveloperReference function definition $functionDefinition with no args? - ignoring" }
            return null
        }

        val name = annotation.name
        val group = annotation.group

        return values.map { args ->
            fun String.replaceArgs(): String {
                if (!contains('%')) return this

                var str = this
                argIndexRegex.findAll(str).forEach { match ->
                    val index = match.value.substring(1).toIntOrNull()
                    str = when (index) {
                        null -> str.replace(match.value, "<${match.value},BAD_INT>")
                        else -> str.replace(match.value, args.elementAtOrElse(index) { "<$index,OUT_OF_BOUNDS>" })
                    }
                }

                // TODO ideally this should be done at compile time like the rest of the special vars
                return str.replace("%FUN_SN%", functionDefinition.method.name.splitCamelCase())
            }

            val nameValue = name.takeIf { it.isNotBlank() }?.replaceArgs()
            val groupValue = group.takeIf { it.isNotBlank() }?.replaceArgs()

            val paramTypes = functionDefinition.method.parameterTypes
            val typedArgs = args.mapIndexed { index: Int, s: String ->
                val type = paramTypes.getOrElse(index) { null }?.kotlin
                when (type) {
                    Boolean::class -> s.toBoolean()
                    Byte::class -> s.toByte()
                    Short::class -> s.toShort()
                    Int::class -> s.toInt()
                    Long::class -> s.toLong()
                    Char::class -> s[0]
                    Float::class -> s.toFloat()
                    Double::class -> s.toDouble()
                    String::class -> s
                    else -> Unit
                }
            }

            object : SimpleFunctionItem(functionDefinition, categoryDefinition) {
                override val name = nameValue ?: super.name
                override val group = groupValue ?: super.group
                override val args = typedArgs
            }
        }
    }
}
