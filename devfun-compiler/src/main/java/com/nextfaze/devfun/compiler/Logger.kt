package com.nextfaze.devfun.compiler

import java.io.PrintWriter
import java.io.StringWriter
import javax.annotation.processing.ProcessingEnvironment
import javax.inject.Inject
import javax.inject.Singleton
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element
import javax.tools.Diagnostic
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

internal interface Logger {
    fun note(element: Element? = null, annotationMirror: AnnotationMirror? = null, body: () -> String)
    fun warn(element: Element? = null, annotationMirror: AnnotationMirror? = null, body: () -> String)
    fun error(element: Element? = null, annotationMirror: AnnotationMirror? = null, body: () -> String)
}

internal val Throwable.stackTraceAsString get() = StringWriter().apply { printStackTrace(PrintWriter(this)) }.toString()

@Singleton
internal class Logging @Inject constructor(private val env: ProcessingEnvironment) {

    fun create(ref: Any): Logger = LoggerImpl(env, ref::class.qualifiedName.toString())

    operator fun invoke() =
        object : ReadOnlyProperty<Any, Logger> {
            override fun getValue(thisRef: Any, property: KProperty<*>): Logger = create(thisRef)
        }
}

private class LoggerImpl(private val env: ProcessingEnvironment, private val ref: String) : Logger {
    override fun note(element: Element?, annotationMirror: AnnotationMirror?, body: () -> String) =
        env.messager.printMessage(
            Diagnostic.Kind.NOTE,
            "$ref: ${body()}",
            element,
            annotationMirror
        )

    override fun warn(element: Element?, annotationMirror: AnnotationMirror?, body: () -> String) =
        env.messager.printMessage(
            Diagnostic.Kind.MANDATORY_WARNING,
            "$ref: ${body()}",
            element,
            annotationMirror
        )

    override fun error(element: Element?, annotationMirror: AnnotationMirror?, body: () -> String) =
        env.messager.printMessage(
            Diagnostic.Kind.ERROR,
            "$ref: ${body()}",
            element,
            annotationMirror
        )
}
