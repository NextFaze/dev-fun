package com.nextfaze.devfun.compiler.processing

import com.nextfaze.devfun.annotations.DeveloperAnnotation
import com.nextfaze.devfun.compiler.DevAnnotationTypeElement
import com.nextfaze.devfun.compiler.Logging
import com.nextfaze.devfun.compiler.get
import com.nextfaze.devfun.compiler.getAnnotation
import javax.annotation.processing.RoundEnvironment
import javax.inject.Inject
import javax.inject.Singleton
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

@Singleton
internal class DeveloperAnnotationProcessor @Inject constructor(
    elements: Elements,
    private val functions: DeveloperFunctionHandler,
    private val categories: DeveloperCategoryHandler,
    private val references: DeveloperReferenceHandler,
    logging: Logging
) {
    private val log by logging()

    private val developerAnnotation = DevAnnotationTypeElement(elements.getTypeElement(DeveloperAnnotation::class.qualifiedName))

    val willGenerateSources get() = functions.willGenerateSource || categories.willGenerateSource || references.willGenerateSource

    fun generateSources(): String {
        val handlers = listOf(categories, functions, references)
        return handlers.filter { it.willGenerateSource }.sortedBy { it::class.java.simpleName }.joinToString("\n") { it.generateSource() }
    }

    fun process(elements: Set<TypeElement>, env: RoundEnvironment) {
        elements.forEach { devAnnotatedElement ->
            val devAnnotation = devAnnotatedElement.getAnnotation(developerAnnotation.element) ?: return@forEach

            val asFunction = devAnnotation[DeveloperAnnotation::developerFunction] ?: developerAnnotation.developerFunction
            val asCategory = devAnnotation[DeveloperAnnotation::developerCategory] ?: developerAnnotation.developerCategory
            val asReference = devAnnotation[DeveloperAnnotation::developerReference] ?: developerAnnotation.developerReference
            if (!asFunction && !asCategory && !asReference) {
                log.error(element = devAnnotatedElement) { "One or more behaviour flags must be true." }
                return@forEach
            }

            env.getElementsAnnotatedWith(devAnnotatedElement).forEach { annotatedElement ->
                if (asFunction) {
                    functions.processAnnotatedElement(devAnnotatedElement, annotatedElement)
                }
                if (asCategory) {
                    categories.processAnnotatedElement(devAnnotatedElement, annotatedElement)
                }
                if (asReference) {
                    references.processAnnotatedElement(devAnnotatedElement, annotatedElement)
                }
            }
        }
    }
}
