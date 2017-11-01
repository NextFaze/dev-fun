[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [DevFun](.)

# DevFun

`class DevFun` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/core/DevFun.kt#L98)

Primary entry point and initializer of DevFun and associated libraries.

Modules can be added post- initialization by way of `devFun += SomeModule()` ([plusAssign](plus-assign.md)), after which [tryInitModules](try-init-modules.md) should be called.

To manually initialize, create instance and call [initialize](initialize.md).
A static reference will be set to this automatically, and can be retrieved using [devFun](../dev-fun.md).

e.g. `DevFun().initialize(applicationContext)`

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `DevFun()`<br>Primary entry point and initializer of DevFun and associated libraries. |

### Properties

| Name | Summary |
|---|---|
| [categories](categories.md) | `val categories: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`CategoryItem`](../-category-item/index.md)`>`<br>Processed list of [DevFunGenerated](../../com.nextfaze.devfun.generated/-dev-fun-generated/index.md) definitions - transformed, filtered, sorted, etc. |
| [context](context.md) | `val context: `[`Application`](https://developer.android.com/reference/android/app/Application.html)<br>Context used to initialize DevFun. |
| [definitions](definitions.md) | `val definitions: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`DevFunGenerated`](../../com.nextfaze.devfun.generated/-dev-fun-generated/index.md)`>`<br>List of raw [DevFunGenerated](../../com.nextfaze.devfun.generated/-dev-fun-generated/index.md) definitions. |
| [instanceProviders](instance-providers.md) | `val instanceProviders: `[`CompositeInstanceProvider`](../../com.nextfaze.devfun.inject/-composite-instance-provider/index.md)<br>Composite list of all [InstanceProvider](../../com.nextfaze.devfun.inject/-instance-provider/index.md)s. |
| [isInitialized](is-initialized.md) | `val isInitialized: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Flag indicating if this instance of DevFun has been initialized. |

### Functions

| Name | Summary |
|---|---|
| [dispose](dispose.md) | `fun dispose(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Disposes initialized modules and clears self (static), context, and module references. |
| [get](get.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> get(): T`<br>Get an instance of a class using [instanceProviders](instance-providers.md). |
| [initialize](initialize.md) | `fun initialize(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`, vararg modules: `[`DevFunModule`](../-dev-fun-module/index.md)`, useServiceLoader: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Initialize the static [devFun](../dev-fun.md) reference to `this`, [context](initialize.md#com.nextfaze.devfun.core.DevFun$initialize(android.content.Context, kotlin.Array((com.nextfaze.devfun.core.DevFunModule)), kotlin.Boolean)/context) to [Context.getApplicationContext](https://developer.android.com/reference/android/content/Context.html#getApplicationContext()), build [instanceProviders](instance-providers.md), call module's [DevFunModule.initialize](../-dev-fun-module/initialize.md), and calls any [initializationCallbacks](#). |
| [instanceOf](instance-of.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> instanceOf(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out T>): T`<br>Get an instance of a class using [instanceProviders](instance-providers.md). |
| [minusAssign](minus-assign.md) | `operator fun minusAssign(onInitialized: `[`OnInitialized`](../-on-initialized.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Remove an initialization callback. |
| [plusAssign](plus-assign.md) | `operator fun plusAssign(module: `[`DevFunModule`](../-dev-fun-module/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Add a module.`operator fun plusAssign(onInitialized: `[`OnInitialized`](../-on-initialized.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Add an initialization callback. |
| [tryInitModules](try-init-modules.md) | `fun tryInitModules(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Attempts to initialize uninitialized modules. |

### Extension Properties

| Name | Summary |
|---|---|
| [devHttpD](../../com.nextfaze.devfun.httpd/dev-http-d.md) | `val DevFun.devHttpD: `[`DevHttpD`](../../com.nextfaze.devfun.httpd/-dev-http-d/index.md) |
| [devMenu](../../com.nextfaze.devfun.menu/dev-menu.md) | `val DevFun.devMenu: `[`DevMenu`](../../com.nextfaze.devfun.menu/-dev-menu/index.md) |
| [httpFrontEnd](../../com.nextfaze.devfun.httpd.frontend/http-front-end.md) | `val DevFun.httpFrontEnd: `[`HttpFrontEnd`](../../com.nextfaze.devfun.httpd.frontend/-http-front-end/index.md) |
| [stetho](../../com.nextfaze.devfun.stetho/stetho.md) | `val DevFun.stetho: `[`DevStetho`](../../com.nextfaze.devfun.stetho/-dev-stetho/index.md) |
