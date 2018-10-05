[gh-pages](../../index.md) / [com.nextfaze.devfun.internal.exception](../index.md) / [ExceptionCategoryDefinition](./index.md)

# ExceptionCategoryDefinition

`object ExceptionCategoryDefinition : `[`CategoryDefinition`](../../com.nextfaze.devfun.category/-category-definition/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-internal/src/main/java/com/nextfaze/devfun/internal/exception/ExceptionTypes.kt#L23)

### Properties

| Name | Summary |
|---|---|
| [name](name.md) | `val name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The name of this category as taken from [DeveloperCategory.value](../../com.nextfaze.devfun.category/-developer-category/value.md). |

### Inherited Properties

| Name | Summary |
|---|---|
| [clazz](../../com.nextfaze.devfun.category/-category-definition/clazz.md) | `open val clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<*>?`<br>[DeveloperFunction](../../com.nextfaze.devfun.function/-developer-function/index.md) usages in [clazz](../../com.nextfaze.devfun.category/-category-definition/clazz.md) will use this category. |
| [group](../../com.nextfaze.devfun.category/-category-definition/group.md) | `open val group: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`?`<br>Items that match this category will be placed in this group, as taken from [DeveloperCategory.group](../../com.nextfaze.devfun.category/-developer-category/group.md). |
| [order](../../com.nextfaze.devfun.category/-category-definition/order.md) | `open val order: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?`<br>The category ordering as taken from [DeveloperCategory.order](../../com.nextfaze.devfun.category/-developer-category/order.md). |
