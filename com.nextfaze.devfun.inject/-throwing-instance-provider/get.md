[gh-pages](../../index.md) / [com.nextfaze.devfun.inject](../index.md) / [ThrowingInstanceProvider](index.md) / [get](./get.md)

# get

`abstract operator fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> get(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`T`](get.md#T)`>): `[`T`](get.md#T) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/inject/InstanceProvider.kt#L57)

Overrides [InstanceProvider.get](../-instance-provider/get.md)

Get an instance of some [clazz](get.md#com.nextfaze.devfun.inject.ThrowingInstanceProvider$get(kotlin.reflect.KClass((com.nextfaze.devfun.inject.ThrowingInstanceProvider.get.T)))/clazz).

### Exceptions

`ClassInstanceNotFoundException` - When [clazz](get.md#com.nextfaze.devfun.inject.ThrowingInstanceProvider$get(kotlin.reflect.KClass((com.nextfaze.devfun.inject.ThrowingInstanceProvider.get.T)))/clazz) could not be found/instantiated

**Return**
An instance of [clazz](get.md#com.nextfaze.devfun.inject.ThrowingInstanceProvider$get(kotlin.reflect.KClass((com.nextfaze.devfun.inject.ThrowingInstanceProvider.get.T)))/clazz)

