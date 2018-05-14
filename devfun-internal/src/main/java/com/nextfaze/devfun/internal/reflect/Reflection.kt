package com.nextfaze.devfun.internal.reflect

import java.lang.reflect.Member
import java.lang.reflect.Modifier
import kotlin.reflect.KClass

inline fun <reified T : Any> KClass<*>.isSubclassOf() = T::class.java.isAssignableFrom(this.java)
fun <T : Any> KClass<*>.isSubclassOf(clazz: KClass<T>) = clazz.java.isAssignableFrom(this.java)

val Member.isStatic get() = Modifier.isStatic(modifiers)
val Member.isProperty get() = isSynthetic && isStatic && name.endsWith("\$annotations")
