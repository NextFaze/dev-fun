package com.nextfaze.devfun.compiler

import com.google.auto.service.AutoService
import com.nextfaze.devfun.annotations.DeveloperAnnotation
import com.nextfaze.devfun.compiler.properties.InterfaceGenerator
import java.io.IOException
import javax.annotation.processing.Filer
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.inject.Inject
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.StandardLocation.SOURCE_OUTPUT

const val GENERATE_INTERFACES = "devfun.interfaces.generate"
const val GENERATE_INTERFACES_NON_SOURCE = "devfun.interfaces.generate.external" // TODO
const val GENERATE_INTERFACES_DESTINATION_DIR = "devfun.interfaces.destinationDir" // TODO

/**
 * Annotation processor for [DeveloperAnnotation] to generate properties interfaces.
 *
 * _Visible for testing purposes only! Use at your own risk._
 */
@SupportedOptions(
    GENERATE_INTERFACES,
    GENERATE_INTERFACES_NON_SOURCE,
    GENERATE_INTERFACES_DESTINATION_DIR,
    ELEMENTS_FILTER_INCLUDE,
    ELEMENTS_FILTER_EXCLUDE
)
@AutoService(Processor::class)
class DevAnnotationProcessor : DaggerProcessor() {
    override fun getSupportedAnnotationTypes() = setOf(DeveloperAnnotation::class.qualifiedName!!)
    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()

    override fun inject(injector: Injector) = injector.inject(this)

    @Inject internal lateinit var options: Options
    @Inject internal lateinit var logging: Logging
    @Inject internal lateinit var filer: Filer
    @Inject internal lateinit var interfaceGenerator: InterfaceGenerator

    private val log by lazy { logging.create(this) }

    override fun process(annotations: Set<TypeElement>, env: RoundEnvironment): Boolean {
        if (!options.generateInterfaces) return true
        if (env.errorRaised()) return true

        try {
            if (env.processingOver()) {
                interfaceGenerator.fileSpecs.forEach { fileSpec ->
                    try {
                        filer.createResource(SOURCE_OUTPUT, fileSpec.packageName, fileSpec.name).openWriter().use {
                            it.write(fileSpec.toString())
                        }
                    } catch (e: IOException) {
                        log.error { "Failed to write source file:\n${e.stackTraceAsString}" }
                    }
                }
            } else {
                annotations.forEach { annotation ->
                    env.getElementsAnnotatedWith(annotation)
                        .filter {
                            options.shouldProcessElement(it).also { willProcess ->
                                if (!willProcess) {
                                    log.warn { "Element $it skipped due to filter." }
                                }
                            }
                        }
                        .forEach {
                            interfaceGenerator.processTypeElement(it as TypeElement)
                        }
                }
            }
        } catch (t: Throwable) {
            log.error { "Unexpected error: ${t.stackTraceAsString}" }
        }

        return true
    }
}
