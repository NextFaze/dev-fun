package com.nextfaze.devfun.inject.dagger2

import com.nextfaze.devfun.internal.log.*
import dagger.Module
import dagger.Provides
import java.lang.reflect.Field
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.inject.Provider
import kotlin.reflect.KClass

private val log = logger("${BuildConfig.APPLICATION_ID}.resolution")

internal val resolvedComponents = mutableMapOf<KClass<*>, ResolvedComponent>()

internal data class ResolvedComponent(val componentType: KClass<*>) {
    private val typedProviders: MutableMap<Field, Type> = mutableMapOf()
    private val untypedProviders: MutableMap<Field, String> = mutableMapOf()
    private val resolvedProviders: MutableMap<KClass<*>, Field> = mutableMapOf()

    private val unresolvedGetters: MutableMap<Method, String> = mutableMapOf()
    private val resolveGetters: MutableMap<KClass<*>, Method> = mutableMapOf()
    private val moduleProviders: MutableMap<KClass<*>, Pair<Field, Method>> = mutableMapOf()

    init {
        log.t { "Resolving types for component $componentType ..." }
        componentType.java.declaredFields.forEach { field ->
            if (field.type == Provider::class.java) {
                log.t { "Found provider: $field" }
                field.isAccessible = true

                val genericType = field.genericType
                if (genericType is ParameterizedType) {
                    typedProviders[field] = genericType.actualTypeArguments[0]
                } else {
                    // raw types (package private non-singleton types)
                    // since we can't know the type until we get it, we at least check the name matches first
                    untypedProviders[field] = field.name.substringBefore('$').removeSuffix("Provider").toLowerCase()
                }
            } else if (field.type.hasAnnotation<Module>()) {
                log.t { "Found module: $field" }
                field.isAccessible = true

                field.type.declaredMethods.forEach {
                    if (it.hasAnnotation<Provides>()) {
                        it.isAccessible = true
                        val clazz = (it.returnType as Class<*>).kotlin
                        moduleProviders[clazz] = field to it
                    }
                }
            }
        }

        // Component getters (@Inject on constructor, but not @Singleton types)
        componentType.java.declaredMethods.forEach {
            if (it.name.startsWith("get")) {
                log.t { "Found getter: $it" }
                it.isAccessible = true

                if (it.returnType == Any::class.java) { // package private
                    unresolvedGetters[it] = it.name.removePrefix("get")
                } else {
                    val clazz = (it.returnType as Class<*>).kotlin
                    resolveGetters[clazz] = it
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Any> getInstance(component: Any, clazz: KClass<T>): T? {
        resolvedProviders[clazz]?.get(component)?.let {
            log.t { "Hit of instance of $clazz from provider $it" }
            return (it as Provider<T>).get()
        }
        resolveGetters[clazz]?.invoke(component)?.let {
            log.t { "Hit of instance of $clazz from getter $it" }
            return it as T
        }

        // Check typed providers
        if (typedProviders.isNotEmpty()) {
            log.t { "Trying to get $clazz from typed $component providers..." }
            val it = typedProviders.iterator()
            it.forEach { (field, type) ->
                if (type == clazz.java) {
                    // found a type match - store for future lookups
                    it.remove()
                    resolvedProviders[clazz] = field
                    return field.get(component)?.let { (it as Provider<T>).get() }
                }
            }
        }

        // Check untyped providers
        if (untypedProviders.isNotEmpty()) {
            log.t { "Trying to get $clazz from untyped $component providers..." }
            val classFieldName = clazz.java.simpleName.toLowerCase()
            val it = untypedProviders.iterator()
            it.forEach { (field, providerFieldName) ->
                if (classFieldName != providerFieldName) return@forEach
                try {
                    val instance = field.get(component)?.let { (it as Provider<*>).get() }
                    if (instance?.javaClass == clazz.java) {
                        // found a type match - store for future lookups
                        it.remove()
                        resolvedProviders[clazz] = field
                        return instance as T
                    }
                } catch (t: Throwable) {
                    log.d(t) { "Exception when trying to get $clazz from raw-type Provider $it (package private non-singleton type) - skipping provider." }
                }
            }
        }

        // Check Component getters (@Inject on constructor, but not @Singleton types)
        if (unresolvedGetters.isNotEmpty()) {
            log.t { "Trying to get $clazz from unresolved $component getters..." }
            val className = clazz.java.simpleName
            val it = unresolvedGetters.iterator()
            it.forEach { (method, name) ->
                if (name != className) return@forEach
                try {
                    val instance = method.invoke(component)
                    if (instance?.javaClass == clazz.java) {
                        // found a type match - store for future lookups
                        it.remove()
                        resolveGetters[clazz] = method
                        return instance as T
                    }
                } catch (t: Throwable) {
                    log.d(t) { "Exception when trying to get $clazz from raw-type Getter $it (package private non-singleton type) - skipping provider." }
                }
            }
        }

        // Get from Module @Provides
        log.t { "Trying to get $clazz from $component modules..." }
        moduleProviders[clazz]?.let { (field, method) ->
            return method.invoke(field.get(component)) as T
        }

        // Not found in this component
        return null
    }
}
