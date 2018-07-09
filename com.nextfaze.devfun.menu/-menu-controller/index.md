[gh-pages](../../index.md) / [com.nextfaze.devfun.menu](../index.md) / [MenuController](./index.md)

# MenuController

`interface MenuController` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-menu/src/main/java/com/nextfaze/devfun/menu/DeveloperMenu.kt#L41)

### Properties

| Name | Summary |
|---|---|
| [actionDescription](action-description.md) | `abstract val actionDescription: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`?`<br>A human-readable description of how this controller is used. |
| [title](title.md) | `abstract val title: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>A human-readable description of the name of this controller. |

### Functions

| Name | Summary |
|---|---|
| [attach](attach.md) | `abstract fun attach(developerMenu: `[`DeveloperMenu`](../-developer-menu/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Called when this controller is being attached/initialized to a [DeveloperMenu](../-developer-menu/index.md). |
| [detach](detach.md) | `abstract fun detach(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Called when this controller is being detached/removed from a [DeveloperMenu](../-developer-menu/index.md). |
| [onDismissed](on-dismissed.md) | `abstract fun onDismissed(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Called when [DeveloperMenu](../-developer-menu/index.md) is dismissed. |
| [onShown](on-shown.md) | `abstract fun onShown(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Called when [DeveloperMenu](../-developer-menu/index.md) is shown. |

### Inheritors

| Name | Summary |
|---|---|
| [CogOverlay](../../com.nextfaze.devfun.menu.controllers/-cog-overlay/index.md) | `class CogOverlay : `[`MenuController`](./index.md)<br>Controls the floating cog overlay. |
| [DeveloperMenu](../-developer-menu/index.md) | `interface DeveloperMenu : `[`MenuController`](./index.md) |
| [KeySequence](../../com.nextfaze.devfun.menu.controllers/-key-sequence/index.md) | `class KeySequence : `[`MenuController`](./index.md)<br>Allows toggling the Developer Menu using button/key sequences. |
