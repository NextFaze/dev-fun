[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [ForegroundTracker](./index.md)

# ForegroundTracker

`interface ForegroundTracker` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/core/ActivityTracking.kt#L60)

Application foreground state tracker.

**See Also**

[ActivityTracker](../-activity-tracker/index.md)

[ActivityProvider](../-activity-provider.md)

[ForegroundChangeListener](../-foreground-change-listener.md)

### Properties

| Name | Summary |
|---|---|
| [isAppInForeground](is-app-in-foreground.md) | `abstract val isAppInForeground: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>The foreground status of the app. |

### Functions

| Name | Summary |
|---|---|
| [addForegroundChangeListener](add-foreground-change-listener.md) | `abstract fun addForegroundChangeListener(listener: `[`ForegroundChangeListener`](../-foreground-change-listener.md)`): `[`ForegroundChangeListener`](../-foreground-change-listener.md)<br>Add a listener for when the app foreground status changes. |
| [minusAssign](minus-assign.md) | `open operator fun minusAssign(listener: `[`ForegroundChangeListener`](../-foreground-change-listener.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Remove a listener for when the app foreground status changes. |
| [plusAssign](plus-assign.md) | `open operator fun plusAssign(listener: `[`ForegroundChangeListener`](../-foreground-change-listener.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Add a listener for when the app foreground status changes. |
| [removeForegroundChangeListener](remove-foreground-change-listener.md) | `abstract fun removeForegroundChangeListener(listener: `[`ForegroundChangeListener`](../-foreground-change-listener.md)`): `[`ForegroundChangeListener`](../-foreground-change-listener.md)<br>Remove a listener for when the app foreground status changes. |
