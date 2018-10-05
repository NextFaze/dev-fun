[gh-pages](../../index.md) / [com.nextfaze.devfun.reference](../index.md) / [DeveloperReference](./index.md)

# DeveloperReference

`annotation class DeveloperReference` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/reference/DeveloperReference.kt#L18)

Annotated elements will be recorded by DevFun for later retrieval via `devFun.developerReferences<DeveloperReference>()`.

In general this is not used much beyond testing.

Typically usage of this type is via your own custom annotations with [DeveloperAnnotation](../../com.nextfaze.devfun/-developer-annotation/index.md) à la [DeveloperLogger](../-developer-logger/index.md).

**See Also**

[DeveloperAnnotation](../../com.nextfaze.devfun/-developer-annotation/index.md)

[Dagger2Component](../-dagger2-component/index.md)

[DeveloperLogger](../-developer-logger/index.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `DeveloperReference()`<br>Annotated elements will be recorded by DevFun for later retrieval via `devFun.developerReferences<DeveloperReference>()`. |
