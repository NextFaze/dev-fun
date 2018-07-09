[gh-pages](../../index.md) / [com.nextfaze.devfun.overlay](../index.md) / [OverlayPermissions](index.md) / [shouldRequestPermission](./should-request-permission.md)

# shouldRequestPermission

`abstract val shouldRequestPermission: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/overlay/Permissions.kt#L63)

Flag indicating if we should request overlay permissions - i.e. we don't have them and the user has not denied them.

Calling [requestPermission](request-permission.md) will only show the request if the user has not denied them already. This flag is here to allow you to
know if you should generate/render the reasoning string etc, and as to whether or not a resumed FragmentActivity is present.
Calling [requestPermission](request-permission.md) when this is false will do nothing.

**See Also**

[canDrawOverlays](can-draw-overlays.md)

[requestPermission](request-permission.md)

