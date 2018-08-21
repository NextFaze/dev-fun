package com.nextfaze.devfun.compiler.processing

import com.nextfaze.devfun.compiler.WithElements
import com.nextfaze.devfun.compiler.getAnnotation
import com.nextfaze.devfun.compiler.toClass
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import kotlin.reflect.KClass

internal interface Processor : WithElements {
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

    fun TypeMirror.toClass(
        kotlinClass: Boolean = true,
        isKtFile: Boolean = false,
        castIfNotPublic: KClass<*>? = null,
        vararg types: KClass<*>
    ) =
        toClass(
            kotlinClass = kotlinClass,
            isKtFile = isKtFile,
            elements = elements,
            castIfNotPublic = castIfNotPublic,
            types = *types
        )

    // during normal gradle builds string types will be java.lang
    // during testing however they will be kotlin types
    val TypeMirror.isString get() = toString().let { it == "java.lang.String" || it == "kotlin.String" }
}

internal interface AnnotationProcessor : Processor {
    val willGenerateSource: Boolean

    fun generateSource(): String

    fun processAnnotatedElement(annotatedElement: AnnotatedElement, env: RoundEnvironment)
}


data class AnnotatedElement(
    val element: Element,
    val annotationElement: TypeElement,
    val asFunction: Boolean,
    val asCategory: Boolean,
    val asReference: Boolean
) {
    val annotation: AnnotationMirror = element.getAnnotation(annotationElement)!!
}
