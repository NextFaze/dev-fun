[gh-pages](../../index.md) / [com.nextfaze.devfun.inject](../index.md) / [CapturingInstanceProvider](index.md) / [get](./get.md)

# get

`fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> get(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`T`](get.md#T)`>): `[`T`](get.md#T)`?` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/inject/InstanceProvider.kt#L95)

Overrides [InstanceProvider.get](../-instance-provider/get.md)

Try to get an instance of some [clazz](../-instance-provider/get.md#com.nextfaze.devfun.inject.InstanceProvider$get(kotlin.reflect.KClass((com.nextfaze.devfun.inject.InstanceProvider.get.T)))/clazz).

**Return**
An instance of [clazz](../-instance-provider/get.md#com.nextfaze.devfun.inject.InstanceProvider$get(kotlin.reflect.KClass((com.nextfaze.devfun.inject.InstanceProvider.get.T)))/clazz), or `null` if this provider can not handle the type

**See Also**

[ThrowingInstanceProvider.get](../-throwing-instance-provider/get.md)

