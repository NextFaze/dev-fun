[gh-pages](../../index.md) / [com.nextfaze.devfun.category](../index.md) / [ContextCategory](./index.md)

# ContextCategory

`@Target([AnnotationTarget.CLASS]) annotation class ContextCategory` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/category/ContextCategory.kt#L16)

[DeveloperCategory](../-developer-category/index.md) annotation used to declare the "Context" category.

By default functions declared within some sort of Android "scope" (Activity/Fragment/View/etc) will have this category.

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `ContextCategory(value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = CONTEXT_CAT_NAME, group: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "", order: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = CONTEXT_CAT_ORDER)`<br>[DeveloperCategory](../-developer-category/index.md) annotation used to declare the "Context" category. |

### Properties

| Name | Summary |
|---|---|
| [group](group.md) | `val group: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [order](order.md) | `val order: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [value](value.md) | `val value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
