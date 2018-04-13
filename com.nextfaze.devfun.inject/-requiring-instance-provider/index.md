[gh-pages](../../index.md) / [com.nextfaze.devfun.inject](../index.md) / [RequiringInstanceProvider](./index.md)

# RequiringInstanceProvider

`interface RequiringInstanceProvider : `[`InstanceProvider`](../-instance-provider/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/inject/InstanceProvider.kt#L52)

Same as [InstanceProvider](../-instance-provider/index.md), but throws [ClassInstanceNotFoundException](../-class-instance-not-found-exception/index.md) instead of returning `null`.

*(TODO: Think of better nomenclature?)*

### Functions

| Name | Summary |
|---|---|
| [get](get.md) | `abstract operator fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> get(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`T`](get.md#T)`>): `[`T`](get.md#T)<br>Get an instance of some [clazz](get.md#com.nextfaze.devfun.inject.RequiringInstanceProvider$get(kotlin.reflect.KClass((com.nextfaze.devfun.inject.RequiringInstanceProvider.get.T)))/clazz). |

### Inheritors

| Name | Summary |
|---|---|
| [CompositeInstanceProvider](../-composite-instance-provider.md) | `interface CompositeInstanceProvider : `[`RequiringInstanceProvider`](./index.md)`, `[`Composite`](../../com.nextfaze.devfun.core/-composite/index.md)`<`[`InstanceProvider`](../-instance-provider/index.md)`>`<br>Instance provider that delegates to other providers. |
