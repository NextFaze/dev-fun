[gh-pages](../../index.md) / [com.nextfaze.devfun.overlay](../index.md) / [OverlayManager](index.md) / [isFullScreenInUse](./is-full-screen-in-use.md)

# isFullScreenInUse

`abstract val isFullScreenInUse: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/overlay/Overlays.kt#L121)

Flag indicating if the full-screen is in use for the current display.

Android creates a new [Display](https://developer.android.com/reference/android/view/Display.html) for each activity. Thus this flag effectively represents the current activity.

**See Also**

[notifyUsingFullScreen](notify-using-full-screen.md)

[addFullScreenUsageStateListener](add-full-screen-usage-state-listener.md)

