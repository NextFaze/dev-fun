[gh-pages](../../index.md) / [com.nextfaze.devfun.overlay](../index.md) / [DisplayBoundsTracker](./index.md)

# DisplayBoundsTracker

`interface DisplayBoundsTracker` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/overlay/DisplayBounds.kt#L24)

Tracks the *activity/app* bounds (not the device).

**See Also**

[DisplayBoundsChangeListener](../-display-bounds-change-listener.md)

### Properties

| Name | Summary |
|---|---|
| [displayBounds](display-bounds.md) | `abstract val displayBounds: `[`Rect`](https://developer.android.com/reference/android/graphics/Rect.html)<br>The foreground status of the app. |

### Functions

| Name | Summary |
|---|---|
| [addDisplayBoundsChangeListener](add-display-bounds-change-listener.md) | `abstract fun addDisplayBoundsChangeListener(listener: `[`DisplayBoundsChangeListener`](../-display-bounds-change-listener.md)`): `[`DisplayBoundsChangeListener`](../-display-bounds-change-listener.md)<br>Add a listener for when the display bounds change. |
| [minusAssign](minus-assign.md) | `open operator fun minusAssign(listener: `[`DisplayBoundsChangeListener`](../-display-bounds-change-listener.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Remove a listener for when the display bounds change. |
| [plusAssign](plus-assign.md) | `open operator fun plusAssign(listener: `[`DisplayBoundsChangeListener`](../-display-bounds-change-listener.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Add a listener for when the display bounds change. |
| [removeDisplayBoundsChangeListener](remove-display-bounds-change-listener.md) | `abstract fun removeDisplayBoundsChangeListener(listener: `[`DisplayBoundsChangeListener`](../-display-bounds-change-listener.md)`): `[`DisplayBoundsChangeListener`](../-display-bounds-change-listener.md)<br>Remove a listener for when the display bounds change. |
