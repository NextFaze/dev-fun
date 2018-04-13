[gh-pages](../../index.md) / [com.nextfaze.devfun.inject](../index.md) / [Constructable](./index.md)

# Constructable

`@Target([AnnotationTarget.CLASS]) annotation class Constructable` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/inject/InstanceProvider.kt#L150)

Tag to allow classes to be instantiated when no other [InstanceProvider](../-instance-provider/index.md) was able to provide the class.

The class must have only one constructor. Any arguments to the constructor will be injected as normal.

In general this should not be used (you should be using your own dependency injection framework).
However for quick-n-dirty uses this can make life a bit easier (e.g. function transformers which are debug only anyway).

Note: `inner` classes will work as long as the outer class can be resolved/injected..

**See Also**

[FunctionTransformer](../../com.nextfaze.devfun.core/-function-transformer/index.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Constructable()`<br>Tag to allow classes to be instantiated when no other [InstanceProvider](../-instance-provider/index.md) was able to provide the class. |
