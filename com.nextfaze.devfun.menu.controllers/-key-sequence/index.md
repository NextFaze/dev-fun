[gh-pages](../../index.md) / [com.nextfaze.devfun.menu.controllers](../index.md) / [KeySequence](./index.md)

# KeySequence

`class KeySequence : `[`MenuController`](../../com.nextfaze.devfun.menu/-menu-controller/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-menu/src/main/java/com/nextfaze/devfun/menu/controllers/Sequence.kt#L44)

Allows toggling the Developer Menu using button/key sequences.

Two sequences are added by default:

* The grave/tilde key: `~`
* Volume button sequence: `down,down,up,down`

### Types

| Name | Summary |
|---|---|
| [Definition](-definition/index.md) | `data class Definition` |

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `KeySequence(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, activityProvider: `[`ActivityProvider`](../../com.nextfaze.devfun.core/-activity-provider.md)`, windowCallbacks: `[`WindowCallbacks`](../../com.nextfaze.devfun.internal/-window-callbacks/index.md)` = devFun.get())`<br>Allows toggling the Developer Menu using button/key sequences. |

### Properties

| Name | Summary |
|---|---|
| [actionDescription](action-description.md) | `val actionDescription: `[`SpannableStringBuilder`](https://developer.android.com/reference/android/text/SpannableStringBuilder.html)`?`<br>A human-readable description of how this controller is used. |
| [title](title.md) | `val title: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>A human-readable description of the name of this controller. |

### Functions

| Name | Summary |
|---|---|
| [attach](attach.md) | `fun attach(developerMenu: `[`DeveloperMenu`](../../com.nextfaze.devfun.menu/-developer-menu/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Called when this controller is being attached/initialized to a [DeveloperMenu](../../com.nextfaze.devfun.menu/-developer-menu/index.md). |
| [detach](detach.md) | `fun detach(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Called when this controller is being detached/removed from a [DeveloperMenu](../../com.nextfaze.devfun.menu/-developer-menu/index.md). |
| [minusAssign](minus-assign.md) | `operator fun minusAssign(sequenceDefinition: `[`Definition`](-definition/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onDismissed](on-dismissed.md) | `fun onDismissed(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Called when [DeveloperMenu](../../com.nextfaze.devfun.menu/-developer-menu/index.md) is dismissed. |
| [onShown](on-shown.md) | `fun onShown(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Called when [DeveloperMenu](../../com.nextfaze.devfun.menu/-developer-menu/index.md) is shown. |
| [plusAssign](plus-assign.md) | `operator fun plusAssign(sequenceDefinition: `[`Definition`](-definition/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
