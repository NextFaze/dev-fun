[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [DevFun](index.md) / [viewFactories](./view-factories.md)

# viewFactories

`val viewFactories: `[`CompositeViewFactoryProvider`](../../com.nextfaze.devfun.view/-composite-view-factory-provider.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/core/DevFun.kt#L168)

Composite list of all [ViewFactoryProvider](../../com.nextfaze.devfun.view/-view-factory-provider/index.md)s.

Various DevFun modules will use these factories for rendering custom views (by providing their own initially
and/or allowing user defined implementations).

Add view factory providers using [Composite.plusAssign](../-composite/plus-assign.md); `devFun.viewFactories += MyViewFactoryProvider()`.

Providers are checked in reverse order.
i.e. Most recently added are checked first.

**See Also**

[ViewFactory](../../com.nextfaze.devfun.view/-view-factory/index.md)

**Getter**

Composite list of all [ViewFactoryProvider](../../com.nextfaze.devfun.view/-view-factory-provider/index.md)s.

Various DevFun modules will use these factories for rendering custom views (by providing their own initially
and/or allowing user defined implementations).

Add view factory providers using [Composite.plusAssign](../-composite/plus-assign.md); `devFun.viewFactories += MyViewFactoryProvider()`.

Providers are checked in reverse order.
i.e. Most recently added are checked first.

**Getter See Also**

[ViewFactory](../../com.nextfaze.devfun.view/-view-factory/index.md)

