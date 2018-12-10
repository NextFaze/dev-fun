[gh-pages](../../index.md) / [com.nextfaze.devfun.invoke.view](../index.md) / [From](./index.md)

# From

`@Target([AnnotationTarget.VALUE_PARAMETER]) annotation class From` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/invoke/view/ValueSource.kt#L18)

Annotate parameters with this specifying a [ValueSource](../-value-source/index.md) class to initialize invoke views with an initial value.

See the [ValueSource](../-value-source/index.md) documentation for more information and examples.

Note: This annotation is optional. If not present then the view state will be whatever it is by default.
i.e. A `Switch` will be off, a `TextView` will be empty, etc.

**See Also**

[Values](../-values/index.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `From(source: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`ValueSource`](../-value-source/index.md)`<*>>)`<br>Annotate parameters with this specifying a [ValueSource](../-value-source/index.md) class to initialize invoke views with an initial value. |

### Properties

| Name | Summary |
|---|---|
| [source](source.md) | `val source: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`ValueSource`](../-value-source/index.md)`<*>>`<br>The [ValueSource](../-value-source/index.md) class that will be injected/instantiated when the parameter value is needed. |
