[gh-pages](../../index.md) / [com.nextfaze.devfun.overlay](../index.md) / [OverlayWindow](./index.md)

# OverlayWindow

`interface OverlayWindow` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/overlay/OverlayWindow.kt#L65)

Overlay windows are used by DevFun to display the loggers [DeveloperLogger](../../com.nextfaze.devfun.reference/-developer-logger/index.md) and DevMenu cog.

**See Also**

[OverlayManager](../-overlay-manager/index.md)

[OverlayPermissions](../-overlay-permissions/index.md)

### Properties

| Name | Summary |
|---|---|
| [configurationOptions](configuration-options.md) | `abstract val configurationOptions: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`UiField`](../../com.nextfaze.devfun.invoke/-ui-field/index.md)`<*>>`<br>Configuration options for this overlay. |
| [enabled](enabled.md) | `abstract var enabled: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>If the overlay is enabled. Setting to false will hide it (but not remove it from the window). |
| [hideWhenDialogsPresent](hide-when-dialogs-present.md) | `abstract var hideWhenDialogsPresent: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>If disabled this overlay will be visible when dialogs are visible (i.e. when DevMenu opens if `true` (default) this overlay will be hidden). |
| [inset](inset.md) | `abstract var inset: `[`Rect`](https://developer.android.com/reference/android/graphics/Rect.html)<br>Insets the overlay window. For a docked window, a pos |
| [isVisible](is-visible.md) | `abstract val isVisible: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Flag indicating if the overlay is currently visible. |
| [name](name.md) | `abstract val name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>User-friendly name. |
| [onAttachListener](on-attach-listener.md) | `abstract var onAttachListener: `[`AttachListener`](../-attach-listener.md)`?`<br>Callback when overlay is added/removed to the window. |
| [onClickListener](on-click-listener.md) | `abstract var onClickListener: `[`ClickListener`](../-click-listener.md)`?`<br>Callback when user taps the overlay. |
| [onLongClickListener](on-long-click-listener.md) | `abstract var onLongClickListener: `[`ClickListener`](../-click-listener.md)`?`<br>Callback when user long-presses the overlay (defaults to showing overlay config dialog). |
| [onVisibilityListener](on-visibility-listener.md) | `abstract var onVisibilityListener: `[`VisibilityListener`](../-visibility-listener.md)`?`<br>Callback when overlay visibility changes. |
| [prefsName](prefs-name.md) | `abstract val prefsName: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Preferences file name to store non-default state - must be unique. |
| [reason](reason.md) | `abstract val reason: `[`OverlayReason`](../-overlay-reason.md)<br>Rendered only if the user has not granted overlay permissions. |
| [snapToEdge](snap-to-edge.md) | `abstract var snapToEdge: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>If enabled the overlay will snap to the closest edge of the window. If disabled it can be moved anywhere and will only snap if it leaves the windows's display bounds. |
| [view](view.md) | `abstract val view: `[`View`](https://developer.android.com/reference/android/view/View.html)<br>The overlay's view. |
| [visibilityScope](visibility-scope.md) | `abstract var visibilityScope: `[`VisibilityScope`](../-visibility-scope/index.md)<br>Determines when the overlay can be visible. |

### Functions

| Name | Summary |
|---|---|
| [addToWindow](add-to-window.md) | `abstract fun addToWindow(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Add this overlay to the window. |
| [dispose](dispose.md) | `abstract fun dispose(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Clean up listeners and callbacks of this window. |
| [removeFromWindow](remove-from-window.md) | `abstract fun removeFromWindow(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Remove this overlay from the window. |
| [resetPositionAndState](reset-position-and-state.md) | `abstract fun resetPositionAndState(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Reset the position and state to its initial default values and clear its preferences. |
