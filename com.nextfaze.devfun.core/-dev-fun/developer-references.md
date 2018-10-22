[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [DevFun](index.md) / [developerReferences](./developer-references.md)

# developerReferences

`inline fun <reified T : `[`Annotation`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-annotation/index.html)`> developerReferences(): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`ReferenceDefinition`](../../com.nextfaze.devfun.reference/-reference-definition/index.md)`>` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/core/DevFun.kt#L450)

Get references to annotations that were annotated as [DeveloperReference](../../com.nextfaze.devfun.reference/-developer-reference/index.md).

**Experimental API**

### Parameters

`T` - The annotation type that was annotation with [DeveloperReference](../../com.nextfaze.devfun.reference/-developer-reference/index.md).

**Return**
A list of references across all modules annotated with [T](developer-references.md#T).

**See Also**

[Dagger2Component](../../com.nextfaze.devfun.reference/-dagger2-component/index.md)

[getDeveloperReferences](get-developer-references.md)

