package com.nextfaze.devfun.compiler.processing

import com.nextfaze.devfun.compiler.get
import com.nextfaze.devfun.compiler.isClassPublic
import com.nextfaze.devfun.compiler.toKClassBlock
import com.nextfaze.devfun.compiler.toTypeName
import com.squareup.kotlinpoet.CodeBlock
import kotlinx.metadata.ClassName
import kotlinx.metadata.Flag
import kotlinx.metadata.Flags
import kotlinx.metadata.KmClassVisitor
import kotlinx.metadata.jvm.KotlinClassHeader
import kotlinx.metadata.jvm.KotlinClassMetadata
import javax.inject.Inject
import javax.inject.Singleton
import javax.lang.model.element.Name
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

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

        val isKtFile by lazy { isClass && metaDataAnnotation?.get<Int>("k") == KotlinClassHeader.FILE_FACADE_KIND }

        private val header: KotlinClassHeader? by lazy {
            val annotation = metaDataAnnotation ?: return@lazy null
            KotlinClassHeader(
                kind = annotation["k"],
                metadataVersion = annotation["mv"],
                bytecodeVersion = annotation["bv"],
                data1 = annotation["d1"],
                data2 = annotation["d2"],
                extraString = annotation["xs"],
                packageName = annotation["pn"],
                extraInt = annotation["xi"]
            )
        }

        private val metadata by lazy { header?.let { KotlinClassMetadata.read(it) } }

        val isKObject by lazy {
            var flag = false
            val metadata = metadata
            if (isClass && !isKtFile && metadata is KotlinClassMetadata.Class) {
                metadata.accept(object : KmClassVisitor() {
                    override fun visit(flags: Flags, name: ClassName) {
                        flag = Flag.Class.IS_OBJECT(flags)
                    }
                })
            }
            return@lazy flag
        }

        val isCompanionObject by lazy {
            var flag = false
            val metadata = metadata
            if (isClass && !isKtFile && metadata is KotlinClassMetadata.Class) {
                metadata.accept(object : KmClassVisitor() {
                    override fun visit(flags: Flags, name: ClassName) {
                        flag = Flag.Class.IS_COMPANION_OBJECT(flags)
                    }
                })
            }
            return@lazy flag
        }
    }
}
