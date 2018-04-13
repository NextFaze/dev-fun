[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [DeveloperReference](./index.md)

# DeveloperReference

`interface DeveloperReference` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/core/Definitions.kt#L162)

Defines references to annotations that are annotated by meta annotation [DeveloperAnnotation](../../com.nextfaze.devfun.annotations/-developer-annotation/index.md).

Developer Annotation annotations will be noted by DevFun and wrapped by this interface.
An example of this is used by the `devfun-inject-dagger2` module.

*At present the scope of references is limited to only `ExecutableElement` but is likely to increase as needed (feel free to make an issue or PR).*

**This is an experimental feature and subject to change. External input/suggestions welcome.**

**See Also**

[Dagger2Component](../../com.nextfaze.devfun.annotations/-dagger2-component/index.md)

### Properties

| Name | Summary |
|---|---|
| [annotation](annotation.md) | `abstract val annotation: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`Annotation`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-annotation/index.html)`>`<br>The annotation that wanted the reference. |
| [method](method.md) | `abstract val method: `[`Method`](https://developer.android.com/reference/java/lang/reflect/Method.html)`?`<br>The annotated method. |
