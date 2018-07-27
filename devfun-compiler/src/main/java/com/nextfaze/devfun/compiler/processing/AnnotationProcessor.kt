package com.nextfaze.devfun.compiler.processing

import com.nextfaze.devfun.compiler.toClass
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import kotlin.reflect.KClass

internal interface AnnotationProcessor {
    val elements: Elements

    val willGenerateSource: Boolean

    fun generateSource(): String

    fun processAnnotatedElement(annotationElement: TypeElement, annotatedElement: Element)

    fun Element.toClass(
        kotlinClass: Boolean = true,
        isKtFile: Boolean = false,
        castIfNotPublic: KClass<*>? = null,
        vararg types: KClass<*>
    ) =
        asType().toClass(
            kotlinClass = kotlinClass,
            isKtFile = isKtFile,
            elements = elements,
            castIfNotPublic = castIfNotPublic,
            types = *types
        )
}
