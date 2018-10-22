[gh-pages](../index.md) / [com.nextfaze.devfun.inject.dagger2](index.md) / [useAutomaticDagger2Injector](./use-automatic-dagger2-injector.md)

# useAutomaticDagger2Injector

`var useAutomaticDagger2Injector: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-inject-dagger2/src/main/java/com/nextfaze/devfun/inject/dagger2/Instances.kt#L65)

Flag to indicate if the default heavy-reflection based Dagger 2 injector should be used.

For small/simple projects the default would probably be fine, but for larger projects the reflection may take its toll.
For implementing your own (slightly more efficient) see the demo project `DemoInstanceProvider`.

This value can be disabled at any time - it can not be re-enabled without reinitializing DevFun.

Alternatively use `@Dagger2Component` on your functions/properties (or `@get:Dagger2Component` for property getters)
that return components to tell DevFun where to find them (they can be whatever/where ever; static, in your app class,
activity class, etc).

**See Also**

[InjectFromDagger2](-inject-from-dagger2/index.md)

[Dagger2Component](../com.nextfaze.devfun.reference/-dagger2-component/index.md)

