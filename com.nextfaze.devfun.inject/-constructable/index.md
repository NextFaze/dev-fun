[gh-pages](../../index.md) / [com.nextfaze.devfun.inject](../index.md) / [Constructable](./index.md)

# Constructable

`@Target([AnnotationTarget.CLASS]) annotation class Constructable` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/inject/InstanceProvider.kt#L187)

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

[FunctionTransformer](../../com.nextfaze.devfun.function/-function-transformer/index.md)

### Constructors

| Name | Summary |
|---|---|
| [&lt;init&gt;](-init-.md) | `Constructable(singleton: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)` = false)`<br>Tag to allow classes to be instantiated when no other [InstanceProvider](../-instance-provider/index.md) was able to provide the class. |

### Properties

| Name | Summary |
|---|---|
| [singleton](singleton.md) | `val singleton: `[`Boolean`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>If `true` then a single shared instance will be constructed. Be careful when using this on inner classes as it will hold a reference to its outer class. i.e. Only use this if the outer class is an object/singleton. |
