[gh-pages](../../index.md) / [com.nextfaze.devfun.annotations](../index.md) / [Dagger2Component](./index.md)

# Dagger2Component

`@Target([AnnotationTarget.PROPERTY, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.FUNCTION]) annotation class Dagger2Component` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/annotations/Dagger2.kt#L40)

Annotated functions (`fun`, properties, or property getters (`@get:Dagger2Component`)) will be checked/used as Dagger 2 components.

If all [scope](scope.md) and [priority](priority.md) are unset/default then a best-guess will be made based on where the reference is.

i.e.

* If its in an `Application` class then it'll be assumed to be application level etc.
* If it's an extension function then the receiver will be used.

**See Also**

[Dagger2Scope](../-dagger2-scope/index.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Dagger2Component(scope: `[`Dagger2Scope`](../-dagger2-scope/index.md)` = Dagger2Scope.UNDEFINED, priority: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)` = 0)`<br>Annotated functions (`fun`, properties, or property getters (`@get:Dagger2Component`)) will be checked/used as Dagger 2 components. |

### Properties

| Name | Summary |
|---|---|
| [priority](priority.md) | `val priority: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Here if for whatever reason you don't want to use [scope](scope.md). |
| [scope](scope.md) | `val scope: `[`Dagger2Scope`](../-dagger2-scope/index.md)<br>The scope of this component. |
