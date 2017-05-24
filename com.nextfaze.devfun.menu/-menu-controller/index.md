[gh-pages](../../index.md) / [com.nextfaze.devfun.menu](../index.md) / [MenuController](.)

# MenuController

`interface MenuController` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-menu/src/main/java/com/nextfaze/devfun/menu/DeveloperMenu.kt#L27)

### Functions

| Name | Summary |
|---|---|
| [attach](attach.md) | `abstract fun attach(developerMenu: `[`DeveloperMenu`](../-developer-menu/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [detach](detach.md) | `abstract fun detach(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onDismissed](on-dismissed.md) | `abstract fun onDismissed(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onShown](on-shown.md) | `abstract fun onShown(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inheritors

| Name | Summary |
|---|---|
| [CogOverlay](../../com.nextfaze.devfun.menu.controllers/-cog-overlay/index.md) | `class CogOverlay : MenuController`<br>Controls the floating cog overlay. |
| [DeveloperMenu](../-developer-menu/index.md) | `interface DeveloperMenu : MenuController` |
| [KeySequence](../../com.nextfaze.devfun.menu.controllers/-key-sequence/index.md) | `class KeySequence : MenuController` |
