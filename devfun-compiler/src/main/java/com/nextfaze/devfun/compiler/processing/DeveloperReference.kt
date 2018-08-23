package com.nextfaze.devfun.compiler.processing

import com.nextfaze.devfun.compiler.*
import com.nextfaze.devfun.compiler.properties.ImplementationGenerator
import com.nextfaze.devfun.core.*
import com.nextfaze.devfun.generated.DevFunGenerated
import java.lang.reflect.Field
import javax.annotation.processing.RoundEnvironment
import javax.inject.Inject
import javax.inject.Singleton
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.util.Elements
import kotlin.reflect.KClass

@Singleton
internal class DeveloperReferenceHandler @Inject constructor(
    override val elements: Elements,
    private val options: Options,
    private val importsTracker: ImportsTracker,
    private val implementations: ImplementationGenerator,
    logging: Logging
) : AnnotationProcessor {
    private val log by logging()

    private val useKotlinReflection get() = options.useKotlinReflection
    private val isDebugCommentsEnabled get() = options.isDebugCommentsEnabled

    private val developerReferences = HashMap<String, String>()
    override val willGenerateSource: Boolean get() = developerReferences.isNotEmpty()

    override fun processAnnotatedElement(annotatedElement: AnnotatedElement, env: RoundEnvironment) {
        if (!annotatedElement.asReference) return

        val element = annotatedElement.element
        when (element) {
            is ExecutableElement -> generateDeveloperExecutableReference(annotatedElement, env)
            is TypeElement -> generateDeveloperTypeReference(annotatedElement, env)
            is VariableElement -> generateDeveloperFieldReference(annotatedElement, env)
            else -> log.error(element = element) {
                """Only executable, type, and variable elements are supported at the moment (elementType=${element::class}).
                                            |Please make an issue if you want something else (or feel free to make a PR)""".trimMargin()
            }
        }
    }

    override fun generateSource() =
        """    override val ${DevFunGenerated::developerReferences.name} = listOf<${ReferenceDefinition::class.simpleName}>(
${developerReferences.values.sorted().joinToString(",").replaceIndentByMargin("        ", "#|")}
    )"""

    private fun generateDeveloperFieldReference(annotatedElement: AnnotatedElement, env: RoundEnvironment) {
        importsTracker += FieldReference::class
        importsTracker += Field::class
        importsTracker += WithProperties::class

        val element = annotatedElement.element as VariableElement
        val annotationElement = annotatedElement.annotationElement

        val clazz = element.enclosingElement as TypeElement
        log.note { "Processing $clazz::$element for $annotationElement..." }

        // The meta annotation class (e.g. Dagger2Component)
        val annotationClass = annotationElement.toClass(castIfNotPublic = KClass::class, types = *arrayOf(Annotation::class))

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

        // Generate any custom properties
        val properties = implementations.processAnnotatedElement(annotatedElement, env)

        val developerAnnotation = "${element.enclosingElement}::$element"
        var debugAnnotationInfo = ""
        if (isDebugCommentsEnabled) {
            debugAnnotationInfo = "\n#|// $developerAnnotation"
        }

        developerReferences[developerAnnotation] =
                """$debugAnnotationInfo
                        #|object : ${FieldReference::class.simpleName}, ${WithProperties::class.simpleName}<Any> {
                        #|    override val ${ReferenceDefinition::annotation.name}: KClass<out Annotation> = $annotationClass
                        #|    override val ${WithProperties<*>::properties.name}: Any = $properties
                        #|    override val ${FieldReference::field.name}: Field by lazy { $field }
                        #|}"""
    }

    private fun generateDeveloperTypeReference(annotatedElement: AnnotatedElement, env: RoundEnvironment) {
        importsTracker += TypeReference::class
        importsTracker += WithProperties::class

        val element = annotatedElement.element as TypeElement
        val annotationElement = annotatedElement.annotationElement

        log.note { "Processing $element for $annotationElement..." }

        // The meta annotation class (e.g. Dagger2Component)
        val annotationClass = annotationElement.toClass(castIfNotPublic = KClass::class, types = *arrayOf(Annotation::class))

        // Generate any custom properties
        val properties = implementations.processAnnotatedElement(annotatedElement, env)

        val developerAnnotation = "${element.enclosingElement}::$element"
        var debugAnnotationInfo = ""
        if (isDebugCommentsEnabled) {
            debugAnnotationInfo = "\n#|// $developerAnnotation"
        }

        developerReferences[developerAnnotation] =
                """$debugAnnotationInfo
                        #|object : ${TypeReference::class.simpleName}, ${WithProperties::class.simpleName}<Any> {
                        #|    override val ${ReferenceDefinition::annotation.name}: KClass<out Annotation> = $annotationClass
                        #|    override val ${WithProperties<*>::properties.name}: Any = $properties
                        #|    override val ${TypeReference::type.name}: KClass<*> = ${element.toClass()}
                        #|}"""
    }

    private fun generateDeveloperExecutableReference(annotatedElement: AnnotatedElement, env: RoundEnvironment) {
        importsTracker += MethodReference::class
        importsTracker += WithProperties::class

        val element = annotatedElement.element as ExecutableElement
        val annotationElement = annotatedElement.annotationElement

        val clazz = element.enclosingElement as TypeElement
        log.note { "Processing $clazz::$element for $annotationElement..." }

        // The meta annotation class (e.g. Dagger2Component)
        val annotationClass = annotationElement.toClass(castIfNotPublic = KClass::class, types = *arrayOf(Annotation::class))

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

        // Generate any custom properties
        val properties = implementations.processAnnotatedElement(annotatedElement, env)

        val developerAnnotation = "${element.enclosingElement}::$element"
        var debugAnnotationInfo = ""
        if (isDebugCommentsEnabled) {
            debugAnnotationInfo = "\n#|// $developerAnnotation"
        }

        developerReferences[developerAnnotation] =
                """$debugAnnotationInfo
                        #|object : ${MethodReference::class.simpleName}, ${WithProperties::class.simpleName}<Any> {
                        #|    override val ${ReferenceDefinition::annotation.name}: KClass<out Annotation> = $annotationClass
                        #|    override val ${WithProperties<*>::properties.name}: Any = $properties
                        #|    override val ${MethodReference::method.name}: Method by lazy { $method }
                        #|}"""
    }
}
