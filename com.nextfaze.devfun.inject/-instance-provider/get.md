[gh-pages](../../index.md) / [com.nextfaze.devfun.inject](../index.md) / [InstanceProvider](index.md) / [get](.)

# get

`abstract operator fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> get(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out T>): T?` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/inject/InstanceProvider.kt#L43)

Try to get an instance of some [clazz](get.md#com.nextfaze.devfun.inject.InstanceProvider$get(kotlin.reflect.KClass((com.nextfaze.devfun.inject.InstanceProvider.get.T)))/clazz).

**Return**
An instance of [clazz](get.md#com.nextfaze.devfun.inject.InstanceProvider$get(kotlin.reflect.KClass((com.nextfaze.devfun.inject.InstanceProvider.get.T)))/clazz), or `null` if this provider can not handle the type

**See Also**

[RequiringInstanceProvider.get](../-requiring-instance-provider/get.md)

