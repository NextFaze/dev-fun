[gh-pages](../../index.md) / [com.nextfaze.devfun.internal](../index.md) / [ReflectedMethod](./index.md)

# ReflectedMethod

`interface ReflectedMethod : () -> `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?, (`[`InstanceProvider`](../../com.nextfaze.devfun.inject/-instance-provider/index.md)`) -> `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/internal/Reflected.kt#L46)

### Properties

| Name | Summary |
|---|---|
| [clazz](clazz.md) | `abstract val clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<*>` |
| [fieldName](field-name.md) | `abstract val fieldName: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [isProperty](is-property.md) | `abstract val isProperty: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isStatic](is-static.md) | `abstract val isStatic: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [method](method.md) | `abstract val method: `[`Method`](https://developer.android.com/reference/java/lang/reflect/Method.html) |
| [name](name.md) | `abstract val name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [parameterTypes](parameter-types.md) | `abstract val parameterTypes: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<*>>` |
| [parameters](parameters.md) | `abstract val parameters: `[`FunctionArgs`](../../com.nextfaze.devfun.function/-function-args.md) |
| [receiver](receiver.md) | `abstract val receiver: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?` |

### Functions

| Name | Summary |
|---|---|
| [parameterInstances](parameter-instances.md) | `abstract fun parameterInstances(instanceProvider: `[`InstanceProvider`](../../com.nextfaze.devfun.inject/-instance-provider/index.md)`, suppliedArgs: `[`FunctionArgs`](../../com.nextfaze.devfun.function/-function-args.md)` = null): `[`FunctionArgs`](../../com.nextfaze.devfun.function/-function-args.md) |
| [receiverInstance](receiver-instance.md) | `abstract fun receiverInstance(instanceProvider: `[`InstanceProvider`](../../com.nextfaze.devfun.inject/-instance-provider/index.md)`): `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?` |
