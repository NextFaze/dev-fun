[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [DevFun](index.md) / [plusAssign](./plus-assign.md)

# plusAssign

`operator fun plusAssign(module: `[`DevFunModule`](../-dev-fun-module/index.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/core/DevFun.kt#L293)

Add a module.

Can be called at any time. Call [tryInitModules](try-init-modules.md) after adding a module.

`operator fun plusAssign(onInitialized: `[`OnInitialized`](../-on-initialized.md)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/core/DevFun.kt#L356)

Add an initialization callback.

Will be called immediately if [isInitialized](is-initialized.md).

References to callbacks will not be held after it has been called.

**See Also**

[initialize](initialize.md)

[minusAssign](minus-assign.md)

