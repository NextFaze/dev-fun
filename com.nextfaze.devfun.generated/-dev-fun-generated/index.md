[gh-pages](../../index.md) / [com.nextfaze.devfun.generated](../index.md) / [DevFunGenerated](./index.md)

# DevFunGenerated

`interface DevFunGenerated` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/generated/Generated.kt#L21)

Generated classes will implement this, which will be loaded using Java's [ServiceLoader](https://developer.android.com/reference/java/util/ServiceLoader.html).

**See Also**

[DeveloperCategory](../../com.nextfaze.devfun.category/-developer-category/index.md)

[CategoryDefinition](../../com.nextfaze.devfun.category/-category-definition/index.md)

[DeveloperFunction](../../com.nextfaze.devfun.function/-developer-function/index.md)

[FunctionDefinition](../../com.nextfaze.devfun.function/-function-definition/index.md)

[DeveloperReference](../../com.nextfaze.devfun.reference/-developer-reference/index.md)

[ReferenceDefinition](../../com.nextfaze.devfun.reference/-reference-definition/index.md)

### Properties

| Name | Summary |
|---|---|
| [categoryDefinitions](category-definitions.md) | `open val categoryDefinitions: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`CategoryDefinition`](../../com.nextfaze.devfun.category/-category-definition/index.md)`>`<br>List of category definitions. |
| [developerReferences](developer-references.md) | `open val developerReferences: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`ReferenceDefinition`](../../com.nextfaze.devfun.reference/-reference-definition/index.md)`>`<br>List of developer references. |
| [functionDefinitions](function-definitions.md) | `open val functionDefinitions: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`FunctionDefinition`](../../com.nextfaze.devfun.function/-function-definition/index.md)`>`<br>List of function definitions. |
