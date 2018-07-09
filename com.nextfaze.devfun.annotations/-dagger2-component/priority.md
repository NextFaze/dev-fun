[gh-pages](../../index.md) / [com.nextfaze.devfun.annotations](../index.md) / [Dagger2Component](index.md) / [priority](./priority.md)

# priority

`val priority: `[`Int`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/annotations/Dagger2.kt#L62)

Here if for whatever reason you can't/don't want to use [scope](scope.md) - will only be used if [scope](scope.md) is [UNDEFINED](../-dagger2-scope/-u-n-d-e-f-i-n-e-d.md).

If [scope](scope.md) is [UNDEFINED](../-dagger2-scope/-u-n-d-e-f-i-n-e-d.md) and [priority](./priority.md) is `0` then a best-guess will be made based on where the component is.
i.e. If its in an Application class then it'll be assumed to be application level etc.
If it's an extension function then the receiver type will be used as to "where".

If [scope](scope.md) is set it takes priority (which is just the enum ordinal value).

**See Also**

[Dagger2Scope](../-dagger2-scope/index.md)

