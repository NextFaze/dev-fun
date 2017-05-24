[gh-pages](../../index.md) / [com.nextfaze.devfun.core](../index.md) / [DevFun](index.md) / [&lt;init&gt;](.)

# &lt;init&gt;

`DevFun()`

Primary entry point and initializer of DevFun and associated libraries.

Modules can be added post- initialization by way of `devFun += SomeModule()` ([plusAssign](plus-assign.md)), after which [tryInitModules](try-init-modules.md) should be called.

To manually initialize, create instance and call [initialize](initialize.md).
A static reference will be set to this automatically, and can be retrieved using [devFun](../dev-fun.md).

e.g. `DevFun().initialize(applicationContext)`

