[gh-pages](../../index.md) / [com.nextfaze.devfun.annotations](../index.md) / [Dagger2Component](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`Dagger2Component(scope: `[`Dagger2Scope`](../-dagger2-scope/index.md)` = Dagger2Scope.UNDEFINED, priority: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 0)`

Annotated functions (`fun` or property getters with `@get:Dagger2Component`) will be checked/used as Dagger 2 components.

If all [scope](-init-.md#com.nextfaze.devfun.annotations.Dagger2Component$<init>(com.nextfaze.devfun.annotations.Dagger2Scope, kotlin.Int)/scope) and [priority](-init-.md#com.nextfaze.devfun.annotations.Dagger2Component$<init>(com.nextfaze.devfun.annotations.Dagger2Scope, kotlin.Int)/priority) are unset/default then a best-guess will be made based on where the reference is.

i.e.

* If its in an `Application` class then it'll be assumed to be application level etc.
* If it's an extension function then the receiver will be used.

**See Also**

[Dagger2Scope](../-dagger2-scope/index.md)

