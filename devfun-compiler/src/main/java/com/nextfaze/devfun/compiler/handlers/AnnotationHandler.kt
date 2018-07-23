package com.nextfaze.devfun.compiler.handlers

import com.nextfaze.devfun.compiler.toClass
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import kotlin.reflect.KClass

internal interface AnnotationHandler {
    val elements: Elements

    val willGenerateSource: Boolean
    fun process(elements: Set<TypeElement>, env: RoundEnvironment)

    fun generateSource(): String

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
        suffix: String = if (kotlinClass) "" else ".java",
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
}
