[gh-pages](../../index.md) / [com.nextfaze.devfun.gradle.plugin](../index.md) / [KotlinGradlePlugin](./index.md)

# KotlinGradlePlugin

`object KotlinGradlePlugin`

The DevFun Kotlin Gradle plugin. Configures the KAPT options.

Attempts to automatically determine the application package and build variant.
Also passes though the script configuration options.

**See Also**

[DevFunGradlePlugin](../-dev-fun-gradle-plugin/index.md)

[DevFunExtension](../-dev-fun-extension/index.md)

### Properties

| Name | Summary |
|---|---|
| [artifactName](artifact-name.md) | `val artifactName: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [compilerPluginId](compiler-plugin-id.md) | `val compilerPluginId: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)<br>The Gradle sub-plugin compiler plugin ID `com.nextfaze.devfun`. |
| [groupName](group-name.md) | `val groupName: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [pluginArtifact](plugin-artifact.md) | `val pluginArtifact: <ERROR CLASS>`<br>The Gradle sub-plugin artifact details. |

### Functions

| Name | Summary |
|---|---|
| [isApplicable](is-applicable.md) | `fun isApplicable(project: <ERROR CLASS>, task: <ERROR CLASS>): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Determine if this plugin can be applied to this [project](is-applicable.md#com.nextfaze.devfun.gradle.plugin.KotlinGradlePlugin$isApplicable(, )/project) and compile [task](is-applicable.md#com.nextfaze.devfun.gradle.plugin.KotlinGradlePlugin$isApplicable(, )/task). |
