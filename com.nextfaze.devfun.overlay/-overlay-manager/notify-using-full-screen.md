[gh-pages](../../index.md) / [com.nextfaze.devfun.overlay](../index.md) / [OverlayManager](index.md) / [notifyUsingFullScreen](./notify-using-full-screen.md)

# notifyUsingFullScreen

`abstract fun notifyUsingFullScreen(who: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/overlay/Overlays.kt#L139)

Notify DevFun that you want to present a full-screen/dialog to signal that you want overlays to be hidden.

Calling this effectively associates [who](notify-using-full-screen.md#com.nextfaze.devfun.overlay.OverlayManager$notifyUsingFullScreen(kotlin.Any)/who) with the current display - Android creates a new [Display](https://developer.android.com/reference/android/view/Display.html) for each activity.
Multiple calls to this does not require multiple finish calls.

Be sure to notify when you're done with [notifyFinishUsingFullScreen](notify-finish-using-full-screen.md).

### Parameters

`who` - A reference to the user of the notification (a weak reference will be held).

**Return**
`true` if the notification was taken successfully (requires there be an Activity present), or `false` if no Activity present.

**See Also**

[removeFullScreenUsageStateListener](remove-full-screen-usage-state-listener.md)

[isFullScreenInUse](is-full-screen-in-use.md)

[addFullScreenUsageStateListener](add-full-screen-usage-state-listener.md)

[removeFullScreenUsageStateListener](remove-full-screen-usage-state-listener.md)

