package com.nextfaze.devfun.compiler.dagger.spi

import com.google.auto.service.AutoService
import com.nextfaze.devfun.compiler.META_INF_SERVICES
import com.nextfaze.devfun.compiler.TypeNames
import com.nextfaze.devfun.compiler.stackTraceAsString
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName
import dagger.model.BindingGraph
import dagger.spi.BindingGraphPlugin
import dagger.spi.DiagnosticReporter
import java.io.File
import java.io.IOException
import javax.tools.StandardLocation

private const val CLASS_NAME = "DaggerGraph"
private const val FILE_NAME = "$CLASS_NAME.kt"

@AutoService(BindingGraphPlugin::class)
internal class GraphRecorderBindingGraphPlugin : BaseBindingGraphPlugin() {
    // technically I believe visitBindingGraph can be called more than once,
    // but for now we're only going to support a single invocation
    private var doneOnce = false

    private val daggerSpiGraphName = ClassName.bestGuess("com.nextfaze.devfun.inject.dagger.DaggerSpiGraph")
    private val componentName = ClassName.bestGuess("com.nextfaze.devfun.inject.dagger.Component")
    private val bindingNodeName = ClassName.bestGuess("com.nextfaze.devfun.inject.dagger.BindingNode")
    private val nodeName = ClassName.bestGuess("com.nextfaze.devfun.inject.dagger.Node")

    private val daggerComponentDefinitionName = ClassName.bestGuess("DaggerComponent")

//    private val daggerComponentDefinition by lazy {
//        val stringList = List::class.asTypeName().parameterizedBy(TypeNames.string)
//        val nodeList = List::class.asTypeName().parameterizedBy(nodeName)
//
//        TypeSpec.classBuilder(daggerComponentDefinitionName)
//            .addSuperinterface(componentName)
//            .addModifiers(KModifier.PRIVATE, KModifier.DATA)
//            .primaryConstructor(
//                FunSpec.constructorBuilder()
//                    .addParameter(ParameterSpec.builder("name", TypeNames.string, KModifier.OVERRIDE).build())
//                    .addParameter(ParameterSpec.builder("parent", TypeNames.string.asNullable(), KModifier.OVERRIDE).build())
//                    .addParameter(ParameterSpec.builder("scopes", stringList, KModifier.OVERRIDE).build())
//                    .addParameter(ParameterSpec.builder("entryPoints", nodeList, KModifier.OVERRIDE).build())
//                    .build()
//            )
//            .addProperty(PropertySpec.builder("name", TypeNames.string).initializer("name").build())
//            .addProperty(PropertySpec.builder("parent", TypeNames.string).initializer("parent").build())
//            .addProperty(PropertySpec.builder("scopes", stringList).initializer("scopes").build())
//            .addProperty(PropertySpec.builder("entryPoints", nodeList).initializer("entryPoints").build())
//            .addFunction(
//                FunSpec.builder("equals")
//                    .returns(Boolean::class)
//                    .addParameter("other", ANY.asNullable())
//                    .addCode("other is Component && other.name == name")
//                    .build()
//            )
//            .addFunction(
//                FunSpec.builder("hashCode")
//                    .returns(Int::class)
//                    .addCode("name.hashCode()")
//                    .build()
//            )
//            .build()
//    }

    private val log by lazy { logging.create(this) }

    override fun visitBindingGraph(bindingGraph: BindingGraph, diagnosticReporter: DiagnosticReporter) {
        if (doneOnce) return
        doneOnce = true

        val components = bindingGraph.componentNodes().map { componentNode ->
            val path = componentNode.componentPath()
            val parent = if (path.atRoot()) null else path.parent()
            val entryPoints = componentNode.entryPoints().map {
                CodeBlock.of(
                    "DaggerNode(\nkey = %V,\nkind = %V,\nelement = %V,\nisNullable = %V\n)",
                    it.key().toString(),
                    it.kind().toString(),
                    it.requestElement().valueOrNull?.toString(),
                    it.isNullable
                )
            }

            CodeBlock.of(
                "DaggerComponent(\nname = %V,\nparent = %V,\nscopes = %L,\nentryPoints = %L\n)",
                path.currentComponent().toString(),
                parent?.toString(),
                componentNode.scopes().map { CodeBlock.of("%V", it.toString()) }.toListOfBlock(TypeNames.string),
                entryPoints.toListOfBlock(nodeName)
            )
        }

        val bindingNodes = bindingGraph.bindingNodes().map {
            val binding = it.binding()
            val dependencies = binding.dependencies().map { dep ->
                CodeBlock.of(
                    "DaggerNode(\nkey = %V,\nkind = %V,\nelement = %V,\nisNullable = %V\n)",
                    dep.key().toString(),
                    dep.kind().toString(),
                    dep.requestElement().valueOrNull?.toString(),
                    dep.isNullable
                )
            }
            CodeBlock.of(
                "DaggerBindingNode(\nkey = %V,\nkind = %V,\nelement = %V,\nisNullable = %V,\nscope = %V,\nmodule = %V,\ndependencies = %L\n)",
                binding.key().toString(),
                binding.kind().toString(),
                binding.bindingElement().valueOrNull?.toString(),
                binding.isNullable,
                binding.scope().valueOrNull?.toString(),
                binding.contributingModule().valueOrNull?.toString(),
                dependencies.toListOfBlock(nodeName)
            )
        }

        val componentList = List::class.asTypeName().parameterizedBy(componentName)
        val bindingNodeList = List::class.asTypeName().parameterizedBy(bindingNodeName)

        val fileSpec = FileSpec.builder(ctx.pkg, FILE_NAME)
            .addType(
                TypeSpec.classBuilder("DaggerGraph")
                    .addSuperinterface(daggerSpiGraphName)
                    .addProperty(
                        PropertySpec.builder("components", componentList)
                            .addModifiers(KModifier.OVERRIDE)
                            .initializer("%L", components.toListOfBlock(componentName))
                            .build()
                    )
                    .addProperty(
                        PropertySpec.builder("bindingNodes", bindingNodeList)
                            .addModifiers(KModifier.OVERRIDE)
                            .initializer("%L", bindingNodes.toListOfBlock(bindingNodeName))
                            .build()
                    )
                    .build()
            )
            .build()

        try {
            filer.createResource(StandardLocation.SOURCE_OUTPUT, fileSpec.packageName, fileSpec.name).openWriter().use {
                it.write(fileSpec.toString())
                it.write("\n")
                it.write(dataClasses)
            }

            writeServiceFile()
        } catch (e: IOException) {
            log.error { "Failed to write source file:\n${e.stackTraceAsString}" }
        }
    }

    private fun writeServiceFile() {
        val servicesPath = "$META_INF_SERVICES/$daggerSpiGraphName"
        val servicesText = "${ctx.pkg}.$CLASS_NAME\n"

        filer.createResource(StandardLocation.CLASS_OUTPUT, "", servicesPath).apply {
            log.note { "Write services file to ${File(toUri()).canonicalPath}" }
            openWriter().use { it.write(servicesText) }
        }
    }

    // TODO lazy
    private val dataClasses = """
private data class DaggerComponent(
    override val name: String,
    override val parent: String?,
    override val scopes: List<String>,
    override val entryPoints: List<Node>
) : Component {
    override fun equals(other: Any?) = other is Component && other.name == name
    override fun hashCode() = name.hashCode()
}

private data class DaggerBindingNode(
    override val key: String,
    override val kind: String,
    override val element: String?,
    override val isNullable: Boolean,
    override val scope: String?,
    override val module: String?,
    override val dependencies: List<Node>
) : BindingNode {
    override fun equals(other: Any?) = other is BindingNode && other.key == key
    override fun hashCode() = key.hashCode()
}

private data class DaggerNode(
    override val key: String,
    override val kind: String,
    override val element: String?,
    override val isNullable: Boolean
) : Node {
    override fun equals(other: Any?) = other is BindingNode && other.key == key
    override fun hashCode() = key.hashCode()
}""".trimIndent()
}

internal fun Collection<*>.toListOfBlock(type: TypeName) =
    CodeBlock.of("listOf<%T>(${",\n%L".repeat(size).drop(1)})", type, *toTypedArray())
