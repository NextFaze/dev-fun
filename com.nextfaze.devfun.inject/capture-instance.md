[gh-pages](../index.md) / [com.nextfaze.devfun.inject](index.md) / [captureInstance](.)

# captureInstance

`inline fun <reified T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> captureInstance(noinline instance: () -> T?): `[`InstanceProvider`](-instance-provider/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/inject/InstanceProvider.kt#L106)

Utility function to capture an instance of an object.

e.g.

``` kotlin
class SomeType : BaseType

val provider = captureInstance { someObject.someType } // triggers for SomeType or BaseType
```

If you want to reduce the type range then specify its base type manually:

``` kotlin
val provider = captureInstance<BaseType> { someObject.someType } // triggers only for BaseType
```

**See Also**

[CapturingInstanceProvider](-capturing-instance-provider/index.md)

