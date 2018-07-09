[gh-pages](../../index.md) / [com.nextfaze.devfun.overlay](../index.md) / [OverlayManager](index.md) / [plusAssign](./plus-assign.md)

# plusAssign

`open operator fun plusAssign(listener: `[`FullScreenUsageStateListener`](../-full-screen-usage-state-listener.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/overlay/Overlays.kt#L185)

Add a listener for the current full-screen status. This callback may be called multiple times with the same value.

Will be called upon each Activity onResume and each time something notifies their usage.

**See Also**

[addFullScreenUsageStateListener](add-full-screen-usage-state-listener.md)

[minusAssign](minus-assign.md)

