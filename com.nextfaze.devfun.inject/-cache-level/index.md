[gh-pages](../../index.md) / [com.nextfaze.devfun.inject](../index.md) / [CacheLevel](./index.md)

# CacheLevel

`enum class CacheLevel` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/inject/InstanceProviders.kt#L53)

Controls how aggressively the [CompositeInstanceProvider](../-composite-instance-provider.md) caches the sources of class instances.

* [AGGRESSIVE](-a-g-g-r-e-s-s-i-v-e.md) Once a type has been found from a provider, that provider will checked before others.
If it returns `null` the next time then the lookup will continue be as if [NONE](-n-o-n-e.md).
Can result in a significant performance improvement.

* [SINGLE_LOOP](-s-i-n-g-l-e_-l-o-o-p.md) Behaves the same as [AGGRESSIVE](-a-g-g-r-e-s-s-i-v-e.md), but is thread-local and caching is only present once per top-level call.
i.e. If something calls `devFun.get<Type>()` - for that one "top-level" call (and thus present for recursion).
This will likely resolve any issues you have with [AGGRESSIVE](-a-g-g-r-e-s-s-i-v-e.md) while still providing a small improvement - please report any issues!

* [NONE](-n-o-n-e.md) No caching - all providers are checked in reverse order they are added as normal.
Can be quite slow for complex hierarchies (e.g. large Dagger graphs).

The cache level can be changed on the fly via DevFun.

### Enum Values

| Name | Summary |
|---|---|
| [AGGRESSIVE](-a-g-g-r-e-s-s-i-v-e.md) |  |
| [SINGLE_LOOP](-s-i-n-g-l-e_-l-o-o-p.md) |  |
| [NONE](-n-o-n-e.md) |  |
