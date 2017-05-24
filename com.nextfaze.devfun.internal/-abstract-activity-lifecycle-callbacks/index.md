[gh-pages](../../index.md) / [com.nextfaze.devfun.internal](../index.md) / [AbstractActivityLifecycleCallbacks](.)

# AbstractActivityLifecycleCallbacks

`abstract class AbstractActivityLifecycleCallbacks : ActivityLifecycleCallbacks` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/internal/ActivityTracking.kt#L25)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `AbstractActivityLifecycleCallbacks()` |

### Functions

| Name | Summary |
|---|---|
| [onActivityCreated](on-activity-created.md) | `open fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onActivityDestroyed](on-activity-destroyed.md) | `open fun onActivityDestroyed(activity: Activity): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onActivityPaused](on-activity-paused.md) | `open fun onActivityPaused(activity: Activity): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onActivityResumed](on-activity-resumed.md) | `open fun onActivityResumed(activity: Activity): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onActivitySaveInstanceState](on-activity-save-instance-state.md) | `open fun onActivitySaveInstanceState(activity: Activity, outState: Bundle): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onActivityStarted](on-activity-started.md) | `open fun onActivityStarted(activity: Activity): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [onActivityStopped](on-activity-stopped.md) | `open fun onActivityStopped(activity: Activity): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Extension Functions

| Name | Summary |
|---|---|
| [unregister](../android.app.-application.-activity-lifecycle-callbacks/unregister.md) | `fun ActivityLifecycleCallbacks.unregister(context: Context): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
