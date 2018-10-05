[gh-pages](../../index.md) / [com.nextfaze.devfun.inject.dagger2](../index.md) / [Dagger2InstanceProvider](index.md) / [get](./get.md)

# get

`open fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> get(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`T`](get.md#T)`>): `[`T`](get.md#T)`?` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-inject-dagger2/src/main/java/com/nextfaze/devfun/inject/dagger2/Instances.kt#L345)

Overrides [InstanceProvider.get](../../com.nextfaze.devfun.inject/-instance-provider/get.md)

Try to get an instance of some [clazz](../../com.nextfaze.devfun.inject/-instance-provider/get.md#com.nextfaze.devfun.inject.InstanceProvider$get(kotlin.reflect.KClass((com.nextfaze.devfun.inject.InstanceProvider.get.T)))/clazz).

**Return**
An instance of [clazz](../../com.nextfaze.devfun.inject/-instance-provider/get.md#com.nextfaze.devfun.inject.InstanceProvider$get(kotlin.reflect.KClass((com.nextfaze.devfun.inject.InstanceProvider.get.T)))/clazz), or `null` if this provider can not handle the type

**See Also**

[ThrowingInstanceProvider.get](../../com.nextfaze.devfun.inject/-throwing-instance-provider/get.md)

