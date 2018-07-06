package com.nextfaze.devfun.core

import android.os.Debug
import android.text.SpannableStringBuilder
import com.nextfaze.devfun.error.ErrorHandler
import com.nextfaze.devfun.internal.exception.ExceptionCategoryItem
import com.nextfaze.devfun.internal.exception.stackTraceAsString
import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.internal.prop.*
import com.nextfaze.devfun.internal.splitSimpleName
import com.nextfaze.devfun.internal.string.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.reflect.KClass

internal class DefinitionsProcessor(private val devFun: DevFun) {
    private val log = logger()

    private val transformers by lazy { TRANSFORMERS.map { devFun.instanceOf(it) } }
    private val errorHandler by lazy { devFun.get<ErrorHandler>() }

    private val userDefinedCategories by lazy {
        devFun.definitions
            .flatMap { it.categoryDefinitions }
            .toSet()
            .associateBy { it.clazz }
    }

    private val categoryDefinitions by lazy { userDefinedCategories.mapKeys { it.value.displayString }.toMutableMap() }

    @Suppress("ConstantConditionIf")
    private val tracing by threadLocal { if (false) AtomicInteger(0) else null }

    fun getTransformedDefinitions(): List<CategoryItem> {
        if (tracing?.getAndIncrement() == 0) {
            Debug.startMethodTracing("DefinitionsProcessor.getTransformedDefinitions.${Thread.currentThread().id}")
        }
        return try {
            transformedDefinitions()
        } catch (t: Throwable) {
            errorHandler.onError(t, "Generate Categories", "Exception while attempting to generate categories.")
            listOf(ExceptionCategoryItem(t.stackTraceAsString))
        } finally {
            if (tracing?.decrementAndGet() == 0) {
                Debug.stopMethodTracing()
            }
        }
    }

    private fun transformedDefinitions(): List<CategoryItem> {
        val functionItems = mutableListOf<FunctionItem>()

        devFun.definitions
            .flatMap { it.functionDefinitions }
            .toSet()
            .forEach {
                try {
                    it.applyTransformers(transformers, functionItems)
                } catch (t: Throwable) {
                    errorHandler.onError(
                        t,
                        "Function Transformation",
                        SpannableStringBuilder().apply {
                            this += u("Failed to transform function definition:\n")
                            this += pre(it.toString())
                            this += u("\nOn method:\n")
                            this += pre(it.method.toString())
                        }
                    )
                }
            }

        return functionItems
            .groupBy {
                // determine category name for item
                it.category.name ?: it.category.clazz?.splitSimpleName ?: "Misc"
            }
            .mapKeys { (categoryName, functionItems) ->
                // create category object for items and determine order
                val order = functionItems.firstOrNull { it.category.order != null }?.category?.order ?: 0
                SimpleCategoryItem(categoryName, functionItems, order)
            }
            .keys
            .sortedWith(compareBy<SimpleCategoryItem> { it.order }.thenBy { it.name.toString() })
    }

    private fun FunctionDefinition.applyTransformers(transformers: List<FunctionTransformer>, functionItems: MutableList<FunctionItem>) {
        log.t { "Processing ${clazz.java.simpleName}::$name" }

        val resolvedCategory = resolveCategoryDefinition()
        transformers.forEach {
            if (!it.accept(this)) {
                log.t { "Transformer $it ignored item" }
                return@forEach
            }

            val items = it.apply(this, resolvedCategory)
            log.t { "Transformer $it accepted item and returned ${items?.size} items: ${items?.joinToString { it.name }}" }
            if (items != null) {
                functionItems.addAll(items)
                return // accepted and transformed by transformer - halt further transformations
            }
        }
    }

    private fun FunctionDefinition.resolveCategoryDefinition(): CategoryDefinition {
        val category = category
                ?: return getOrCreateCategoryDefinitionForClass(categoryClass) // not defined at function - use class

        val categoryName = category.name?.toString()
        if (categoryName == null) { // they're overriding some class-level category
            val parentCategory = getOrCreateCategoryDefinitionForClass(categoryClass)
            return InheritingCategoryDefinition(parentCategory, category)
        }

        val parentCategory = categoryDefinitions[categoryName]
        if (parentCategory == null) { // defining a new category
            categoryDefinitions[categoryName] = category
            return category
        }

        // inheriting some other previously defined category
        return InheritingCategoryDefinition(parentCategory, category)
    }

    /** If a function definition is declared in a Companion object we want to use the "real" (outer) class for the category definition. */
    private val resolvedCategoryClasses = mutableMapOf<KClass<*>, KClass<*>>()

    private val FunctionDefinition.categoryClass: KClass<*>
        get() = resolvedCategoryClasses[clazz]
                ?: try {
                    when {
                        clazz.java.enclosingClass != null && clazz.isCompanion -> clazz.java.enclosingClass.kotlin
                        else -> clazz
                    }
                } catch (ignore: UnsupportedOperationException) {
                    clazz // happens with top-level functions (reflection not supported for them yet)
                }.also {
                    resolvedCategoryClasses[clazz] = it
                }

    private fun getOrCreateCategoryDefinitionForClass(clazz: KClass<*>) =
        userDefinedCategories[clazz] ?: getOrCreateCategoryDefinitionForName(clazz.splitSimpleName)

    private fun getOrCreateCategoryDefinitionForName(name: String) =
        categoryDefinitions.getOrPut(name) { SimpleCategoryDefinition(name) }

    private val CategoryDefinition.displayString: String
        get() = name?.toString() ?: clazz?.splitSimpleName ?: "Misc"
}

private class SimpleCategoryItem(
    override val name: CharSequence,
    override val items: List<FunctionItem>,
    override val order: Int = 0
) : CategoryItem {
    override fun equals(other: Any?) = if (other is CategoryItem) name == other.name else false
    override fun hashCode() = name.hashCode()
}

private data class SimpleCategoryDefinition(override val name: String) : CategoryDefinition

private data class InheritingCategoryDefinition(val parent: CategoryDefinition, val child: CategoryDefinition) : CategoryDefinition {
    override val clazz get() = child.clazz ?: parent.clazz
    override val name get() = child.name ?: parent.name
    override val group get() = child.group ?: parent.group
    override val order get() = child.order ?: parent.order
}
