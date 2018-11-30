[gh-pages](../../index.md) / [com.nextfaze.devfun.invoke](../index.md) / [WithKParameter](index.md) / [kParameter](./k-parameter.md)

# kParameter

`abstract val kParameter: `[`KParameter`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-parameter/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/invoke/View.kt#L92)

Reference to the underlying [KParameter](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-parameter/index.html) if this parameter represents a native function.

If you need to use this please create an issue for why so that the [Parameter](../-parameter/index.md) API can be updated accordingly.

If you do use this you can safely ignore the warnings regarding the kotlin-reflect lib as DevFun requires it.
If/when the reflect lib is no longer needed, this field will be deprecated and removed.
Having said that, I don't expect it to be removed any time soon.

