[gh-pages](../../index.md) / [com.nextfaze.devfun.category](../index.md) / [CategoryDefinition](./index.md)

# CategoryDefinition

`interface CategoryDefinition` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/category/CategoryDefinition.kt#L10)

Classes annotated with [DeveloperCategory](../-developer-category/index.md) will be defined using this interface at compile time.

### Properties

| Name | Summary |
|---|---|
| [clazz](clazz.md) | `open val clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<*>?`<br>[DeveloperFunction](../../com.nextfaze.devfun.function/-developer-function/index.md) usages in [clazz](clazz.md) will use this category. |
| [group](group.md) | `open val group: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`?`<br>Items that match this category will be placed in this group, as taken from [DeveloperCategory.group](../-developer-category/group.md). |
| [name](name.md) | `open val name: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`?`<br>The name of this category as taken from [DeveloperCategory.value](../-developer-category/value.md). |
| [order](order.md) | `open val order: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?`<br>The category ordering as taken from [DeveloperCategory.order](../-developer-category/order.md). |

### Inheritors

| Name | Summary |
|---|---|
| [ExceptionCategoryDefinition](../../com.nextfaze.devfun.internal.exception/-exception-category-definition/index.md) | `object ExceptionCategoryDefinition : `[`CategoryDefinition`](./index.md) |
