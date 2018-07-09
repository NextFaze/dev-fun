[gh-pages](../../index.md) / [com.nextfaze.devfun.overlay](../index.md) / [OverlayWindow](index.md) / [visibilityScope](./visibility-scope.md)

# visibilityScope

`abstract var visibilityScope: `[`VisibilityScope`](../-visibility-scope/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/overlay/OverlayWindow.kt#L83)

Determines when the overlay can be visible.

* [FOREGROUND_ONLY](../-visibility-scope/-f-o-r-e-g-r-o-u-n-d_-o-n-l-y.md): Only visible when the app is in the foreground.
* [ALWAYS](../-visibility-scope/-a-l-w-a-y-s.md): Visible even if the app has no active activities. *App cannot be swipe-closed if you have one of these.*
