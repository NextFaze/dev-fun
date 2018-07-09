[gh-pages](../../index.md) / [com.nextfaze.devfun.overlay.logger](../index.md) / [OverlayLogging](index.md) / [createLogger](./create-logger.md)

# createLogger

`abstract fun createLogger(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, updateCallback: `[`UpdateCallback`](../-update-callback.md)`, onClick: `[`OnClick`](../-on-click.md)`? = null): `[`OverlayLogger`](../-overlay-logger/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/overlay/logger/Logging.kt#L35)

Creates an over logger instance. Call [OverlayLogger.start](../-overlay-logger/start.md) to add to window and start updating.

Instances created from this will *not* be managed automatically.
This function is used internally to create instances that *are* managed automatically however.

**See Also**

[OverlayLogger.stop](../-overlay-logger/stop.md)

