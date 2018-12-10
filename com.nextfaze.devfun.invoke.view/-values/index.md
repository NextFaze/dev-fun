[gh-pages](../../index.md) / [com.nextfaze.devfun.invoke.view](../index.md) / [Values](./index.md)

# Values

`@Target([AnnotationTarget.VALUE_PARAMETER]) annotation class Values` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/invoke/view/ValueSource.kt#L63)

Annotate parameters with this specifying an [Iterable](../-value-source/index.md) class to initialize invoke views with a list of values.

See the [ValueSource](../-value-source/index.md) documentation for more information and examples.

Note: This annotation is optional. If not present then the view state will be whatever it is by default.
i.e. A `Switch` will be off, a `TextView` will be empty, etc.

This annotation is somewhat experimental.
TODO? Allow specifying default value on annotation

**See Also**

[From](../-from/index.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Values(source: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`ValueSource`](../-value-source/index.md)`<`[`Iterable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)`<*>>>)`<br>Annotate parameters with this specifying an [Iterable](../-value-source/index.md) class to initialize invoke views with a list of values. |

### Properties

| Name | Summary |
|---|---|
| [source](source.md) | `val source: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`ValueSource`](../-value-source/index.md)`<`[`Iterable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)`<*>>>`<br>The [ValueSource](../-value-source/index.md) class that will be injected/instantiated when the parameter value is needed. |
