[gh-pages](../../index.md) / [com.nextfaze.devfun.inject](../index.md) / [Constructable](index.md) / [&lt;init&gt;](.)

# &lt;init&gt;

`Constructable()`

Tag to allow classes to be instantiated when no other [InstanceProvider](../-instance-provider/index.md) was able to provide the class.

The class must have only one constructor. Any arguments to the constructor will be injected as normal.

In general this should not be used (you should be using your own dependency injection framework).
However for quick-n-dirty uses this can make life a bit easier (e.g. function transformers which are debug only anyway).

**See Also**

[FunctionTransformer](../../com.nextfaze.devfun.core/-function-transformer/index.md)

