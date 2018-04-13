[gh-pages](../../index.md) / [com.nextfaze.devfun.invoke.view](../index.md) / [From](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`From(source: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`ValueSource`](../-value-source/index.md)`<*>>)`

Annotate parameters with this specifying a [ValueSource](../-value-source/index.md) class to initialize invoke views with an initial value.

See the [ValueSource](../-value-source/index.md) documentation for more information and examples.

Note: This annotation is optional. If not present then the view state will be whatever it is by default.
i.e. A `Switch` will be off, a `TextView` will be empty, etc.

