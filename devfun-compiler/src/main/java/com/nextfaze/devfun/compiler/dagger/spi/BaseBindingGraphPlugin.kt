package com.nextfaze.devfun.compiler.dagger.spi

import com.nextfaze.devfun.compiler.CompileContext
import com.nextfaze.devfun.compiler.Logging
import com.nextfaze.devfun.compiler.Options
import dagger.model.BindingGraph
import dagger.spi.BindingGraphPlugin
import dagger.spi.DiagnosticReporter
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.AnnotationValue
import javax.lang.model.element.Element
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic.Kind

@Suppress("unused")
internal abstract class BaseBindingGraphPlugin(private val enabled: Boolean = true) : BindingGraphPlugin {
    private lateinit var lateInitFiler: Filer
    private lateinit var lateInitTypes: Types
    private lateinit var lateInitElements: Elements
    private var lateInitOptions: Map<String, String> = emptyMap()

    protected val filer get() = this.lateInitFiler
    protected val types get() = this.lateInitTypes
    protected val elements get() = this.lateInitElements
    protected val options by lazy { Options(elements, lateInitOptions) }
    protected val logging by lazy { Logging(ConsoleMessager, options) }
    protected val ctx by lazy { CompileContext(options, filer, logging) }

    override fun initFiler(filer: Filer) {
        lateInitFiler = filer
    }

    override fun initTypes(types: Types) {
        lateInitTypes = types
    }

    override fun initElements(elements: Elements) {
        lateInitElements = elements
    }

    override fun initOptions(options: Map<String, String>) {
        lateInitOptions = options
    }

    final override fun visitGraph(bindingGraph: BindingGraph, diagnosticReporter: DiagnosticReporter) {
        if (enabled) {
            visitBindingGraph(bindingGraph, diagnosticReporter)
        }
    }

    abstract fun visitBindingGraph(bindingGraph: BindingGraph, diagnosticReporter: DiagnosticReporter)
}

private object ConsoleMessager : Messager {
    override fun printMessage(kind: Kind, msg: CharSequence) = println("$kind: $msg")
    override fun printMessage(kind: Kind, msg: CharSequence, element: Element?) = println("$kind: $msg @ $element")

    override fun printMessage(kind: Kind, msg: CharSequence, element: Element?, mirror: AnnotationMirror?) =
        println("$kind: $msg @ $element, $mirror")

    override fun printMessage(kind: Kind, msg: CharSequence, element: Element?, mirror: AnnotationMirror?, value: AnnotationValue?) =
        println("$kind: $msg @ $element, $mirror, $value")
}
