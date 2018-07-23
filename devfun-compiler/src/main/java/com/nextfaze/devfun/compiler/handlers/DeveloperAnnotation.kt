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
import javax.lang.model.element.*
import javax.lang.model.type.ArrayType
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.PrimitiveType
import javax.lang.model.type.TypeKind.*
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import kotlin.reflect.KClass

@Singleton
internal class DeveloperAnnotationHandler @Inject constructor(
    override val elements: Elements,
    private val options: Options,
    private val annotations: AnnotationElements,
    private val importsTracker: ImportsTracker,
    private val developerFunctions: DeveloperFunctionHandler,
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
                            it
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
        val annotationOnElement = element.annotationMirrors.first { it.annotationType.toString() == annotation.qualifiedName.toString() }
        val data = annotationOnElement.toMap(liftDefaults)

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
        val annotationOnElement = element.annotationMirrors.first { it.annotationType.toString() == annotation.qualifiedName.toString() }
        val data = annotationOnElement.toMap(liftDefaults)

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
        val annotationOnElement = element.annotationMirrors.first { it.annotationType.toString() == annotation.qualifiedName.toString() }
        log.note { "element=$element, annotationOnElement=$annotationOnElement" }
        val data = annotationOnElement.toMap(liftDefaults)

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

    // todo this is a bit gross, but might be moving to Kotlin Poet so meh for now
    private fun AnnotationMirror.toMap(liftDefaults: Boolean): String? {
//        val liftDefaults = annotationType.asElement().getAnnotation(LiftDefaults::class.java) != null
        val annotationElements =
            if (liftDefaults) {
                annotationType.asElement().enclosedElements.associate { it as ExecutableElement to elementValues[it] }
            } else {
                elementValues
            }

        val entries = annotationElements.mapNotNull { (element: ExecutableElement, annotationValue) ->
            val value = annotationValue?.value ?: element.defaultValue?.value ?: return@mapNotNull null
            val valueType: TypeMirror = element.returnType
            val mapValue = when (valueType) {
                is PrimitiveType ->
                    when (valueType.kind) {
                        BOOLEAN -> value.toString()
                        BYTE -> "$value"
                        SHORT -> value.toString()
                        INT -> value.toString()
                        LONG -> "${value}L"
                        CHAR -> "'$value'"
                        FLOAT -> "${value}f"
                        DOUBLE -> value.toString()
                        else -> throw RuntimeException("Unexpected PrimitiveType.kind: ${valueType.kind}")
                    }
                is DeclaredType -> {
                    val elementKind by lazy { valueType.asElement().kind }
                    when {
                        value is String -> value.toKString()
                        elementKind == ElementKind.CLASS ->
                            when (value) {
                                is PrimitiveType -> "${value.toKType().typeName}::class"
                                is DeclaredType -> value.asElement().toClass()
                                is ArrayType -> value.toClass()
                                else -> throw NotImplementedError("Not implemented elementKind: $elementKind for $value (${value::class})")
                            }
                        elementKind == ElementKind.ENUM -> {
                            if (valueType.isClassPublic) {
                                "${valueType.toType()}.$value"
                            } else {
                                "java.lang.Enum.valueOf(${valueType.toClass(kotlinClass = false)} as Class<Nothing>, \"$value\") as Any"
                            }
                        }
                        elementKind == ElementKind.ANNOTATION_TYPE -> (value as AnnotationMirror).toMap(liftDefaults)
                        else -> throw NotImplementedError("Not implemented elementKind: $elementKind for $value (${value::class})")
                    }
                }
                is ArrayType -> {
                    value as List<*>
                    val componentType = valueType.componentType
                    when (componentType) {
                        is PrimitiveType ->
                            when (componentType.kind) {
                                BOOLEAN -> "arrayOf<Boolean>($value)"
                                BYTE -> "arrayOf<Byte>(${value.toString().replace("(byte)", "")})"
                                SHORT -> "arrayOf<Short>($value)"
                                INT -> "arrayOf<Int>($value)"
                                LONG -> "arrayOf<Long>($value)"
                                CHAR -> "arrayOf<Char>($value)"
                                FLOAT -> "arrayOf<Float>($value)"
                                DOUBLE -> "arrayOf<Double>($value)"
                                else -> throw RuntimeException("Unexpected array component type PrimitiveType.kind: ${componentType.kind}")
                            }
                        is DeclaredType ->
                            when {
                                componentType.isString -> "arrayOf<String>(${value.joinToString { it.toString().toKString() }})"
                                componentType.isClass -> "arrayOf<KClass<*>>(${value.joinToString {
                                    val arrValue = (it as AnnotationValue).value
                                    when (arrValue) {
                                        is PrimitiveType -> "${arrValue.toKType().typeName}::class"
                                        is DeclaredType -> arrValue.asElement().toClass()
                                        is ArrayType -> arrValue.toClass()
                                        else -> throw NotImplementedError("Not implemented arrValue: $arrValue (${arrValue::class}) for $value (${value::class})")
                                    }
                                }})"
                                componentType.asElement().kind == ElementKind.ENUM ->
                                    "arrayOf<Enum<*>>(${value.joinToString {
                                        val arrAnnotationValue = (it as AnnotationValue).value
                                        val arrValue = (arrAnnotationValue as VariableElement).asType()
                                        if (arrValue.isClassPublic) {
                                            "$it"
                                        } else {
                                            "java.lang.Enum.valueOf(${arrValue.toClass(kotlinClass = false)} as Class<Nothing>, \"$arrAnnotationValue\") as Enum<*>"
                                        }
                                    }})"
                                componentType.asElement().kind == ElementKind.ANNOTATION_TYPE -> {
                                    val entries = value.mapNotNull { (it as AnnotationMirror).toMap(liftDefaults) }
                                    "arrayOf<Map<String, *>>(${entries.joinToString()})"
                                }
                                else -> throw NotImplementedError("Not implemented array componentType: $componentType for $value (${value::class})")
                            }
                        else -> throw NotImplementedError("Not implemented array componentType: $componentType for $value (${value::class})")
                    }
                }
                else -> throw NotImplementedError("Not implemented type: $valueType (${valueType::class})")
            }

            "\"${element.simpleName}\" to $mapValue"
        }

        return if (entries.isEmpty()) "null" else "mapOf(${entries.joinToString()})"
    }

    private val TypeElement.isDevAnnotated get() = getAnnotation(DeveloperAnnotation::class.java) != null
    private val Element.devAnnotation get() = annotationMirrors.first { it.annotationType.toString() == DeveloperAnnotation::class.qualifiedName }

    private val TypeMirror.isString get() = toString() == "java.lang.String"
    private val TypeMirror.isClass get() = toString().startsWith("java.lang.Class")
}
