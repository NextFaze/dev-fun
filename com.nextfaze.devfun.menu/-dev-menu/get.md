[gh-pages](../../index.md) / [com.nextfaze.devfun.menu](../index.md) / [DevMenu](index.md) / [get](.)

# get

`fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> get(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out T>): T?` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-menu/src/main/java/com/nextfaze/devfun/menu/DeveloperMenu.kt#L147)

Overrides [InstanceProvider.get](../../com.nextfaze.devfun.inject/-instance-provider/get.md)

Try to get an instance of some [clazz](get.md#com.nextfaze.devfun.menu.DevMenu$get(kotlin.reflect.KClass((com.nextfaze.devfun.menu.DevMenu.get.T)))/clazz).

**Return**
An instance of [clazz](get.md#com.nextfaze.devfun.menu.DevMenu$get(kotlin.reflect.KClass((com.nextfaze.devfun.menu.DevMenu.get.T)))/clazz), or `null` if this provider can not handle the type

**See Also**

[RequiringInstanceProvider.get](#)

