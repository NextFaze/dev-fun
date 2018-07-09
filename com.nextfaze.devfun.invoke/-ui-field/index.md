[gh-pages](../../index.md) / [com.nextfaze.devfun.invoke](../index.md) / [UiField](./index.md)

# UiField

`interface UiField<T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> : `[`Parameter`](../-parameter/index.md)`, `[`WithInitialValue`](../-with-initial-value/index.md)`<`[`T`](index.md#T)`>` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/invoke/UiUtil.kt#L12)

Utility interface to easily generate an invoke UI/dialog. *(experimental)*

**See Also**

[uiField](../ui-field.md)

[uiButton](../ui-button.md)

[uiFunction](../ui-function.md)

### Properties

| Name | Summary |
|---|---|
| [setValue](set-value.md) | `abstract val setValue: (`[`T`](index.md#T)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Callback when the value is to be set. |

### Inherited Properties

| Name | Summary |
|---|---|
| [annotations](../-parameter/annotations.md) | `open val annotations: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Annotation`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-annotation/index.html)`>`<br>The annotations on the parameter. |
| [name](../-parameter/name.md) | `abstract val name: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`?`<br>The name of the parameter. |
| [type](../-parameter/type.md) | `abstract val type: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<*>`<br>The parameter's type/class. |
| [value](../-with-initial-value/value.md) | `abstract val value: `[`T`](../-with-initial-value/index.md#T)<br>The initial/default value of the parameter. |
