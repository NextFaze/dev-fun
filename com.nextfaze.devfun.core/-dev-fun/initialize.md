[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [DevFun](index.md) / [initialize](./initialize.md)

# initialize

`fun initialize(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, vararg modules: `[`DevFunModule`](../-dev-fun-module/index.md)`, useServiceLoader: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/core/DevFun.kt#L286)

Initialize the static [devFun](../dev-fun.md) reference to `this`, [context](initialize.md#com.nextfaze.devfun.core.DevFun$initialize(android.content.Context, kotlin.Array((com.nextfaze.devfun.core.DevFunModule)), kotlin.Boolean)/context) to [Context.getApplicationContext](https://developer.android.com/reference/android/content/Context.html#getApplicationContext()), build
[instanceProviders](instance-providers.md), call module's [DevFunModule.initialize](../-dev-fun-module/initialize.md), and calls any [initializationCallbacks](#).

Can be called any number of times (will no-op more than once).

If using more than one `DevFun` instance, [dispose](dispose.md) should be called.

