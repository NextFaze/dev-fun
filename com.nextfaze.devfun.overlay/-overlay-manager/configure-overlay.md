[gh-pages](../../index.md) / [com.nextfaze.devfun.overlay](../index.md) / [OverlayManager](index.md) / [configureOverlay](./configure-overlay.md)

# configureOverlay

`abstract fun configureOverlay(overlayWindow: `[`OverlayWindow`](../-overlay-window/index.md)`, additionalOptions: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`UiField`](../../com.nextfaze.devfun.invoke/-ui-field/index.md)`<*>> = emptyList(), onResetClick: `[`OnClick`](../../com.nextfaze.devfun.invoke/-on-click.md)` = { overlayWindow.resetPositionAndState() }): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/overlay/Overlays.kt#L92)

Show a configuration dialog for an overlay.

### Parameters

`overlayWindow` - The overlay to configure.

`additionalOptions` - Additional options to show in the configuration dialog.

`onResetClick` - Callback when reset clicked. By default calls the overlay's [OverlayWindow.resetPositionAndState](../-overlay-window/reset-position-and-state.md).