[gh-pages](../../index.md) / [com.nextfaze.devfun.compiler](../index.md) / [DevFunProcessor](./index.md)

# DevFunProcessor

`@SupportedOptions(["devfun.kotlin.reflection", "devfun.debug.comments", "devfun.debug.verbose", "devfun.package.root", "devfun.package.suffix", "devfun.package.override", "devfun.application.package", "devfun.application.variant", "devfun.ext.package.suffix", "devfun.ext.package.root", "devfun.ext.package.override", "devfun.definitions.generate", "devfun.elements.include", "devfun.elements.exclude"]) class DevFunProcessor : `[`DaggerProcessor`](../-dagger-processor/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-compiler/src/main/java/com/nextfaze/devfun/compiler/DevFunProcessor.kt#L321)

Annotation processor for [DeveloperAnnotation](../../com.nextfaze.devfun/-developer-annotation/index.md) annotated annotations.

*Visible for testing purposes only! Use at your own risk.*

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `DevFunProcessor()`<br>Annotation processor for [DeveloperAnnotation](../../com.nextfaze.devfun/-developer-annotation/index.md) annotated annotations. |

### Functions

| Name | Summary |
|---|---|
| [getSupportedAnnotationTypes](get-supported-annotation-types.md) | `fun getSupportedAnnotationTypes(): `[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>` |
| [getSupportedSourceVersion](get-supported-source-version.md) | `fun getSupportedSourceVersion(): `[`SourceVersion`](http://docs.oracle.com/javase/6/docs/api/javax/lang/model/SourceVersion.html) |
| [inject](inject.md) | `fun inject(injector: `[`Injector`](../-injector/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [process](process.md) | `fun process(annotations: `[`Set`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-set/index.html)`<`[`TypeElement`](http://docs.oracle.com/javase/6/docs/api/javax/lang/model/element/TypeElement.html)`>, env: `[`RoundEnvironment`](http://docs.oracle.com/javase/6/docs/api/javax/annotation/processing/RoundEnvironment.html)`): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

### Inherited Functions

| Name | Summary |
|---|---|
| [init](../-dagger-processor/init.md) | `open fun init(processingEnv: `[`ProcessingEnvironment`](http://docs.oracle.com/javase/6/docs/api/javax/annotation/processing/ProcessingEnvironment.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
