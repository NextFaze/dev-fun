package com.nextfaze.devfun.compiler.processing

import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.compiler.*
import com.nextfaze.devfun.compiler.properties.ImplementationGenerator
import com.nextfaze.devfun.core.FunctionDefinition
import com.nextfaze.devfun.core.FunctionTransformer
import com.nextfaze.devfun.core.ReferenceDefinition
import com.nextfaze.devfun.core.WithProperties
import com.nextfaze.devfun.generated.DevFunGenerated
import javax.annotation.processing.RoundEnvironment
import javax.inject.Inject
import javax.inject.Singleton
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.util.Elements
import kotlin.reflect.KClass

private const val RECEIVER_VAR_NAME = "receiver"
private const val ARGS_VAR_NAME = "args"

@Singleton
internal class DeveloperFunctionHandler @Inject constructor(
    override val elements: Elements,
    private val options: Options,
    private val preprocessor: StringPreprocessor,
    private val annotations: AnnotationElements,
    private val importsTracker: ImportsTracker,
    private val developerCategory: DeveloperCategoryHandler,
    private val implementationGenerator: ImplementationGenerator,
    logging: Logging
) : AnnotationProcessor {
    private val log by logging()

    private val useKotlinReflection get() = options.useKotlinReflection
    private val isDebugCommentsEnabled get() = options.isDebugCommentsEnabled

    private val functionInvokeName = "${FunctionDefinition::class.simpleName!!.replace("Definition", "")}Invoke"

    private val functionDefinitions = HashMap<String, String>()
    override val willGenerateSource: Boolean get() = functionDefinitions.isNotEmpty()

    override fun generateSource() =
        """    override val ${DevFunGenerated::functionDefinitions.name} = listOf<${FunctionDefinition::class.simpleName}>(
${functionDefinitions.values.sorted().joinToString(",").replaceIndentByMargin("        ", "#|")}
    )"""

    override fun processAnnotatedElement(annotatedElement: AnnotatedElement, env: RoundEnvironment) {
        if (annotatedElement.asFunction) {
            addDefinition(annotatedElement, env)
        }
    }

    private fun addDefinition(annotatedElement: AnnotatedElement, env: RoundEnvironment) {
        val element = annotatedElement.element
        if (element !is ExecutableElement) {
            log.error(element = element) {
                """Only executable elements are supported with DeveloperFunction (elementType=${element::class}).
                            |Please make an issue if you want something else (or feel free to make a PR)""".trimMargin()
            }
            return
        }

        val clazz = element.enclosingElement as TypeElement
        log.note { "Processing $clazz::$element..." }

        if (clazz.isInterface) {
            log.error(element) { "Due to kapt issue @${DeveloperFunction::class.simpleName} is not supported in interfaces yet." }
            // Specifically the problem is related to functions with default methods.
            // Kapt creates a static inner class "DefaultImpls" that it delegates to at run-time, however it also
            // copies the annotations. It does the same on the implementing class.
            // i.e. one annotation turns into three annotations.
            return
        }

        //
        // Using @JvmStatic in companion objects results in the function being copied to the main class object (with
        // "static" modifier), along with any annotations present.
        // This function is hidden from Kotlin code, which still calls/references the Companion object function.
        // At compile time, the Companion object function call is directed to the generated static function.
        //
        // However during the APT stage this results in being given *two* elements with the same details, resulting
        // in a duplicate function definition.
        // Since the Companion object calls the generated static function at run-time, it's easier to just ignore
        // the Companion object definition and only consider the static method when it's eventually processed (if it hasn't been already).
        //

        if (clazz.isCompanionObject) {
            val superClass = clazz.enclosingElement as TypeElement
            if (superClass.enclosedElements.count {
                    it.isStatic && it.modifiers.containsAll(element.modifiers) && it.simpleName == element.simpleName
                } == 1) {
                log.note { "Skipping companion @JvmStatic $element" }
                // This is a @JvmStatic method that is copied to the parent class during APT so just ignore this
                // one and use the copied one instead, which will (or already has been) processed.
                return
            }
        }

        // Annotation values
        val annotation = annotations.createDevFunAnnotation(annotatedElement.annotation, annotatedElement.annotationElement)

        // Name
        val name = annotation.value?.let {
            "\n#|    override val ${FunctionDefinition::name.name} = ${preprocessor.run(it.toKString(), element)}"
        } ?: ""

        // Category
        val category = annotation.category?.let {
            "\n#|    override val ${FunctionDefinition::category.name} by lazy { ${developerCategory.createCatDefSource(
                it,
                FunctionDefinition::clazz.name,
                clazz
            )} }"
        } ?: ""

        // Requires API
        val requiresApi = annotation.requiresApi?.let {
            "\n#|    override val ${FunctionDefinition::requiresApi.name} = $it"
        } ?: ""

        // Transformer
        val transformer = annotation.transformer?.let {
            "\n#|    override val ${FunctionDefinition::transformer.name} = ${it.asElement().toClass(
                castIfNotPublic = KClass::class,
                types = *arrayOf(FunctionTransformer::class)
            )}"
        } ?: ""

        // Can we call the function directly
        val funIsPublic = element.isPublic
        val classIsPublic = funIsPublic && clazz.isClassPublic
        val allArgTypesPublic = element.parameters.all { it.asType().isPublic }
        val callFunDirectly = classIsPublic && allArgTypesPublic && element.typeParameters.all { it.bounds.all { it.isClassPublic } }

        // If true the the function is top-level (file-level) declared (and thus we cant directly reference its enclosing class)
        val isClassKtFile = clazz.isClassKtFile

        // For simplicity, for now we always invoke extension functions via reflection
        val isExtensionFunction by lazy { element.parameters.firstOrNull()?.simpleName?.toString() == "\$receiver" }

        // Kotlin properties
        val isProperty = element.isProperty

        // Arguments
        val receiver = clazz.toInstance(RECEIVER_VAR_NAME, element.isStatic, isClassKtFile)
        val needReceiverArg = !callFunDirectly && !element.isStatic
        val args = run generateInvocationArgs@{
            val arguments = ArrayList<String>()
            if (needReceiverArg) {
                arguments += receiver
            } else if ((!callFunDirectly && element.isStatic) || isExtensionFunction) {
                arguments += "null"
            }

            element.parameters.forEachIndexed { index, arg ->
                arguments += arg.toInstance("$ARGS_VAR_NAME[$index]")
            }

            arguments.joiner(
                separator = ",\n#|            ",
                prefix = "\n#|            ",
                postfix = "\n#|        "
            )
        }

        // Method reference
        val methodRef = run getMethodReference@{
            val funName = element.simpleName.escapeDollar()
            val setAccessible = if (!classIsPublic || !element.isPublic) ".apply { isAccessible = true }" else ""
            val methodArgTypes = element.parameters.joiner(prefix = ", ") { it.toClass(false) }
            when {
                useKotlinReflection -> """${clazz.toClass()}.declaredFunctions.filter { it.name == "${element.simpleName.stripInternal()}" && it.parameters.size == ${element.parameters.size + 1} }.single().javaMethod!!$setAccessible"""
                else -> """${clazz.toClass(false, isClassKtFile)}.getDeclaredMethod("$funName"$methodArgTypes)$setAccessible"""
            }
        }

        // Call invocation
        val invocation = run generateInvocation@{
            when {
                isProperty -> "throw UnsupportedOperationException(\"Direct invocation of an annotated property is not supported. This invocation should have been handled by the PropertyTransformer.\")"
                callFunDirectly && !isExtensionFunction -> {
                    val typeParams = if (element.typeParameters.isNotEmpty()) {
                        element.typeParameters.map { it.asType().toType() }.joiner(prefix = "<", postfix = ">")
                    } else {
                        ""
                    }
                    "$receiver.${element.simpleName.stripInternal()}$typeParams($args)"
                }
                else -> "${FunctionDefinition::method.name}.invoke($args)"
            }
        }

        val receiverVar = if (!element.isStatic && !clazz.isKObject) RECEIVER_VAR_NAME else "_"
        val argsVar = if (element.parameters.isNotEmpty()) ARGS_VAR_NAME else "_"
        val invocationArgs = "$receiverVar, $argsVar"

        // Debug info
        val functionDefinition = "${element.enclosingElement}::$element"
        var debugAnnotationInfo = ""
        if (isDebugCommentsEnabled) {
            debugAnnotationInfo = "\n#|// $functionDefinition"
        }

        var debugElementInfo = ""
        if (isDebugCommentsEnabled) {
            debugElementInfo = """
                #|        // element modifiers: ${element.modifiers.joinToString()}
                #|        // enclosing element modifiers: ${element.enclosingElement.modifiers.joinToString()}
                #|        // enclosing element metadata: isClassKtFile=$isClassKtFile
                #|        // enclosing element as type element: ${(element.enclosingElement.asType() as? DeclaredType)?.asElement() as? TypeElement}
                #|        // param type modifiers: ${element.parameters.joiner {
                "${it.simpleName}=${(it.asType() as? DeclaredType)?.asElement()?.modifiers?.joinToString(",")}"
            }}
                #|        // params: ${element.parameters.joiner {
                "${it.simpleName}=@[${it.annotationMirrors}] isTypePublic=${it.asType().isPublic} ${it.asType()}"
            }}
                #|        // classIsPublic=$classIsPublic, funIsPublic=$funIsPublic, allArgTypesPublic=$allArgTypesPublic, callFunDirectly=$callFunDirectly, needReceiverArg=$needReceiverArg, isExtensionFunction=$isExtensionFunction, isProperty=$isProperty"""
        }

        // ReferenceDefinition
        importsTracker += ReferenceDefinition::class
        var implements = ", ${ReferenceDefinition::class.java.simpleName}"

        // The meta annotation class (e.g. Dagger2Component)
        val annotationElement = annotatedElement.annotationElement
        val annotationClass = annotationElement.toClass(castIfNotPublic = KClass::class, types = *arrayOf(Annotation::class))
        var overrides = "\n#|    override val ${ReferenceDefinition::annotation.name}: KClass<out Annotation> = $annotationClass"

        // Generate any custom properties
        val propertiesImpl = implementationGenerator.processAnnotatedElement(annotatedElement, env)
        if (propertiesImpl != null) {
            importsTracker += WithProperties::class
            implements += ", ${WithProperties::class.java.simpleName}<Any>"
            overrides += "\n#|    override val ${WithProperties<*>::properties.name}: Any = $propertiesImpl"
        }

        // Generate definition
        if (functionDefinitions.containsKey(functionDefinition)) {
            log.error(element) { "Only one function definition supported per function." }
        }

        functionDefinitions[functionDefinition] =
                """$debugAnnotationInfo
                     #|object : AbstractFunctionDefinition()$implements {
                     #|    override val ${FunctionDefinition::method.name} by lazy { $methodRef }$name$category$requiresApi$transformer$overrides
                     #|    override val ${FunctionDefinition::invoke.name}: $functionInvokeName = { $invocationArgs -> $debugElementInfo
                     #|        $invocation
                     #|    }
                     #|}"""
    }

    private fun Element.toInstance(from: String, forStaticUse: Boolean = false, isClassKtFile: Boolean = false): String = when {
        this is TypeElement && (forStaticUse || isKObject) -> when {
            isClassPublic -> {
                when {
                    isClassKtFile -> enclosingElement.toString() // top-level function so just the package
                    else -> toString()
                }
            }
            else -> "${toClass()}.privateObjectInstance"
        }
        else -> "($from${asType().toCast()})"
    }
}
