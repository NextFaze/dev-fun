[gh-pages](../../index.md) / [com.nextfaze.devfun.inject](../index.md) / [Constructable](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`Constructable(singleton: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false)`

Tag to allow classes to be instantiated when no other [InstanceProvider](../-instance-provider/index.md) was able to provide the class.

The class must have only one constructor. Any arguments to the constructor will be injected as normal.

In general this should not be used (you should be using your own dependency injection framework).
However for quick-n-dirty uses this can make life a bit easier (e.g. function transformers which are debug only anyway).

Note: `inner` classes will work as long as the outer class can be resolved/injected..

Types annotated with @[Singleton](#) will only be created once.

### Parameters

`singleton` - If `true` then a single shared instance will be constructed.
Be careful when using this on inner classes as it will hold a reference to its outer class.
i.e. Only use this if the outer class is an object/singleton.

**See Also**

[FunctionTransformer](../../com.nextfaze.devfun.core/-function-transformer/index.md)

