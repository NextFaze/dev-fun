[gh-pages](../../index.md) / [com.nextfaze.devfun.generated](../index.md) / [DevFunGenerated](.)

# DevFunGenerated

`interface DevFunGenerated` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/generated/Generated.kt#L13)

Generated classes will implement this, which will be loaded using Java's [ServiceLoader](http://docs.oracle.com/javase/6/docs/api/java/util/ServiceLoader.html).

**See Also**

[CategoryDefinition](../../com.nextfaze.devfun.core/-category-definition/index.md)

[FunctionDefinition](../../com.nextfaze.devfun.core/-function-definition/index.md)

### Properties

| Name | Summary |
|---|---|
| [categoryDefinitions](category-definitions.md) | `abstract val categoryDefinitions: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`CategoryDefinition`](../../com.nextfaze.devfun.core/-category-definition/index.md)`>` |
| [functionDefinitions](function-definitions.md) | `abstract val functionDefinitions: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`FunctionDefinition`](../../com.nextfaze.devfun.core/-function-definition/index.md)`>` |
