[gh-pages](../../index.md) / [com.nextfaze.devfun.invoke](../index.md) / [Parameter](./index.md)

# Parameter

`interface Parameter` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/invoke/View.kt#L25)

Effectively just a wrapper for [KParameter](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-parameter/index.html) to allow libraries to use it without declaring a dependency on the kotlin-reflect lib.

Currently DevFun requires the reflect lib so its safe to hide it like this for implementors to use.
In the future (ideally) the need for the reflect lib will be removed without the need for old code to change.

### Properties

| Name | Summary |
|---|---|
| [annotations](annotations.md) | `open val annotations: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Annotation`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-annotation/index.html)`>`<br>The annotations on the parameter. |
| [name](name.md) | `abstract val name: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`?`<br>The name of the parameter. |
| [type](type.md) | `abstract val type: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<*>`<br>The parameter's type/class. |
