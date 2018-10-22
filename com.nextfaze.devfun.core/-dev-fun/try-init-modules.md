[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [DevFun](index.md) / [tryInitModules](./try-init-modules.md)

# tryInitModules

`fun tryInitModules(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/core/DevFun.kt#L361)

Attempts to initialize uninitialized modules.

Can be called any number of times.

Can also be called at any time as long as the module's initialization function [DevFunModule.initialize](../-dev-fun-module/initialize.md)
doesn't try to use [devFun](../dev-fun.md). [initialize](initialize.md) should be called called before this to be safe.

Modules without dependencies will fail to initialize. Add them and call this again.

