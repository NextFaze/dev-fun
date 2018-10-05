[gh-pages](../../index.md) / [com.nextfaze.devfun.overlay](../index.md) / [OverlayManager](index.md) / [notifyFinishUsingFullScreen](./notify-finish-using-full-screen.md)

# notifyFinishUsingFullScreen

`abstract fun notifyFinishUsingFullScreen(who: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/overlay/Overlays.kt#L155)

Notify that you are done with your full-screen/dialog usage to signal the overlays they can be visible (if nothing else is using it).

### Parameters

`who` - The same reference as passed in with [notifyUsingFullScreen](notify-using-full-screen.md).

**See Also**

[notifyUsingFullScreen](notify-using-full-screen.md)

[isFullScreenInUse](is-full-screen-in-use.md)

[addFullScreenUsageStateListener](add-full-screen-usage-state-listener.md)

[removeFullScreenUsageStateListener](remove-full-screen-usage-state-listener.md)

