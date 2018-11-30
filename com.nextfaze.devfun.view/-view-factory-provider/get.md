[gh-pages](../../index.md) / [com.nextfaze.devfun.view](../index.md) / [ViewFactoryProvider](index.md) / [get](./get.md)

# get

`abstract operator fun get(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<*>): `[`ViewFactory`](../-view-factory/index.md)`<`[`View`](https://developer.android.com/reference/android/view/View.html)`>?` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/view/Factory.kt#L47)

Get a view factory for some [clazz](get.md#com.nextfaze.devfun.view.ViewFactoryProvider$get(kotlin.reflect.KClass((kotlin.Any)))/clazz) key.

**Return**
The view factory assigned to this [clazz](get.md#com.nextfaze.devfun.view.ViewFactoryProvider$get(kotlin.reflect.KClass((kotlin.Any)))/clazz) key or `null` if no factory is assigned to this key yet.

