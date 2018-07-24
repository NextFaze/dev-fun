package com.nextfaze.devfun.compiler.handlers

import com.nextfaze.devfun.annotations.DeveloperAnnotation
import com.nextfaze.devfun.compiler.*
import com.nextfaze.devfun.core.DeveloperFieldReference
import com.nextfaze.devfun.core.DeveloperMethodReference
import com.nextfaze.devfun.core.DeveloperReference
import com.nextfaze.devfun.core.DeveloperTypeReference
import com.nextfaze.devfun.generated.DevFunGenerated
import java.lang.reflect.Field
import javax.annotation.processing.RoundEnvironment
import javax.inject.Inject
import javax.inject.Singleton
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.util.Elements
import kotlin.reflect.KClass

@Singleton
internal class DeveloperAnnotationHandler @Inject constructor(
    override val elements: Elements,
    private val options: Options,
    private val annotations: AnnotationElements,
    private val importsTracker: ImportsTracker,
    private val developerFunctions: DeveloperFunctionHandler,
    private val annotationSerializer: AnnotationSerializer,
    logging: Logging
) : AnnotationHandler {
    private val log by logging()

    private val useKotlinReflection get() = options.useKotlinReflection
    private val isDebugCommentsEnabled get() = options.isDebugCommentsEnabled

    private val developerReferences = HashMap<String, String>()
    override val willGenerateSource: Boolean get() = developerReferences.isNotEmpty()

    override fun process(elements: Set<TypeElement>, env: RoundEnvironment) {
        elements.filter { it.isDevAnnotated }.forEach { devAnnotatedElement ->
            val devAnnotation = annotations.createDevAnnotation(devAnnotatedElement.devAnnotation, devAnnotatedElement)
            val handleAsDeveloperFunction = devAnnotation.developerFunction ?: annotations.devAnnElement.developerFunction
            val liftDefaults = true
            env.getElementsAnnotatedWith(devAnnotatedElement).forEach {
                if (handleAsDeveloperFunction) {
                    if (it is ExecutableElement) {
                        val annotation =
                            it.annotationMirrors.first { it.annotationType.toString() == devAnnotatedElement.qualifiedName.toString() }
                        developerFunctions.generateFunctionDefinition(
                            annotations.createDevFunAnnotation(annotation, devAnnotatedElement),
                            it,
                            devAnnotatedElement
                        )
                    } else {
                        log.error(element = it) {
                            """Only executable elements are supported with developerFunction=true (elementType=${it::class}).
                            |Please make an issue if you want something else (or feel free to make a PR)""".trimMargin()
                        }
                    }
                } else {
                    when (it) {
                        is ExecutableElement -> generateDeveloperExecutableReference(devAnnotatedElement, it, liftDefaults)
                        is TypeElement -> generateDeveloperTypeReference(devAnnotatedElement, it, liftDefaults)
                        is VariableElement -> generateDeveloperFieldReference(devAnnotatedElement, it, liftDefaults)
                        else -> log.error(element = it) {
                            """Only executable, type, and variable elements are supported at the moment (elementType=${it::class}).
                                            |Please make an issue if you want something else (or feel free to make a PR)""".trimMargin()
                        }
                    }
                }
            }
        }
    }

    override fun generateSource() =
        """    override val ${DevFunGenerated::developerReferences.name} = listOf<${DeveloperReference::class.simpleName}>(
${developerReferences.values.sorted().joinToString(",").replaceIndentByMargin("        ", "#|")}
    )"""

    private fun generateDeveloperFieldReference(annotation: TypeElement, element: VariableElement, liftDefaults: Boolean) {
        importsTracker += DeveloperFieldReference::class
        importsTracker += Field::class

        val clazz = element.enclosingElement as TypeElement
        log.note { "Processing $clazz::$element for $annotation..." }

        // The meta annotation class (e.g. Dagger2Component)
        val annotationClass = annotation.toClass(castIfNotPublic = KClass::class, types = *arrayOf(Annotation::class))

        // Can we reference the field directly
        val fieldIsPublic = element.isPublic
        val classIsPublic = fieldIsPublic && clazz.isClassPublic

        // If true the the field is top-level (file-level) declared (and thus we cant directly reference its enclosing class)
        val isClassKtFile = clazz.isClassKtFile

        // Generate field reference
        val field = run {
            val fieldName = element.simpleName.escapeDollar()
            val setAccessible = if (!classIsPublic || !element.isPublic) ".apply { isAccessible = true }" else ""
            """${clazz.toClass(false, isClassKtFile)}.getDeclaredField("$fieldName")$setAccessible"""
        }

        // Generate any data
        val data = annotationSerializer.findAndSerialize(
            annotatedElement = element,
            annotationTypeElement = annotation,
            liftDefaults = liftDefaults
        )

        val developerAnnotation = "${element.enclosingElement}::$element"
        var debugAnnotationInfo = ""
        if (isDebugCommentsEnabled) {
            debugAnnotationInfo = "\n#|// $developerAnnotation"
        }

        developerReferences[developerAnnotation] =
                """$debugAnnotationInfo
                        #|object : ${DeveloperFieldReference::class.simpleName} {
                        #|    override val ${DeveloperReference::annotation.name}: KClass<out Annotation> = $annotationClass
                        #|    override val ${DeveloperReference::properties.name}: Map<String, *>? = $data
                        #|    override val ${DeveloperFieldReference::field.name}: Field by lazy { $field }
                        #|}"""
    }

    private fun generateDeveloperTypeReference(annotation: TypeElement, element: TypeElement, liftDefaults: Boolean) {
        importsTracker += DeveloperTypeReference::class

        log.note { "Processing $element for $annotation..." }

        // The meta annotation class (e.g. Dagger2Component)
        val annotationClass = annotation.toClass(castIfNotPublic = KClass::class, types = *arrayOf(Annotation::class))

        // Generate any data
        val data = annotationSerializer.findAndSerialize(
            annotatedElement = element,
            annotationTypeElement = annotation,
            liftDefaults = liftDefaults
        )

        val developerAnnotation = "${element.enclosingElement}::$element"
        var debugAnnotationInfo = ""
        if (isDebugCommentsEnabled) {
            debugAnnotationInfo = "\n#|// $developerAnnotation"
        }

        developerReferences[developerAnnotation] =
                """$debugAnnotationInfo
                        #|object : ${DeveloperTypeReference::class.simpleName} {
                        #|    override val ${DeveloperReference::annotation.name}: KClass<out Annotation> = $annotationClass
                        #|    override val ${DeveloperReference::properties.name}: Map<String, *>? = $data
                        #|    override val ${DeveloperTypeReference::type.name}: KClass<*> = ${element.toClass()}
                        #|}"""
    }

    private fun generateDeveloperExecutableReference(annotation: TypeElement, element: ExecutableElement, liftDefaults: Boolean) {
        importsTracker += DeveloperMethodReference::class

        val clazz = element.enclosingElement as TypeElement
        log.note { "Processing $clazz::$element for $annotation..." }

        // The meta annotation class (e.g. Dagger2Component)
        val annotationClass = annotation.toClass(castIfNotPublic = KClass::class, types = *arrayOf(Annotation::class))

        // Can we call the function directly
        val funIsPublic = element.isPublic
        val classIsPublic = funIsPublic && clazz.isClassPublic

        // If true the the function is top-level (file-level) declared (and thus we cant directly reference its enclosing class)
        val isClassKtFile = clazz.isClassKtFile

        // Generate method reference
        val method = run {
            val funName = element.simpleName.escapeDollar()
            val setAccessible = if (!classIsPublic || !element.isPublic) ".apply { isAccessible = true }" else ""
            val methodArgTypes = element.parameters.joiner(prefix = ", ") { it.toClass(false) }
            when {
                useKotlinReflection -> """${clazz.toClass()}.declaredFunctions.filter { it.name == "${element.simpleName.stripInternal()}" && it.parameters.size == ${element.parameters.size + 1} }.single().javaMethod!!$setAccessible"""
                else -> """${clazz.toClass(false, isClassKtFile)}.getDeclaredMethod("$funName"$methodArgTypes)$setAccessible"""
            }
        }

        // Generate any data
        val data = annotationSerializer.findAndSerialize(
            annotatedElement = element,
            annotationTypeElement = annotation,
            liftDefaults = liftDefaults
        )

        val developerAnnotation = "${element.enclosingElement}::$element"
        var debugAnnotationInfo = ""
        if (isDebugCommentsEnabled) {
            debugAnnotationInfo = "\n#|// $developerAnnotation"
        }

        developerReferences[developerAnnotation] =
                """$debugAnnotationInfo
                        #|object : ${DeveloperMethodReference::class.simpleName} {
                        #|    override val ${DeveloperReference::annotation.name}: KClass<out Annotation> = $annotationClass
                        #|    override val ${DeveloperReference::properties.name}: Map<String, *>? = $data
                        #|    override val ${DeveloperMethodReference::method.name}: Method by lazy { $method }
                        #|}"""
    }

    private val TypeElement.isDevAnnotated get() = getAnnotation(DeveloperAnnotation::class.java) != null
    private val Element.devAnnotation get() = annotationMirrors.first { it.annotationType.toString() == DeveloperAnnotation::class.qualifiedName }
}
