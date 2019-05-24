[gh-pages](../../index.md) / [com.nextfaze.devfun.compiler](../index.md) / [DaggerProcessor](./index.md)

# DaggerProcessor

`abstract class DaggerProcessor : `[`AbstractProcessor`](http://docs.oracle.com/javase/6/docs/api/javax/annotation/processing/AbstractProcessor.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-compiler/src/main/java/com/nextfaze/devfun/compiler/Dagger.kt#L53)

Base [AbstractProcessor](http://docs.oracle.com/javase/6/docs/api/javax/annotation/processing/AbstractProcessor.html) class with Dagger support.

*Visible for testing purposes only! Use at your own risk.*

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `DaggerProcessor()`<br>Base [AbstractProcessor](http://docs.oracle.com/javase/6/docs/api/javax/annotation/processing/AbstractProcessor.html) class with Dagger support. |

### Functions

| Name | Summary |
|---|---|
| [init](init.md) | `open fun init(processingEnv: `[`ProcessingEnvironment`](http://docs.oracle.com/javase/6/docs/api/javax/annotation/processing/ProcessingEnvironment.html)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [inject](inject.md) | `abstract fun inject(injector: `[`Injector`](../-injector/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inheritors

| Name | Summary |
|---|---|
| [DevAnnotationProcessor](../-dev-annotation-processor/index.md) | `class DevAnnotationProcessor : `[`DaggerProcessor`](./index.md)<br>Annotation processor for [DeveloperAnnotation](../../com.nextfaze.devfun/-developer-annotation/index.md) to generate properties interfaces. |
| [DevFunProcessor](../-dev-fun-processor/index.md) | `class DevFunProcessor : `[`DaggerProcessor`](./index.md)<br>Annotation processor for [DeveloperAnnotation](../../com.nextfaze.devfun/-developer-annotation/index.md) annotated annotations. |
