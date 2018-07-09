[gh-pages](../index.md) / [com.nextfaze.devfun.internal.pref](index.md) / [KNullablePreference](./-k-nullable-preference.md)

# KNullablePreference

`interface KNullablePreference<T : `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`> : `[`KPreference`](-k-preference/index.md)`<`[`T`](-k-nullable-preference.md#T)`?>` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-internal/src/main/java/com/nextfaze/devfun/internal/pref/SharedPreferences.kt#L24)

### Inherited Properties

| Name | Summary |
|---|---|
| [isSet](-k-preference/is-set.md) | `abstract val isSet: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [value](-k-preference/value.md) | `abstract var value: `[`TValue`](-k-preference/index.md#TValue) |

### Inherited Functions

| Name | Summary |
|---|---|
| [delete](-k-preference/delete.md) | `abstract fun delete(): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [getValue](-k-preference/get-value.md) | `abstract operator fun getValue(thisRef: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?, property: `[`KProperty`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property/index.html)`<*>): `[`TValue`](-k-preference/index.md#TValue) |
| [setValue](-k-preference/set-value.md) | `abstract operator fun setValue(thisRef: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?, property: `[`KProperty`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-property/index.html)`<*>, value: `[`TValue`](-k-preference/index.md#TValue)`): `[`Unit`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
