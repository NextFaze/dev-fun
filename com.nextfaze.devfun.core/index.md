[gh-pages](../index.md) / [com.nextfaze.devfun.core](./index.md)

## Package com.nextfaze.devfun.core

Core [DevFun](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.core/-dev-fun/) package - handles loading and processing of modules and definitions.

### Types

| Name | Summary |
|---|---|
| [AbstractDevFunModule](-abstract-dev-fun-module/index.md) | `abstract class AbstractDevFunModule : `[`DevFunModule`](-dev-fun-module/index.md)<br>Implementation of [DevFunModule](-dev-fun-module/index.md) providing various convenience functions. |
| [ActivityProvider](-activity-provider.md) | `interface ActivityProvider : () -> `[`Activity`](https://developer.android.com/reference/android/app/Activity.html)`?`<br>Function signature of DevFun's activity tracker/provider. |
| [ActivityTracker](-activity-tracker/index.md) | `interface ActivityTracker`<br>Activity tracker that provides the currently (resumed) activity if present. |
| [Composite](-composite/index.md) | `interface Composite<T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> : `[`Iterable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)`<`[`T`](-composite/index.md#T)`>`<br>Use by providers to facilitate user provided types [T](-composite/index.md#T) to the composting provider. |
| [DevFun](-dev-fun/index.md) | `class DevFun`<br>Primary entry point and initializer of DevFun and associated libraries. |
| [DevFunInitializerProvider](-dev-fun-initializer-provider/index.md) | `class DevFunInitializerProvider : `[`ContentProvider`](https://developer.android.com/reference/android/content/ContentProvider.html)<br>Used to automatically initialize [DevFun](-dev-fun/index.md) without user input. |
| [DevFunModule](-dev-fun-module/index.md) | `interface DevFunModule`<br>Modules that extend/use the functionality of [DevFun](-dev-fun/index.md). |
| [ForegroundTracker](-foreground-tracker/index.md) | `interface ForegroundTracker`<br>Application foreground state tracker. |

### Type Aliases

| Name | Summary |
|---|---|
| [ForegroundChangeListener](-foreground-change-listener.md) | `typealias ForegroundChangeListener = (foreground: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Function signature of callbacks for foreground status changes. |
| [OnInitialized](-on-initialized.md) | `typealias OnInitialized = `[`DevFun`](-dev-fun/index.md)`.() -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Callback signature if/when [DevFun](-dev-fun/index.md) has been initialized. |

### Properties

| Name | Summary |
|---|---|
| [devFun](dev-fun.md) | `val devFun: `[`DevFun`](-dev-fun/index.md)<br>Currently active/initialized instance of [DevFun](-dev-fun/index.md) |
| [devFunVerbose](dev-fun-verbose.md) | `var devFunVerbose: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Controls trace-level logging. Disabled (`false`) by default. |
| [isDevFunInitialized](is-dev-fun-initialized.md) | `val isDevFunInitialized: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Flag indicating if DevFun has been initialized yet. |
| [resumedActivity](resumed-activity.md) | `val `[`ActivityTracker`](-activity-tracker/index.md)`.resumedActivity: `[`Activity`](https://developer.android.com/reference/android/app/Activity.html)`?`<br>Extension function to only get the activity if it is resumed. |

### Functions

| Name | Summary |
|---|---|
| [call](call.md) | `fun `[`FunctionItem`](../com.nextfaze.devfun.function/-function-item/index.md)`.call(invoker: `[`Invoker`](../com.nextfaze.devfun.invoke/-invoker/index.md)` = devFun.get()): `[`InvokeResult`](../com.nextfaze.devfun.function/-invoke-result/index.md)`?`<br>Convenience function for invoking a [FunctionItem](../com.nextfaze.devfun.function/-function-item/index.md) using the current [devFun](dev-fun.md) instance. |
