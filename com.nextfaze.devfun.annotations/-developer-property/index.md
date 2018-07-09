[gh-pages](../../index.md) / [com.nextfaze.devfun.annotations](../index.md) / [DeveloperProperty](./index.md)

# DeveloperProperty

`@Target([AnnotationTarget.PROPERTY]) annotation class DeveloperProperty` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/annotations/Properties.kt#L32)

An annotation that, when used on Kotlin properties, allows DevFun to provide the means of getting/setting properties on the fly.

For simple/registered types DevFun will render a UI by the same means as when invoking a function.

DevFun will correctly use the delegated getter/setter.

*Be aware, when DevFun attempts to process its generated code (such as when you open the DevMenu or access the web server), propertygetters will be called for their `toString` (special exception for `lazy` types however).*

**This feature is experimental.**

**See Also**

[PropertyTransformer](../-property-transformer.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `DeveloperProperty(value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "", category: `[`DeveloperCategory`](../-developer-category/index.md)` = DeveloperCategory(group = "Properties"), requiresApi: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 0, transformer: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`FunctionTransformer`](../../com.nextfaze.devfun.core/-function-transformer/index.md)`> = PropertyTransformer::class)`<br>An annotation that, when used on Kotlin properties, allows DevFun to provide the means of getting/setting properties on the fly. |

### Properties

| Name | Summary |
|---|---|
| [category](category.md) | `val category: `[`DeveloperCategory`](../-developer-category/index.md) |
| [requiresApi](requires-api.md) | `val requiresApi: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [transformer](transformer.md) | `val transformer: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`FunctionTransformer`](../../com.nextfaze.devfun.core/-function-transformer/index.md)`>` |
| [value](value.md) | `val value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
