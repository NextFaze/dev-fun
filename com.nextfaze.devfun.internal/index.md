[gh-pages](../index.md) / [com.nextfaze.devfun.internal](.)

## Package com.nextfaze.devfun.internal

### Types

| Name | Summary |
|---|---|
| [AbstractActivityLifecycleCallbacks](-abstract-activity-lifecycle-callbacks/index.md) | `abstract class AbstractActivityLifecycleCallbacks : `[`ActivityLifecycleCallbacks`](https://developer.android.com/reference/android/app/Application/ActivityLifecycleCallbacks.html) |
| [ActivityTracker](-activity-tracker/index.md) | `class ActivityTracker : `[`ActivityProvider`](-activity-provider.md) |

### Type Aliases

| Name | Summary |
|---|---|
| [ActivityProvider](-activity-provider.md) | `typealias ActivityProvider = () -> `[`Activity`](https://developer.android.com/reference/android/app/Activity.html)`?` |

### Extensions for External Classes

| Name | Summary |
|---|---|
| [android.app.Application.ActivityLifecycleCallbacks](android.app.-application.-activity-lifecycle-callbacks/index.md) |  |
| [android.content.Context](android.content.-context/index.md) |  |
| [kotlin.String](kotlin.-string/index.md) |  |
| [kotlin.reflect.KClass](kotlin.reflect.-k-class/index.md) |  |
| [org.slf4j.Logger](org.slf4j.-logger/index.md) |  |

### Properties

| Name | Summary |
|---|---|
| [devFunVerbose](dev-fun-verbose.md) | `var devFunVerbose: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Controls trace-level logging. Disabled (`false`) by default. |
| [isEmulator](is-emulator.md) | `val isEmulator: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

### Functions

| Name | Summary |
|---|---|
| [logger](logger.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> T.logger(): Logger`<br>Creates a new logger instance using the containing class' qualified name.`fun logger(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): Logger`<br>Creates a new logger instance using [name](logger.md#com.nextfaze.devfun.internal$logger(kotlin.String)/name). |
