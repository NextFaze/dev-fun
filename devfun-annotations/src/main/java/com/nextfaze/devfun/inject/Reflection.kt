package com.nextfaze.devfun.inject

import kotlin.reflect.KClass

inline fun <reified T : Any> KClass<*>.isSubclassOf() = T::class.java.isAssignableFrom(this.java)
fun <T : Any> KClass<*>.isSubclassOf(base: KClass<out T>) = base.java.isAssignableFrom(this.java)

inline fun <reified T : Any> KClass<*>.isSuperclassOf() = java.isAssignableFrom(T::class.java)
fun <T : Any> KClass<*>.isSuperclassOf(derived: KClass<out T>) = java.isAssignableFrom(derived.java)
