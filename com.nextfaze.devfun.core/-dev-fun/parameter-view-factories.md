[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [DevFun](index.md) / [parameterViewFactories](./parameter-view-factories.md)

# parameterViewFactories

`val parameterViewFactories: `[`CompositeParameterViewFactoryProvider`](../../com.nextfaze.devfun.invoke/-composite-parameter-view-factory-provider.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/core/DevFun.kt#L187)

Composite list of all [ParameterViewFactoryProvider](../../com.nextfaze.devfun.invoke/-parameter-view-factory-provider/index.md)s.

If DevFun is unable to inject all parameters of a function, it will attempt to generate a UI that allows the user
to input the missing parameter values.

By default only simple types can be rendered (int, string, bool, etc). For an example of a custom type, see
the DevMenu `Cog.kt` file which renders a color picker for an annotated Int parameter. *(this factory will likelybe put into a lib at some point)*

Add parameter view factory providers using [Composite.plusAssign](../-composite/plus-assign.md); `devFun.parameterViewFactories += MyParameterViewFactoryProvider()`.

Providers are checked in reverse order.
i.e. Most recently added are checked first.

**See Also**

[ViewFactory](../../com.nextfaze.devfun.view/-view-factory/index.md)

**Getter**

Composite list of all [ParameterViewFactoryProvider](../../com.nextfaze.devfun.invoke/-parameter-view-factory-provider/index.md)s.

If DevFun is unable to inject all parameters of a function, it will attempt to generate a UI that allows the user
to input the missing parameter values.

By default only simple types can be rendered (int, string, bool, etc). For an example of a custom type, see
the DevMenu `Cog.kt` file which renders a color picker for an annotated Int parameter. *(this factory will likelybe put into a lib at some point)*

Add parameter view factory providers using [Composite.plusAssign](../-composite/plus-assign.md); `devFun.parameterViewFactories += MyParameterViewFactoryProvider()`.

Providers are checked in reverse order.
i.e. Most recently added are checked first.

**Getter See Also**

[ViewFactory](../../com.nextfaze.devfun.view/-view-factory/index.md)

