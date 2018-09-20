package com.nextfaze.devfun.compiler.processing

import com.nextfaze.devfun.compiler.get
import com.nextfaze.devfun.compiler.isClassPublic
import com.nextfaze.devfun.compiler.toKClassBlock
import com.nextfaze.devfun.compiler.toTypeName
import com.squareup.kotlinpoet.CodeBlock
import javax.inject.Inject
import javax.inject.Singleton
import javax.lang.model.element.AnnotationValue
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Name
import javax.lang.model.element.TypeElement
import javax.lang.model.type.ArrayType
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.PrimitiveType
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import kotlin.reflect.jvm.internal.impl.descriptors.SourceElement
import kotlin.reflect.jvm.internal.impl.load.java.JvmAnnotationNames
import kotlin.reflect.jvm.internal.impl.load.kotlin.header.KotlinClassHeader
import kotlin.reflect.jvm.internal.impl.load.kotlin.header.ReadKotlinClassHeaderAnnotationVisitor
import kotlin.reflect.jvm.internal.impl.metadata.ProtoBuf
import kotlin.reflect.jvm.internal.impl.metadata.deserialization.Flags.CLASS_KIND
import kotlin.reflect.jvm.internal.impl.metadata.jvm.deserialization.JvmProtoBufUtil
import kotlin.reflect.jvm.internal.impl.name.ClassId

@Singleton
internal class KElements @Inject constructor(
    private val elements: Elements,
    private val types: Types
) {
    private val typeElementMap = mutableMapOf<Name, ClassElement>()
    private val metaDataElement: DeclaredType = types.getDeclaredType(elements.getTypeElement("kotlin.Metadata"))

    operator fun get(typeElement: TypeElement) =
        with(typeElement) { typeElementMap.getOrPut(qualifiedName) { ClassElement(this) } }

    inner class ClassElement(val element: TypeElement) : TypeElement by element {
        private val metaDataAnnotation by lazy {
            element.annotationMirrors.firstOrNull { types.isSameType(it.annotationType, metaDataElement) }
        }

        val isInterface = element.kind.isInterface
        private val isClass = element.kind.isClass

        val simpleName = element.simpleName.toString()
        override fun toString() = element.toString()

        val isPublic by lazy { element.isClassPublic }
        val type: TypeMirror by lazy { element.asType() }

        val typeName by lazy { type.toTypeName() }
        val typeBlock: CodeBlock by lazy { CodeBlock.of("%T", typeName) }
        val klassBlock: CodeBlock by lazy { type.toKClassBlock(elements = elements) }

        val isKtFile by lazy {
            isClass && metaDataAnnotation?.get<Int>("k")?.let { KotlinClassHeader.Kind.getById(it) } == KotlinClassHeader.Kind.FILE_FACADE
        }

        private val header: KotlinClassHeader? by lazy {
            val annotation = metaDataAnnotation ?: return@lazy null

            val headerVisitor = ReadKotlinClassHeaderAnnotationVisitor()
            val visitor = headerVisitor.visitAnnotation(
                ClassId.topLevel(JvmAnnotationNames.METADATA_FQ_NAME),
                SourceElement.NO_SOURCE
            )!!

            annotation.elementValues.forEach { (executableElement, annotationValue) ->
                val value = annotationValue.value
                val valueType = executableElement.returnType
                val name = kotlin.reflect.jvm.internal.impl.name.Name.identifier(executableElement.simpleName.toString())
                when (valueType) {
                    is PrimitiveType -> visitor.visit(name, value)
                    is ArrayType -> {
                        @Suppress("UNCHECKED_CAST")
                        value as List<AnnotationValue>
                        when (valueType.componentType) {
                            is PrimitiveType -> visitor.visit(name, value.map { it.value as Int }.toTypedArray().toIntArray())
                            is DeclaredType -> visitor.visitArray(name)!!.apply {
                                value.forEach { visit(it.value as String) }
                                visitEnd()
                            }
                            else -> throw RuntimeException("executableElement=${executableElement.simpleName}, v=$value, (${value::class})")
                        }
                    }
                    else -> throw RuntimeException("Unexpected type: ${executableElement.returnType} (${executableElement.returnType::class}")
                }
                visitor.visitEnd()
            }

            headerVisitor.createHeader()!!
        }

        private val classData by lazy {
            header?.let { JvmProtoBufUtil.readClassDataFrom(it.data!!, it.strings!!) }
        }

        private val protoBufClass by lazy { classData?.second }

        private val kind: ProtoBuf.Class.Kind by lazy {
            when (element.kind) {
                ElementKind.ENUM -> ProtoBuf.Class.Kind.ENUM_CLASS
                ElementKind.ENUM_CONSTANT -> ProtoBuf.Class.Kind.ENUM_ENTRY
                ElementKind.ANNOTATION_TYPE -> ProtoBuf.Class.Kind.ANNOTATION_CLASS
                ElementKind.INTERFACE -> ProtoBuf.Class.Kind.INTERFACE
                ElementKind.CLASS -> {
                    if (protoBufClass != null) {
                        CLASS_KIND.get(protoBufClass!!.flags)!!
                    } else {
                        ProtoBuf.Class.Kind.CLASS
                    }
                }
                else -> throw RuntimeException("Unexpected element kind ${element.kind} for $element")
            }
        }

        val isKObject by lazy { isClass && !isKtFile && kind == ProtoBuf.Class.Kind.OBJECT }
        val isCompanionObject by lazy { isClass && !isKtFile && kind == ProtoBuf.Class.Kind.COMPANION_OBJECT }
    }
}
