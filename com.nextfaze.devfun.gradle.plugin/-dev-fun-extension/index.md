[gh-pages](../../index.md) / [com.nextfaze.devfun.gradle.plugin](../index.md) / [DevFunExtension](./index.md)

# DevFunExtension

`open class DevFunExtension`

Gradle DSL for configuring DevFun.

Values provided via Gradle plugin config can/will be overridden by values provided via. APT options.

**See Also**

[DevFunProcessor](../../com.nextfaze.devfun.compiler/-dev-fun-processor/index.md)

[PACKAGE_SUFFIX](../../com.nextfaze.devfun.compiler/-p-a-c-k-a-g-e_-s-u-f-f-i-x.md)

[PACKAGE_ROOT](../../com.nextfaze.devfun.compiler/-p-a-c-k-a-g-e_-r-o-o-t.md)

[PACKAGE_OVERRIDE](../../com.nextfaze.devfun.compiler/-p-a-c-k-a-g-e_-o-v-e-r-r-i-d-e.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `DevFunExtension()`<br>Gradle DSL for configuring DevFun. |

### Properties

| Name | Summary |
|---|---|
| [packageOverride](package-override.md) | `var packageOverride: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`<br>Sets the package for the generated code. *(default: `<none>`)* |
| [packageRoot](package-root.md) | `var packageRoot: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`<br>Sets the package root for the generated code. *(default: `<application package>`)* |
| [packageSuffix](package-suffix.md) | `var packageSuffix: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`?`<br>Sets the package suffix for the generated code. *(default: `devfun_generated`)* |
