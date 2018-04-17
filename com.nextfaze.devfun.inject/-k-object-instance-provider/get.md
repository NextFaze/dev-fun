[gh-pages](../../index.md) / [com.nextfaze.devfun.inject](../index.md) / [KObjectInstanceProvider](index.md) / [get](./get.md)

# get

`fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> get(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`T`](get.md#T)`>): `[`T`](get.md#T)`?` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/inject/InstanceProviders.kt#L120)

Overrides [InstanceProvider.get](../-instance-provider/get.md)

Get the Kotlin `object` instance of some [clazz](get.md#com.nextfaze.devfun.inject.KObjectInstanceProvider$get(kotlin.reflect.KClass((com.nextfaze.devfun.inject.KObjectInstanceProvider.get.T)))/clazz) type.

Automatically handles `internal` or `private` types.

