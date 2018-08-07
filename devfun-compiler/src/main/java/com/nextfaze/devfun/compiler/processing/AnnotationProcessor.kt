package com.nextfaze.devfun.compiler.processing

import com.nextfaze.devfun.compiler.getAnnotation
import com.nextfaze.devfun.compiler.toClass
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element
import javax.lang.model.element.PackageElement
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import kotlin.reflect.KClass

internal interface Processor {
    val elements: Elements

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

    val Element.packageElement: PackageElement
        get() = elements.getPackageOf(this)
}

internal interface AnnotationProcessor : Processor {
    val willGenerateSource: Boolean

    fun generateSource(): String

    fun processAnnotatedElement(annotatedElement: AnnotatedElement)
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
