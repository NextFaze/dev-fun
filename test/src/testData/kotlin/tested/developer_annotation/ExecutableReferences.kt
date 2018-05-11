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
    @ExecutableElementTagging fun someFunction() = Unit
    @ExecutableElementTagging private fun somePrivateFunction(str: String) = Unit
}

private class SomePrivateClass {
    @ExecutableElementTagging fun someFunction() = Unit
    @ExecutableElementTagging private fun somePrivateFunction(str: String) = Unit
}

object SomeObject {
    @ExecutableElementTagging fun someFunction() = Unit
    @ExecutableElementTagging private fun somePrivateFunction(str: String) = Unit
}

private object SomePrivateObject {
    @ExecutableElementTagging fun someFunction() = Unit
    @ExecutableElementTagging private fun somePrivateFunction(str: String) = Unit
}

class SomeClassWithCompanionObject {
    companion object {
        @ExecutableElementTagging fun someFunction() = Unit
        @ExecutableElementTagging private fun somePrivateFunction(str: String) = Unit
    }
}

private class SomePrivateClassWithCompanionObject {
    companion object {
        @ExecutableElementTagging fun someFunction() = Unit
        @ExecutableElementTagging private fun somePrivateFunction(str: String) = Unit
    }
}

@ExecutableElementTagging fun someTopLevelFunction() = Unit
@ExecutableElementTagging private fun somePrivateTopLevelFunction() = Unit

@ExecutableElementTagging fun String.someTopLevelExtensionFunction() = Unit
@ExecutableElementTagging private fun String.somePrivateTopLevelExtensionFunction() = Unit
