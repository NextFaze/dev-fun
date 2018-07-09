[gh-pages](../../index.md) / [com.nextfaze.devfun.annotations](../index.md) / [Dagger2Scope](./index.md)

# Dagger2Scope

`enum class Dagger2Scope` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/annotations/Dagger2.kt#L12)

Some range of scopes for use with [Dagger2Component](../-dagger2-component/index.md). Priority is based on their ordinal value (higher = broader scope).

If for whatever reason you want more control or don't want named like this then you can manually set [Dagger2Component.priority](../-dagger2-component/priority.md).

### Enum Values

| Name | Summary |
|---|---|
| [UNDEFINED](-u-n-d-e-f-i-n-e-d.md) |  |
| [LOWEST](-l-o-w-e-s-t.md) |  |
| [VIEW](-v-i-e-w.md) |  |
| [LOWER](-l-o-w-e-r.md) |  |
| [FRAGMENT](-f-r-a-g-m-e-n-t.md) |  |
| [LOW](-l-o-w.md) |  |
| [ACTIVITY](-a-c-t-i-v-i-t-y.md) |  |
| [HIGH](-h-i-g-h.md) |  |
| [RETAINED_FRAGMENT](-r-e-t-a-i-n-e-d_-f-r-a-g-m-e-n-t.md) |  |
| [HIGHER](-h-i-g-h-e-r.md) |  |
| [APPLICATION](-a-p-p-l-i-c-a-t-i-o-n.md) |  |
| [HIGHEST](-h-i-g-h-e-s-t.md) |  |

### Properties

| Name | Summary |
|---|---|
| [isActivityRequired](is-activity-required.md) | `val isActivityRequired: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [isFragmentActivityRequired](is-fragment-activity-required.md) | `val isFragmentActivityRequired: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
