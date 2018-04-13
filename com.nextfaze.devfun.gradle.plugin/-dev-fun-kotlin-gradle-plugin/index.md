[gh-pages](../../index.md) / [com.nextfaze.devfun.gradle.plugin](../index.md) / [DevFunKotlinGradlePlugin](./index.md)

# DevFunKotlinGradlePlugin

`class DevFunKotlinGradlePlugin`

The DevFun Kotlin Gradle plugin. Configures the KAPT options.

Attempts to automatically determine the application package and build variant.
Also passes though the script configuration options.

**See Also**

[DevFunExtension](../-dev-fun-extension/index.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `DevFunKotlinGradlePlugin()`<br>The DevFun Kotlin Gradle plugin. Configures the KAPT options. |

### Functions

| Name | Summary |
|---|---|
| [apply](apply.md) | `fun apply(project: <ERROR CLASS>, kotlinCompile: <ERROR CLASS>, javaCompile: <ERROR CLASS>, variantData: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?, androidProjectHandler: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?, javaSourceSet: <ERROR CLASS>?): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<<ERROR CLASS>>` |
| [getArtifactName](get-artifact-name.md) | `fun getArtifactName(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getCompilerPluginId](get-compiler-plugin-id.md) | `fun getCompilerPluginId(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [getGroupName](get-group-name.md) | `fun getGroupName(): `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [isApplicable](is-applicable.md) | `fun isApplicable(project: <ERROR CLASS>, task: <ERROR CLASS>): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
