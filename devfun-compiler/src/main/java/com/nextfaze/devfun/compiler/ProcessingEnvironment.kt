package com.nextfaze.devfun.compiler

import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element
import javax.tools.Diagnostic

internal interface WithProcessingEnvironment {
    val processingEnvironment: ProcessingEnvironment
    val filer: Filer get() = processingEnvironment.filer

    val isDebugVerbose: Boolean get() = false

    fun note(condition: Boolean = isDebugVerbose, body: () -> String) =
        runIf(condition) { processingEnvironment.messager.printMessage(Diagnostic.Kind.NOTE, body()) }

    fun warn(message: String, element: Element? = null, annotationMirror: AnnotationMirror? = null) =
        processingEnvironment.messager.printMessage(Diagnostic.Kind.MANDATORY_WARNING, message, element, annotationMirror)

    fun error(message: String, element: Element? = null, annotationMirror: AnnotationMirror? = null) =
        processingEnvironment.messager.printMessage(Diagnostic.Kind.ERROR, message, element, annotationMirror)

    fun String.optionOf(): String? = processingEnvironment.options[this]?.trim()?.takeIf { it.isNotBlank() }
}
