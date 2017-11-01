[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [DevFunModule](.)

# DevFunModule

`interface DevFunModule` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/core/Module.kt#L12)

Modules that extend/use the functionality of [DevFun](../-dev-fun/index.md).

Instances will be loaded using Java's [ServiceLoader](https://developer.android.com/reference/java/util/ServiceLoader.html).

### Properties

| Name | Summary |
|---|---|
| [dependsOn](depends-on.md) | `open val dependsOn: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out DevFunModule>>`<br>List of dependencies that this module requires to function correctly. |
| [name](name.md) | `open val name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Name of the module. |

### Functions

| Name | Summary |
|---|---|
| [dispose](dispose.md) | `open fun dispose(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Module cleanup. |
| [initialize](initialize.md) | `abstract fun initialize(devFun: `[`DevFun`](../-dev-fun/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Module initialization. |

### Inheritors

| Name | Summary |
|---|---|
| [AbstractDevFunModule](../-abstract-dev-fun-module/index.md) | `abstract class AbstractDevFunModule : DevFunModule`<br>Implementation of DevFunModule providing various convenience functions. |
