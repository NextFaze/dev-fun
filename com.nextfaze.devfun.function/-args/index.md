[gh-pages](../../index.md) / [com.nextfaze.devfun.function](../index.md) / [Args](./index.md)

# Args

`annotation class Args` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/function/DeveloperArguments.kt#L152)

Nested annotation for declaring the arguments of a function invocation.

### Parameters

`value` - The arguments of the invocation. At present only primitive types are supported (`.toType()` will be used on the entry).

**See Also**

[DeveloperArguments](../-developer-arguments/index.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Args(value: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>)`<br>Nested annotation for declaring the arguments of a function invocation. |

### Properties

| Name | Summary |
|---|---|
| [value](value.md) | `val value: `[`Array`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)`<`[`String`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)`>`<br>The arguments of the invocation. At present only primitive types are supported (`.toType()` will be used on the entry). |
