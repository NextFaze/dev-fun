[gh-pages](../../index.md) / [com.nextfaze.devfun.invoke.view](../index.md) / [Values](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`Values(source: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`ValueSource`](../-value-source/index.md)`<`[`Iterable`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/index.html)`<*>>>)`

Annotate parameters with this specifying an [Iterable](../-value-source/index.md) class to initialize invoke views with a list of values.

See the [ValueSource](../-value-source/index.md) documentation for more information and examples.

Note: This annotation is optional. If not present then the view state will be whatever it is by default.
i.e. A `Switch` will be off, a `TextView` will be empty, etc.

This annotation is somewhat experimental.
TODO? Allow specifying default value on annotation

**See Also**

[From](../-from/index.md)

