[gh-pages](../../index.md) / [com.nextfaze.devfun.overlay.logger](../index.md) / [OverlayLogger](index.md) / [refreshRate](./refresh-rate.md)

# refreshRate

`abstract var refreshRate: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/overlay/logger/Logger.kt#L52)

How frequently this overlay should update (in milliseconds). *(default: `1000`)*

Be aware: Low/quick refresh rates may slow down the app due to heavy reflection (depending on what/where the logging occurs).

