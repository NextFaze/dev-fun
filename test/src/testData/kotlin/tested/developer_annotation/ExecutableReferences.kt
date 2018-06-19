@file:Suppress("UNUSED_PARAMETER", "unused", "PackageName")

package tested.developer_annotation

import com.nextfaze.devfun.annotations.DeveloperAnnotation
import kotlin.annotation.AnnotationRetention.BINARY
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.PROPERTY_GETTER

annotation class ExecutableReferences

@Retention(BINARY)
@DeveloperAnnotation
@Target(PROPERTY_GETTER, FUNCTION)
annotation class ExecutableElementTagging

class SomeClass {
    @ExecutableElementTagging fun someFunction() = true
    @ExecutableElementTagging private fun somePrivateFunction(str: String) = true
}

private class SomePrivateClass {
    @ExecutableElementTagging fun someFunction() = true
    @ExecutableElementTagging private fun somePrivateFunction(str: String) = true
}

object SomeObject {
    @ExecutableElementTagging fun someFunction() = true
    @ExecutableElementTagging private fun somePrivateFunction(str: String) = true
}

private object SomePrivateObject {
    @ExecutableElementTagging fun someFunction() = true
    @ExecutableElementTagging private fun somePrivateFunction(str: String) = true
}

class SomeClassWithCompanionObject {
    companion object {
        @ExecutableElementTagging fun someFunction() = true
        @ExecutableElementTagging private fun somePrivateFunction(str: String) = true
    }
}

private class SomePrivateClassWithCompanionObject {
    companion object SomeName {
        @ExecutableElementTagging fun someFunction() = true
        @ExecutableElementTagging private fun somePrivateFunction(str: String) = true
    }
}

@ExecutableElementTagging fun someTopLevelFunction() = true
@ExecutableElementTagging private fun somePrivateTopLevelFunction() = true

@ExecutableElementTagging fun String.someTopLevelExtensionFunction() = true
@ExecutableElementTagging private fun String.somePrivateTopLevelExtensionFunction() = true
