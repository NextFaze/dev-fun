[gh-pages](../index.md) / [com.nextfaze.devfun.inject.dagger2](index.md) / [tryGetInstanceFromComponent](./try-get-instance-from-component.md)

# tryGetInstanceFromComponent

`fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> tryGetInstanceFromComponent(component: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<`[`T`](try-get-instance-from-component.md#T)`>): `[`T`](try-get-instance-from-component.md#T)`?` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-inject-dagger2/src/main/java/com/nextfaze/devfun/inject/dagger2/Instances.kt#L64)

Helper function to be used on Dagger 2.x [Component](#) implementations.

Will traverse the component providers and modules for an instance type matching [clazz](try-get-instance-from-component.md#com.nextfaze.devfun.inject.dagger2$tryGetInstanceFromComponent(kotlin.Any, kotlin.reflect.KClass((com.nextfaze.devfun.inject.dagger2.tryGetInstanceFromComponent.T)))/clazz) - scoping is not considered.

Alternatively use `@Dagger2Component` on your functions (`@get:Dagger2Component` for properties) that return
components to tell DevFun where to find them (they can be whatever/where ever; static, in your app class, activity
class, etc) - which will end up using this method anyway.

**See Also**

[Dagger2Component](../com.nextfaze.devfun.annotations/-dagger2-component/index.md)

