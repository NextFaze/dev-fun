[gh-pages](../../index.md) / [com.nextfaze.devfun.inject](../index.md) / [RequiringInstanceProvider](index.md) / [get](.)

# get

`abstract operator fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> get(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out T>): T` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/inject/InstanceProvider.kt#L59)

Overrides [InstanceProvider.get](../-instance-provider/get.md)

Get an instance of some [clazz](get.md#com.nextfaze.devfun.inject.RequiringInstanceProvider$get(kotlin.reflect.KClass((com.nextfaze.devfun.inject.RequiringInstanceProvider.get.T)))/clazz).

### Exceptions

`ClassInstanceNotFoundException` - When [clazz](get.md#com.nextfaze.devfun.inject.RequiringInstanceProvider$get(kotlin.reflect.KClass((com.nextfaze.devfun.inject.RequiringInstanceProvider.get.T)))/clazz) could not be found/instantiated

**Return**
An instance of [clazz](get.md#com.nextfaze.devfun.inject.RequiringInstanceProvider$get(kotlin.reflect.KClass((com.nextfaze.devfun.inject.RequiringInstanceProvider.get.T)))/clazz)

