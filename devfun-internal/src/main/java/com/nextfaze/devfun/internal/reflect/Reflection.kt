package com.nextfaze.devfun.internal.reflect

import java.lang.reflect.Member
import java.lang.reflect.Modifier

val Member.isStatic get() = Modifier.isStatic(modifiers)
val Member.isProperty get() = isSynthetic && isStatic && name.endsWith("\$annotations")
