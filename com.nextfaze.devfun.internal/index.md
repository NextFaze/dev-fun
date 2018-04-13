[gh-pages](../index.md) / [com.nextfaze.devfun.internal](./index.md)

## Package com.nextfaze.devfun.internal

### Types

| Name | Summary |
|---|---|
| [AbstractActivityLifecycleCallbacks](-abstract-activity-lifecycle-callbacks/index.md) | `abstract class AbstractActivityLifecycleCallbacks : `[`ActivityLifecycleCallbacks`](https://developer.android.com/reference/android/app/Application/ActivityLifecycleCallbacks.html) |

### Type Aliases

| Name | Summary |
|---|---|
| [OnActivityCreated](-on-activity-created.md) | `typealias OnActivityCreated = (activity: `[`Activity`](https://developer.android.com/reference/android/app/Activity.html)`, savedInstanceState: `[`Bundle`](https://developer.android.com/reference/android/os/Bundle.html)`?) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [OnActivityDestroyed](-on-activity-destroyed.md) | `typealias OnActivityDestroyed = (activity: `[`Activity`](https://developer.android.com/reference/android/app/Activity.html)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [OnActivityPaused](-on-activity-paused.md) | `typealias OnActivityPaused = (activity: `[`Activity`](https://developer.android.com/reference/android/app/Activity.html)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [OnActivityResumed](-on-activity-resumed.md) | `typealias OnActivityResumed = (activity: `[`Activity`](https://developer.android.com/reference/android/app/Activity.html)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [OnActivitySave](-on-activity-save.md) | `typealias OnActivitySave = (activity: `[`Activity`](https://developer.android.com/reference/android/app/Activity.html)`, outState: `[`Bundle`](https://developer.android.com/reference/android/os/Bundle.html)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [OnActivityStarted](-on-activity-started.md) | `typealias OnActivityStarted = (activity: `[`Activity`](https://developer.android.com/reference/android/app/Activity.html)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [OnActivityStopped](-on-activity-stopped.md) | `typealias OnActivityStopped = (activity: `[`Activity`](https://developer.android.com/reference/android/app/Activity.html)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Extensions for External Classes

| Name | Summary |
|---|---|
| [android.app.Application.ActivityLifecycleCallbacks](android.app.-application.-activity-lifecycle-callbacks/index.md) |  |
| [android.content.Context](android.content.-context/index.md) |  |
| [android.text.SpannableStringBuilder](android.text.-spannable-string-builder/index.md) |  |
| [java.lang.Appendable](java.lang.-appendable/index.md) |  |
| [java.lang.reflect.Member](java.lang.reflect.-member/index.md) |  |
| [kotlin.String](kotlin.-string/index.md) |  |
| [kotlin.reflect.KClass](kotlin.reflect.-k-class/index.md) |  |
| [org.slf4j.Logger](org.slf4j.-logger/index.md) |  |

### Properties

| Name | Summary |
|---|---|
| [allowTraceLogs](allow-trace-logs.md) | `var allowTraceLogs: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Controls trace-level logging. Disabled (`false`) by default. |

### Functions

| Name | Summary |
|---|---|
| [bold](bold.md) | `fun bold(str: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`): `[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`, `[`StyleSpan`](https://developer.android.com/reference/android/text/style/StyleSpan.html)`>` |
| [logger](logger.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> `[`T`](logger.md#T)`.logger(): Logger`<br>Creates a new logger instance using the containing class' qualified name.`fun logger(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`): Logger`<br>Creates a new logger instance using [name](logger.md#com.nextfaze.devfun.internal$logger(kotlin.String)/name). |
| [pre](pre.md) | `fun pre(str: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`): `[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`, `[`TypefaceSpan`](https://developer.android.com/reference/android/text/style/TypefaceSpan.html)`>` |
| [u](u.md) | `fun u(str: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`): `[`Pair`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-pair/index.html)`<`[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`, `[`UnderlineSpan`](https://developer.android.com/reference/android/text/style/UnderlineSpan.html)`>` |
