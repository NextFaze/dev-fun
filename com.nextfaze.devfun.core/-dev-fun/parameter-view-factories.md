[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [DevFun](index.md) / [parameterViewFactories](./parameter-view-factories.md)

# parameterViewFactories

`val parameterViewFactories: `[`CompositeParameterViewFactoryProvider`](../../com.nextfaze.devfun.invoke/-composite-parameter-view-factory-provider.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/core/DevFun.kt#L221)

Composite list of all [ParameterViewFactoryProvider](../../com.nextfaze.devfun.invoke/-parameter-view-factory-provider/index.md)s.

If DevFun is unable to inject all parameters of a function, it will attempt to generate a UI that allows the user
to input the missing parameter values. By default only simple types can be rendered (int, string, bool, etc).

Add parameter view factory providers using [Composite.plusAssign](../-composite/plus-assign.md); `devFun.parameterViewFactories += MyParameterViewFactoryProvider()`.

Providers are checked in reverse order.
i.e. Most recently added are checked first.

For an example of a custom type, see module `devfun-invoke-view-colorpicker` which provides a color picker for
annotated int types using @[ColorPicker](../../com.nextfaze.devfun.invoke.view/-color-picker/index.md) ([ColorPickerParameterViewProvider](https://github.com/NextFaze/dev-fun/blob/master/devfun-invoke-view-colorpicker/src/main/java/com/nextfaze/devfun/invoke/view/colorpicker/Module.kt#L28)).
If you have included the menu `devfun-menu` then the color picker module will be included transitively.

**See Also**

[ViewFactory](../../com.nextfaze.devfun.view/-view-factory/index.md)

[inflate](../../com.nextfaze.devfun.view/inflate.md)

**Getter**

Composite list of all [ParameterViewFactoryProvider](../../com.nextfaze.devfun.invoke/-parameter-view-factory-provider/index.md)s.

If DevFun is unable to inject all parameters of a function, it will attempt to generate a UI that allows the user
to input the missing parameter values. By default only simple types can be rendered (int, string, bool, etc).

Add parameter view factory providers using [Composite.plusAssign](../-composite/plus-assign.md); `devFun.parameterViewFactories += MyParameterViewFactoryProvider()`.

Providers are checked in reverse order.
i.e. Most recently added are checked first.

For an example of a custom type, see module `devfun-invoke-view-colorpicker` which provides a color picker for
annotated int types using @[ColorPicker](../../com.nextfaze.devfun.invoke.view/-color-picker/index.md) ([ColorPickerParameterViewProvider](https://github.com/NextFaze/dev-fun/blob/master/devfun-invoke-view-colorpicker/src/main/java/com/nextfaze/devfun/invoke/view/colorpicker/Module.kt#L28)).
If you have included the menu `devfun-menu` then the color picker module will be included transitively.

**Getter See Also**

[ViewFactory](../../com.nextfaze.devfun.view/-view-factory/index.md)

[inflate](../../com.nextfaze.devfun.view/inflate.md)

