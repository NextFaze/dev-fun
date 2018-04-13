[gh-pages](../../index.md) / [com.nextfaze.devfun.invoke.view](../index.md) / [WithValue](./index.md)

# WithValue

`interface WithValue<T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/invoke/view/ParameterView.kt#L18)

Views used with the Invoke UI should implement this to ensure the correct value is obtainable upon invocation.

If absent, DevFun will attempt to grab the value based on the view type (e.g. if TextView it will use `.text`)

Currently `null` types are not supported due to limitations/complications of KAPT and reflection. It is intended to
be permissible in the future.

### Properties

| Name | Summary |
|---|---|
| [value](value.md) | `abstract var value: `[`T`](index.md#T)<br>The value of this view to be passed to the function for invocation. |

### Inheritors

| Name | Summary |
|---|---|
| [ErrorParameterView](../../com.nextfaze.devfun.invoke.view.simple/-error-parameter-view.md) | `interface ErrorParameterView : `[`WithValue`](./index.md)`<`[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<*>>`<br>A simple view that should tell the user if a view could not be injected/rendered. |
| [InjectedParameterView](../../com.nextfaze.devfun.invoke.view.simple/-injected-parameter-view.md) | `interface InjectedParameterView : `[`WithValue`](./index.md)`<`[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<*>>`<br>A simple view that should tell the user if a parameter is being injected. |
