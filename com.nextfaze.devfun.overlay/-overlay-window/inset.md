[gh-pages](../../index.md) / [com.nextfaze.devfun.overlay](../index.md) / [OverlayWindow](index.md) / [inset](./inset.md)

# inset

`abstract var inset: `[`Rect`](https://developer.android.com/reference/android/graphics/Rect.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/overlay/OverlayWindow.kt#L96)

Insets the overlay window. For a docked window, a pos

e.g. The DevMenu cog has an inset of half its width (and thus sits half way off the window).

For non-docking overlays, this will affect how it sits if you try to drag it off window when it snaps back. *Be careful with this asit could result in the overlay sitting outside the window.*

