[gh-pages](../index.md) / [com.nextfaze.devfun.compiler](./index.md)

## Package com.nextfaze.devfun.compiler

Annotation processor that handles [DeveloperFunction](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.function/-developer-function/)
 and [DeveloperCategory](https://nextfaze.github.io/dev-fun/com.nextfaze.devfun.category/-developer-category/) annotations.

### Types

| Name | Summary |
|---|---|
| [DaggerProcessor](-dagger-processor/index.md) | `abstract class DaggerProcessor : `[`AbstractProcessor`](http://docs.oracle.com/javase/6/docs/api/javax/annotation/processing/AbstractProcessor.html)<br>Base [AbstractProcessor](http://docs.oracle.com/javase/6/docs/api/javax/annotation/processing/AbstractProcessor.html) class with Dagger support. |
| [DevAnnotationProcessor](-dev-annotation-processor/index.md) | `class DevAnnotationProcessor : `[`DaggerProcessor`](-dagger-processor/index.md)<br>Annotation processor for [DeveloperAnnotation](../com.nextfaze.devfun/-developer-annotation/index.md) to generate properties interfaces. |
| [DevFunProcessor](-dev-fun-processor/index.md) | `class DevFunProcessor : `[`DaggerProcessor`](-dagger-processor/index.md)<br>Annotation processor for [DeveloperAnnotation](../com.nextfaze.devfun/-developer-annotation/index.md) annotated annotations. |
| [Injector](-injector/index.md) | `interface Injector`<br>Dagger injector for [DaggerProcessor](-dagger-processor/index.md). |

### Properties

| Name | Summary |
|---|---|
| [APPLICATION_PACKAGE](-a-p-p-l-i-c-a-t-i-o-n_-p-a-c-k-a-g-e.md) | `const val APPLICATION_PACKAGE: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Your application's package as sourced from your manifest file via the DevFun Gradle plugin. |
| [APPLICATION_VARIANT](-a-p-p-l-i-c-a-t-i-o-n_-v-a-r-i-a-n-t.md) | `const val APPLICATION_VARIANT: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The current build variant as sourced from the variant data/compile task via the DevFun Gradle plugin. |
| [ELEMENTS_FILTER_EXCLUDE](-e-l-e-m-e-n-t-s_-f-i-l-t-e-r_-e-x-c-l-u-d-e.md) | `const val ELEMENTS_FILTER_EXCLUDE: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Restrict DevFun to only process elements matching filter `elementFQN.startsWith(it)`. *(default: `<none>`)* |
| [ELEMENTS_FILTER_INCLUDE](-e-l-e-m-e-n-t-s_-f-i-l-t-e-r_-i-n-c-l-u-d-e.md) | `const val ELEMENTS_FILTER_INCLUDE: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Restrict DevFun to only process elements matching filter `elementFQN.startsWith(it)`.  *(default: `<none>`)* |
| [EXT_PACKAGE_OVERRIDE](-e-x-t_-p-a-c-k-a-g-e_-o-v-e-r-r-i-d-e.md) | `const val EXT_PACKAGE_OVERRIDE: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The same as [PACKAGE_OVERRIDE](-p-a-c-k-a-g-e_-o-v-e-r-r-i-d-e.md), but is from the `devFun {}` configuration of the DevFun Grade plugin. |
| [EXT_PACKAGE_ROOT](-e-x-t_-p-a-c-k-a-g-e_-r-o-o-t.md) | `const val EXT_PACKAGE_ROOT: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The same as [PACKAGE_ROOT](-p-a-c-k-a-g-e_-r-o-o-t.md), but is from the `devFun {}` configuration of the DevFun Grade plugin. |
| [EXT_PACKAGE_SUFFIX](-e-x-t_-p-a-c-k-a-g-e_-s-u-f-f-i-x.md) | `const val EXT_PACKAGE_SUFFIX: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The same as [PACKAGE_SUFFIX](-p-a-c-k-a-g-e_-s-u-f-f-i-x.md), but is from the `devFun {}` configuration of the DevFun Grade plugin. |
| [FLAG_DEBUG_COMMENTS](-f-l-a-g_-d-e-b-u-g_-c-o-m-m-e-n-t-s.md) | `const val FLAG_DEBUG_COMMENTS: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Flag to output additional debug info as code comments. *(default: `false`)* |
| [FLAG_DEBUG_VERBOSE](-f-l-a-g_-d-e-b-u-g_-v-e-r-b-o-s-e.md) | `const val FLAG_DEBUG_VERBOSE: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Flag to enable additional compile/processing log output. *(default: `false`)* |
| [FLAG_USE_KOTLIN_REFLECTION](-f-l-a-g_-u-s-e_-k-o-t-l-i-n_-r-e-f-l-e-c-t-i-o-n.md) | `const val FLAG_USE_KOTLIN_REFLECTION: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Flag to enable Kotlin reflection to get method references. *(default: `false`)* **(experimental)** |
| [GENERATE_DEFINITIONS](-g-e-n-e-r-a-t-e_-d-e-f-i-n-i-t-i-o-n-s.md) | `const val GENERATE_DEFINITIONS: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Flag to control generation of implementations of [DevFunGenerated](../com.nextfaze.devfun.generated/-dev-fun-generated/index.md). *(default: `<true>`)* |
| [GENERATE_INTERFACES](-g-e-n-e-r-a-t-e_-i-n-t-e-r-f-a-c-e-s.md) | `const val GENERATE_INTERFACES: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Flag to control generation of [DeveloperAnnotation](../com.nextfaze.devfun/-developer-annotation/index.md) properties `AnnotationProperties` interfaces. *(default: `<true>`)* |
| [NOTE_LOGGING_ENABLED](-n-o-t-e_-l-o-g-g-i-n-g_-e-n-a-b-l-e-d.md) | `const val NOTE_LOGGING_ENABLED: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Flag to enable logging of `note` messages.  *(default: `<false>`)* |
| [PACKAGE_OVERRIDE](-p-a-c-k-a-g-e_-o-v-e-r-r-i-d-e.md) | `const val PACKAGE_OVERRIDE: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Sets the package for the generated code. *(default: `<none>`)* |
| [PACKAGE_ROOT](-p-a-c-k-a-g-e_-r-o-o-t.md) | `const val PACKAGE_ROOT: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Sets the package root for the generated code. *(default: `<application package>`)* |
| [PACKAGE_SUFFIX](-p-a-c-k-a-g-e_-s-u-f-f-i-x.md) | `const val PACKAGE_SUFFIX: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Sets the package suffix for the generated code. *(default: `devfun_generated`)* |
| [PACKAGE_SUFFIX_DEFAULT](-p-a-c-k-a-g-e_-s-u-f-f-i-x_-d-e-f-a-u-l-t.md) | `const val PACKAGE_SUFFIX_DEFAULT: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Default package output suffix: `devfun_generated` |
| [PROMOTE_NOTE_LOG_MESSAGES](-p-r-o-m-o-t-e_-n-o-t-e_-l-o-g_-m-e-s-s-a-g-e-s.md) | `const val PROMOTE_NOTE_LOG_MESSAGES: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>Flag to have all `note` messages output as `warning` - workaround for various KAPT logging implementations and for debugging purposes.  *(default: `<false>`)* |
