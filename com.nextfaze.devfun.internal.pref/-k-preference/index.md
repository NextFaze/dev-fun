[gh-pages](../../index.md) / [com.nextfaze.devfun.internal.pref](../index.md) / [KPreference](./index.md)

# KPreference

`interface KPreference<TValue>` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-internal/src/main/java/com/nextfaze/devfun/internal/pref/SharedPreferences.kt#L13)

### Properties

| Name | Summary |
|---|---|
| [isSet](is-set.md) | `abstract val isSet: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [value](value.md) | `abstract var value: `[`TValue`](index.md#TValue) |

### Functions

| Name | Summary |
|---|---|
| [delete](delete.md) | `abstract fun delete(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [getValue](get-value.md) | `abstract operator fun getValue(thisRef: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?, property: `[`KProperty`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property/index.html)`<*>): `[`TValue`](index.md#TValue) |
| [setValue](set-value.md) | `abstract operator fun setValue(thisRef: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?, property: `[`KProperty`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property/index.html)`<*>, value: `[`TValue`](index.md#TValue)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

### Inheritors

| Name | Summary |
|---|---|
| [KNullablePreference](../-k-nullable-preference.md) | `interface KNullablePreference<T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> : `[`KPreference`](./index.md)`<`[`T`](../-k-nullable-preference.md#T)`?>` |
