[gh-pages](../index.md) / [com.nextfaze.devfun.category](./index.md)

## Package com.nextfaze.devfun.category

### Types

| Name | Summary |
|---|---|
| [CategoryDefinition](-category-definition/index.md) | `interface CategoryDefinition`<br>Classes annotated with [DeveloperCategory](-developer-category/index.md) will be defined using this interface at compile time. |
| [CategoryItem](-category-item/index.md) | `interface CategoryItem`<br>Items are derived from [CategoryDefinition](-category-definition/index.md) at run-time during [FunctionDefinition](../com.nextfaze.devfun.function/-function-definition/index.md) processing. |
| [DeveloperCategoryProperties](-developer-category-properties/index.md) | `interface DeveloperCategoryProperties`<br>Properties interface for @[DeveloperCategory](-developer-category/index.md). |

### Annotations

| Name | Summary |
|---|---|
| [ContextCategory](-context-category/index.md) | `annotation class ContextCategory`<br>[DeveloperCategory](-developer-category/index.md) annotation used to declare the "Context" category. |
| [DeveloperCategory](-developer-category/index.md) | `annotation class DeveloperCategory`<br>This annotation is optional, and is used to change the category's name/order or the group of the functions defined in this class. |

### Properties

| Name | Summary |
|---|---|
| [CONTEXT_CAT_NAME](-c-o-n-t-e-x-t_-c-a-t_-n-a-m-e.md) | `const val CONTEXT_CAT_NAME: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [CONTEXT_CAT_ORDER](-c-o-n-t-e-x-t_-c-a-t_-o-r-d-e-r.md) | `const val CONTEXT_CAT_ORDER: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
