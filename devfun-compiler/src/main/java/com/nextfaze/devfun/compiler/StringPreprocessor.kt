package com.nextfaze.devfun.compiler

import javax.inject.Inject
import javax.inject.Singleton
import javax.lang.model.element.TypeElement

private typealias ProcessingVar = String.(TypeElement) -> String

@Singleton
internal class StringPreprocessor @Inject constructor() {

    private val processingVars = listOf<ProcessingVar>(
        { it -> replace("%CLASS_QN%", it.qualifiedName.toString()) },
        { it -> replace("%CLASS_SN%", it.simpleName.toString()) }
    )

    fun run(input: String, typeElement: TypeElement): String {
        var str = input
        processingVars.forEach { replace ->
            str = replace(str, typeElement)
        }
        return str
    }
}
