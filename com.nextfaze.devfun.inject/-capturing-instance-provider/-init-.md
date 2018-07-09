[gh-pages](../../index.md) / [com.nextfaze.devfun.inject](../index.md) / [CapturingInstanceProvider](index.md) / [&lt;init&gt;](./-init-.md)

# &lt;init&gt;

`CapturingInstanceProvider(instanceClass: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<`[`T`](index.md#T)`>, instance: () -> `[`T`](index.md#T)`?)`

An instance provider that requests an instance of a class from a captured lambda.

Be aware of leaks! The lambda could implicitly hold a local `this` reference.

Be wary of using a `typealias` as a type - the resultant function "type" itself is used at compile time.
e.g.

``` kotlin
typealias MyStringAlias = () -> String?
val provider1 = captureInstance<MyStringAlias> { ... }

typealias MyOtherAlias = () -> Type?
// will be triggered for MyStringAlias and MyOtherAlias since behind the scenes they are both kotlin.Function0<T>
val provider2 = captureInstance<MyOtherAlias> { ... }
```

**See Also**

[captureInstance](../capture-instance.md)

