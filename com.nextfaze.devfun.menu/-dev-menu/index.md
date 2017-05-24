[gh-pages](../../index.md) / [com.nextfaze.devfun.menu](../index.md) / [DevMenu](.)

# DevMenu

`class DevMenu : `[`AbstractDevFunModule`](../../com.nextfaze.devfun.core/-abstract-dev-fun-module/index.md)`, `[`DeveloperMenu`](../-developer-menu/index.md)`, `[`InstanceProvider`](../../com.nextfaze.devfun.inject/-instance-provider/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-menu/src/main/java/com/nextfaze/devfun/menu/DeveloperMenu.kt#L36)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `DevMenu()` |

### Properties

| Name | Summary |
|---|---|
| [isVisible](is-visible.md) | `val isVisible: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

### Inherited Properties

| Name | Summary |
|---|---|
| [context](../../com.nextfaze.devfun.core/-abstract-dev-fun-module/context.md) | `val context: Context`<br>Convenience delegate to [DevFun.context](../../com.nextfaze.devfun.core/-dev-fun/context.md). |
| [devFun](../../com.nextfaze.devfun.core/-abstract-dev-fun-module/dev-fun.md) | `val devFun: `[`DevFun`](../../com.nextfaze.devfun.core/-dev-fun/index.md)<br>Reference to owning [DevFun](../../com.nextfaze.devfun.core/-dev-fun/index.md) instance. |

### Functions

| Name | Summary |
|---|---|
| [addController](add-controller.md) | `fun addController(menuController: `[`MenuController`](../-menu-controller/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [attach](attach.md) | `fun attach(developerMenu: `[`DeveloperMenu`](../-developer-menu/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [detach](detach.md) | `fun detach(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [dispose](dispose.md) | `fun dispose(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Module cleanup. |
| [get](get.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> get(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out T>): T?`<br>Try to get an instance of some [clazz](get.md#com.nextfaze.devfun.menu.DevMenu$get(kotlin.reflect.KClass((com.nextfaze.devfun.menu.DevMenu.get.T)))/clazz). |
| [hide](hide.md) | `fun hide(activity: FragmentActivity): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [init](init.md) | `fun init(context: Context): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Called upon [initialize](../../com.nextfaze.devfun.core/-abstract-dev-fun-module/initialize.md). |
| [onDismissed](on-dismissed.md) | `fun onDismissed(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onShown](on-shown.md) | `fun onShown(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [removeController](remove-controller.md) | `fun removeController(menuController: `[`MenuController`](../-menu-controller/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [show](show.md) | `fun show(activity: FragmentActivity): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inherited Functions

| Name | Summary |
|---|---|
| [get](../../com.nextfaze.devfun.core/-abstract-dev-fun-module/get.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> get(): T` |
| [initialize](../../com.nextfaze.devfun.core/-abstract-dev-fun-module/initialize.md) | `open fun initialize(devFun: `[`DevFun`](../../com.nextfaze.devfun.core/-dev-fun/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Module initialization. |
| [instanceOf](../../com.nextfaze.devfun.core/-abstract-dev-fun-module/instance-of.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> instanceOf(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out T>): T` |
| [minusAssign](../-developer-menu/minus-assign.md) | `open operator fun minusAssign(menuController: `[`MenuController`](../-menu-controller/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [plusAssign](../-developer-menu/plus-assign.md) | `open operator fun plusAssign(menuController: `[`MenuController`](../-menu-controller/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
