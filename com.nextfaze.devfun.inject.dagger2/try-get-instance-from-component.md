[gh-pages](../index.md) / [com.nextfaze.devfun.inject.dagger2](index.md) / [tryGetInstanceFromComponent](./try-get-instance-from-component.md)

# tryGetInstanceFromComponent

`fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> tryGetInstanceFromComponent(component: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<`[`T`](try-get-instance-from-component.md#T)`>, cacheResolvedTypes: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true): `[`T`](try-get-instance-from-component.md#T)`?` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-inject-dagger2/src/main/java/com/nextfaze/devfun/inject/dagger2/Instances.kt#L95)

Helper function to be used on Dagger 2.x [Component](#) implementations.

Will traverse the component providers and modules for an instance type matching [clazz](try-get-instance-from-component.md#com.nextfaze.devfun.inject.dagger2$tryGetInstanceFromComponent(kotlin.Any, kotlin.reflect.KClass((com.nextfaze.devfun.inject.dagger2.tryGetInstanceFromComponent.T)), kotlin.Boolean)/clazz) - scoping is not considered.

This function delegates to [tryGetInstanceFromComponentCache](try-get-instance-from-component-cache.md) and [tryGetInstanceFromComponentReflection](try-get-instance-from-component-reflection.md). If you are having issues with
new instances being created when they shouldn't be, ensure your `@Scope` annotations are `@Retention(RUNTIME)`.

Alternatively use `@Dagger2Component` on your functions/properties (or `@get:Dagger2Component` for property getters)
that return components to tell DevFun where to find them (they can be whatever/where ever; static, in your app class,
activity class, etc) - which will end up using this method anyway.

**See Also**

[Dagger2Component](../com.nextfaze.devfun.reference/-dagger2-component/index.md)

