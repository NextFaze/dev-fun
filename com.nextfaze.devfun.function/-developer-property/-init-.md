[gh-pages](../../index.md) / [com.nextfaze.devfun.function](../index.md) / [DeveloperProperty](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`DeveloperProperty(value: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)` = "", category: `[`DeveloperCategory`](../../com.nextfaze.devfun.category/-developer-category/index.md)` = DeveloperCategory(group = "Properties"), requiresApi: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 0, transformer: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`FunctionTransformer`](../-function-transformer/index.md)`> = PropertyTransformer::class)`

An annotation that, when used on Kotlin properties, allows DevFun to provide the means of getting/setting properties on the fly.

For simple/registered types DevFun will render a UI by the same means as when invoking a function.

DevFun will correctly use the delegated getter/setter.

*Be aware, when DevFun attempts to process its generated code (such as when you open the DevMenu or access the web server), propertygetters will be called for their `toString` (special exception for `lazy` types however).*

**This feature is experimental.**

**See Also**

[PropertyTransformer](../-property-transformer.md)

[DeveloperFunction](../-developer-function/index.md)

