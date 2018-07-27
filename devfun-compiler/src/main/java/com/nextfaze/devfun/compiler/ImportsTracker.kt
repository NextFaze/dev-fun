package com.nextfaze.devfun.compiler

import com.nextfaze.devfun.core.CategoryDefinition
import com.nextfaze.devfun.core.ReferenceDefinition
import com.nextfaze.devfun.core.FunctionDefinition
import com.nextfaze.devfun.generated.DevFunGenerated
import java.lang.reflect.Method
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KClass

@Singleton
internal class ImportsTracker @Inject constructor() : Iterable<KClass<*>> {
    private val typeImports = mutableSetOf<KClass<*>>().apply {
        this += CategoryDefinition::class
        this += DevFunGenerated::class
        this += FunctionDefinition::class
        this += ReferenceDefinition::class
        this += Method::class
        this += KClass::class
    }

    override fun iterator(): Iterator<KClass<*>> = typeImports.iterator()

    operator fun plusAssign(clazz: KClass<*>) {
        typeImports += clazz
    }
}
