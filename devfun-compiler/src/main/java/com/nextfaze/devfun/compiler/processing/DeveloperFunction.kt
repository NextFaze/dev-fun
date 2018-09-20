package com.nextfaze.devfun.compiler.processing

import com.nextfaze.devfun.annotations.DeveloperFunction
import com.nextfaze.devfun.compiler.*
import com.nextfaze.devfun.compiler.properties.ImplementationGenerator
import com.nextfaze.devfun.core.FunctionDefinition
import com.nextfaze.devfun.core.ReferenceDefinition
import com.nextfaze.devfun.core.WithProperties
import com.nextfaze.devfun.generated.DevFunGenerated
import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import javax.annotation.processing.RoundEnvironment
import javax.inject.Inject
import javax.inject.Singleton
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.util.Elements
import kotlin.reflect.KClass

private const val RECEIVER_VAR_NAME = "receiver"
private const val ARGS_VAR_NAME = "args"
private const val PRIVATE_OBJECT_INSTANCE_EXT_FUN = "privateObjectInstance"

@Singleton
internal class DeveloperFunctionHandler @Inject constructor(
    override val elements: Elements,
    override val preprocessor: StringPreprocessor,
    override val options: Options,
    override val kElements: KElements,
    private val annotations: AnnotationElements,
    private val developerCategory: DeveloperCategoryHandler,
    private val implementationGenerator: ImplementationGenerator,
    logging: Logging
) : AnnotationProcessor {
    private val log by logging()

    private val isDebugCommentsEnabled get() = options.isDebugCommentsEnabled

    private val functionInvokeName =
        ClassName.bestGuess("com.nextfaze.devfun.core.${FunctionDefinition::class.simpleName!!.replace("Definition", "Invoke")}")

    private val functionDefinitions = sortedMapOf<String, TypeSpec>()
    override val willGenerateSource get() = functionDefinitions.isNotEmpty()

    private var abstractFunctionDefinitionUsed = false
    private val abstractFunctionDefinitionName = ClassName.bestGuess("AbstractFunctionDefinition")
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

    private var privateObjectInstanceFuncUsed = false
    private val privateObjectInstanceFunc by lazy {
        val t = TypeVariableName("T", ANY)
        PropertySpec.builder(PRIVATE_OBJECT_INSTANCE_EXT_FUN, t, KModifier.PRIVATE)
            .receiver(KClass::class.asTypeName().parameterizedBy(t))
            .addTypeVariable(t)
            .getter(
                FunSpec.getterBuilder()
                    .addModifiers(KModifier.INLINE)
                    .addStatement("return java.getDeclaredField(\"INSTANCE\").apply { isAccessible = true }.get(null) as %T", t)
                    .build()
            )
            .build()
    }

    override fun applyToFileSpec(fileSpec: FileSpec.Builder) {
        if (abstractFunctionDefinitionUsed) {
            fileSpec.addType(abstractFunctionDefinition)
        }
        if (privateObjectInstanceFuncUsed) {
            fileSpec.addProperty(privateObjectInstanceFunc)
        }
    }

    override fun applyToTypeSpec(typeSpec: TypeSpec.Builder) {
        typeSpec.addProperty(
            DevFunGenerated::functionDefinitions.toPropertySpec(
                initBlock = functionDefinitions.values.toListOfBlock(FunctionDefinition::class)
            ).build()
        )
    }

    override fun processAnnotatedElement(annotatedElement: AnnotatedElement, env: RoundEnvironment) {
        if (!annotatedElement.asFunction) return

        val element = annotatedElement.element
        if (element !is ExecutableElement) {
            log.error(element = element) {
                """Only executable elements are supported with DeveloperFunction (elementType=${element::class}).
                            |Please make an issue if you want something else (or feel free to make a PR)""".trimMargin()
            }
            return
        }

        val elementDesc = "${element.enclosingElement}::$element"
        if (functionDefinitions.containsKey(elementDesc)) {
            log.error(element) { "Only one function definition supported per function." }
            return
        }

        val clazz = element.classElement
        log.warn { "Processing $clazz::$element..." }

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
        if (clazz.isCompanionObject) {
            if (clazz.enclosingElement.enclosedElements.count {
                    it.isStatic && it.modifiers.containsAll(element.modifiers) && it.simpleName == element.simpleName
                } == 1) {
                log.note { "Skipping companion @JvmStatic $element" }
                // This is a @JvmStatic method that is copied to the parent class during APT so just ignore this
                // one and use the copied one instead, which will (or already has been) processed.
                return
            }
        }

        // Definition implementation
        abstractFunctionDefinitionUsed = true
        val def = TypeSpec.anonymousClassBuilder().superclass(abstractFunctionDefinitionName)

        // Annotation values
        annotations.createDevFunAnnotation(annotatedElement.annotation, annotatedElement.annotationElement).apply {
            value?.apply {
                def.addProperty(
                    FunctionDefinition::name.toPropertySpec(initBlock = CodeBlock.of("%V", toValue(element))).build()
                )
            }
            category?.apply {
                val cat = developerCategory.createCatDefSource(this, FunctionDefinition::clazz.name, clazz)
                def.addProperty(FunctionDefinition::category.toPropertySpec(lazy = cat).build())
            }
            requiresApi?.apply { def.addProperty(FunctionDefinition::requiresApi.toPropertySpec(init = this).build()) }
            transformer?.apply {
                def.addProperty(
                    FunctionDefinition::transformer.let {
                        it.toPropertySpec(initBlock = toKClassBlock(castIfNotPublic = it.returnType.asTypeName())).build()
                    }
                )
            }
        }

        // Can we call the function directly
        val allArgTypesPublic by lazy { element.parameters.all { it.asType().isPublic } }
        val allTypeParamsPublic by lazy { element.typeParameters.all { typeParam -> typeParam.bounds.all { it.isClassPublic } } }
        val callFunDirectly = element.isPublic && clazz.isPublic && allArgTypesPublic && allTypeParamsPublic

        // For simplicity, for now we always invoke extension functions via reflection
        val isExtensionFunction by lazy { element.parameters.firstOrNull()?.simpleName?.toString() == "\$receiver" }

        // Kotlin properties
        val isProperty = element.isProperty

        // Arguments
        val receiverVar = if (!element.isStatic && !clazz.isKObject) RECEIVER_VAR_NAME else "_"
        val argsVar = if (element.parameters.isNotEmpty()) ARGS_VAR_NAME else "_"
        val needReceiverArg = !callFunDirectly && !element.isStatic
        val args = run generateInvocationArgs@{
            val arguments = mutableListOf<CodeBlock>()
            if (needReceiverArg) {
                arguments += when {
                    clazz.isPublic -> if (clazz.isKObject) clazz.typeBlock else CodeBlock.of("$RECEIVER_VAR_NAME as %T", clazz.typeName)
                    clazz.isKObject -> {
                        privateObjectInstanceFuncUsed = true
                        CodeBlock.of("%L.$PRIVATE_OBJECT_INSTANCE_EXT_FUN", clazz.klassBlock)
                    }
                    else -> CodeBlock.of(RECEIVER_VAR_NAME)
                }
            } else if ((!callFunDirectly && element.isStatic) || isExtensionFunction) {
                arguments += CodeBlock.of("null")
            }

            element.parameters.forEachIndexed { index, arg ->
                val argType = arg.asType()
                val codeBlock =
                    if (!callFunDirectly || isExtensionFunction) {
                        if (argType.isPublic) {
                            CodeBlock.of("$ARGS_VAR_NAME[$index] as %T", argType.toTypeName())
                        } else {
                            CodeBlock.of("$ARGS_VAR_NAME[$index]")
                        }
                    } else {
                        CodeBlock.of("$ARGS_VAR_NAME[$index] as %T", argType.toTypeName())
                    }

                arguments += codeBlock
            }

            arguments.joinToCode()
        }

        // Method reference
        def.addProperty(FunctionDefinition::method.toPropertySpec(lazy = element.toMethodRef(), kDoc = elementDesc).build())

        // Call invocation
        val invocation = run generateInvocation@{
            when {
                isProperty -> CodeBlock.of("throw UnsupportedOperationException(\"Direct invocation of an annotated property is not supported. This invocation should have been handled by the PropertyTransformer.\")")
                callFunDirectly && !isExtensionFunction -> {
                    val invokeBlock = element.toInvokeCodeBlock(RECEIVER_VAR_NAME)
                    if (element.typeParameters.isEmpty()) {
                        CodeBlock.of("%L(%L)", invokeBlock, args)
                    } else {
                        val typeParams = element.typeParameters.map { it.asType().toTypeName().toCodeBlock() }.joinToCode()
                        CodeBlock.of("%L<%L>(%L)", invokeBlock, typeParams, args)
                    }
                }
                else -> CodeBlock.of("${FunctionDefinition::method.name}.invoke(%L)", args)
            }
        }

        val lambdaBlock = buildCodeBlock {
            beginControlFlow("{ %N, %N ->", receiverVar, argsVar)
            addStatement("%L", invocation)
            endControlFlow()
        }

        val debugComments = if (isDebugCommentsEnabled) {
            """element modifiers: ${element.modifiers.joinToString()}
                |enclosing element modifiers: ${element.enclosingElement.modifiers.joinToString()}
                |enclosing element metadata: isKtFile=${clazz.isKtFile}
                |enclosing element as type element: ${(element.enclosingElement.asType() as? DeclaredType)?.asElement() as? TypeElement}
                |param type modifiers: ${element.parameters
                .joiner { "${it.simpleName}=${(it.asType() as? DeclaredType)?.asElement()?.modifiers?.joinToString(",")}" }}
                |params: ${element.parameters.joiner { "${it.simpleName}=@[${it.annotationMirrors}] isTypePublic=${it.asType().isPublic} ${it.asType()}" }}
                |classIsPublic=${clazz.isPublic}, funIsPublic=${element.isPublic}, allArgTypesPublic=$allArgTypesPublic, allTypeParamsPublic=$allTypeParamsPublic, callFunDirectly=$callFunDirectly, needReceiverArg=$needReceiverArg, isExtensionFunction=$isExtensionFunction, isProperty=$isProperty""${'"'}""".trimMargin()
        } else null

        def.addProperty(
            FunctionDefinition::invoke.toPropertySpec(
                returns = functionInvokeName,
                initBlock = lambdaBlock,
                kDoc = debugComments
            ).build()
        )

        // Generate any properties
        val properties = implementationGenerator.processAnnotatedElement(annotatedElement, env)
        if (properties != null) {
            val (typeName, impl) = properties
            def.addSuperinterface(ReferenceDefinition::class)
                .addProperty(
                    ReferenceDefinition::annotation.let {
                        it.toPropertySpec( // The meta annotation class (e.g. Dagger2Component)
                            initBlock = annotatedElement.annotationElement.type.toKClassBlock(castIfNotPublic = it.returnType.asTypeName())
                        ).build()
                    }
                )
                .addSuperinterface(WithProperties::class.asTypeName().parameterizedBy(typeName))
                .addProperty(WithProperties<Any>::properties.toPropertySpec(returns = typeName, init = impl).build())
        }

        // Generate definition
        functionDefinitions[elementDesc] = def.build()
    }

    private fun ExecutableElement.toInvokeCodeBlock(from: String): CodeBlock {
        val clazz = classElement
        if (!clazz.isPublic) return CodeBlock.of("%N", from)

        return when {
            clazz.isKtFile -> CodeBlock.of("%T", ClassName(enclosingElement.enclosingElement.toString(), simpleName.stripInternal()))
            isStatic -> CodeBlock.of("%L.%L", clazz.typeBlock, simpleName.stripInternal())
            clazz.isKObject -> CodeBlock.of("%T.${simpleName.stripInternal()}", clazz.typeName)
            else -> CodeBlock.of("(%N as %T).${simpleName.stripInternal()}", from, clazz.typeName)
        }
    }
}
