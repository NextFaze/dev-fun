[gh-pages](../../index.md) / [com.nextfaze.devfun](../index.md) / [DeveloperAnnotation](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`DeveloperAnnotation(developerFunction: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, developerCategory: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, developerReference: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false)`

Annotation used to by DevFun to "tag" references to other DevFun/developer -related annotations.

# Behaviour

By default this annotation will do nothing - you must set one of the flags to `true` to take effect.

*This is a somewhat experimental annotation - though DevFun uses it directly now (`@DeveloperFunction` is annotated `@DeveloperAnnotation(developerFunction = true)`).Having said that, attempts have been made for it to be quite versatile but it is still quite a recent aspect.*

## [developerFunction](developer-function.md)

When `true` the compiler will treat it as if it was an @[DeveloperFunction](../../com.nextfaze.devfun.function/-developer-function/index.md) annotation. In this state the compiler will check for the
same fields of `@DeveloperFunction`, falling back to the standard defaults if they are absent.

If you have different defaults defined compared to [DeveloperFunction](../../com.nextfaze.devfun.function/-developer-function/index.md) then these values will be written as if you had used
`@DeveloperFunction(field = value)` at the declaration site - this behaviour is somewhat experimental. Please report any issues you encounter.

An example of this can be seen with @[DeveloperProperty](../../com.nextfaze.devfun.function/-developer-property/index.md).

## [developerCategory](developer-category.md)

When `true` the compiler will treat it as if it was an @[DeveloperCategory](../../com.nextfaze.devfun.category/-developer-category/index.md) annotation. In this state the compiler will check for the
same fields of `@DeveloperCategory`, falling back to the standard defaults if they are absent.

An example of this can be seen with @[ContextCategory](../../com.nextfaze.devfun.category/-context-category/index.md).

## [developerReference](developer-reference.md)

When `true` the compiler will treat it as if it was an @[DeveloperReference](../../com.nextfaze.devfun.reference/-developer-reference/index.md) annotation.

An example of this can be seen with @[Dagger2Component](../../com.nextfaze.devfun.reference/-dagger2-component/index.md).

# Custom Properties

Properties named the same as the DevFun properties have the same meaning/behaviour.
Other properties will be "serialized" and are available on the associated definition - DevFun will generate a "properties"
object for annotations (e.g. [DeveloperLogger](../../com.nextfaze.devfun.reference/-developer-logger/index.md) -&gt; [DeveloperLoggerProperties](../../com.nextfaze.devfun.reference/-developer-logger-properties/index.md))

Use helper functions [withProperties](../../com.nextfaze.devfun.reference/with-properties.md) or [getProperties](../../com.nextfaze.devfun.reference/get-properties.md) to get an instance of the properties object.
Unset values will return their default values as defined in the annotation.

### Parameters

`developerFunction` - Set to `true` to have the compiler treat the annotation as a @[DeveloperFunction](../../com.nextfaze.devfun.function/-developer-function/index.md). *(experimental)*

`developerCategory` - Set to `true` to have the compiler treat the annotation as a @[DeveloperCategory](../../com.nextfaze.devfun.category/-developer-category/index.md). *(experimental)*

`developerReference` - Set to `true` to have the compiler treat the annotation as a @[DeveloperReference](../../com.nextfaze.devfun.reference/-developer-reference/index.md). *(experimental)*

**See Also**

[Dagger2Component](../../com.nextfaze.devfun.reference/-dagger2-component/index.md)

[DeveloperArguments](../../com.nextfaze.devfun.function/-developer-arguments/index.md)

[ReferenceDefinition](../../com.nextfaze.devfun.reference/-reference-definition/index.md)

