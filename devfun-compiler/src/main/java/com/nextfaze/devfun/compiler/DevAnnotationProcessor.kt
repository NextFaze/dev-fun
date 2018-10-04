package com.nextfaze.devfun.compiler

import com.google.auto.service.AutoService
import com.nextfaze.devfun.DeveloperAnnotation
import com.nextfaze.devfun.compiler.properties.InterfaceGenerator
import com.nextfaze.devfun.generated.DevFunGenerated
import java.io.IOException
import javax.annotation.processing.Filer
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.inject.Inject
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.StandardLocation.SOURCE_OUTPUT

/**
 * Flag to control generation of [DeveloperAnnotation] properties `AnnotationProperties` interfaces. _(default: `<true>`)_
 *
 * Useful for testing or if you only want to generate [DevFunGenerated] implementations.
 *
 * Set using APT options:
 * ```kotlin
 * android {
 *      defaultConfig {
 *          javaCompileOptions {
 *              annotationProcessorOptions {
 *                  argument("devfun.interfaces.generate", "false")
 *              }
 *          }
 *      }
 * }
 * ```
 *
 * @see GENERATE_DEFINITIONS
 * @see DevAnnotationProcessor
 */
const val GENERATE_INTERFACES = "devfun.interfaces.generate"

/**
 * Annotation processor for [DeveloperAnnotation] to generate properties interfaces.
 *
 * _Visible for testing purposes only! Use at your own risk._
 */
@SupportedOptions(
    GENERATE_INTERFACES,
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
        if (!options.generateInterfaces) return false // not generating interfaces - allow other Processors to use them if they really want
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
