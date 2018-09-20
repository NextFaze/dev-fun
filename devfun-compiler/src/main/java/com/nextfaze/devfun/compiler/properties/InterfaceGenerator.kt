package com.nextfaze.devfun.compiler.properties

import com.nextfaze.devfun.compiler.Logging
import com.nextfaze.devfun.compiler.StringPreprocessor
import com.nextfaze.devfun.compiler.processing.KElements
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.TypeSpec
import java.util.ArrayDeque
import javax.inject.Inject
import javax.inject.Singleton
import javax.lang.model.element.ElementKind
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.ArrayType
import javax.lang.model.type.DeclaredType
import javax.lang.model.util.Elements

@Singleton
internal class InterfaceGenerator @Inject constructor(
    elements: Elements,
    kElements: KElements,
    preprocessor: StringPreprocessor,
    logging: Logging
) : PropertiesGenerator(elements, kElements, preprocessor, logging) {
    private val log by logging()

    private val interfaces = mutableMapOf<String, FileSpec>()
    private val queue = ArrayDeque<TypeElement>()

    private val uselessCastAnnotation by lazy {
        AnnotationSpec.builder(Suppress::class)
            .useSiteTarget(AnnotationSpec.UseSiteTarget.FILE)
            .addMember("%L", """"UNCHECKED_CAST", "USELESS_CAST"""")
            .build()
    }

    val fileSpecs get() = interfaces.values

    fun processTypeElement(annotation: TypeElement) {
        queue += annotation

        do {
            val element = queue.remove()
            val interfaceFqn = element.interfaceFqn
            if (!interfaces.containsKey(interfaceFqn)) {
                interfaces[interfaceFqn] = FileSpec
                    .builder(element.interfacePackage, element.interfaceFileName)
                    .addAnnotation(uselessCastAnnotation)
                    .addType(element.generateInterface())
                    .build()
            }
        } while (queue.isNotEmpty())
    }

    private fun TypeElement.generateInterface(excludeFields: List<String> = emptyList()): TypeSpec {
        val builder = TypeSpec.interfaceBuilder(interfaceName)
        log.note { "Generate interface for $this (interfaceName=$interfaceName, excludeFields=$excludeFields)" }

        enclosedElements.forEach { element ->
            if (element.simpleName.toString() in excludeFields) return@forEach
            element as ExecutableElement

            val property = element.toPropertySpec(valueInitType = ValueInitType.GETTER)
            builder.addProperty(property.build())

            // referenced types (not necessarily @DeveloperAnnotation)
            val returnType = element.returnType
            val returnTypeElement = when (returnType) {
                is DeclaredType -> returnType.asElement()
                is ArrayType -> (returnType.componentType as? DeclaredType)?.asElement()
                else -> null
            } as? TypeElement?
            if (returnTypeElement?.kind == ElementKind.ANNOTATION_TYPE) {
                queue += returnTypeElement
            }
        }

        return builder.build()
    }
}
