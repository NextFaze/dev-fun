package com.nextfaze.devfun.compiler.properties

import com.nextfaze.devfun.annotations.DeveloperAnnotation
import com.nextfaze.devfun.compiler.Logging
import com.nextfaze.devfun.compiler.Options
import com.nextfaze.devfun.compiler.StringPreprocessor
import com.nextfaze.devfun.compiler.processing.AnnotatedElement
import com.squareup.kotlinpoet.TypeSpec
import javax.annotation.processing.RoundEnvironment
import javax.inject.Inject
import javax.inject.Singleton
import javax.lang.model.util.Elements

@Singleton
internal class ImplementationGenerator @Inject constructor(
    elements: Elements,
    preprocessor: StringPreprocessor,
    private val options: Options,
    logging: Logging
) : PropertiesGenerator(elements, preprocessor, logging) {
    private val log by logging()

    fun processAnnotatedElement(annotatedElement: AnnotatedElement, env: RoundEnvironment): TypeSpec? {
        if (!options.generateInterfaces) return null

        val interfaceFqn = annotatedElement.annotationElement.interfaceFqn
        log.note { "Get interface type element $interfaceFqn: ${elements.getTypeElement(interfaceFqn)}" }
        if (elements.getTypeElement(interfaceFqn) == null) {
            log.note { "Interface type element $interfaceFqn not found..." }
            log.note { "DeveloperAnnotation elements:\n${env.getElementsAnnotatedWith(DeveloperAnnotation::class.java).joinToString(",\n")}" }
            if (env.getElementsAnnotatedWith(DeveloperAnnotation::class.java).none { it == annotatedElement.annotationElement }) {
                log.warn(annotatedElement.element) { "Interface type $interfaceFqn not found - not generating implementation for $annotatedElement" }
                return null
            }
        }

        return generateImplementation(annotatedElement.annotation)
    }
}
