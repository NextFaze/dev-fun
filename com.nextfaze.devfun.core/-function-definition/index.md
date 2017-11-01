[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [FunctionDefinition](.)

# FunctionDefinition

`interface FunctionDefinition` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/core/Definitions.kt#L20)

Functions/methods annotated with [DeveloperFunction](../../com.nextfaze.devfun.annotations/-developer-function/index.md) will be defined using this interface at compile time.

Definitions will be convert to items via [FunctionTransformer](../-function-transformer/index.md).

**See Also**

[FunctionItem](../-function-item/index.md)

### Properties

| Name | Summary |
|---|---|
| [category](category.md) | `open val category: `[`CategoryDefinition`](../-category-definition/index.md)`?`<br>The category for this definition as taken from [DeveloperFunction.category](../../com.nextfaze.devfun.annotations/-developer-function/category.md). |
| [clazz](clazz.md) | `open val clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>`<br>The class where this item was defined. |
| [invoke](invoke.md) | `abstract val invoke: `[`FunctionInvoke`](../-function-invoke.md)<br>Called when this item is to be invoked. |
| [method](method.md) | `abstract val method: `[`Method`](https://developer.android.com/reference/java/lang/reflect/Method.html)<br>The method where this function was defined. |
| [name](name.md) | `open val name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The name of this item as taken from [DeveloperFunction.value](../../com.nextfaze.devfun.annotations/-developer-function/value.md). |
| [requiresApi](requires-api.md) | `open val requiresApi: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Required API to allow this item to be shown as taken from [DeveloperFunction.requiresApi](../../com.nextfaze.devfun.annotations/-developer-function/requires-api.md). |
| [transformer](transformer.md) | `open val transformer: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`FunctionTransformer`](../-function-transformer/index.md)`>`<br>Function transformer for this instance as taken from [DeveloperFunction.transformer](../../com.nextfaze.devfun.annotations/-developer-function/transformer.md). |
