[gh-pages](../../index.md) / [com.nextfaze.devfun.internal](../index.md) / [ReflectedMethod](./index.md)

# ReflectedMethod

`interface ReflectedMethod` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/internal/Reflected.kt#L40)

### Properties

| Name | Summary |
|---|---|
| [clazz](clazz.md) | `abstract val clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<*>` |
| [fieldName](field-name.md) | `abstract val fieldName: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [isProperty](is-property.md) | `abstract val isProperty: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [method](method.md) | `abstract val method: `[`Method`](https://developer.android.com/reference/java/lang/reflect/Method.html) |
| [name](name.md) | `abstract val name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [receiver](receiver.md) | `abstract val receiver: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?` |

### Functions

| Name | Summary |
|---|---|
| [invoke](invoke.md) | `abstract fun invoke(instanceProvider: `[`InstanceProvider`](../../com.nextfaze.devfun.inject/-instance-provider/index.md)`): `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?`<br>`abstract fun invoke(): `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?` |
| [receiverInstance](receiver-instance.md) | `abstract fun receiverInstance(instanceProvider: `[`InstanceProvider`](../../com.nextfaze.devfun.inject/-instance-provider/index.md)`): `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?` |
