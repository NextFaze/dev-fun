[gh-pages](../index.md) / [com.nextfaze.devfun.annotations](index.md) / [PropertyTransformer](./-property-transformer.md)

# PropertyTransformer

`interface PropertyTransformer : `[`FunctionTransformer`](../com.nextfaze.devfun.core/-function-transformer/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/annotations/Properties.kt#L13)

A function transformer that tells DevFun how to render Kotlin properties.

**See Also**

[DeveloperProperty](-developer-property/index.md)

### Inherited Functions

| Name | Summary |
|---|---|
| [accept](../com.nextfaze.devfun.core/-function-transformer/accept.md) | `open fun accept(functionDefinition: `[`FunctionDefinition`](../com.nextfaze.devfun.core/-function-definition/index.md)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Should this transformer accept this item for transforming. |
| [apply](../com.nextfaze.devfun.core/-function-transformer/apply.md) | `abstract fun apply(functionDefinition: `[`FunctionDefinition`](../com.nextfaze.devfun.core/-function-definition/index.md)`, categoryDefinition: `[`CategoryDefinition`](../com.nextfaze.devfun.core/-category-definition/index.md)`): `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`FunctionItem`](../com.nextfaze.devfun.core/-function-item/index.md)`>?`<br>Transforms a [FunctionDefinition](../com.nextfaze.devfun.core/-function-definition/index.md) to one or more [FunctionItem](../com.nextfaze.devfun.core/-function-item/index.md). |
