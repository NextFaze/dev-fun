package com.nextfaze.devfun.compiler.dagger.spi

import com.google.auto.service.AutoService
import com.nextfaze.devfun.compiler.joiner
import dagger.model.BindingGraph
import dagger.model.Key
import dagger.spi.BindingGraphPlugin
import dagger.spi.DiagnosticReporter
import javax.tools.StandardLocation

@AutoService(BindingGraphPlugin::class)
internal class UmlBindingGraphPlugin : BaseBindingGraphPlugin(enabled = false) {
    private val invalidKeyChars = listOf(",", "<", ">", "@", "$", " ", "#", ".")

    private val Key.umlKey: String
        get() {
            var str = this.toString()
            invalidKeyChars.forEach {
                str = str.replace(it, "")
            }
            return str
        }

    private val stripPackages = listOf(
        "java.util.",
        "kotlin.jvm.functions.",
        "kotlin.",
        "android.app.",
        "android.content.",
        "com.nextfaze.devfun."
    )

    private val Key.umlTitle: String
        get() {
            var str = this.toString()
            stripPackages.forEach {
                str = str.replace(it, "")
            }
            return str
        }

    override fun visitBindingGraph(bindingGraph: BindingGraph, diagnosticReporter: DiagnosticReporter) {
        writeBindingNodesUml(bindingGraph.bindingNodes())
    }

    private fun writeBindingNodesUml(bindingNodes: Collection<BindingGraph.BindingNode>) {
        filer.createResource(StandardLocation.SOURCE_OUTPUT, "", "bindingGraph.puml").apply {
            openWriter().use { writer ->
                writer.appendln(
                    """
                    @startuml
                    skinparam componentStyle uml2

                    title Dagger Graph - Binding Nodes
                """.trimIndent()
                )

                fun Collection<BindingGraph.BindingNode>.writeNodes(kind: String? = null) {
                    if (kind != null) {
                        writer.appendln("package \"$kind\" {")
                    }
                    val indent = if (kind != null) "    " else ""
                    forEach {
                        val binding = it.binding()
                        val dependencies = mutableListOf<String>()

                        binding.dependencies().forEach {
                            dependencies += """
                            |    $indent${binding.key().umlKey} <--- ${it.key().umlKey}
                        """.trim()
                        }

                        writer.appendln(
                            """
                        |${indent}node "${binding.key().umlTitle}" as ${binding.key().umlKey} {
                        ${dependencies.joiner("\n", postfix = "\n")}
                        |$indent}
                        |
                    """.trimMargin()
                        )
                    }
                    if (kind != null) {
                        writer.appendln("}")
                    }
                }

//                bindingNodes.groupBy { it.binding().kind() }.forEach { (bindingKind, list) ->
//                    list.writeNodes(bindingKind.toString())
//                }

//                bindingNodes.filter { it.binding().kind() == BindingKind.MEMBERS_INJECTION }.writeNodes("MEMBERS_INJECTION")
//                bindingNodes.filter { it.binding().kind() != BindingKind.MEMBERS_INJECTION }.writeNodes()

                bindingNodes.writeNodes()

                writer.appendln("@enduml")
            }
        }
    }
}
