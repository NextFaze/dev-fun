[gh-pages](../../index.md) / [com.nextfaze.devfun.invoke.view](../index.md) / [ValueSource](./index.md)

# ValueSource

`interface ValueSource<out V : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/invoke/view/ValueSource.kt#L42)

Used in conjunction with [From](../-from/index.md) or [Values](../-values/index.md) to provide the Invoke UI with an initial value or set of values.

Example usage from DevMenu (~line 215 in `com.nextfaze.devfun.menu.controllers.Cog.kt`):

``` kotlin
@Constructable
private inner class CurrentVisibility : ValueSource<Boolean> {
    override val value get() = cogVisible
}

@DeveloperFunction
private fun setCogVisibility(@From(CurrentVisibility::class) visible: Boolean) {
    cogVisible = visible
    ...
}
```

The above example will render an Invoke UI with a `Switch` with an initial state representative of the actual state.

### Properties

| Name | Summary |
|---|---|
| [value](value.md) | `abstract val value: `[`V`](index.md#V)<br>The initial value passed to the Invoke UI dialog. |
