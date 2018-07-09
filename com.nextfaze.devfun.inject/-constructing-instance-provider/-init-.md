[gh-pages](../../index.md) / [com.nextfaze.devfun.inject](../index.md) / [ConstructingInstanceProvider](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`ConstructingInstanceProvider(rootInstanceProvider: `[`InstanceProvider`](../-instance-provider/index.md)`? = null, requireConstructable: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = true)`

Provides objects via instance construction. Type must be annotated with [Constructable](../-constructable/index.md).

Only supports objects with a single constructor. Constructor arguments will fetched using param `rootInstanceProvider`.

If [Constructable.singleton](../-constructable/singleton.md) is `true` or type is annotated @[Singleton](#) then only one instance will be created and shared.

### Parameters

`rootInstanceProvider` - An instance provider used to fetch constructor args. If `null`,  then self (`this`) is used

`requireConstructable` - Flag indicating if a type must be [Constructable](../-constructable/index.md) to be instantiable

**Internal**
Visible for testing - use at your own risk.

