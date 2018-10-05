[gh-pages](../../index.md) / [com.nextfaze.devfun.invoke.view](../index.md) / [ColorPicker](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`ColorPicker()`

Annotated `Int` value parameters will render a color picker view rather than an input/edit for use with invoke UI.

DevFun's invocation UI is shown when one ore more parameters could not be injected, and thus a UI is provided to
allow for manual entry by the user.

Example usage from DevMenu (~line 240 in `com.nextfaze.devfun.menu.controllers.Cog.kt`):
*(the `@From` is not required and is used to provide the initial value)*

``` kotlin
@Constructable
private inner class CurrentColor : ValueSource<Int> {
    override val value get() = cogColor
}

@DeveloperFunction
private fun setColor(@ColorPicker @From(CurrentColor::class) color: Int) {
    cogColor = color
    ...
}
```

*Usage Note: The DevFun library `devfun-invoke-view-colorpicker` must be present for the annotation to be handled (it willsimply be ignored otherwise).**If you have `devfun-menu` then you will have the colorpicker transitively.***

**See Also**

[From](../-from/index.md)

[ValueSource](../-value-source/index.md)

[Constructable](../../com.nextfaze.devfun.inject/-constructable/index.md)

[DeveloperFunction](../../com.nextfaze.devfun.function/-developer-function/index.md)

