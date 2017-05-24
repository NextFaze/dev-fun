[gh-pages](../../index.md) / [com.nextfaze.devfun.inject](../index.md) / [CompositeInstanceProvider](index.md) / [get](.)

# get

`fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> get(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out T>): T` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/inject/InstanceProviders.kt#L37)

Overrides [RequiringInstanceProvider.get](../-requiring-instance-provider/get.md)

Get an instance of some [clazz](get.md#com.nextfaze.devfun.inject.CompositeInstanceProvider$get(kotlin.reflect.KClass((com.nextfaze.devfun.inject.CompositeInstanceProvider.get.T)))/clazz).

### Exceptions

`ClassInstanceNotFoundException` - When [clazz](get.md#com.nextfaze.devfun.inject.CompositeInstanceProvider$get(kotlin.reflect.KClass((com.nextfaze.devfun.inject.CompositeInstanceProvider.get.T)))/clazz) could not be found/instantiated

**Return**
An instance of [clazz](get.md#com.nextfaze.devfun.inject.CompositeInstanceProvider$get(kotlin.reflect.KClass((com.nextfaze.devfun.inject.CompositeInstanceProvider.get.T)))/clazz)

