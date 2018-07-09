[gh-pages](../../index.md) / [com.nextfaze.devfun.annotations](../index.md) / [Dagger2Component](index.md) / [scope](./scope.md)

# scope

`val scope: `[`Dagger2Scope`](../-dagger2-scope/index.md) [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/annotations/Dagger2.kt#L49)

The scope of this component.

If [scope](./scope.md) is [UNDEFINED](../-dagger2-scope/-u-n-d-e-f-i-n-e-d.md) and [priority](priority.md) is `0` then a best-guess will be made based on where the component is.
i.e. If its in an Application class then it'll be assumed to be application level etc.
If it's an extension function then the receiver type will be used as to "where".

