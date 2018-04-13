package com.nextfaze.devfun.compiler

import com.google.auto.service.AutoService
import com.nextfaze.devfun.annotations.DeveloperAnnotation
import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.core.CategoryDefinition
import com.nextfaze.devfun.core.DeveloperReference
import com.nextfaze.devfun.core.FunctionDefinition
import com.nextfaze.devfun.core.FunctionTransformer
import com.nextfaze.devfun.generated.DevFunGenerated
import java.io.File
import java.io.IOException
import java.lang.reflect.Method
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.lang.model.SourceVersion
import javax.lang.model.element.*
import javax.lang.model.type.DeclaredType
import javax.tools.Diagnostic
import javax.tools.StandardLocation.CLASS_OUTPUT
import javax.tools.StandardLocation.SOURCE_OUTPUT
import kotlin.reflect.KCallable
import kotlin.reflect.KClass

/**
 * Flag to enable Kotlin reflection to get method references. _(default: `false`)_ **(experimental)**
 *
 * Normal java reflection works fine - Kotlin reflection was disable as it was *extremely* slow (~0.5ms vs.
 * **~1.5s** in some cases). *(last tested around 1.1)*
 *
 * *Also, be aware that when last used, for unknown reasons every second private function reflection call using Kotlin
 * reflection failed with `IllegalAccessViolation`, even though `isAccessible = true` was clearly being called.*
 *
 * Set using APT options:
 * ```gradle
 * android {
 *      defaultConfig {
 *          javaCompileOptions {
 *              annotationProcessorOptions {
 *                  argument 'devfun.kotlin.reflection', 'true'
 *              }
 *          }
 *      }
 * }
 * ```
 *
 * **This feature is largely untested and mostly academic. It also has issues with overloaded functions.**
 */
const val FLAG_USE_KOTLIN_REFLECTION = "devfun.kotlin.reflection"

/**
 * Flag to output additional debug info as code comments. _(default: `false`)_
 *
 * Will show various class/function enclosing types, arg types, modifiers, etc.
 *
 * Set using APT options:
 * ```gradle
 * android {
 *      defaultConfig {
 *          javaCompileOptions {
 *              annotationProcessorOptions {
 *                  argument 'devfun.debug.comments', 'true'
 *              }
 *          }
 *      }
 * }
 * ```
 */
private const val FLAG_DEBUG_COMMENTS = "devfun.debug.comments"

/**
 * Flag to enable additional compile/processing log output. _(default: `false`)_
 *
 * Set using APT options:
 * ```gradle
 * android {
 *      defaultConfig {
 *          javaCompileOptions {
 *              annotationProcessorOptions {
 *                  argument 'devfun.debug.verbose', 'true'
 *              }
 *          }
 *      }
 * }
 * ```
 */
const val FLAG_DEBUG_VERBOSE = "devfun.debug.verbose"

/**
 * Sets the package suffix for the generated code. _(default: `devfun_generated`)_
 *
 * This is primarily for testing purposes to allow multiple generations in the same classpath.
 * - If this is null (unset) [PACKAGE_SUFFIX_DEFAULT] will be used.
 * - If this is empty the suffix will be omitted.
 *
 * Final output package will be: [PACKAGE_ROOT].`<variant?>`.`PACKAGE_SUFFIX`
 *
 * `<variant?>` will be omitted if both `packageRoot` and `packageSuffix` are provided.
 *
 * Set using APT options:
 * ```gradle
 * android {
 *      defaultConfig {
 *          javaCompileOptions {
 *              annotationProcessorOptions {
 *                  argument 'devfun.package.suffix', 'custom.suffix'
 *              }
 *          }
 *      }
 * }
 * ```
 */
const val PACKAGE_SUFFIX = "devfun.package.suffix"

/**
 * Sets the package root for the generated code. _(default: `<application package>`)_
 *
 * Attempts will be made to auto-detect the project package by using the class output directory and known/standard
 * relative paths to various build files, but if necessary this option can be set instead.
 *
 * Final output package will be: `PACKAGE_ROOT`.`<variant?>`.[PACKAGE_SUFFIX]
 *
 * `<variant?>` will be omitted if both `packageRoot` and `packageSuffix` are provided.
 *
 * Set using APT options:
 * ```gradle
 * android {
 *      defaultConfig {
 *          javaCompileOptions {
 *              annotationProcessorOptions {
 *                  argument 'devfun.package.root', 'com.your.application'
 *              }
 *          }
 *      }
 * }
 * ```
 */
const val PACKAGE_ROOT = "devfun.package.root"

/**
 * Sets the package for the generated code. _(default: `<none>`)_
 *
 * This will override [PACKAGE_ROOT] and [PACKAGE_SUFFIX].
 *
 * Set using APT options:
 * ```gradle
 * android {
 *      defaultConfig {
 *          javaCompileOptions {
 *              annotationProcessorOptions {
 *                  argument 'devfun.package.override', 'com.my.full.pkg.devfun.generated'
 *              }
 *          }
 *      }
 * }
 * ```
 */
const val PACKAGE_OVERRIDE = "devfun.package.override"

/**
 * Your application's package as sourced from your manifest file via the DevFun Gradle plugin.
 *
 * You should not set this directly.
 */
const val APPLICATION_PACKAGE = "devfun.application.package"

/**
 * The current build variant as sourced from the variant data/compile task via the DevFun Gradle plugin.
 *
 * You should not set this directly.
 */
const val APPLICATION_VARIANT = "devfun.application.variant"

/**
 * The same as [PACKAGE_SUFFIX], but is from the `devFun {}` configuration of the DevFun Grade plugin.
 *
 * This value is overridden by [PACKAGE_SUFFIX].
 *
 * You should not set this directly.
 */
const val EXT_PACKAGE_SUFFIX = "devfun.ext.package.suffix"

/**
 * The same as [PACKAGE_ROOT], but is from the `devFun {}` configuration of the DevFun Grade plugin.
 *
 * This value is overridden by [PACKAGE_ROOT].
 *
 * You should not set this directly.
 */
const val EXT_PACKAGE_ROOT = "devfun.ext.package.root"

/**
 * The same as [PACKAGE_OVERRIDE], but is from the `devFun {}` configuration of the DevFun Grade plugin.
 *
 * This value is overridden by [PACKAGE_OVERRIDE].
 *
 * You should not set this directly.
 */
const val EXT_PACKAGE_OVERRIDE = "devfun.ext.package.override"

/**
 * Default package output suffix: `devfun_generated`
 *
 * @see PACKAGE_SUFFIX
 */
const val PACKAGE_SUFFIX_DEFAULT = "devfun_generated"

internal const val META_INF_SERVICES = "META-INF/services"
private const val DEFINITIONS_FILE_NAME = "DevFunDefinitions.kt"
private const val DEFINITIONS_CLASS_NAME = "DevFunDefinitions"

/**
 * Annotation processor for [DeveloperFunction] and [DeveloperCategory].
 */
@SupportedOptions(
    FLAG_USE_KOTLIN_REFLECTION,
    FLAG_DEBUG_COMMENTS,
    FLAG_DEBUG_VERBOSE,
    PACKAGE_ROOT,
    PACKAGE_SUFFIX,
    PACKAGE_OVERRIDE,
    APPLICATION_PACKAGE,
    APPLICATION_VARIANT,
    EXT_PACKAGE_SUFFIX,
    EXT_PACKAGE_ROOT,
    EXT_PACKAGE_OVERRIDE
)
@AutoService(Processor::class)
class DevFunProcessor : AbstractProcessor() {
    override fun getSupportedAnnotationTypes() = setOf("*") // we need to accept all to be able to process meta annotated elements
    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()

    private val useKotlinReflection by lazy { processingEnv.options[FLAG_USE_KOTLIN_REFLECTION]?.toBoolean() ?: false }
    private val isDebugCommentsEnabled by lazy { isDebugVerbose || processingEnv.options[FLAG_DEBUG_COMMENTS]?.toBoolean() ?: false }
    private val isDebugVerbose by lazy { processingEnv.options[FLAG_DEBUG_VERBOSE]?.toBoolean() ?: false }
    private val ctx by lazy { CompileContext(processingEnv) }

    private val filer by lazy { processingEnv.filer }

    private val categoryDefinitions = HashMap<String, String>()
    private val functionDefinitions = HashMap<String, String>()
    private val developerMethods = HashMap<String, String>()

    private val TypeElement.isDeveloperAnnotation get() = getAnnotation(DeveloperAnnotation::class.java) != null

    override fun process(elements: Set<TypeElement>, env: RoundEnvironment): Boolean {
        try {
            if (!env.errorRaised()) {
                val devAnnotationElements = elements.filter { it.isDeveloperAnnotation }
                doProcess(devAnnotationElements, env)
            }
        } catch (t: Throwable) {
            error("Unexpected error: ${t.stackTraceAsString}")
        }

        // since we accept "*" now we return false to allow other processors to use what we didn't want
        return false
    }

    private fun doProcess(devAnnotationElements: List<TypeElement>, env: RoundEnvironment) {
        if (env.processingOver()) {
            if (categoryDefinitions.isNotEmpty() || functionDefinitions.isNotEmpty()) {
                writeServiceFile()
                writeSourceFile(generateKSource())
            }
        } else {
            processAnnotations(devAnnotationElements, env)
        }
    }

    /**
     * `FunctionInvoke` is a type alias - currently no way to resolve that at runtime (on road-map though)
     *
     * @see com.nextfaze.devfun.core.FunctionInvoke
     */
    private val functionInvokeQualified = "${FunctionDefinition::class.qualifiedName!!.replace("Definition", "")}Invoke"
    private val functionInvokeName = "${FunctionDefinition::class.simpleName!!.replace("Definition", "")}Invoke"

    private fun Element.toClass(
        kotlinClass: Boolean = true,
        isKtFile: Boolean = false,
        castIfNotPublic: KClass<*>? = null,
        vararg types: KClass<*>
    ) =
        asType().toClass(
            kotlinClass = kotlinClass,
            isKtFile = isKtFile,
            elements = processingEnv.elementUtils,
            castIfNotPublic = castIfNotPublic,
            types = *types
        )

    private fun processAnnotations(devAnnotationElements: List<TypeElement>, env: RoundEnvironment) {
        fun Element.toInstance(from: String, forStaticUse: Boolean = false, isClassKtFile: Boolean = false): String = when {
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

        //
        // DeveloperCategory
        //

        fun generateCatDef(clazz: String, devCat: AnnotationMirror) = mutableMapOf<KCallable<*>, Any>().apply {
            this += CategoryDefinition::clazz to clazz
            devCat[DeveloperCategory::value]?.let { this += CategoryDefinition::name to it.toKString() }
            devCat[DeveloperCategory::group]?.let { this += CategoryDefinition::group to it.toKString() }
            devCat[DeveloperCategory::order]?.let { this += CategoryDefinition::order to it }
        }.let { "SimpleCategoryDefinition(${it.entries.joinToString { "${it.key.name} = ${it.value}" }})" }

        fun addCategoryDefinition(element: TypeElement, devCat: AnnotationMirror) {
            // Debugging
            val categoryDefinition = "${element.enclosingElement}::$element"
            var debugAnnotationInfo = ""
            if (isDebugCommentsEnabled) {
                debugAnnotationInfo = "\n#|// $categoryDefinition"
            }

            // Generate definition
            categoryDefinitions[element.asType().toString()] =
                    """$debugAnnotationInfo
                     #|${generateCatDef(element.toClass(), devCat)}"""
        }

        env.getElementsAnnotatedWith(DeveloperCategory::class.java).forEach { element ->
            element as TypeElement
            note { "Processing ${element.enclosingElement}::$element..." }

            if (element.kind == ElementKind.ANNOTATION_TYPE) {
                error("MetaCategories are not supported yet.", element)
                return
            }

            val devCat = element.annotationMirrors.single { it.annotationType.toString() == DeveloperCategory::class.qualifiedName }
            addCategoryDefinition(element, devCat)

            if (element.kind == ElementKind.ANNOTATION_TYPE) {
                env.getElementsAnnotatedWith(element).forEach {
                    addCategoryDefinition(it as TypeElement, devCat)
                }
            }
        }

        //
        // DeveloperFunction
        //

        fun generateFunctionDefinition(element: ExecutableElement) {
            val clazz = element.enclosingElement as TypeElement
            note { "Processing $clazz::$element..." }

            if (clazz.isInterface) {
                error("Due to kapt issue @${DeveloperFunction::class.simpleName} is not supported in interfaces yet.", element)
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
                    note { "Skipping companion @JvmStatic $element" }
                    // This is a @JvmStatic method that is copied to the parent class during APT so just ignore this
                    // one and use the copied one instead, which will (or already has been) processed.
                    return
                }
            }

            // Annotation values
            val devFunc = element.annotationMirrors.single { it.annotationType.toString() == DeveloperFunction::class.qualifiedName }

            // Name
            val name = devFunc[DeveloperFunction::value]?.let {
                "\n#|    override val ${FunctionDefinition::name.name} = ${it.toKString()}"
            } ?: ""

            // Category
            val category = devFunc[DeveloperFunction::category]?.let {
                "\n#|    override val ${FunctionDefinition::category.name} = ${generateCatDef(FunctionDefinition::clazz.name, it)}"
            } ?: ""

            // Requires API
            val requiresApi = devFunc[DeveloperFunction::requiresApi]?.let {
                "\n#|    override val ${FunctionDefinition::requiresApi.name} = $it"
            } ?: ""

            // Transformer
            val transformer = devFunc[DeveloperFunction::transformer]?.let {
                "\n#|    override val ${FunctionDefinition::transformer.name} = ${it.asElement().toClass(
                    castIfNotPublic = KClass::class,
                    types = *arrayOf(FunctionTransformer::class)
                )}"
            } ?: ""

            // Can we call the function directly
            val funIsPublic = element.isPublic
            val classIsPublic = funIsPublic && clazz.isClassPublic
            val callFunDirectly = classIsPublic && element.typeParameters.all { it.bounds.all { it.isClassPublic } }

            // If true the the function is top-level (file-level) declared (and thus we cant directly reference its enclosing class)
            val isClassKtFile = clazz.isClassKtFile

            // For simplicity, for now we always invoke extension functions via reflection
            val isExtensionFunction by lazy { element.parameters.firstOrNull()?.simpleName?.toString() == "\$receiver" }

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
                    "${it.simpleName}=@[${it.annotationMirrors}] ${it.asType()}"
                }}
                #|        // classIsPublic=$classIsPublic, funIsPublic=$funIsPublic, callFunDirectly=$callFunDirectly, needReceiverArg=$needReceiverArg, isExtensionFunction=$isExtensionFunction"""
            }

            // Generate definition
            functionDefinitions[functionDefinition] =
                    """$debugAnnotationInfo
                     #|object : AbstractFunctionDefinition() {
                     #|    override val ${FunctionDefinition::method.name} = $methodRef$name$category$requiresApi$transformer
                     #|    override val ${FunctionDefinition::invoke.name}: $functionInvokeName = { $invocationArgs -> $debugElementInfo
                     #|        $invocation
                     #|    }
                     #|}"""
        }

        env.getElementsAnnotatedWith(DeveloperFunction::class.java).forEach {
            generateFunctionDefinition(it as ExecutableElement)
        }

        //
        // DeveloperAnnotation
        //

        fun generateDeveloperReferences(annotation: TypeElement, element: Element) {
            val clazz = element.enclosingElement as TypeElement
            note { "Processing $clazz::$element for $annotation..." }

            // The meta annotation class (e.g. Dagger2Component)
            val annotationClass = annotation.toClass()

            // Can we call the function directly
            val funIsPublic = element.isPublic
            val classIsPublic = funIsPublic && clazz.isClassPublic

            // If true the the function is top-level (file-level) declared (and thus we cant directly reference its enclosing class)
            val isClassKtFile = clazz.isClassKtFile

            // If we are referencing a method
            val method = (element as? ExecutableElement)?.let {
                val funName = element.simpleName.escapeDollar()
                val setAccessible = if (!classIsPublic || !element.isPublic) ".apply { isAccessible = true }" else ""
                val methodArgTypes = element.parameters.joiner(prefix = ", ") { it.toClass(false) }
                when {
                    useKotlinReflection -> """${clazz.toClass()}.declaredFunctions.filter { it.name == "${element.simpleName.stripInternal()}" && it.parameters.size == ${element.parameters.size + 1} }.single().javaMethod!!$setAccessible"""
                    else -> """${clazz.toClass(false, isClassKtFile)}.getDeclaredMethod("$funName"$methodArgTypes)$setAccessible"""
                }
            }

            val developerAnnotation = "${element.enclosingElement}::$element"
            var debugAnnotationInfo = ""
            if (isDebugCommentsEnabled) {
                debugAnnotationInfo = "\n#|// $developerAnnotation"
            }

            developerMethods[developerAnnotation] =
                    """$debugAnnotationInfo
                        #|object : ${DeveloperReference::class.simpleName} {
                        #|    override val ${DeveloperReference::annotation.name}: KClass<out Annotation> by lazy { $annotationClass }
                        #|    override val ${DeveloperReference::method.name}: Method? by lazy { $method }
                        #|}"""
        }

        devAnnotationElements.forEach { devAnnotation ->
            env.getElementsAnnotatedWith(devAnnotation).forEach {
                if (it !is ExecutableElement) {
                    error(
                        """Only executable elements are supported at the moment.
                            |Please make an issue if you want someone else (or feel free to make a PR)""".trimMargin(),
                        element = it
                    )
                } else {
                    generateDeveloperReferences(devAnnotation, it)
                }
            }
        }
    }

    private fun generateKSource(): String {
        val imports = mutableSetOf<String?>().apply {
            this += CategoryDefinition::class.qualifiedName
            this += DevFunGenerated::class.qualifiedName
            this += DeveloperReference::class.qualifiedName
            this += FunctionDefinition::class.qualifiedName
            this += functionInvokeQualified
            this += Method::class.qualifiedName
            this += KClass::class.qualifiedName

            if (useKotlinReflection) {
                this += "kotlin.reflect.full.declaredFunctions"
                this += "kotlin.reflect.jvm.javaMethod"
            }
        }.toList().filterNotNull().sorted()

        //
        // FTR we generate/use 'class' as opposed to 'object' due to issues with JRebel hot-swapping, creating a new
        // instance as needed (caching results after processing etc.).
        //
        // Under the hood the 'object' type holds its instance in a static field 'INSTANCE'. When JRebel does its thing,
        // it doesn't replace/update static fields *inside* of the class (only the class definition itself). This
        // wouldn't be a problem normally w.r.t. normal singletons, but this field is not accessible/assignable by us.
        //
        return """@file:Suppress("UNCHECKED_CAST", "PackageDirectoryMismatch")

package ${ctx.pkg}

${imports.joinToString("\n") { "import $it" }}

private data class SimpleCategoryDefinition(
    override val clazz: KClass<*>? = null,
    override val name: String? = null,
    override val group: String? = null,
    override val order: Int? = null
) : CategoryDefinition

private abstract class AbstractFunctionDefinition : FunctionDefinition {
    override fun equals(other: Any?) = this === other || other is FunctionDefinition && method == other.method
    override fun hashCode() = method.hashCode()
    override fun toString() = "FunctionDefinition(${'$'}method)"
}

private inline fun <reified T : Any> kClass(): KClass<T> = T::class

private inline val <T : Any> KClass<T>.privateObjectInstance
    get() = java.getDeclaredField("INSTANCE").apply { isAccessible = true }.get(null) as T

class $DEFINITIONS_CLASS_NAME : ${DevFunGenerated::class.simpleName} {
    override val ${DevFunGenerated::categoryDefinitions.name} = listOf<${CategoryDefinition::class.simpleName}>(
${categoryDefinitions.values.sorted().joinToString(",").replaceIndentByMargin("        ", "#|")}
    )
    override val ${DevFunGenerated::functionDefinitions.name} = listOf<${FunctionDefinition::class.simpleName}>(
${functionDefinitions.values.sorted().joinToString(",").replaceIndentByMargin("        ", "#|")}
    )
    override val ${DevFunGenerated::developerReferences.name} = listOf<${DeveloperReference::class.simpleName}>(
${developerMethods.values.sorted().joinToString(",").replaceIndentByMargin("        ", "#|")}
    )
}
"""
    }

    private fun writeServiceFile() {
        val servicesPath = "$META_INF_SERVICES/${DevFunGenerated::class.qualifiedName}"
        val servicesText = "${ctx.pkg}.$DEFINITIONS_CLASS_NAME\n"

        filer.createResource(CLASS_OUTPUT, "", servicesPath).apply {
            note { "Write services file to ${File(toUri()).canonicalPath}" }
            openWriter().use { it.write(servicesText) }
        }
    }

    private fun writeSourceFile(text: String) {
        try {
            filer.createResource(SOURCE_OUTPUT, ctx.pkg, DEFINITIONS_FILE_NAME).openWriter().use {
                it.write(text)
            }
        } catch (e: IOException) {
            error("Failed to write source file:\n${e.stackTraceAsString}")
        }
    }

    private fun error(message: String, element: Element? = null, annotationMirror: AnnotationMirror? = null) =
        processingEnv.messager.printMessage(Diagnostic.Kind.ERROR, message, element, annotationMirror)

    @Suppress("unused")
    private fun warn(message: String, element: Element? = null, annotationMirror: AnnotationMirror? = null) =
        processingEnv.messager.printMessage(Diagnostic.Kind.MANDATORY_WARNING, message, element, annotationMirror)

    private fun note(condition: Boolean = isDebugVerbose, body: () -> String) =
        runIf(condition) { processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, body()) }
}

private const val RECEIVER_VAR_NAME = "receiver"
private const val ARGS_VAR_NAME = "args"
