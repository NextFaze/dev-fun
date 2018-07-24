package com.nextfaze.devfun.compiler

import javax.inject.Inject
import javax.inject.Singleton
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

private typealias TypeVar = String.(TypeElement) -> String
private typealias ExeVar = String.(ExecutableElement) -> String
private typealias VarVar = String.(VariableElement) -> String

@Singleton
internal class StringPreprocessor @Inject constructor() {

    private val typeVars = listOf<TypeVar>(
        { it -> replace("%CLASS_SN%", it.simpleName.toString()) },
        { it -> replace("%CLASS_QN%", it.qualifiedName.toString()) }
    )
    private val exeVars = listOf<ExeVar>(
        { it -> replace("%FUN_SN%", it.simpleName.toString()) },
        { it -> replace("%FUN_QN%", it.toString()) }
    )
    private val varVars = listOf<VarVar>(
        { it -> replace("%VAR_SN%", it.simpleName.toString()) },
        { it -> replace("%VAR_QN%", it.toString()) }
    )

    fun run(input: String, element: Element?): String {
        if (element == null || !input.contains('%')) return input

        val executableElement = element as? ExecutableElement
        val varElement = element as? VariableElement
        val typeElement = element as? TypeElement
                ?: executableElement?.enclosingElement as? TypeElement
                ?: varElement?.enclosingElement as? TypeElement

        var str = input

        if (typeElement != null) {
            typeVars.forEach { replace ->
                if (!str.contains('%')) return@forEach
                str = replace(str, typeElement)
            }
        }
        if (executableElement != null) {
            exeVars.forEach { replace ->
                if (!str.contains('%')) return@forEach
                str = replace(str, executableElement)
            }
        }
        if (varElement != null) {
            varVars.forEach { replace ->
                if (!str.contains('%')) return@forEach
                str = replace(str, varElement)
            }
        }

        return str
    }
}
