[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [DevFun](index.md) / [get](./get.md)

# get

`inline fun <reified T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> get(): `[`T`](get.md#T) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/core/DevFun.kt#L298)

Get an instance of a class using [instanceProviders](instance-providers.md).

Intended for use in Kotlin code.

### Exceptions

`ClassInstanceNotFoundException` - When [T](get.md#T) could not be found/instantiated and all providers have been checked.

**See Also**

[instanceOf](instance-of.md)

[InstanceProvider](../../com.nextfaze.devfun.inject/-instance-provider/index.md)

