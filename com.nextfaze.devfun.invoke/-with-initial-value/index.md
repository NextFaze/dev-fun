[gh-pages](../../index.md) / [com.nextfaze.devfun.invoke](../index.md) / [WithInitialValue](./index.md)

# WithInitialValue

`interface WithInitialValue<out T>` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/invoke/View.kt#L58)

[Parameter](../-parameter/index.md) objects that implement this will use the value provided by [WithInitialValue.value](value.md) rather than checking
for an @[From](../../com.nextfaze.devfun.invoke.view/-from/index.md) annotation.

**See Also**

[ValueSource](../../com.nextfaze.devfun.invoke.view/-value-source/index.md)

### Properties

| Name | Summary |
|---|---|
| [value](value.md) | `abstract val value: `[`T`](index.md#T)<br>The initial/default value of the parameter. |

### Inheritors

| Name | Summary |
|---|---|
| [UiField](../-ui-field/index.md) | `interface UiField<T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> : `[`Parameter`](../-parameter/index.md)`, `[`WithInitialValue`](./index.md)`<`[`T`](../-ui-field/index.md#T)`>`<br>Utility interface to easily generate an invoke UI/dialog. *(experimental)* |
