[gh-pages](../../index.md) / [com.nextfaze.devfun.internal](../index.md) / [WindowCallbacks](./index.md)

# WindowCallbacks

`class WindowCallbacks` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/internal/WindowCallbacks.kt#L48)

Handles wrapping and posting [Window](https://developer.android.com/reference/android/view/Window.html) events throughout an app's life.

Used to tell the DevMenu overlays key events, and overlays when the current activity has focus. i.e. when they should
hide if a dialog is visible etc.

**See Also**

[KeyEventListener](../-key-event-listener.md)

[FocusChangeListener](../-focus-change-listener.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `WindowCallbacks(application: `[`Application`](https://developer.android.com/reference/android/app/Application.html)`, activityTracker: `[`ActivityTracker`](../../com.nextfaze.devfun.core/-activity-tracker/index.md)`)`<br>Handles wrapping and posting [Window](https://developer.android.com/reference/android/view/Window.html) events throughout an app's life. |

### Properties

| Name | Summary |
|---|---|
| [resumedActivityHasFocus](resumed-activity-has-focus.md) | `var resumedActivityHasFocus: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

### Functions

| Name | Summary |
|---|---|
| [addKeyEventListener](add-key-event-listener.md) | `fun addKeyEventListener(listener: `[`KeyEventListener`](../-key-event-listener.md)`): `[`KeyEventListener`](../-key-event-listener.md) |
| [addResumedActivityFocusChangeListener](add-resumed-activity-focus-change-listener.md) | `fun addResumedActivityFocusChangeListener(listener: `[`FocusChangeListener`](../-focus-change-listener.md)`): `[`FocusChangeListener`](../-focus-change-listener.md) |
| [minusAssign](minus-assign.md) | `operator fun minusAssign(listener: `[`FocusChangeListener`](../-focus-change-listener.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>`operator fun minusAssign(listener: `[`KeyEventListener`](../-key-event-listener.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [plusAssign](plus-assign.md) | `operator fun plusAssign(listener: `[`FocusChangeListener`](../-focus-change-listener.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>`operator fun plusAssign(listener: `[`KeyEventListener`](../-key-event-listener.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [removeKeyEventListener](remove-key-event-listener.md) | `fun removeKeyEventListener(listener: `[`KeyEventListener`](../-key-event-listener.md)`): `[`KeyEventListener`](../-key-event-listener.md) |
| [removeResumedActivityFocusChangeListener](remove-resumed-activity-focus-change-listener.md) | `fun removeResumedActivityFocusChangeListener(listener: `[`FocusChangeListener`](../-focus-change-listener.md)`): `[`FocusChangeListener`](../-focus-change-listener.md) |
