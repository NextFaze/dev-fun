package com.nextfaze.devfun.compiler.dagger.spi

import com.google.auto.service.AutoService
import com.nextfaze.devfun.compiler.joiner
import dagger.model.BindingGraph
import dagger.model.DependencyRequest
import dagger.spi.BindingGraphPlugin
import dagger.spi.DiagnosticReporter
import java.util.Optional
import javax.lang.model.element.Element
import javax.lang.model.element.QualifiedNameable
import javax.tools.Diagnostic.Kind.WARNING
import javax.tools.StandardLocation

@AutoService(BindingGraphPlugin::class)
internal class PseudoJsonBindingGraphPlugin : BaseBindingGraphPlugin(enabled = true) {
    private val indent = "  "

    private fun Optional<out Element>.toJson(): String? = valueOrNull?.let {
        if (it is QualifiedNameable) "'${it.qualifiedName}'" else "'${it.enclosingElement}.$it'"
    }

    private fun DependencyRequest.toJson(isNamed: Boolean = false, extraIndent: String = if (isNamed) indent else "") = """
        |{
        |$extraIndent${indent}key: '${key()}',
        |$extraIndent${indent}kind: ${kind()},
        |$extraIndent${indent}element: ${requestElement().toJson()},
        |$extraIndent${indent}isNullable: $isNullable
        |$extraIndent}
    """.trimMargin()

    private fun BindingGraph.ComponentNode.toJson(): String {
        val componentPath = componentPath()
        val isRoot = componentPath.atRoot()
        return """
            |{
            |${indent}name: '${componentPath.currentComponent()}',
            |${indent}isRoot: $isRoot,
            |${indent}parent: ${if (isRoot) "null" else "'${componentPath.parentComponent()}'"},
            |${indent}path: '$componentPath',
            |${scopes().toJsonArray("scopes") { it.toString() }}
            |${entryPoints().toJsonArray("entryPoints", comma = "") { it.toJson() }}
            |}
        """.trimMargin()
    }

    private fun BindingGraph.MaybeBindingNode.toJson(): String {
        val binding = maybeBinding().valueOrNull
        val componentPath = componentPath()
        return """
            |{
            |${indent}key: '${key()}',
            |${indent}kind: ${binding?.kind()},
            |${indent}bindingElement: ${binding?.bindingElement()?.toJson()},
            |${indent}isNullable: ${binding?.isNullable},
            |${indent}scope: ${binding?.scope()?.valueOrNull},
            |${indent}path: '$componentPath',
            |${indent}isProduction: ${binding?.isProduction},
            |${indent}contributingModule: ${binding?.contributingModule()?.toJson()},
            |${binding?.dependencies()?.toJsonArray("dependencies", comma = "") { it.toJson() } ?: "${indent}dependencies: null"}
            |}
        """.trimMargin()
    }

    private fun BindingGraph.DependencyEdge.toJson() = """
        |{
        |${indent}isEntryPoint: $isEntryPoint,
        |${indent}dependencyRequest: ${dependencyRequest().toJson(true)}
        |}
    """.trimMargin()

    private inline fun <reified T : Any> Collection<T>.toJsonArray(
        name: String,
        comma: String = ",",
        crossinline transform: (T) -> String
    ) = joiner(",\n", prefix = "$indent$name: [\n", postfix = "\n$indent]$comma", ifEmpty = "$indent$name: []$comma") {
        transform(it).prependIndent("$indent$indent")
    }

    override fun visitBindingGraph(bindingGraph: BindingGraph, diagnosticReporter: DiagnosticReporter) {
        filer.createResource(StandardLocation.SOURCE_OUTPUT, "", "dagger-graph.json").apply {
            openWriter().use { writer ->
                writer.appendln("{")

                writer.appendln(bindingGraph.componentNodes().toJsonArray("components") { it.toJson() })
                writer.appendln(bindingGraph.bindingNodes().toJsonArray("bindingNodes") { it.toJson() })
                writer.appendln(bindingGraph.dependencyEdges().toJsonArray("dependencyEdges") { it.toJson() })
                writer.appendln(bindingGraph.entryPointBindingNodes().toJsonArray("entryPointBindingNodes") { it.toJson() })
                writer.appendln(bindingGraph.entryPointEdges().toJsonArray("entryPointEdges") { it.toJson() })
                writer.appendln(bindingGraph.missingBindingNodes().toJsonArray("missingBindingNodes", comma = "") { it.toJson() })

                writer.appendln("}")
            }
        }

        val rootComponentNode = bindingGraph.rootComponentNode()
        diagnosticReporter.reportComponent(
            WARNING,
            rootComponentNode,
            "entryPoints=${rootComponentNode.entryPoints()}, scopes=${rootComponentNode.scopes()}, componentPath=${rootComponentNode.componentPath()}\n" +
                    "==================================================================="
        )
        bindingGraph.bindingNodes().forEach {
            diagnosticReporter.reportBinding(
                WARNING,
                it,
                "\nit=$it\nbinding=${it.binding()}\ncomponentPath=${it.componentPath()}\n==================================================================="
            )
        }
    }
}
