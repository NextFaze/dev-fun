[gh-pages](../../index.md) / [com.nextfaze.devfun.inject](../index.md) / [KObjectInstanceProvider](./index.md)

# KObjectInstanceProvider

`class KObjectInstanceProvider : `[`InstanceProvider`](../-instance-provider/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/inject/InstanceProviders.kt#L114)

Handles Kotlin `object` types.

Automatically handles `internal` or `private` types.

**Internal**
Visible for testing - use at your own risk.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `KObjectInstanceProvider()`<br>Handles Kotlin `object` types. |

### Functions

| Name | Summary |
|---|---|
| [get](get.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> get(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`T`](get.md#T)`>): `[`T`](get.md#T)`?`<br>Get the Kotlin `object` instance of some [clazz](get.md#com.nextfaze.devfun.inject.KObjectInstanceProvider$get(kotlin.reflect.KClass((com.nextfaze.devfun.inject.KObjectInstanceProvider.get.T)))/clazz) type. |