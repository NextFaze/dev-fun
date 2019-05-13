package com.nextfaze.devfun.test.kotlin

import org.jetbrains.kotlin.base.kapt3.KaptOptions
import org.jetbrains.kotlin.cli.common.messages.MessageCollector
import org.jetbrains.kotlin.codegen.AbstractClassBuilder
import org.jetbrains.kotlin.codegen.ClassBuilder
import org.jetbrains.kotlin.codegen.ClassBuilderFactory
import org.jetbrains.kotlin.codegen.ClassBuilderMode
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.kapt3.AbstractKapt3Extension
import org.jetbrains.kotlin.kapt3.KaptContextForStubGeneration
import org.jetbrains.kotlin.kapt3.base.KaptContext
import org.jetbrains.kotlin.kapt3.base.LoadedProcessors
import org.jetbrains.kotlin.kapt3.base.incremental.IncrementalProcessor
import org.jetbrains.kotlin.kapt3.javac.KaptJavaFileObject
import org.jetbrains.kotlin.kapt3.stubs.ClassFileToSourceStubConverter
import org.jetbrains.kotlin.kapt3.util.MessageCollectorBackedKaptLogger
import org.jetbrains.kotlin.resolve.jvm.diagnostics.JvmDeclarationOrigin
import org.jetbrains.org.objectweb.asm.ClassWriter
import org.jetbrains.org.objectweb.asm.FieldVisitor
import org.jetbrains.org.objectweb.asm.MethodVisitor
import org.jetbrains.org.objectweb.asm.tree.ClassNode
import org.jetbrains.org.objectweb.asm.tree.FieldNode
import org.jetbrains.org.objectweb.asm.tree.MethodNode
import javax.annotation.processing.Processor

internal class Kapt3ExtensionForTests(
    options: KaptOptions,
    private val processors: List<IncrementalProcessor>
) : AbstractKapt3Extension(options, MessageCollectorBackedKaptLogger(options.flags), CompilerConfiguration.EMPTY) {
    internal var savedStubs: List<String>? = null
    internal var savedBindings: Map<String, KaptJavaFileObject>? = null

    override fun loadProcessors() = LoadedProcessors(processors, Kapt3ExtensionForTests::class.java.classLoader!!)

    override fun saveStubs(kaptContext: KaptContext, stubs: List<ClassFileToSourceStubConverter.KaptStub>) {
        if (savedStubs != null) {
            error("Stubs are already saved")
        }

        savedStubs = stubs.map { it.toString() }
        super.saveStubs(kaptContext, stubs)
    }

    override fun saveIncrementalData(
        kaptContext: KaptContextForStubGeneration,
        messageCollector: MessageCollector,
        converter: ClassFileToSourceStubConverter
    ) {
        if (savedBindings != null) {
            error("Bindings are already saved")
        }

        savedBindings = converter.bindings
        super.saveIncrementalData(kaptContext, messageCollector, converter)
    }
}

internal class Kapt3BuilderFactory : ClassBuilderFactory {
    private val compiledClasses = mutableListOf<ClassNode>()
    internal val origins = mutableMapOf<Any, JvmDeclarationOrigin>()

    override fun getClassBuilderMode(): ClassBuilderMode = ClassBuilderMode.KAPT3

    override fun newClassBuilder(origin: JvmDeclarationOrigin): AbstractClassBuilder.Concrete {
        val classNode = ClassNode()
        compiledClasses += classNode
        origins[classNode] = origin
        return Kapt3ClassBuilder(classNode)
    }

    private inner class Kapt3ClassBuilder(val classNode: ClassNode) : AbstractClassBuilder.Concrete(classNode) {
        override fun newField(
            origin: JvmDeclarationOrigin,
            access: Int,
            name: String,
            desc: String,
            signature: String?,
            value: Any?
        ): FieldVisitor {
//            val flags = Flags.asFlagSet(access.toLong())
//            log.i { "newField: origin=$origin, access=$access (flags=$flags), name=$name, desc=$desc, signature=$signature, value=$value" }
            val fieldNode = super.newField(origin, access, name, desc, signature, value) as FieldNode
            origins[fieldNode] = origin
            return fieldNode
        }

        override fun newMethod(
            origin: JvmDeclarationOrigin,
            access: Int,
            name: String,
            desc: String,
            signature: String?,
            exceptions: Array<out String>?
        ): MethodVisitor {
//            val flags = Flags.asFlagSet(access.toLong())
//            log.i { "newMethod: origin=$origin, access=$access (flags=$flags), name=$name, desc=$desc, signature=$signature, exceptions=$exceptions" }
            val methodNode = super.newMethod(origin, access, name, desc, signature, exceptions) as MethodNode
            origins[methodNode] = origin
            return methodNode
        }
    }

    override fun asBytes(builder: ClassBuilder): ByteArray =
        ClassWriter(0).also { (builder as Kapt3ClassBuilder).classNode.accept(it) }.toByteArray()

    override fun asText(builder: ClassBuilder) = throw UnsupportedOperationException()

    override fun close() {}
}
