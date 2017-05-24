[gh-pages](../../index.md) / [com.nextfaze.devfun.menu](../index.md) / [DeveloperMenu](.)

# DeveloperMenu

`interface DeveloperMenu : `[`MenuController`](../-menu-controller/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-menu/src/main/java/com/nextfaze/devfun/menu/DeveloperMenu.kt#L15)

### Properties

| Name | Summary |
|---|---|
| [isVisible](is-visible.md) | `abstract val isVisible: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

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
| [attach](../-menu-controller/attach.md) | `abstract fun attach(developerMenu: DeveloperMenu): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [detach](../-menu-controller/detach.md) | `abstract fun detach(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onDismissed](../-menu-controller/on-dismissed.md) | `abstract fun onDismissed(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onShown](../-menu-controller/on-shown.md) | `abstract fun onShown(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inheritors

| Name | Summary |
|---|---|
| [DevMenu](../-dev-menu/index.md) | `class DevMenu : `[`AbstractDevFunModule`](../../com.nextfaze.devfun.core/-abstract-dev-fun-module/index.md)`, DeveloperMenu, `[`InstanceProvider`](../../com.nextfaze.devfun.inject/-instance-provider/index.md) |
