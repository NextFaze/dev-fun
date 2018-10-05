[gh-pages](../../index.md) / [com.nextfaze.devfun.reference](../index.md) / [DeveloperLogger](./index.md)

# DeveloperLogger

`@Target([AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.CLASS, AnnotationTarget.PROPERTY_GETTER]) annotation class DeveloperLogger` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/reference/DeveloperLogger.kt#L21)

Annotated references will be rendered as an overlay.

TODO support FIELD (if this is desired make an issue to expedite).

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `DeveloperLogger(enabled: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true, refreshRate: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)` = 1000L, snapToEdge: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, dock: `[`Dock`](../../com.nextfaze.devfun.overlay/-dock/index.md)` = Dock.TOP_LEFT, delta: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 0f, top: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 0f, left: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)` = 0f)`<br>Annotated references will be rendered as an overlay. |

### Properties

| Name | Summary |
|---|---|
| [delta](delta.md) | `val delta: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [dock](dock.md) | `val dock: `[`Dock`](../../com.nextfaze.devfun.overlay/-dock/index.md) |
| [enabled](enabled.md) | `val enabled: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [left](left.md) | `val left: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [refreshRate](refresh-rate.md) | `val refreshRate: `[`Long`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [snapToEdge](snap-to-edge.md) | `val snapToEdge: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [top](top.md) | `val top: `[`Float`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
