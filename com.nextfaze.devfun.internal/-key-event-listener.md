[gh-pages](../index.md) / [com.nextfaze.devfun.internal](index.md) / [KeyEventListener](./-key-event-listener.md)

# KeyEventListener

`typealias KeyEventListener = (event: `[`KeyEvent`](https://developer.android.com/reference/android/view/KeyEvent.html)`) -> `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/internal/WindowCallbacks.kt#L27)

Function signature of a listener for [Window](https://developer.android.com/reference/android/view/Window.html) key events.

**Return**
Flag indicating if the event should be consumed (not forwarded to the system level handler). i.e. `true` will block propagation.
    All key event listeners will receive the event even if one of them returns `true`.

**See Also**

[WindowCallbacks.addKeyEventListener](-window-callbacks/add-key-event-listener.md)

[WindowCallbacks.removeKeyEventListener](-window-callbacks/remove-key-event-listener.md)

