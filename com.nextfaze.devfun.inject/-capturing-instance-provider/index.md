[gh-pages](../../index.md) / [com.nextfaze.devfun.inject](../index.md) / [CapturingInstanceProvider](.)

# CapturingInstanceProvider

`class CapturingInstanceProvider<out T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> : `[`InstanceProvider`](../-instance-provider/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/inject/InstanceProvider.kt#L77)

An instance provider that requests an instance of a class from a captured lambda.

Be aware of leaks! The lambda could implicitly hold a local `this` reference.

**See Also**

[captureInstance](../capture-instance.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `CapturingInstanceProvider(instanceClass: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<T>, instance: () -> T?)`<br>An instance provider that requests an instance of a class from a captured lambda. |

### Functions

| Name | Summary |
|---|---|
| [get](get.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> get(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out T>): T?`<br>Try to get an instance of some [clazz](get.md#com.nextfaze.devfun.inject.CapturingInstanceProvider$get(kotlin.reflect.KClass((com.nextfaze.devfun.inject.CapturingInstanceProvider.get.T)))/clazz). |
