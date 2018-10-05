[gh-pages](../index.md) / [com.nextfaze.devfun.inject](index.md) / [captureInstance](./capture-instance.md)

# captureInstance

`inline fun <reified T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> captureInstance(noinline instance: () -> `[`T`](capture-instance.md#T)`?): `[`InstanceProvider`](-instance-provider/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/inject/InstanceProvider.kt#L131)

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

Be wary of using a `typealias` as a type - the resultant function "type" itself is used at compile time.
e.g.

``` kotlin
typealias MyStringAlias = () -> String?
val provider1 = captureInstance<MyStringAlias> { ... }

typealias MyOtherAlias = () -> Type?
// will be triggered for MyStringAlias and MyOtherAlias since behind the scenes they are both kotlin.Function0<T>
val provider2 = captureInstance<MyOtherAlias> { ... }
```

**See Also**

[singletonInstance](singleton-instance.md)

[CapturingInstanceProvider](-capturing-instance-provider/index.md)

