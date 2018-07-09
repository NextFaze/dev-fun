[gh-pages](../../index.md) / [com.nextfaze.devfun.overlay.logger](../index.md) / [OverlayLogger](./index.md)

# OverlayLogger

`interface OverlayLogger` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/overlay/logger/Logger.kt#L43)

An overlay logger is a floating semi-transparent `TextView` that will display the `.toString()` of a function, property, or class.

They are contextually aware if created via @[DeveloperLogger](../../com.nextfaze.devfun.annotations/-developer-logger/index.md) (managed by [OverlayLogging](../-overlay-logging/index.md)) and will appear/disappear when in context (if
the annotation is within an Activity or Fragment).

* By default it will update every second (see [refreshRate](refresh-rate.md)).
* If the annotation is on a property also annotated with @[DeveloperProperty](../../com.nextfaze.devfun.annotations/-developer-property/index.md) then a single click will allow you to edit the value (opens
the standard DevFun invoker dialog).
* The overlay's floating behaviour can be configured with a long click (snapping, refresh rate, etc.)
* Overlays can also be configured via. DevFun &gt; Overlays

**See Also**

[OverlayManager](../../com.nextfaze.devfun.overlay/-overlay-manager/index.md)

[OverlayPermissions](../../com.nextfaze.devfun.overlay/-overlay-permissions/index.md)

### Properties

| Name | Summary |
|---|---|
| [configurationOptions](configuration-options.md) | `abstract val configurationOptions: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`UiField`](../../com.nextfaze.devfun.invoke/-ui-field/index.md)`<*>>`<br>Additional configuration options for this overlay. |
| [enabled](enabled.md) | `abstract var enabled: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Enabled state of this logger. Delegates to [OverlayWindow.enabled](../../com.nextfaze.devfun.overlay/-overlay-window/enabled.md). |
| [overlay](overlay.md) | `abstract val overlay: `[`OverlayWindow`](../../com.nextfaze.devfun.overlay/-overlay-window/index.md)<br>The overlay instance used by this logger. |
| [refreshRate](refresh-rate.md) | `abstract var refreshRate: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)<br>How frequently this overlay should update (in milliseconds). *(default: `1000`)* |
| [visibilityScope](visibility-scope.md) | `abstract var visibilityScope: `[`VisibilityScope`](../../com.nextfaze.devfun.overlay/-visibility-scope/index.md)<br>Determines when an overlay can be visible. Delegates to [OverlayWindow.visibilityScope](../../com.nextfaze.devfun.overlay/-overlay-window/visibility-scope.md). |

### Functions

| Name | Summary |
|---|---|
| [resetPositionAndState](reset-position-and-state.md) | `abstract fun resetPositionAndState(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Reset the position and state to its initial default values and clear its preferences. |
| [start](start.md) | `abstract fun start(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Add this logger to the window and start updating. |
| [stop](stop.md) | `abstract fun stop(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Stop this logger and remove it from the window. |
