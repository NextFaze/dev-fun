[gh-pages](../index.md) / [com.nextfaze.devfun.inject.dagger2](index.md) / [useAutomaticDagger2Injector](.)

# useAutomaticDagger2Injector

`var useAutomaticDagger2Injector: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-inject-dagger2/src/main/java/com/nextfaze/devfun/inject/dagger2/Instances.kt#L34)

Flag to indicate if the default heavy-reflection based Dagger 2 injector should be used.

For small/simple projects the default would probably be fine, but for larger projects the reflection may take its toll.
For implementing your own (slightly more efficient) see the demo project `DemoInstanceProvider`.

This value can be disabled at any time - it can not be re-enabled without reinitializing DevFun.

**See Also**

[InjectFromDagger2](-inject-from-dagger2/index.md)

