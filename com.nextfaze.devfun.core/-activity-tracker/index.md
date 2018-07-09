[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [ActivityTracker](./index.md)

# ActivityTracker

`interface ActivityTracker` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/core/ActivityTracking.kt#L27)

Activity tracker that provides the currently (resumed) activity if present.

**See Also**

[ActivityProvider](../-activity-provider.md)

### Properties

| Name | Summary |
|---|---|
| [activity](activity.md) | `abstract val activity: `[`Activity`](https://developer.android.com/reference/android/app/Activity.html)`?`<br>The current activity - may not be in a resumed state. |
| [isResumed](is-resumed.md) | `abstract val isResumed: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Is the current activity resumed. |

### Extension Properties

| Name | Summary |
|---|---|
| [resumedActivity](../resumed-activity.md) | `val `[`ActivityTracker`](./index.md)`.resumedActivity: `[`Activity`](https://developer.android.com/reference/android/app/Activity.html)`?`<br>Extension function to only get the activity if it is resumed. |
