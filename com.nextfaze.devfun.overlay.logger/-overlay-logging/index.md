[gh-pages](../../index.md) / [com.nextfaze.devfun.overlay.logger](../index.md) / [OverlayLogging](./index.md)

# OverlayLogging

`interface OverlayLogging` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/overlay/logger/Logging.kt#L40)

Handles the creation, maintenance, and permissions of [OverlayLogger](../-overlay-logger/index.md) instances.

### Functions

| Name | Summary |
|---|---|
| [createLogger](create-logger.md) | `abstract fun createLogger(name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`, updateCallback: `[`UpdateCallback`](../-update-callback.md)`, onClick: `[`OnClick`](../-on-click.md)`? = null): `[`OverlayLogger`](../-overlay-logger/index.md)<br>Creates an over logger instance. Call [OverlayLogger.start](../-overlay-logger/start.md) to add to window and start updating. |
