[gh-pages](../../index.md) / [com.nextfaze.devfun.overlay.logger](../index.md) / [OverlayLogger](index.md) / [visibilityScope](./visibility-scope.md)

# visibilityScope

`abstract var visibilityScope: `[`VisibilityScope`](../../com.nextfaze.devfun.overlay/-visibility-scope/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/overlay/logger/Logger.kt#L60)

Determines when an overlay can be visible. Delegates to [OverlayWindow.visibilityScope](../../com.nextfaze.devfun.overlay/-overlay-window/visibility-scope.md).

* [FOREGROUND_ONLY](../../com.nextfaze.devfun.overlay/-visibility-scope/-f-o-r-e-g-r-o-u-n-d_-o-n-l-y.md): Only visible when the app is in the foreground.
* [ALWAYS](../../com.nextfaze.devfun.overlay/-visibility-scope/-a-l-w-a-y-s.md): Visible even if the app has no active activities. *App cannot be swipe-closed if you have one of these.*
