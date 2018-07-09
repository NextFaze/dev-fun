[gh-pages](../../index.md) / [com.nextfaze.devfun.inject.dagger2](../index.md) / [Dagger2InstanceProvider](./index.md)

# Dagger2InstanceProvider

`abstract class Dagger2InstanceProvider : `[`InstanceProvider`](../../com.nextfaze.devfun.inject/-instance-provider/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-inject-dagger2/src/main/java/com/nextfaze/devfun/inject/dagger2/Instances.kt#L328)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Dagger2InstanceProvider(androidInstances: `[`AndroidInstanceProviderInternal`](../../com.nextfaze.devfun.internal.android/-android-instance-provider-internal/index.md)`)` |

### Properties

| Name | Summary |
|---|---|
| [androidInstances](android-instances.md) | `val androidInstances: `[`AndroidInstanceProviderInternal`](../../com.nextfaze.devfun.internal.android/-android-instance-provider-internal/index.md) |
| [deferToAndroidInstanceProvider](defer-to-android-instance-provider.md) | `var deferToAndroidInstanceProvider: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

### Functions

| Name | Summary |
|---|---|
| [description](description.md) | `abstract fun description(): `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html) |
| [get](get.md) | `open fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> get(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`T`](get.md#T)`>): `[`T`](get.md#T)`?`<br>Try to get an instance of some [clazz](../../com.nextfaze.devfun.inject/-instance-provider/get.md#com.nextfaze.devfun.inject.InstanceProvider$get(kotlin.reflect.KClass((com.nextfaze.devfun.inject.InstanceProvider.get.T)))/clazz). |
