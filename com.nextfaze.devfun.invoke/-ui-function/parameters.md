[gh-pages](../../index.md) / [com.nextfaze.devfun.invoke](../index.md) / [UiFunction](index.md) / [parameters](./parameters.md)

# parameters

`abstract val parameters: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Parameter`](../-parameter/index.md)`>` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/invoke/UiFunction.kt#L73)

The parameters of the function.

Example usage from shared preferences dialog:

``` kotlin
class Preference(key: String, override var value: Any) : Parameter, WithInitialValue<Any>, WithNullability {
    override val name: String = key
    override val type = value::class
}
```

