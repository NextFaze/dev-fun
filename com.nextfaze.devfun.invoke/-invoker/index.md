[gh-pages](../../index.md) / [com.nextfaze.devfun.invoke](../index.md) / [Invoker](./index.md)

# Invoker

`interface Invoker` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/invoke/Invoker.kt#L17)

Used to invoke a [FunctionItem](../../com.nextfaze.devfun.core/-function-item/index.md) or [UiFunction](../-ui-function/index.md) and automatically handles parameter injection and errors.

### Functions

| Name | Summary |
|---|---|
| [invoke](invoke.md) | `abstract fun invoke(item: `[`FunctionItem`](../../com.nextfaze.devfun.core/-function-item/index.md)`): `[`InvokeResult`](../../com.nextfaze.devfun.core/-invoke-result/index.md)`?`<br>Invokes a function item, using the invocation UI if needed.`abstract fun invoke(function: `[`UiFunction`](../-ui-function/index.md)`): `[`InvokeResult`](../../com.nextfaze.devfun.core/-invoke-result/index.md)`?`<br>Invokes a function using the invocation UI. |
