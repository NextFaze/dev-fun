[gh-pages](../../index.md) / [com.nextfaze.devfun.annotations](../index.md) / [DeveloperAnnotation](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`DeveloperAnnotation(developerFunction: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false)`

Annotation used to by DevFun to "tag" references to some other annotations.

By default usages of the annotated annotations result in generated [DeveloperReference](../../com.nextfaze.devfun.core/-developer-reference/index.md) instances.

However, if [developerFunction](developer-function.md) is set to `true` then the compiler will treat it as if it was an @[DeveloperFunction](../-developer-function/index.md) annotation.
In this state the compiler will check for the same fields of `@DeveloperFunction`.

If you have different defaults defined compared to [DeveloperFunction](../-developer-function/index.md) then these values will be written as if you had used
`@DeveloperFunction(field = value)` at the declaration site - this behaviour is somewhat experimental. Please report any issues you have.

An example of this can be seen with @[DeveloperProperty](../-developer-property/index.md)

### Parameters

`developerFunction` - Set to `true` to have the compiler treat the annotation as a @[DeveloperFunction](../-developer-function/index.md). *(experimental)*

**See Also**

[Dagger2Component](../-dagger2-component/index.md)

[DeveloperProperty](../-developer-property/index.md)

