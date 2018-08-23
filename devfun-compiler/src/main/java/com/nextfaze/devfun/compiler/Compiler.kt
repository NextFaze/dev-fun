package com.nextfaze.devfun.compiler

import com.google.auto.service.AutoService
import com.nextfaze.devfun.annotations.DeveloperAnnotation
import com.nextfaze.devfun.compiler.processing.DeveloperAnnotationProcessor
import com.nextfaze.devfun.core.CategoryDefinition
import com.nextfaze.devfun.core.FunctionDefinition
import com.nextfaze.devfun.generated.DevFunGenerated
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import java.io.File
import java.io.IOException
import javax.annotation.processing.Filer
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedOptions
import javax.inject.Inject
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.StandardLocation.CLASS_OUTPUT
import javax.tools.StandardLocation.SOURCE_OUTPUT
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
const val FLAG_DEBUG_COMMENTS = "devfun.debug.comments"

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

/**
 * Restrict DevFun to only process elements matching filter `elementFQN.startsWith(it)`.
 *
 * Value can be a comma separated list. Whitespace will be trimmed.
 *
 * In general this shouldn't be used and is primarily for testing/development purposes.
 *
 * Example usage (from test sources):
 * ```kotlin
 * android {
 *     defaultConfig {
 *         javaCompileOptions {
 *             annotationProcessorOptions {
 *                 argument("devfun.elements.include", "tested.developer_reference.HasSimpleTypes, tested.custom_names.")
 *             }
 *         }
 *     }
 * }
 * ```
 * Will match classes `tested.developer_reference.HasSimpleTypes` and `tested.developer_reference.HasSimpleTypesWithDefaults`,
 * and anything in package `tested.custom_names.` (including nested).
 */
const val ELEMENTS_FILTER_INCLUDE = "devfun.elements.include"

/**
 * Restrict DevFun to only process elements matching filter `elementFQN.startsWith(it)`.
 *
 * Value can be a comma separated list. Whitespace will be trimmed.
 *
 * In general this shouldn't be used and is primarily for testing/development purposes.
 *
 * Example usage (from test sources):
 * ```kotlin
 * android {
 *     defaultConfig {
 *         javaCompileOptions {
 *             annotationProcessorOptions {
 *                 argument("devfun.elements.include", "tested.developer_reference.HasSimpleTypes, tested.custom_names.")
 *             }
 *         }
 *     }
 * }
 * ```
 * Will match classes `tested.developer_reference.HasSimpleTypes` and `tested.developer_reference.HasSimpleTypesWithDefaults`,
 * and anything in package `tested.custom_names.` (including nested).
 */
const val ELEMENTS_FILTER_EXCLUDE = "devfun.elements.exclude"

/**
 * Flag to disable DevFun from generating implementations of [DevFunGenerated]. Useful for testing or if you only want to generate
 * annotation interfaces.
 *
 * @see GENERATE_INTERFACES
 */
const val GENERATE_DEFINITIONS = "devfun.definitions.generate"

internal const val META_INF_SERVICES = "META-INF/services"
private const val DEFINITIONS_FILE_NAME = "DevFunDefinitions.kt"
private const val DEFINITIONS_CLASS_NAME = "DevFunDefinitions"

/**
 * Annotation processor for [DeveloperAnnotation] annotated annotations.
 *
 * _Visible for testing purposes only! Use at your own risk._
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
    EXT_PACKAGE_OVERRIDE,
    GENERATE_DEFINITIONS,
    ELEMENTS_FILTER_INCLUDE,
    ELEMENTS_FILTER_EXCLUDE
)
@AutoService(Processor::class)
class DevFunProcessor : DaggerProcessor() {
    override fun getSupportedAnnotationTypes() = setOf("*") // we need to accept all to be able to process meta annotated elements
    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latestSupported()

    override fun inject(injector: Injector) = injector.inject(this)

    private val useKotlinReflection get() = options.useKotlinReflection

    @Inject internal lateinit var options: Options
    @Inject internal lateinit var logging: Logging
    @Inject internal lateinit var filer: Filer
    @Inject internal lateinit var ctx: CompileContext
    @Inject internal lateinit var processor: DeveloperAnnotationProcessor
    @Inject internal lateinit var typeImports: ImportsTracker

    private val log by lazy { logging.create(this) }

    override fun process(annotations: Set<TypeElement>, env: RoundEnvironment): Boolean {
        if (!options.generateDefinitions || env.errorRaised()) return false

        try {
            if (env.processingOver()) {
                if (processor.willGenerateSources) {
                    writeServiceFile()
                    writeSourceFile(generateKSource())
                }
            } else {
                processor.process(annotations, env)
            }
        } catch (t: Throwable) {
            log.error { "Unexpected error: ${t.stackTraceAsString}" }
        }

        // since we accept "*" now we return false to allow other processors to use what we didn't want
        return false
    }

    /**
     * `FunctionInvoke` is a type alias - currently no way to resolve that at runtime (on road-map though)
     *
     * @see com.nextfaze.devfun.core.FunctionInvoke
     */
    private val functionInvokeQualified = "${FunctionDefinition::class.qualifiedName!!.replace("Definition", "")}Invoke"

    private val simpleCategoryDefinition by lazy {
        val kClassType = KClass::class.asTypeName().parameterizedBy(WildcardTypeName.STAR).asNullable()
        val stringType = String::class.asTypeName().asNullable()
        val intType = Int::class.asTypeName().asNullable()

        TypeSpec.classBuilder(simpleCategoryDefinitionName)
            .addSuperinterface(CategoryDefinition::class)
            .addModifiers(KModifier.PRIVATE, KModifier.DATA)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter(ParameterSpec.builder("clazz", kClassType, KModifier.OVERRIDE).defaultValue("null").build())
                    .addParameter(ParameterSpec.builder("name", stringType, KModifier.OVERRIDE).defaultValue("null").build())
                    .addParameter(ParameterSpec.builder("group", stringType, KModifier.OVERRIDE).defaultValue("null").build())
                    .addParameter(ParameterSpec.builder("order", intType, KModifier.OVERRIDE).defaultValue("null").build())
                    .build()
            )
            .addProperty(PropertySpec.builder("clazz", kClassType).initializer("clazz").build())
            .addProperty(PropertySpec.builder("name", stringType).initializer("name").build())
            .addProperty(PropertySpec.builder("group", stringType).initializer("group").build())
            .addProperty(PropertySpec.builder("order", intType).initializer("order").build())
            .build()
    }

    private val abstractFunctionDefinition by lazy {
        TypeSpec.classBuilder(abstractFunctionDefinitionName)
            .addSuperinterface(FunctionDefinition::class)
            .addModifiers(KModifier.PRIVATE, KModifier.ABSTRACT)
            .addFunction(
                FunSpec.builder("equals")
                    .addModifiers(KModifier.OVERRIDE)
                    .addParameter("other", ANY.asNullable())
                    .addStatement("return %L", "this === other || other is FunctionDefinition && method == other.method")
                    .build()
            )
            .addFunction(
                FunSpec.builder("hashCode")
                    .addModifiers(KModifier.OVERRIDE)
                    .addStatement("return %L", "method.hashCode()")
                    .build()
            )
            .addFunction(
                FunSpec.builder("toString")
                    .addModifiers(KModifier.OVERRIDE)
                    .addStatement("return %S", "FunctionDefinition(\$method)")
                    .build()
            )
            .build()
    }

    private val privateObjectInstanceFunc by lazy {
        // TODO https://github.com/square/kotlinpoet/issues/437
        PropertySpec.builder("privateObjectInstance", TypeVariableName("T"), KModifier.PRIVATE)
            .receiver(KClass::class.asTypeName().parameterizedBy(TypeVariableName("T")))
            .getter(
                FunSpec.getterBuilder()
                    .addModifiers(KModifier.INLINE)
                    .addStatement("return %L", "java.getDeclaredField(\"INSTANCE\").apply { isAccessible = true }.get(null) as T")
                    .build()
            )
            .build()

        CodeBlock.of(
            """
private inline val <T : Any> KClass<T>.privateObjectInstance
    get() = java.getDeclaredField("INSTANCE").apply { isAccessible = true }.get(null) as T""".trimIndent()
        )
    }

    private fun generateKSource(): String {
        val imports = mutableSetOf<String?>().apply {
            typeImports.forEach { this += it.qualifiedName }
            this += functionInvokeQualified

            if (useKotlinReflection) {
                this += "kotlin.reflect.full.declaredFunctions"
                this += "kotlin.reflect.jvm.javaMethod"
            }
        }.toList().filterNotNull().sorted()

        val annotations = AnnotationSpec.builder(Suppress::class)
            .useSiteTarget(AnnotationSpec.UseSiteTarget.FILE)
            .addMember("%L", """"UNCHECKED_CAST", "CAST_NEVER_SUCCEEDS", "PackageDirectoryMismatch", "PackageName"""")
            .build()

        //
        // FTR we generate/use 'class' as opposed to 'object' due to issues with JRebel hot-swapping, creating a new
        // instance as needed (caching results after processing etc.).
        //
        // Under the hood the 'object' type holds its instance in a static field 'INSTANCE'. When JRebel does its thing,
        // it doesn't replace/update static fields *inside* of the class (only the class definition itself). This
        // wouldn't be a problem normally w.r.t. normal singletons, but this field is not accessible/assignable by us.
        //
        return """$annotations

package ${ctx.pkg}

${imports.joinToString("\n") { "import $it" }}

$simpleCategoryDefinition
$abstractFunctionDefinition
$kClassFunc
$privateObjectInstanceFunc

class $DEFINITIONS_CLASS_NAME : ${DevFunGenerated::class.simpleName} {
${processor.generateSources()}
}
"""
    }

    private fun writeServiceFile() {
        val servicesPath = "$META_INF_SERVICES/${DevFunGenerated::class.qualifiedName}"
        val servicesText = "${ctx.pkg}.$DEFINITIONS_CLASS_NAME\n"

        filer.createResource(CLASS_OUTPUT, "", servicesPath).apply {
            log.note { "Write services file to ${File(toUri()).canonicalPath}" }
            openWriter().use { it.write(servicesText) }
        }
    }

    private fun writeSourceFile(text: String) {
        try {
            filer.createResource(SOURCE_OUTPUT, ctx.pkg, DEFINITIONS_FILE_NAME).openWriter().use {
                it.write(text)
            }
        } catch (e: IOException) {
            log.error { "Failed to write source file:\n${e.stackTraceAsString}" }
        }
    }
}

val kClassFunc by lazy {
    FunSpec.builder("kClass")
        .addTypeVariable(TypeVariableName("T", ANY).reified(true))
        .addModifiers(KModifier.PRIVATE, KModifier.INLINE)
        .addStatement("return T::class")
        .build()
}

val simpleCategoryDefinitionName = ClassName.bestGuess("SimpleCategoryDefinition")
val abstractFunctionDefinitionName = ClassName.bestGuess("AbstractFunctionDefinitionName")
