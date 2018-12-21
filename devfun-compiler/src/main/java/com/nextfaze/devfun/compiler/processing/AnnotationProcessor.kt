package com.nextfaze.devfun.compiler.processing

import com.nextfaze.devfun.compiler.Options
import com.nextfaze.devfun.compiler.StringPreprocessor
import com.nextfaze.devfun.compiler.WithElements
import com.nextfaze.devfun.compiler.escapeDollar
import com.nextfaze.devfun.compiler.getAnnotation
import com.nextfaze.devfun.compiler.isPublic
import com.nextfaze.devfun.compiler.joiner
import com.nextfaze.devfun.compiler.stripInternal
import com.nextfaze.devfun.compiler.toKClassBlock
import com.squareup.kotlinpoet.*
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.type.TypeMirror
import kotlin.reflect.KCallable
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties

internal interface Processor : WithElements {
    val preprocessor: StringPreprocessor
    val kElements: KElements

    fun TypeMirror.toKClassBlock(
        kotlinClass: Boolean = true,
        isKtFile: Boolean = false,
        castIfNotPublic: TypeName? = null
    ) = toKClassBlock(kotlinClass, isKtFile, castIfNotPublic, elements)

    fun TypeElement.toClassElement() = kElements[this]

    // during normal gradle builds string types will be java.lang
    // during testing however they will be kotlin types
    val TypeMirror.isString get() = toString().let { it == "java.lang.String" || it == "kotlin.String" }

    fun String.toValue(element: Element?) = preprocessor.run(this, element)
}

internal interface AnnotationProcessor : Processor {
    val options: Options

    val willGenerateSource: Boolean

    fun processAnnotatedElement(annotatedElement: AnnotatedElement, env: RoundEnvironment)

    fun applyToFileSpec(fileSpec: FileSpec.Builder) = Unit
    fun applyToTypeSpec(typeSpec: TypeSpec.Builder)

    val VariableElement.classElement get() = kElements[enclosingElement as TypeElement]
    val ExecutableElement.classElement get() = kElements[enclosingElement as TypeElement]

    fun KCallable<*>.toPropertySpec(
        propName: String = name,
        returns: TypeName = returnType.asTypeName(),
        init: Any? = null,
        initBlock: CodeBlock? = null,
        lazy: Any? = null,
        kDoc: String? = null,
        kDocEnabled: Boolean = options.isDebugCommentsEnabled
    ) =
        PropertySpec.builder(
            propName,
            returns,
            KModifier.OVERRIDE
        ).apply {
            if (init != null) initializer("%L", init)
            if (initBlock != null) initializer(initBlock)
            if (lazy != null) delegate("lazy { %L }", lazy)
            if (kDoc != null && kDocEnabled) addKdoc(kDoc)
        }

    fun Collection<*>.toListOfBlock(type: KClass<*>) =
        CodeBlock.of("listOf<%T>(${",\n%L".repeat(size).drop(1)})", type, *toTypedArray())

    fun Element.getSetAccessible(classIsPublic: Boolean) = if (!classIsPublic || !isPublic) ".apply { isAccessible = true }" else ""

    fun ExecutableElement.toMethodRef(): CodeBlock {
        val clazz = (enclosingElement as TypeElement).toClassElement()

        // Can we call the function directly
        val funIsPublic = isPublic
        val classIsPublic = funIsPublic && clazz.isPublic

        val funName = simpleName.escapeDollar()
        val methodArgTypes = parameters.map { it.asType().toKClassBlock(kotlinClass = false) }.joiner(prefix = ", ")
        val setAccessible = getSetAccessible(classIsPublic)
        return if (options.useKotlinReflection) {
            CodeBlock.of(
                """%L.declaredFunctions.filter { it.name == "${simpleName.stripInternal()}" && it.parameters.size == ${parameters.size + 1} }.single().javaMethod!!$setAccessible""",
                clazz.type.toKClassBlock(kotlinClass = false, isKtFile = clazz.isKtFile)
            )
        } else {
            CodeBlock.of(
                "%L.getDeclaredMethod(\"$funName\"%L)$setAccessible",
                clazz.type.toKClassBlock(kotlinClass = false, isKtFile = clazz.isKtFile),
                methodArgTypes
            )
        }
    }

    fun TypeSpec.Builder.setConstructorParams(vararg params: CtorParam) =
        primaryConstructor(
            FunSpec.constructorBuilder().addParameters(
                params.map {
                    ParameterSpec.builder(it.name, it.type, KModifier.OVERRIDE).apply {
                        if (it.defaultValue != null) {
                            defaultValue(it.defaultValue)
                        }
                    }.build()
                }
            ).build()
        ).addProperties(
            params.map {
                PropertySpec.builder(it.name, it.type, KModifier.OVERRIDE).initializer(it.name).build()
            }
        )

    fun KClass<*>.toDataClassTypeSpec(name: ClassName) =
        TypeSpec.classBuilder(name)
            .addModifiers(KModifier.PRIVATE, KModifier.DATA)
            .addSuperinterface(this)
            .setConstructorParams(
                *memberProperties.map {
                    val type = it.returnType
                    val typeName = if (type.isMarkedNullable) type.asTypeName().asNullable() else type.asTypeName()
                    CtorParam(it.name, typeName, if (type.isMarkedNullable) "null" else null)
                }.sortedBy { it.name }.toTypedArray()
            )
}

internal data class AnnotatedElement(
    val element: Element,
    val annotationElement: KElements.ClassElement,
    val asFunction: Boolean,
    val asCategory: Boolean,
    val asReference: Boolean
) {
    val annotation: AnnotationMirror = element.getAnnotation(annotationElement)!!
}

internal data class CtorParam(
    val name: String,
    val type: TypeName,
    val defaultValue: String? = null
)
