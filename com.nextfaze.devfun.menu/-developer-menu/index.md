[gh-pages](../../index.md) / [com.nextfaze.devfun.menu](../index.md) / [DeveloperMenu](./index.md)

# DeveloperMenu

`interface DeveloperMenu : `[`MenuController`](../-menu-controller/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-menu/src/main/java/com/nextfaze/devfun/menu/DeveloperMenu.kt#L30)

### Properties

| Name | Summary |
|---|---|
| [isVisible](is-visible.md) | `abstract val isVisible: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Flag indicating if the menu is currently visible. |

### Inherited Properties

| Name | Summary |
|---|---|
| [actionDescription](../-menu-controller/action-description.md) | `abstract val actionDescription: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`?`<br>A human-readable description of how this controller is used. |
| [title](../-menu-controller/title.md) | `abstract val title: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>A human-readable description of the name of this controller. |

### Functions

| Name | Summary |
|---|---|
| [addController](add-controller.md) | `abstract fun addController(menuController: `[`MenuController`](../-menu-controller/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [hide](hide.md) | `abstract fun hide(activity: FragmentActivity): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [minusAssign](minus-assign.md) | `open operator fun minusAssign(menuController: `[`MenuController`](../-menu-controller/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [plusAssign](plus-assign.md) | `open operator fun plusAssign(menuController: `[`MenuController`](../-menu-controller/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [removeController](remove-controller.md) | `abstract fun removeController(menuController: `[`MenuController`](../-menu-controller/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [show](show.md) | `abstract fun show(activity: FragmentActivity): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inherited Functions

| Name | Summary |
|---|---|
| [attach](../-menu-controller/attach.md) | `abstract fun attach(developerMenu: `[`DeveloperMenu`](./index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Called when this controller is being attached/initialized to a [DeveloperMenu](./index.md). |
| [detach](../-menu-controller/detach.md) | `abstract fun detach(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Called when this controller is being detached/removed from a [DeveloperMenu](./index.md). |
| [onDismissed](../-menu-controller/on-dismissed.md) | `abstract fun onDismissed(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Called when [DeveloperMenu](./index.md) is dismissed. |
| [onShown](../-menu-controller/on-shown.md) | `abstract fun onShown(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Called when [DeveloperMenu](./index.md) is shown. |

### Inheritors

| Name | Summary |
|---|---|
| [DevMenu](../-dev-menu/index.md) | `class DevMenu : `[`AbstractDevFunModule`](../../com.nextfaze.devfun.core/-abstract-dev-fun-module/index.md)`, `[`DeveloperMenu`](./index.md) |
