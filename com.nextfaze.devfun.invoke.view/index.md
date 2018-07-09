[gh-pages](../index.md) / [com.nextfaze.devfun.invoke.view](./index.md)

## Package com.nextfaze.devfun.invoke.view

### Types

| Name | Summary |
|---|---|
| [InvokeParameterView](-invoke-parameter-view/index.md) | `interface InvokeParameterView : `[`WithLabel`](-with-label/index.md)<br>Parameter views rendered for the Invoke UI will be wrapped with this type (to provide a label etc.). |
| [ValueSource](-value-source/index.md) | `interface ValueSource<out V : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>`<br>Used in conjunction with [From](-from/index.md) to allow the Invoke UI to provide an initial value. |
| [WithLabel](-with-label/index.md) | `interface WithLabel`<br>View used with the Invoke UI will automatically be wrapped and be given a label unless they provide their own with this interface. |
| [WithValue](-with-value/index.md) | `interface WithValue<T>`<br>Views used with the Invoke UI should implement this to ensure the correct value is obtainable upon invocation. |

### Annotations

| Name | Summary |
|---|---|
| [ColorPicker](-color-picker/index.md) | `annotation class ColorPicker`<br>Annotated `Int` value parameters will render a color picker view rather than an input/edit for use with invoke UI. |
| [From](-from/index.md) | `annotation class From`<br>Annotate parameters with this specifying a [ValueSource](-value-source/index.md) class to initialize invoke views with an initial value. |
| [Ranged](-ranged/index.md) | `annotation class Ranged`<br>Used to restrict the range of a [Number](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-number/index.html) for user input. Using this will render a slider rather than a text view. |
