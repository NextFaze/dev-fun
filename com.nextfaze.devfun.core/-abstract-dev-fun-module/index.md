[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [AbstractDevFunModule](.)

# AbstractDevFunModule

`abstract class AbstractDevFunModule : `[`DevFunModule`](../-dev-fun-module/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/core/Module.kt#L41)

Implementation of [DevFunModule](../-dev-fun-module/index.md) providing various convenience functions.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `AbstractDevFunModule()`<br>Implementation of [DevFunModule](../-dev-fun-module/index.md) providing various convenience functions. |

### Properties

| Name | Summary |
|---|---|
| [context](context.md) | `val context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)<br>Convenience delegate to [DevFun.context](../-dev-fun/context.md). |
| [devFun](dev-fun.md) | `val devFun: `[`DevFun`](../-dev-fun/index.md)<br>Reference to owning [DevFun](../-dev-fun/index.md) instance. |

### Inherited Properties

| Name | Summary |
|---|---|
| [dependsOn](../-dev-fun-module/depends-on.md) | `open val dependsOn: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`DevFunModule`](../-dev-fun-module/index.md)`>>`<br>List of dependencies that this module requires to function correctly. |
| [name](../-dev-fun-module/name.md) | `open val name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Name of the module. |

### Functions

| Name | Summary |
|---|---|
| [get](get.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> get(): T` |
| [init](init.md) | `abstract fun init(context: `[`Context`](https://developer.android.com/reference/android/content/Context.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Called upon [initialize](initialize.md). |
| [initialize](initialize.md) | `open fun initialize(devFun: `[`DevFun`](../-dev-fun/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Module initialization. |
| [instanceOf](instance-of.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> instanceOf(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out T>): T` |

### Inherited Functions

| Name | Summary |
|---|---|
| [dispose](../-dev-fun-module/dispose.md) | `open fun dispose(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Module cleanup. |

### Inheritors

| Name | Summary |
|---|---|
| [DevHttpD](../../com.nextfaze.devfun.httpd/-dev-http-d/index.md) | `class DevHttpD : AbstractDevFunModule` |
| [DevMenu](../../com.nextfaze.devfun.menu/-dev-menu/index.md) | `class DevMenu : AbstractDevFunModule, `[`DeveloperMenu`](../../com.nextfaze.devfun.menu/-developer-menu/index.md)`, `[`InstanceProvider`](../../com.nextfaze.devfun.inject/-instance-provider/index.md) |
| [DevStetho](../../com.nextfaze.devfun.stetho/-dev-stetho/index.md) | `class DevStetho : AbstractDevFunModule` |
| [HttpFrontEnd](../../com.nextfaze.devfun.httpd.frontend/-http-front-end/index.md) | `class HttpFrontEnd : AbstractDevFunModule` |
| [InjectFromDagger2](../../com.nextfaze.devfun.inject.dagger2/-inject-from-dagger2/index.md) | `class InjectFromDagger2 : AbstractDevFunModule`<br>This module adds rudimentary support for searching Dagger 2.x component graphs for object instances. |
