package com.nextfaze.devfun.core

import android.app.Activity
import android.os.Build
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.inject.Constructable
import com.nextfaze.devfun.inject.RequiringInstanceProvider
import com.nextfaze.devfun.internal.*
import java.lang.reflect.Modifier

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
internal class ContextTransformer(private val activity: Activity, private val instanceProvider: RequiringInstanceProvider) : FunctionTransformer {
    // if the method is static then we don't care about context
    // we don't check for Activity/Fragment subclass here though since we'll just need to do it again in apply (to remove it if not in context)
    override fun accept(functionDefinition: FunctionDefinition) = !Modifier.isStatic(functionDefinition.method.modifiers)

    override fun apply(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition): Collection<FunctionItem>? {
        if (Activity::class.java.isAssignableFrom(functionDefinition.clazz.java)) {
            return when {
                functionDefinition.clazz.java.isAssignableFrom(activity::class.java) -> transformItem(functionDefinition, categoryDefinition)
                else -> emptyList() // remove this item if it's not related to the current activity
            }
        }

        if (Fragment::class.java.isAssignableFrom(functionDefinition.clazz.java)) {
            if (activity is FragmentActivity) {
                if (activity.supportFragmentManager.fragments.orEmpty().any {
                    // not sure how/why, but under some circumstances some of them are null
                    it != null && it.isAdded && functionDefinition.clazz.java.isAssignableFrom(it::class.java)
                }) {
                    return transformItem(functionDefinition, categoryDefinition)
                } else {
                    // remove fragments that are not added or related to added fragments
                    return emptyList()
                }
            } else {
                // remove all fragment items if we're not in a fragment activity
                return emptyList()
            }
        }

        return null
    }

    private fun transformItem(functionDefinition: FunctionDefinition, categoryDefinition: CategoryDefinition): Collection<FunctionItem>? =
            with(instanceProvider[functionDefinition.transformer]) {
                when {
                    accept(functionDefinition) -> apply(functionDefinition, categoryDefinition)?.map { ContextFunctionItem(it, functionDefinition.clazz.splitSimpleName) } ?: emptyList()
                    else -> emptyList()
                }
            }
}
