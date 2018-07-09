[gh-pages](../index.md) / [com.nextfaze.devfun.inject.dagger2](./index.md)

## Package com.nextfaze.devfun.inject.dagger2

Provides default heavy-reflection based Dagger 2 injector and convenience functions for reflectively
 locating object instances from Dagger 2.x `@Component` objects.

### Types

| Name | Summary |
|---|---|
| [Dagger2InstanceProvider](-dagger2-instance-provider/index.md) | `abstract class Dagger2InstanceProvider : `[`InstanceProvider`](../com.nextfaze.devfun.inject/-instance-provider/index.md) |
| [InjectFromDagger2](-inject-from-dagger2/index.md) | `class InjectFromDagger2 : `[`AbstractDevFunModule`](../com.nextfaze.devfun.core/-abstract-dev-fun-module/index.md)<br>This module adds rudimentary support for searching Dagger 2.x component graphs for object instances. |

### Properties

| Name | Summary |
|---|---|
| [useAutomaticDagger2Injector](use-automatic-dagger2-injector.md) | `var useAutomaticDagger2Injector: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Flag to indicate if the default heavy-reflection based Dagger 2 injector should be used. |

### Functions

| Name | Summary |
|---|---|
| [tryGetInstanceFromComponent](try-get-instance-from-component.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> tryGetInstanceFromComponent(component: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<`[`T`](try-get-instance-from-component.md#T)`>, cacheResolvedTypes: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true): `[`T`](try-get-instance-from-component.md#T)`?`<br>Helper function to be used on Dagger 2.x [Component](#) implementations. |
| [tryGetInstanceFromComponentCache](try-get-instance-from-component-cache.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> tryGetInstanceFromComponentCache(component: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<`[`T`](try-get-instance-from-component-cache.md#T)`>): `[`T`](try-get-instance-from-component-cache.md#T)`?`<br>Helper function to be used on Dagger 2.x [Component](#) implementations. |
| [tryGetInstanceFromComponentReflection](try-get-instance-from-component-reflection.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> tryGetInstanceFromComponentReflection(component: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<`[`T`](try-get-instance-from-component-reflection.md#T)`>): `[`T`](try-get-instance-from-component-reflection.md#T)`?`<br>Helper function to be used on Dagger 2.x [Component](#) implementations. |
