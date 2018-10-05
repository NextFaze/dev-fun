[gh-pages](../../index.md) / [com.nextfaze.devfun.overlay](../index.md) / [OverlayPermissions](./index.md)

# OverlayPermissions

`interface OverlayPermissions` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/overlay/Permissions.kt#L46)

Handles overlay permissions.

**See Also**

[OverlayManager](../-overlay-manager/index.md)

[OverlayWindow](../-overlay-window/index.md)

### Properties

| Name | Summary |
|---|---|
| [canDrawOverlays](can-draw-overlays.md) | `abstract val canDrawOverlays: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Flag indicating if the user has granted overlay permissions. |
| [shouldRequestPermission](should-request-permission.md) | `abstract val shouldRequestPermission: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Flag indicating if we should request overlay permissions - i.e. we don't have them and the user has not denied them. |

### Functions

| Name | Summary |
|---|---|
| [addOverlayPermissionListener](add-overlay-permission-listener.md) | `abstract fun addOverlayPermissionListener(listener: `[`OverlayPermissionListener`](../-overlay-permission-listener.md)`): `[`OverlayPermissionListener`](../-overlay-permission-listener.md)<br>Add a listener for when overlay permissions have changed. |
| [minusAssign](minus-assign.md) | `open operator fun minusAssign(listener: `[`OverlayPermissionListener`](../-overlay-permission-listener.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Remove a listener for when overlay permissions have changed. |
| [plusAssign](plus-assign.md) | `open operator fun plusAssign(listener: `[`OverlayPermissionListener`](../-overlay-permission-listener.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Add a listener for when overlay permissions have changed. |
| [removeOverlayPermissionListener](remove-overlay-permission-listener.md) | `abstract fun removeOverlayPermissionListener(listener: `[`OverlayPermissionListener`](../-overlay-permission-listener.md)`): `[`OverlayPermissionListener`](../-overlay-permission-listener.md)<br>Remove a listener for when overlay permissions have changed. |
| [requestPermission](request-permission.md) | `abstract fun requestPermission(reason: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`? = null): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Signal a request to the user that we want permission for overlays. |
