[gh-pages](../../index.md) / [com.nextfaze.devfun.overlay](../index.md) / [OverlayPermissions](index.md) / [requestPermission](./request-permission.md)

# requestPermission

`abstract fun requestPermission(reason: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`? = null): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/overlay/Permissions.kt#L74)

Signal a request to the user that we want permission for overlays.

Calling this when permissions are already granted will do nothing.

### Parameters

`reason` - Optional reason to show the user.

**See Also**

[canDrawOverlays](can-draw-overlays.md)

