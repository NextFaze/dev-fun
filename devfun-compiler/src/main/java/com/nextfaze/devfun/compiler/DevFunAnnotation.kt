package com.nextfaze.devfun.compiler

import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import kotlin.reflect.KCallable
import kotlin.reflect.KClass

internal class DevFunAnnotation(
    override val processingEnvironment: ProcessingEnvironment,
    private val annotation: AnnotationMirror,
    private val annotationTypeElement: TypeElement,
    private val devFunTypeElement: DevFunTypeElement
) : WithProcessingEnvironment {
    val value by lazy {
        annotation[DeveloperFunction::value] ?: when {
            annotationTypeElement != devFunTypeElement.element ->
                annotationTypeElement.getDefaultValueFor(DeveloperFunction::value)?.takeIf { it != devFunTypeElement.value }
            else -> null
        }
    }

    val category: DevCategory? by lazy {
        val localCategory = annotation[DeveloperFunction::category]
        val metaCategory = when {
            annotationTypeElement != devFunTypeElement.element ->
                annotationTypeElement.getDefaultValueFor(DeveloperFunction::category)?.takeIf {
                    warn("it=$it")
                    !it.isEqualTo(devFunTypeElement.category)
                }
            else -> null
        }
        when (localCategory) {
            null -> metaCategory?.let { DevCategory(it) }
            else -> DevCategory(localCategory, metaCategory)
        }
    }

    val requiresApi by lazy {
        annotation[DeveloperFunction::requiresApi] ?: when {
            annotationTypeElement != devFunTypeElement.element ->
                annotationTypeElement.getDefaultValueFor(DeveloperFunction::requiresApi)?.takeIf { it != devFunTypeElement.requiresApi }
            else -> null
        }
    }

    val transformer = annotation[DeveloperFunction::transformer, {
        when {
            annotationTypeElement != devFunTypeElement.element ->
                annotationTypeElement.getDefaultValueFor(DeveloperFunction::transformer)?.takeIf { it != devFunTypeElement.transformer }
            else -> null
        }
    }]
}

internal data class DevCategory(private val annotation: AnnotationMirror, private val superCat: AnnotationMirror? = null) {
    val value: String? get() = annotation[DeveloperCategory::value] ?: superCat?.let { it[DeveloperCategory::value] }
    val group: String? get() = annotation[DeveloperCategory::group] ?: superCat?.let { it[DeveloperCategory::group] }
    val order: Int? get() = annotation[DeveloperCategory::order] ?: superCat?.let { it[DeveloperCategory::order] }
}

internal class DevFunTypeElement(val element: TypeElement) {
    val value = element.getDefaultValueFor(DeveloperFunction::value)!!
    val category = element.getDefaultValueFor(DeveloperFunction::category)!!
    val requiresApi = element.getDefaultValueFor(DeveloperFunction::requiresApi)!!
    val transformer = element.getDefaultValueFor(DeveloperFunction::transformer)!!
}

private inline fun <reified T : Any> Element.getDefaultValueFor(callable: KCallable<T>) =
    (enclosedElements.singleOrNull { it.simpleName.toString() == callable.name } as ExecutableElement?)?.defaultValue?.value as T?

private inline fun <reified T : Annotation> Element.getDefaultValueFor(callable: KCallable<T>) =
    (enclosedElements.singleOrNull { it.simpleName.toString() == callable.name } as ExecutableElement?)?.defaultValue?.value as AnnotationMirror?

private inline fun <reified K : KClass<*>> Element.getDefaultValueFor(callable: KCallable<K>) =
    (enclosedElements.singleOrNull { it.simpleName.toString() == callable.name } as ExecutableElement?)?.defaultValue?.value as DeclaredType?

private fun AnnotationMirror.isEqualTo(other: AnnotationMirror): Boolean {
    val thisValues = this.elementValues
    val otherValues = other.elementValues

    if (thisValues.isEmpty() && otherValues.isEmpty()) return true
    if (thisValues.size != otherValues.size) return false

    val defaultKeys = thisValues.keys
    val devFunKeys = otherValues.keys
    if (defaultKeys != devFunKeys) return false

    return defaultKeys.all {
        thisValues[it]!!.value == otherValues[it]!!.value
    }
}
