[gh-pages](../index.md) / [com.nextfaze.devfun.function](index.md) / [PropertyTransformer](./-property-transformer.md)

# PropertyTransformer

`interface PropertyTransformer : `[`FunctionTransformer`](-function-transformer/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/function/DeveloperProperty.kt#L15)

A function transformer that tells DevFun how to render Kotlin properties.

**See Also**

[DeveloperProperty](-developer-property/index.md)

### Inherited Functions

| Name | Summary |
|---|---|
| [accept](-function-transformer/accept.md) | `open fun accept(functionDefinition: `[`FunctionDefinition`](-function-definition/index.md)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Should this transformer accept this item for transforming. |
| [apply](-function-transformer/apply.md) | `abstract fun apply(functionDefinition: `[`FunctionDefinition`](-function-definition/index.md)`, categoryDefinition: `[`CategoryDefinition`](../com.nextfaze.devfun.category/-category-definition/index.md)`): `[`Collection`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)`<`[`FunctionItem`](-function-item/index.md)`>?`<br>Transforms a [FunctionDefinition](-function-definition/index.md) to one or more [FunctionItem](-function-item/index.md). |