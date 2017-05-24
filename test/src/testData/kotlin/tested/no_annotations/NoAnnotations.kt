@file:Suppress("unused")

package tested.no_annotations

annotation class NoAnnotations

class na_SomeClass {
    fun someFun() = Unit
}

private class na_AnotherClass {
    fun AnotherFun() = Unit
}

internal class na_Yac
