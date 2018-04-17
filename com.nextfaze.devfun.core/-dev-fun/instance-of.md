[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [DevFun](index.md) / [instanceOf](./instance-of.md)

# instanceOf

`fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> instanceOf(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`T`](instance-of.md#T)`>): `[`T`](instance-of.md#T) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/core/DevFun.kt#L344)

Get an instance of a class using [instanceProviders](instance-providers.md).

Intended for use in Java code, or when type erasure prohibits use of [get](get.md).

### Exceptions

`ClassInstanceNotFoundException` - When [clazz](instance-of.md#com.nextfaze.devfun.core.DevFun$instanceOf(kotlin.reflect.KClass((com.nextfaze.devfun.core.DevFun.instanceOf.T)))/clazz) could not be found/instantiated and all providers have been checked.

**See Also**

[get](get.md)

[InstanceProvider](../../com.nextfaze.devfun.inject/-instance-provider/index.md)

