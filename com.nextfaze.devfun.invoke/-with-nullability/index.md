[gh-pages](../../index.md) / [com.nextfaze.devfun.invoke](../index.md) / [WithNullability](./index.md)

# WithNullability

`interface WithNullability` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/invoke/View.kt#L76)

[Parameter](../-parameter/index.md) objects that implement this will can be nullable.

*Warning: Should only be used for user-defined [UiFunction](../-ui-function/index.md) parameters as most of DevFun does not support nullability.*

### Properties

| Name | Summary |
|---|---|
| [isNullable](is-nullable.md) | `open val isNullable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Is this parameter nullable. If true, then a `null` checkbox will be visible. |
