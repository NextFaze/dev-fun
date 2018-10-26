[gh-pages](../index.md) / [com.nextfaze.devfun.inject](index.md) / [singletonInstance](./singleton-instance.md)

# singletonInstance

`inline fun <reified T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> singletonInstance(noinline instance: () -> `[`T`](singleton-instance.md#T)`?): `[`InstanceProvider`](-instance-provider/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/inject/InstanceProvider.kt#L161)

Utility function to provide a single instance of some type.

e.g.

``` kotlin
class SomeType : BaseType

val provider = singletonInstance { SomeType() } // triggers for SomeType or BaseType (result of invocation is saved)
```

If you want to reduce the type range then specify its base type manually:

``` kotlin
val provider = singletonInstance<BaseType> { SomeType() } // triggers only for BaseType (result of invocation is saved)
```

Be wary of using a `typealias` as a type - the resultant function "type" itself is used at compile time.
e.g.

``` kotlin
typealias MyStringAlias = () -> String?
val provider1 = singletonInstance<MyStringAlias> { ... }

typealias MyOtherAlias = () -> Type?
// will be triggered for MyStringAlias and MyOtherAlias since behind the scenes they are both kotlin.Function0<T>
val provider2 = singletonInstance<MyOtherAlias> { ... }
```

**See Also**

[captureInstance](capture-instance.md)

[CapturingInstanceProvider](-capturing-instance-provider/index.md)

