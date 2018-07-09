[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [CategoryDefinition](index.md) / [order](./order.md)

# order

`open val order: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)`?` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/core/Definitions.kt#L122)

The category ordering as taken from [DeveloperCategory.order](../../com.nextfaze.devfun.annotations/-developer-category/order.md).

If this is `null` then it was not set explicitly for this definition and will be resolved at runtime (i.e. if
this category was defined elsewhere already). Otherwise it will default to `0`.

**When a category's order is declared more than once the outcome is effectively undefined, leaving you at themercy of the annotation processor (javac does not define one) and parsing/processing order.**

