[gh-pages](../../index.md) / [com.nextfaze.devfun.generated](../index.md) / [DevFunGenerated](./index.md)

# DevFunGenerated

`interface DevFunGenerated` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/generated/Generated.kt#L21)

Generated classes will implement this, which will be loaded using Java's [ServiceLoader](https://developer.android.com/reference/java/util/ServiceLoader.html).

**See Also**

[DeveloperCategory](../../com.nextfaze.devfun.annotations/-developer-category/index.md)

[CategoryDefinition](../../com.nextfaze.devfun.core/-category-definition/index.md)

[DeveloperFunction](../../com.nextfaze.devfun.annotations/-developer-function/index.md)

[FunctionDefinition](../../com.nextfaze.devfun.core/-function-definition/index.md)

[DeveloperAnnotation](../../com.nextfaze.devfun.annotations/-developer-annotation/index.md)

[DeveloperReference](../../com.nextfaze.devfun.core/-developer-reference/index.md)

### Properties

| Name | Summary |
|---|---|
| [categoryDefinitions](category-definitions.md) | `open val categoryDefinitions: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`CategoryDefinition`](../../com.nextfaze.devfun.core/-category-definition/index.md)`>`<br>List of category definitions. |
| [developerReferences](developer-references.md) | `open val developerReferences: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`DeveloperReference`](../../com.nextfaze.devfun.core/-developer-reference/index.md)`>`<br>List of developer references. |
| [functionDefinitions](function-definitions.md) | `open val functionDefinitions: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`FunctionDefinition`](../../com.nextfaze.devfun.core/-function-definition/index.md)`>`<br>List of function definitions. |
