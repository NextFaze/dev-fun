[gh-pages](../../index.md) / [com.nextfaze.devfun.inject](../index.md) / [CompositeInstanceProvider](.)

# CompositeInstanceProvider

`class CompositeInstanceProvider : `[`RequiringInstanceProvider`](../-requiring-instance-provider/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/inject/InstanceProviders.kt#L30)

Instance provider that delegates to other providers.

Checks in reverse order of added.
i.e. most recently added is checked first

**Internal**
Visible for testing - use at your own risk.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `CompositeInstanceProvider()`<br>Instance provider that delegates to other providers. |

### Functions

| Name | Summary |
|---|---|
| [get](get.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> get(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out T>): T`<br>Get an instance of some [clazz](get.md#com.nextfaze.devfun.inject.CompositeInstanceProvider$get(kotlin.reflect.KClass((com.nextfaze.devfun.inject.CompositeInstanceProvider.get.T)))/clazz). |
| [minusAssign](minus-assign.md) | `operator fun minusAssign(instanceProvider: `[`InstanceProvider`](../-instance-provider/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [plusAssign](plus-assign.md) | `operator fun plusAssign(instanceProvider: `[`InstanceProvider`](../-instance-provider/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
