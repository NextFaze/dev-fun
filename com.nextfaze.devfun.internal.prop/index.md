[gh-pages](../index.md) / [com.nextfaze.devfun.internal.prop](./index.md)

## Package com.nextfaze.devfun.internal.prop

### Types

| Name | Summary |
|---|---|
| [ThreadLocalDelegate](-thread-local-delegate/index.md) | `class ThreadLocalDelegate<T> : `[`ReadWriteProperty`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.properties/-read-write-property/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`, `[`T`](-thread-local-delegate/index.md#T)`>` |
| [WeakProperty](-weak-property/index.md) | `class WeakProperty<T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`>` |

### Functions

| Name | Summary |
|---|---|
| [threadLocal](thread-local.md) | `fun <T> threadLocal(initializer: () -> `[`T`](thread-local.md#T)`): `[`ThreadLocalDelegate`](-thread-local-delegate/index.md)`<`[`T`](thread-local.md#T)`>` |
| [weak](weak.md) | `fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> weak(body: () -> `[`T`](weak.md#T)`?): `[`WeakProperty`](-weak-property/index.md)`<`[`T`](weak.md#T)`>`<br>`fun <T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> weak(): `[`WeakProperty`](-weak-property/index.md)`<`[`T`](weak.md#T)`>` |
