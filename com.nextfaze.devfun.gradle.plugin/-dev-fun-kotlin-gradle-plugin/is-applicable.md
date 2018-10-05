[gh-pages](../../index.md) / [com.nextfaze.devfun.gradle.plugin](../index.md) / [DevFunKotlinGradlePlugin](index.md) / [isApplicable](./is-applicable.md)

# isApplicable

`fun isApplicable(project: <ERROR CLASS>, task: <ERROR CLASS>): `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

Determine if this plugin can be applied to this [project](is-applicable.md#com.nextfaze.devfun.gradle.plugin.DevFunKotlinGradlePlugin$isApplicable(, )/project) and compile [task](is-applicable.md#com.nextfaze.devfun.gradle.plugin.DevFunKotlinGradlePlugin$isApplicable(, )/task).

For some reason the [apply](apply.md) call never receives the first variant so most of the logic is performed in here instead.

