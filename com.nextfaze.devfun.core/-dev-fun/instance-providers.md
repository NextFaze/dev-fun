[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [DevFun](index.md) / [instanceProviders](.)

# instanceProviders

`val instanceProviders: `[`CompositeInstanceProvider`](../../com.nextfaze.devfun.inject/-composite-instance-provider/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/core/DevFun.kt#L120)

Composite list of all [InstanceProvider](../../com.nextfaze.devfun.inject/-instance-provider/index.md)s.

Add instance providers using [CompositeInstanceProvider.plusAssign](../../com.nextfaze.devfun.inject/-composite-instance-provider/plus-assign.md) `devFun.instanceProviders += MyInstanceProvider()`

Providers are checked in reverse order.
i.e. Most recently added are checked first.

**See Also**

[get](get.md)

[instanceOf](instance-of.md)

