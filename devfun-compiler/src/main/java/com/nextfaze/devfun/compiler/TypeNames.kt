package com.nextfaze.devfun.compiler

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.WildcardTypeName.Companion.STAR
import com.squareup.kotlinpoet.asTypeName
import kotlin.reflect.KClass

object TypeNames {
    val kClass = KClass::class.asTypeName()
    val clazz = Class::class.asTypeName()
    val kClassStar = kClass.parameterizedBy(STAR)
    val enum = Enum::class.asTypeName()
    val enumStar = enum.parameterizedBy(STAR)
    val array = Array<Any>::class.asTypeName()
    val nothing = ClassName("kotlin", "Nothing")
}
