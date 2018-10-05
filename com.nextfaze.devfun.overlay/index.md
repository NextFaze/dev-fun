[gh-pages](../index.md) / [com.nextfaze.devfun.overlay](./index.md)

## Package com.nextfaze.devfun.overlay

### Types

| Name | Summary |
|---|---|
| [DisplayBoundsTracker](-display-bounds-tracker/index.md) | `interface DisplayBoundsTracker`<br>Tracks the *activity/app* bounds (not the device). |
| [Dock](-dock/index.md) | `enum class Dock` |
| [OverlayManager](-overlay-manager/index.md) | `interface OverlayManager`<br>Handles creation, destruction, and visibility of overlays. |
| [OverlayPermissions](-overlay-permissions/index.md) | `interface OverlayPermissions`<br>Handles overlay permissions. |
| [OverlayWindow](-overlay-window/index.md) | `interface OverlayWindow`<br>Overlay windows are used by DevFun to display the loggers [DeveloperLogger](../com.nextfaze.devfun.reference/-developer-logger/index.md) and DevMenu cog. |
| [VisibilityScope](-visibility-scope/index.md) | `enum class VisibilityScope`<br>Determines when an overlay can be visible. |

### Type Aliases

| Name | Summary |
|---|---|
| [AttachListener](-attach-listener.md) | `typealias AttachListener = (attached: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [ClickListener](-click-listener.md) | `typealias ClickListener = (`[`OverlayWindow`](-overlay-window/index.md)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [DisplayBoundsChangeListener](-display-bounds-change-listener.md) | `typealias DisplayBoundsChangeListener = (old: `[`Rect`](https://developer.android.com/reference/android/graphics/Rect.html)`, new: `[`Rect`](https://developer.android.com/reference/android/graphics/Rect.html)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Function signature of callbacks for display bound changes. |
| [FullScreenUsageStateListener](-full-screen-usage-state-listener.md) | `typealias FullScreenUsageStateListener = (`[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Function signature of a listener for the current full-screen status. This callback may be called multiple times with the same value. |
| [OverlayPermissionListener](-overlay-permission-listener.md) | `typealias OverlayPermissionListener = (havePermission: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [OverlayReason](-overlay-reason.md) | `typealias OverlayReason = () -> `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html) |
| [VisibilityListener](-visibility-listener.md) | `typealias VisibilityListener = (visible: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [VisibilityPredicate](-visibility-predicate.md) | `typealias VisibilityPredicate = (`[`Context`](https://developer.android.com/reference/android/content/Context.html)`) -> `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
