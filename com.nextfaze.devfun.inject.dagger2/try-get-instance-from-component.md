[gh-pages](../index.md) / [com.nextfaze.devfun.inject.dagger2](index.md) / [tryGetInstanceFromComponent](.)

# tryGetInstanceFromComponent

`fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> tryGetInstanceFromComponent(component: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<T>): T?` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-inject-dagger2/src/main/java/com/nextfaze/devfun/inject/dagger2/Instances.kt#L47)

Helper function to be used on Dagger 2.x [Component](#) implementations.

Will traverse the component providers and modules for an instance type matching [clazz](try-get-instance-from-component.md#com.nextfaze.devfun.inject.dagger2$tryGetInstanceFromComponent(kotlin.Any, kotlin.reflect.KClass((com.nextfaze.devfun.inject.dagger2.tryGetInstanceFromComponent.T)))/clazz) - scoping is not considered.

