package com.nextfaze.devfun.compiler.processing

import com.nextfaze.devfun.annotations.DeveloperAnnotation
import com.nextfaze.devfun.compiler.*
import javax.annotation.processing.RoundEnvironment
import javax.inject.Inject
import javax.inject.Singleton
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

@Singleton
internal class DeveloperAnnotationProcessor @Inject constructor(
    override val elements: Elements,
    override val preprocessor: StringPreprocessor,
    private val handlers: Set<@JvmSuppressWildcards AnnotationProcessor>,
    private val options: Options,
    logging: Logging
) : Processor {
    private val log by logging()

    private val developerAnnotation = DevAnnotationTypeElement(elements.getTypeElement(DeveloperAnnotation::class.qualifiedName))

    val willGenerateSources get() = handlers.any { it.willGenerateSource }

    fun generateSources() =
        handlers.filter { it.willGenerateSource }.sortedBy { it::class.java.simpleName }.joinToString("\n") { it.generateSource() }

    fun process(elements: Set<TypeElement>, env: RoundEnvironment) =
        elements.forEach { devAnnotatedElement ->
            val devAnnotation = devAnnotatedElement.getAnnotation(developerAnnotation.element) ?: return@forEach

            val asFunction = devAnnotation[DeveloperAnnotation::developerFunction] ?: developerAnnotation.developerFunction
            val asCategory = devAnnotation[DeveloperAnnotation::developerCategory] ?: developerAnnotation.developerCategory
            val asReference = devAnnotation[DeveloperAnnotation::developerReference] ?: developerAnnotation.developerReference

            env.getElementsAnnotatedWith(devAnnotatedElement).forEach { element ->
                val annotatedElement by lazy { AnnotatedElement(element, devAnnotatedElement, asFunction, asCategory, asReference) }
                if (options.shouldProcessElement(element)) {
                    handlers.forEach {
                        it.processAnnotatedElement(annotatedElement, env)
                    }
                } else {
                    log.note { "Element $element skipped due to name filter." }
                }
            }
        }
}
