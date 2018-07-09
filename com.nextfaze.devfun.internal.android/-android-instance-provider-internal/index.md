[gh-pages](../../index.md) / [com.nextfaze.devfun.internal.android](../index.md) / [AndroidInstanceProviderInternal](./index.md)

# AndroidInstanceProviderInternal

`interface AndroidInstanceProviderInternal : `[`InstanceProvider`](../../com.nextfaze.devfun.inject/-instance-provider/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-internal/src/main/java/com/nextfaze/devfun/internal/android/InstanceProvider.kt#L6)

### Properties

| Name | Summary |
|---|---|
| [activity](activity.md) | `abstract val activity: `[`Activity`](https://developer.android.com/reference/android/app/Activity.html)`?` |

### Inherited Functions

| Name | Summary |
|---|---|
| [get](../../com.nextfaze.devfun.inject/-instance-provider/get.md) | `abstract operator fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> get(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`T`](../../com.nextfaze.devfun.inject/-instance-provider/get.md#T)`>): `[`T`](../../com.nextfaze.devfun.inject/-instance-provider/get.md#T)`?`<br>Try to get an instance of some [clazz](../../com.nextfaze.devfun.inject/-instance-provider/get.md#com.nextfaze.devfun.inject.InstanceProvider$get(kotlin.reflect.KClass((com.nextfaze.devfun.inject.InstanceProvider.get.T)))/clazz). |

### Inheritors

| Name | Summary |
|---|---|
| [AndroidInstanceProvider](../../com.nextfaze.devfun.inject/-android-instance-provider/index.md) | `interface AndroidInstanceProvider : `[`AndroidInstanceProviderInternal`](./index.md) |
