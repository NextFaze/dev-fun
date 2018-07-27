package com.nextfaze.devfun.compiler

import com.nextfaze.devfun.annotations.DeveloperAnnotation
import com.nextfaze.devfun.annotations.DeveloperCategory
import com.nextfaze.devfun.annotations.DeveloperFunction
import javax.inject.Inject
import javax.inject.Singleton
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.util.Elements
import kotlin.reflect.KCallable
import kotlin.reflect.KClass

@Singleton
internal class AnnotationElements @Inject constructor(elements: Elements) {
    private val devFunElement = DevFunTypeElement(elements.getTypeElement(DeveloperFunction::class.qualifiedName))
    private val devCatElement = DevCatTypeElement(elements.getTypeElement(DeveloperCategory::class.qualifiedName))

    fun createDevFunAnnotation(
        annotation: AnnotationMirror,
        annotationTypeElement: TypeElement = devFunElement.element
    ) = DevFunAnnotation(annotation, annotationTypeElement, devFunElement, devCatElement)

    fun createDevCatAnnotation(
        annotation: AnnotationMirror,
        annotationTypeElement: TypeElement = devCatElement.element
    ) = DevFunCategory(annotation, annotationTypeElement, devCatElement)
}

internal class DevFunAnnotation(
    private val annotation: AnnotationMirror,
    private val annotationTypeElement: TypeElement,
    private val devFunTypeElement: DevFunTypeElement,
    private val devCatTypeElement: DevCatTypeElement
) {
    val value by lazy {
        annotation[DeveloperFunction::value] ?: when {
            annotationTypeElement != devFunTypeElement.element ->
                annotationTypeElement.getDefaultValueFor(DeveloperFunction::value)?.takeIf { it != devFunTypeElement.value }
            else -> null
        }
    }

    val category: DevFunCategory? by lazy {
        val localCategory = annotation[DeveloperFunction::category]
        val metaCategory = when {
            annotationTypeElement != devFunTypeElement.element ->
                annotationTypeElement.getDefaultValueFor(DeveloperFunction::category)?.takeIf {
                    !it.isEqualTo(devFunTypeElement.category)
                }
            else -> null
        }
        when (localCategory) {
            null -> metaCategory?.let { DevFunCategory(it, devCatTypeElement.element, devCatTypeElement) }
            else -> DevFunCategory(localCategory, devCatTypeElement.element, devCatTypeElement, metaCategory)
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

internal data class DevFunCategory(
    private val annotation: AnnotationMirror,
    private val annotationTypeElement: TypeElement,
    private val devCatTypeElement: DevCatTypeElement,
    private val superCat: AnnotationMirror? = null
) {
    val value by lazy {
        annotation[DeveloperCategory::value] ?: superCat?.let { it[DeveloperCategory::value] } ?: when {
            annotationTypeElement != devCatTypeElement.element ->
                annotationTypeElement.getDefaultValueFor(DeveloperCategory::value)?.takeIf { it != devCatTypeElement.value }
            else -> null
        }
    }

    val group by lazy {
        annotation[DeveloperCategory::group] ?: superCat?.let { it[DeveloperCategory::group] } ?: when {
            annotationTypeElement != devCatTypeElement.element ->
                annotationTypeElement.getDefaultValueFor(DeveloperCategory::group)?.takeIf { it != devCatTypeElement.group }
            else -> null
        }
    }

    val order by lazy {
        annotation[DeveloperCategory::order] ?: superCat?.let { it[DeveloperCategory::order] } ?: when {
            annotationTypeElement != devCatTypeElement.element ->
                annotationTypeElement.getDefaultValueFor(DeveloperCategory::order)?.takeIf { it != devCatTypeElement.order }
            else -> null
        }
    }
}

internal class DevFunTypeElement(val element: TypeElement) {
    val value = element.getDefaultValueFor(DeveloperFunction::value)!!
    val category = element.getDefaultValueFor(DeveloperFunction::category)!!
    val requiresApi = element.getDefaultValueFor(DeveloperFunction::requiresApi)!!
    val transformer = element.getDefaultValueFor(DeveloperFunction::transformer)!!
}

internal class DevCatTypeElement(val element: TypeElement) {
    val value = element.getDefaultValueFor(DeveloperCategory::value)!!
    val group = element.getDefaultValueFor(DeveloperCategory::group)!!
    val order = element.getDefaultValueFor(DeveloperCategory::order)!!
}

internal class DevAnnotationTypeElement(val element: TypeElement) {
    val developerFunction = element.getDefaultValueFor(DeveloperAnnotation::developerFunction)!!
    val developerCategory = element.getDefaultValueFor(DeveloperAnnotation::developerCategory)!!
    val developerReference = element.getDefaultValueFor(DeveloperAnnotation::developerReference)!!
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
