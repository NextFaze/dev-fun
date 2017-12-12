[gh-pages](../index.md) / [com.nextfaze.devfun.menu](.)

## Package com.nextfaze.devfun.menu

Adds a developer menu, accessible by a floating cog (long-press to drag) or device button sequence.

### Types

| Name | Summary |
|---|---|
| [DevMenu](-dev-menu/index.md) | `class DevMenu : `[`AbstractDevFunModule`](../com.nextfaze.devfun.core/-abstract-dev-fun-module/index.md)`, `[`DeveloperMenu`](-developer-menu/index.md)`, `[`InstanceProvider`](../com.nextfaze.devfun.inject/-instance-provider/index.md) |
| [DeveloperMenu](-developer-menu/index.md) | `interface DeveloperMenu : `[`MenuController`](-menu-controller/index.md) |
| [MenuController](-menu-controller/index.md) | `interface MenuController` |
| [MenuHeader](-menu-header/index.md) | `interface MenuHeader<T : `[`View`](https://developer.android.com/reference/android/view/View.html)`>`<br>Provide an implementation of this to define your own header view. |

### Properties

| Name | Summary |
|---|---|
| [devMenu](dev-menu.md) | `val `[`DevFun`](../com.nextfaze.devfun.core/-dev-fun/index.md)`.devMenu: `[`DevMenu`](-dev-menu/index.md) |
