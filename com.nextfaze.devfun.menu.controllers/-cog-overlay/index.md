[gh-pages](../../index.md) / [com.nextfaze.devfun.menu.controllers](../index.md) / [CogOverlay](.)

# CogOverlay

`class CogOverlay : `[`MenuController`](../../com.nextfaze.devfun.menu/-menu-controller/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-menu/src/main/java/com/nextfaze/devfun/menu/controllers/Cog.kt#L46)

Controls the floating cog overlay.

Manages/requests permissions as needed, and hides/shows when app view context changes.

Background color/tint of the cog can be changed by declaring (overriding) a color resource `df_menu_cog_background_color`

e.g.

``` xml
    <color name="df_menu_cog_background_color">#FF0000</color> <!-- red -->
```

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `CogOverlay(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, activityProvider: `[`ActivityProvider`](../../com.nextfaze.devfun.internal/-activity-provider.md)`)`<br>Controls the floating cog overlay. |

### Functions

| Name | Summary |
|---|---|
| [attach](attach.md) | `fun attach(developerMenu: `[`DeveloperMenu`](../../com.nextfaze.devfun.menu/-developer-menu/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [detach](detach.md) | `fun detach(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onDismissed](on-dismissed.md) | `fun onDismissed(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onShown](on-shown.md) | `fun onShown(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
