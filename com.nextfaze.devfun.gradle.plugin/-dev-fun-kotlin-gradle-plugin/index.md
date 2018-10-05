[gh-pages](../../index.md) / [com.nextfaze.devfun.gradle.plugin](../index.md) / [DevFunKotlinGradlePlugin](./index.md)

# DevFunKotlinGradlePlugin

`class DevFunKotlinGradlePlugin`

The DevFun Kotlin Gradle plugin. Configures the KAPT options.

Attempts to automatically determine the application package and build variant.
Also passes though the script configuration options.

**See Also**

[DevFunGradlePlugin](../-dev-fun-gradle-plugin/index.md)

[DevFunExtension](../-dev-fun-extension/index.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `DevFunKotlinGradlePlugin()`<br>The DevFun Kotlin Gradle plugin. Configures the KAPT options. |

### Functions

| Name | Summary |
|---|---|
| [apply](apply.md) | `fun apply(project: <ERROR CLASS>, kotlinCompile: <ERROR CLASS>, javaCompile: <ERROR CLASS>?, variantData: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?, androidProjectHandler: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?, kotlinCompilation: <ERROR CLASS>?): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<<ERROR CLASS>>`<br>Apply this plugin to the project. *(is current a NOP due to never receiving first variant bug?)* |
| [getCompilerPluginId](get-compiler-plugin-id.md) | `fun getCompilerPluginId(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The Gradle sub-plugin compiler plugin ID `com.nextfaze.devfun`. |
| [getPluginArtifact](get-plugin-artifact.md) | `fun getPluginArtifact(): <ERROR CLASS>`<br>The Gradle sub-plugin artifact details. |
| [isApplicable](is-applicable.md) | `fun isApplicable(project: <ERROR CLASS>, task: <ERROR CLASS>): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Determine if this plugin can be applied to this [project](is-applicable.md#com.nextfaze.devfun.gradle.plugin.DevFunKotlinGradlePlugin$isApplicable(, )/project) and compile [task](is-applicable.md#com.nextfaze.devfun.gradle.plugin.DevFunKotlinGradlePlugin$isApplicable(, )/task). |
