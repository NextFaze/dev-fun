[gh-pages](../../index.md) / [com.nextfaze.devfun.compiler](../index.md) / [DevFunProcessor](./index.md)

# DevFunProcessor

`@SupportedOptions(["devfun.kotlin.reflection", "devfun.debug.comments", "devfun.debug.verbose", "devfun.package.root", "devfun.package.suffix", "devfun.package.override", "devfun.application.package", "devfun.application.variant", "devfun.ext.package.suffix", "devfun.ext.package.root", "devfun.ext.package.override"]) class DevFunProcessor : `[`AbstractProcessor`](http://docs.oracle.com/javase/6/docs/api/javax/annotation/processing/AbstractProcessor.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-compiler/src/main/java/com/nextfaze/devfun/compiler/Compiler.kt#L232)

Annotation processor for [DeveloperFunction](../../com.nextfaze.devfun.annotations/-developer-function/index.md) and [DeveloperCategory](../../com.nextfaze.devfun.annotations/-developer-category/index.md).

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `DevFunProcessor()`<br>Annotation processor for [DeveloperFunction](../../com.nextfaze.devfun.annotations/-developer-function/index.md) and [DeveloperCategory](../../com.nextfaze.devfun.annotations/-developer-category/index.md). |

### Functions

| Name | Summary |
|---|---|
| [getSupportedAnnotationTypes](get-supported-annotation-types.md) | `fun getSupportedAnnotationTypes(): `[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [getSupportedSourceVersion](get-supported-source-version.md) | `fun getSupportedSourceVersion(): `[`SourceVersion`](http://docs.oracle.com/javase/6/docs/api/javax/lang/model/SourceVersion.html) |
| [process](process.md) | `fun process(elements: `[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`TypeElement`](http://docs.oracle.com/javase/6/docs/api/javax/lang/model/element/TypeElement.html)`>, env: `[`RoundEnvironment`](http://docs.oracle.com/javase/6/docs/api/javax/annotation/processing/RoundEnvironment.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
