[gh-pages](../../index.md) / [com.nextfaze.devfun.menu.controllers](../index.md) / [CogOverlay](./index.md)

# CogOverlay

`class CogOverlay : `[`MenuController`](../../com.nextfaze.devfun.menu/-menu-controller/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-menu/src/main/java/com/nextfaze/devfun/menu/controllers/Cog.kt#L53)

Controls the floating cog overlay.

Manages/requests permissions as needed, and hides/shows when app view context changes.

Background color/tint of the cog can be changed by declaring (overriding) a color resource `df_menu_cog_tint`

e.g.

``` xml
<color name="df_menu_cog_tint">#77FF0000</color> <!-- red -->
```

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `CogOverlay(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, activityProvider: `[`ActivityProvider`](../../com.nextfaze.devfun.core/-activity-provider.md)`, overlays: `[`OverlayManager`](../../com.nextfaze.devfun.overlay/-overlay-manager/index.md)`)`<br>Controls the floating cog overlay. |

### Properties

| Name | Summary |
|---|---|
| [actionDescription](action-description.md) | `val actionDescription: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`?`<br>A human-readable description of how this controller is used. |
| [title](title.md) | `val title: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>A human-readable description of the name of this controller. |

### Functions

| Name | Summary |
|---|---|
| [attach](attach.md) | `fun attach(developerMenu: `[`DeveloperMenu`](../../com.nextfaze.devfun.menu/-developer-menu/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Called when this controller is being attached/initialized to a [DeveloperMenu](../../com.nextfaze.devfun.menu/-developer-menu/index.md). |
| [detach](detach.md) | `fun detach(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Called when this controller is being detached/removed from a [DeveloperMenu](../../com.nextfaze.devfun.menu/-developer-menu/index.md). |
| [onDismissed](on-dismissed.md) | `fun onDismissed(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Called when [DeveloperMenu](../../com.nextfaze.devfun.menu/-developer-menu/index.md) is dismissed. |
| [onShown](on-shown.md) | `fun onShown(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Called when [DeveloperMenu](../../com.nextfaze.devfun.menu/-developer-menu/index.md) is shown. |
