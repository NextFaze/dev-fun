[gh-pages](../../index.md) / [com.nextfaze.devfun.httpd.frontend](../index.md) / [HttpFrontEnd](./index.md)

# HttpFrontEnd

`class HttpFrontEnd : `[`AbstractDevFunModule`](../../com.nextfaze.devfun.core/-abstract-dev-fun-module/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-httpd-frontend/src/main/java/com/nextfaze/devfun/httpd/frontend/FrontEnd.kt#L25)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `HttpFrontEnd()` |

### Properties

| Name | Summary |
|---|---|
| [dependsOn](depends-on.md) | `val dependsOn: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`DevFunModule`](../../com.nextfaze.devfun.core/-dev-fun-module/index.md)`>>`<br>List of dependencies that this module requires to function correctly. |

### Inherited Properties

| Name | Summary |
|---|---|
| [context](../../com.nextfaze.devfun.core/-abstract-dev-fun-module/context.md) | `val context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)<br>Convenience delegate to [DevFun.context](../../com.nextfaze.devfun.core/-dev-fun/context.md). |
| [devFun](../../com.nextfaze.devfun.core/-abstract-dev-fun-module/dev-fun.md) | `val devFun: `[`DevFun`](../../com.nextfaze.devfun.core/-dev-fun/index.md)<br>Reference to owning [DevFun](../../com.nextfaze.devfun.core/-dev-fun/index.md) instance. |

### Functions

| Name | Summary |
|---|---|
| [init](init.md) | `fun init(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Called upon [initialize](../../com.nextfaze.devfun.core/-abstract-dev-fun-module/initialize.md). |

### Inherited Functions

| Name | Summary |
|---|---|
| [get](../../com.nextfaze.devfun.core/-abstract-dev-fun-module/get.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> get(): `[`T`](../../com.nextfaze.devfun.core/-abstract-dev-fun-module/get.md#T) |
| [initialize](../../com.nextfaze.devfun.core/-abstract-dev-fun-module/initialize.md) | `open fun initialize(devFun: `[`DevFun`](../../com.nextfaze.devfun.core/-dev-fun/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Module initialization. |
| [instanceOf](../../com.nextfaze.devfun.core/-abstract-dev-fun-module/instance-of.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> instanceOf(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`T`](../../com.nextfaze.devfun.core/-abstract-dev-fun-module/instance-of.md#T)`>): `[`T`](../../com.nextfaze.devfun.core/-abstract-dev-fun-module/instance-of.md#T) |
