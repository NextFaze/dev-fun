[gh-pages](../../index.md) / [com.nextfaze.devfun.internal.exception](../index.md) / [ExceptionCategoryItem](./index.md)

# ExceptionCategoryItem

`class ExceptionCategoryItem : `[`CategoryItem`](../../com.nextfaze.devfun.category/-category-item/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-internal/src/main/java/com/nextfaze/devfun/internal/exception/ExceptionTypes.kt#L33)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `ExceptionCategoryItem(ex: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`)` |

### Properties

| Name | Summary |
|---|---|
| [items](items.md) | `val items: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`FunctionItem`](../../com.nextfaze.devfun.function/-function-item/index.md)`>`<br>List of items for this category. |
| [name](name.md) | `val name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Categories with the same name will be merged at runtime (case-sensitive). |

### Inherited Properties

| Name | Summary |
|---|---|
| [order](../../com.nextfaze.devfun.category/-category-item/order.md) | `open val order: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Categories are ordered by [order](../../com.nextfaze.devfun.category/-category-item/order.md) (top-&gt;bottom is lowest-&gt;highest), then alphabetically by [name](../../com.nextfaze.devfun.category/-category-item/name.md). |
