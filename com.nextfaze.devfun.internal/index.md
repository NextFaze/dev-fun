[gh-pages](../index.md) / [com.nextfaze.devfun.internal](./index.md)

## Package com.nextfaze.devfun.internal

### Types

| Name | Summary |
|---|---|
| [ReflectedMethod](-reflected-method/index.md) | `interface ReflectedMethod : () -> `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?, (`[`InstanceProvider`](../com.nextfaze.devfun.inject/-instance-provider/index.md)`) -> `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?` |
| [ReflectedProperty](-reflected-property/index.md) | `interface ReflectedProperty` |
| [WindowCallbacks](-window-callbacks/index.md) | `class WindowCallbacks`<br>Handles wrapping and posting [Window](https://developer.android.com/reference/android/view/Window.html) events throughout an app's life. |
| [WithSubGroup](-with-sub-group/index.md) | `interface WithSubGroup`<br>When implemented by a [FunctionItem](../com.nextfaze.devfun.function/-function-item/index.md) the DevMenu will render a sub-group for it. *Not explicitly intended for public use - primarilyhere as a "fix/workaround" for #19 (where Context overrides the user defined group)* |

### Type Aliases

| Name | Summary |
|---|---|
| [FocusChangeListener](-focus-change-listener.md) | `typealias FocusChangeListener = (focus: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)`) -> `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)<br>Function signature of a listener for [Window](https://developer.android.com/reference/android/view/Window.html) focus changes. |
| [KeyEventListener](-key-event-listener.md) | `typealias KeyEventListener = (event: `[`KeyEvent`](https://developer.android.com/reference/android/view/KeyEvent.html)`) -> `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Function signature of a listener for [Window](https://developer.android.com/reference/android/view/Window.html) key events. |

### Extensions for External Classes

| Name | Summary |
|---|---|
| [java.lang.reflect.Method](java.lang.reflect.-method/index.md) |  |
| [kotlin.String](kotlin.-string/index.md) |  |
| [kotlin.reflect.KClass](kotlin.reflect.-k-class/index.md) |  |

### Properties

| Name | Summary |
|---|---|
| [isInstrumentationTest](is-instrumentation-test.md) | `val isInstrumentationTest: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
