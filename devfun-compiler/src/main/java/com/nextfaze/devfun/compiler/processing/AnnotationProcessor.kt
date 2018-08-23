package com.nextfaze.devfun.compiler.processing

import com.nextfaze.devfun.compiler.*
import com.squareup.kotlinpoet.*
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import kotlin.reflect.KCallable
import kotlin.reflect.KClass

internal interface Processor : WithElements {
    val preprocessor: StringPreprocessor

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

    // todo remove this once https://github.com/square/kotlinpoet/issues/439 resolved
    fun String.toLiteral(element: Element?) = preprocessor.run(toKString(trimMargin = true), element)
}

internal interface AnnotationProcessor : Processor {
    val options: Options

    val willGenerateSource: Boolean

    fun generateSource(): String

    fun processAnnotatedElement(annotatedElement: AnnotatedElement, env: RoundEnvironment)

    fun KCallable<*>.toPropertySpec(
        propName: String = name,
        returns: TypeName = returnType.asTypeName(),
        init: Any? = null,
        lazy: Any? = null,
        kDoc: Any? = null,
        kDocEnabled: Boolean = options.isDebugCommentsEnabled,
        initType: TypeElement? = null,
        initTypeCast: KClass<*>? = KClass::class,
        vararg initTypeParams: KClass<*>
    ) =
        PropertySpec.builder(
            propName,
            returns,
            KModifier.OVERRIDE
        ).apply {
            if (init != null) initializer("%L", init)
            if (initType != null) {
                when {
                    initType.isClassPublic -> initializer("%T::class", initType.asClassName())
                    else -> initializer("%L", initType.toClass(castIfNotPublic = initTypeCast, types = *initTypeParams))
                }
            }
            if (lazy != null) delegate("lazy { %L }", lazy)
            if (kDoc != null && kDocEnabled) addKdoc("%L", kDoc)
        }

    fun Collection<*>.toInitList(type: KClass<*>) =
        CodeBlock.of("listOf<%T>(${",\n%L".repeat(size).drop(1)})", type, *toTypedArray())
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
