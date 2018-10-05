[gh-pages](../../index.md) / [com.nextfaze.devfun.overlay](../index.md) / [OverlayManager](index.md) / [createOverlay](./create-overlay.md)

# createOverlay

`abstract fun createOverlay(@LayoutRes layoutId: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`, name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, prefsName: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, reason: `[`OverlayReason`](../-overlay-reason.md)`, onClick: `[`ClickListener`](../-click-listener.md)`? = null, onLongClick: `[`ClickListener`](../-click-listener.md)`? = null, onVisibilityChange: `[`VisibilityListener`](../-visibility-listener.md)`? = null, onAttachChange: `[`AttachListener`](../-attach-listener.md)`? = null, visibilityPredicate: `[`VisibilityPredicate`](../-visibility-predicate.md)`? = null, visibilityScope: `[`VisibilityScope`](../-visibility-scope/index.md)` = VisibilityScope.FOREGROUND_ONLY, initialDock: `[`Dock`](../-dock/index.md)` = Dock.TOP_LEFT, initialDelta: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 0f, snapToEdge: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true, initialLeft: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 0f, initialTop: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 0f): `[`OverlayWindow`](../-overlay-window/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/overlay/Overlays.kt#L79)

Creates an overlay window.

Add the overlay to the screen using [OverlayWindow.addToWindow](../-overlay-window/add-to-window.md).

When you are done with the overlay by sure to destroy it with [destroyOverlay](destroy-overlay.md).

### Parameters

`layoutId` - A layout resource ID to inflate.

`name` - A user-friendly name.

`prefsName` - The name of the preferences file the window will use - **must be unique**!

`reason` - A function that generates the reason description. Will only be called when we need to request overlays permission.

`onClick` - Callback when the overlay is clicked.

`onLongClick` - Callback when the overlay is long-clicked.

`onVisibilityChange` - Callback when the overlay's visibility is changed.

`onAttachChange` - Callback when overlay is added/removed to the window.

`visibilityPredicate` - Predicate that determines if/when the overlay should be visible (e.g. DevMenu uses `context is FragmentActivity`).

`visibilityScope` - The [VisibilityScope](../-visibility-scope/index.md) defaults to `FOREGROUND_ONLY`. i.e. When the app is visible and resumed to the user.

`initialDock` - The initial edge of the screen to dock to (see [snapToEdge](create-overlay.md#com.nextfaze.devfun.overlay.OverlayManager$createOverlay(kotlin.Int, kotlin.String, kotlin.String, kotlin.Function0((kotlin.CharSequence)), kotlin.Function1((com.nextfaze.devfun.overlay.OverlayWindow, kotlin.Unit)), kotlin.Function1((com.nextfaze.devfun.overlay.OverlayWindow, kotlin.Unit)), kotlin.Function1((kotlin.Boolean, kotlin.Unit)), kotlin.Function1((kotlin.Boolean, kotlin.Unit)), kotlin.Function1((android.content.Context, kotlin.Boolean)), com.nextfaze.devfun.overlay.VisibilityScope, com.nextfaze.devfun.overlay.Dock, kotlin.Float, kotlin.Boolean, kotlin.Float, kotlin.Float)/snapToEdge)).

`initialDelta` - The percentage down/across the screen (e.g. An [initialDock](create-overlay.md#com.nextfaze.devfun.overlay.OverlayManager$createOverlay(kotlin.Int, kotlin.String, kotlin.String, kotlin.Function0((kotlin.CharSequence)), kotlin.Function1((com.nextfaze.devfun.overlay.OverlayWindow, kotlin.Unit)), kotlin.Function1((com.nextfaze.devfun.overlay.OverlayWindow, kotlin.Unit)), kotlin.Function1((kotlin.Boolean, kotlin.Unit)), kotlin.Function1((kotlin.Boolean, kotlin.Unit)), kotlin.Function1((android.content.Context, kotlin.Boolean)), com.nextfaze.devfun.overlay.VisibilityScope, com.nextfaze.devfun.overlay.Dock, kotlin.Float, kotlin.Boolean, kotlin.Float, kotlin.Float)/initialDock) of `RIGHT` and delta of 0.7 (DevMenu cog) puts it at 70% down the right-hand side of the screen).

`snapToEdge` - `true` to enable edge docking, `false` to allow it to sit/drag anywhere on the screen.

`initialLeft` - Used when [snapToEdge](create-overlay.md#com.nextfaze.devfun.overlay.OverlayManager$createOverlay(kotlin.Int, kotlin.String, kotlin.String, kotlin.Function0((kotlin.CharSequence)), kotlin.Function1((com.nextfaze.devfun.overlay.OverlayWindow, kotlin.Unit)), kotlin.Function1((com.nextfaze.devfun.overlay.OverlayWindow, kotlin.Unit)), kotlin.Function1((kotlin.Boolean, kotlin.Unit)), kotlin.Function1((kotlin.Boolean, kotlin.Unit)), kotlin.Function1((android.content.Context, kotlin.Boolean)), com.nextfaze.devfun.overlay.VisibilityScope, com.nextfaze.devfun.overlay.Dock, kotlin.Float, kotlin.Boolean, kotlin.Float, kotlin.Float)/snapToEdge) is `true` - the initial left position (percentage of screen).

`initialTop` - Used when [snapToEdge](create-overlay.md#com.nextfaze.devfun.overlay.OverlayManager$createOverlay(kotlin.Int, kotlin.String, kotlin.String, kotlin.Function0((kotlin.CharSequence)), kotlin.Function1((com.nextfaze.devfun.overlay.OverlayWindow, kotlin.Unit)), kotlin.Function1((com.nextfaze.devfun.overlay.OverlayWindow, kotlin.Unit)), kotlin.Function1((kotlin.Boolean, kotlin.Unit)), kotlin.Function1((kotlin.Boolean, kotlin.Unit)), kotlin.Function1((android.content.Context, kotlin.Boolean)), com.nextfaze.devfun.overlay.VisibilityScope, com.nextfaze.devfun.overlay.Dock, kotlin.Float, kotlin.Boolean, kotlin.Float, kotlin.Float)/snapToEdge) is `true` - the initial top position (percentage of screen).

**See Also**

[OverlayWindow](../-overlay-window/index.md)

