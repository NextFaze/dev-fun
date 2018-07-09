[gh-pages](../../index.md) / [com.nextfaze.devfun.internal](../index.md) / [ReflectedProperty](./index.md)

# ReflectedProperty

`interface ReflectedProperty` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/internal/Reflected.kt#L54)

### Properties

| Name | Summary |
|---|---|
| [desc](desc.md) | `abstract val desc: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [descWithDeclaringClass](desc-with-declaring-class.md) | `abstract val descWithDeclaringClass: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [field](field.md) | `abstract val field: `[`Field`](https://developer.android.com/reference/java/lang/reflect/Field.html)`?` |
| [getter](getter.md) | `abstract val getter: `[`Method`](https://developer.android.com/reference/java/lang/reflect/Method.html)`?` |
| [isLateinit](is-lateinit.md) | `abstract val isLateinit: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isUninitialized](is-uninitialized.md) | `abstract val isUninitialized: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [property](property.md) | `abstract val property: `[`KProperty`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property/index.html)`<*>` |
| [setter](setter.md) | `abstract val setter: `[`Method`](https://developer.android.com/reference/java/lang/reflect/Method.html)`?` |
| [type](type.md) | `abstract val type: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<*>` |
| [value](value.md) | `abstract var value: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?` |

### Functions

| Name | Summary |
|---|---|
| [getDesc](get-desc.md) | `abstract fun getDesc(withDeclaringClass: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getValue](get-value.md) | `abstract fun getValue(instanceProvider: `[`InstanceProvider`](../../com.nextfaze.devfun.inject/-instance-provider/index.md)`): `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?` |
| [isUninitialized](is-uninitialized.md) | `abstract fun isUninitialized(receiver: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [setValue](set-value.md) | `abstract fun setValue(instanceProvider: `[`InstanceProvider`](../../com.nextfaze.devfun.inject/-instance-provider/index.md)`, value: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?): `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?` |
