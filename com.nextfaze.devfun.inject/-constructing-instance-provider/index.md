[gh-pages](../../index.md) / [com.nextfaze.devfun.inject](../index.md) / [ConstructingInstanceProvider](./index.md)

# ConstructingInstanceProvider

`class ConstructingInstanceProvider : `[`InstanceProvider`](../-instance-provider/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/inject/Constructable.kt#L23)

Provides objects via instance construction. Type must be annotated with [Constructable](../-constructable/index.md).

Only supports objects with a single constructor. Constructor arguments will fetched using param `rootInstanceProvider`.

If [Constructable.singleton](../-constructable/singleton.md) is `true` or type is annotated @[Singleton](#) then only one instance will be created and shared.

### Parameters

`rootInstanceProvider` - An instance provider used to fetch constructor args. If `null`,  then self (`this`) is used

`requireConstructable` - Flag indicating if a type must be [Constructable](../-constructable/index.md) to be instantiable

**Internal**
Visible for testing - use at your own risk.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `ConstructingInstanceProvider(rootInstanceProvider: `[`InstanceProvider`](../-instance-provider/index.md)`? = null, requireConstructable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true)`<br>Provides objects via instance construction. Type must be annotated with [Constructable](../-constructable/index.md). |

### Properties

| Name | Summary |
|---|---|
| [requireConstructable](require-constructable.md) | `var requireConstructable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Flag indicating if a type must be [Constructable](../-constructable/index.md) to be instantiable |

### Functions

| Name | Summary |
|---|---|
| [get](get.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> get(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`T`](get.md#T)`>): `[`T`](get.md#T)`?`<br>Try to get an instance of some [clazz](../-instance-provider/get.md#com.nextfaze.devfun.inject.InstanceProvider$get(kotlin.reflect.KClass((com.nextfaze.devfun.inject.InstanceProvider.get.T)))/clazz).`fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> get(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`T`](get.md#T)`>, requireConstructable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`): `[`T`](get.md#T)`?` |
| [toString](to-string.md) | `fun toString(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
