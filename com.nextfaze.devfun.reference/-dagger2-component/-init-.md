[gh-pages](../../index.md) / [com.nextfaze.devfun.reference](../index.md) / [Dagger2Component](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`Dagger2Component(scope: `[`Dagger2Scope`](../-dagger2-scope/index.md)` = UNDEFINED, priority: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 0, isActivityRequired: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false, isFragmentActivityRequired: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false)`

Annotated functions (`fun`, properties, or property getters (`@get:Dagger2Component`)) will be checked/used as Dagger 2 components.

If all [scope](scope.md) and [priority](priority.md) are unset/default then a best-guess will be made based on where the reference is.

i.e.

* If its in an `Application` class then it'll be assumed to be application level etc.
* If it's an extension function then the receiver will be used.

**See Also**

[Dagger2Scope](../-dagger2-scope/index.md)

