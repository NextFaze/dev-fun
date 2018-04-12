@file:Suppress("UNUSED_PARAMETER", "unused", "PackageName")

package tested.developer_annotation

import com.nextfaze.devfun.annotations.DeveloperAnnotation

annotation class ExecutableReferences

@Retention(AnnotationRetention.BINARY)
@DeveloperAnnotation
@Target(AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.FUNCTION)
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
