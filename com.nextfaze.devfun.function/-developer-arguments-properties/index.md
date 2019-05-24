[gh-pages](../../index.md) / [com.nextfaze.devfun.function](../index.md) / [DeveloperArgumentsProperties](./index.md)

# DeveloperArgumentsProperties

`interface DeveloperArgumentsProperties` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/function/DeveloperArguments.kt#L171)

Properties interface for @[DeveloperArguments](../-developer-arguments/index.md).

TODO: This interface should be generated by DevFun at compile time, but as the annotations are in a separate module to the compiler
that itself depends on the annotations module, it is non-trivial to run the DevFun processor upon it (module dependencies become cyclic).

### Properties

| Name | Summary |
|---|---|
| [args](args.md) | `abstract val args: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`ArgsProperties`](../-args-properties/index.md)`>` |
| [category](category.md) | `open val category: `[`DeveloperCategoryProperties`](../../com.nextfaze.devfun.category/-developer-category-properties/index.md) |
| [group](group.md) | `open val group: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [name](name.md) | `open val name: `[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [requiresApi](requires-api.md) | `open val requiresApi: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [transformer](transformer.md) | `open val transformer: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<out `[`FunctionTransformer`](../-function-transformer/index.md)`>` |