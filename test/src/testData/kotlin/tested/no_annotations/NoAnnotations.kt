@file:Suppress("unused", "ClassName", "PackageName", "TestFunctionName")

package tested.no_annotations

annotation class NoAnnotations

class na_SomeClass {
    fun someFun() = Unit
}

private class na_AnotherClass {
    fun AnotherFun() = Unit
}

internal class na_Yac
