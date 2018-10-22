[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [DevFun](index.md) / [getDeveloperReferences](./get-developer-references.md)

# getDeveloperReferences

`fun getDeveloperReferences(clazz: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`Annotation`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-annotation/index.html)`>): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`ReferenceDefinition`](../../com.nextfaze.devfun.reference/-reference-definition/index.md)`>` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/core/DevFun.kt#L465)

Get references to annotations that were annotated as [DeveloperReference](../../com.nextfaze.devfun.reference/-developer-reference/index.md).

**Experimental API**

### Parameters

`clazz` - The annotation class that was annotation with [DeveloperReference](../../com.nextfaze.devfun.reference/-developer-reference/index.md).

**Return**
A list of references across all modules annotated with [clazz](get-developer-references.md#com.nextfaze.devfun.core.DevFun$getDeveloperReferences(kotlin.reflect.KClass((kotlin.Annotation)))/clazz).

**See Also**

[developerReferences](developer-references.md)

