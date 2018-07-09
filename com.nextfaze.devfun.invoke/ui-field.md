[gh-pages](../index.md) / [com.nextfaze.devfun.invoke](index.md) / [uiField](./ui-field.md)

# uiField

`fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> uiField(name: `[`CharSequence`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-char-sequence/index.html)`, initialValue: `[`T`](ui-field.md#T)`, annotations: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Annotation`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-annotation/index.html)`> = emptyList(), onSetValue: (`[`T`](ui-field.md#T)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)`): `[`UiField`](-ui-field/index.md)`<`[`T`](ui-field.md#T)`>` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/invoke/UiUtil.kt#L24)

Utility function to create a [UiField](-ui-field/index.md) instance. *(experimental)*

### Parameters

`name` - The name of this option.

`initialValue` - The initial value.

`onSetValue` - Callback when the value is to be set.