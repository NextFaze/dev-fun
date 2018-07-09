[gh-pages](../index.md) / [com.nextfaze.devfun.core](index.md) / [FunctionInvoke](./-function-invoke.md)

# FunctionInvoke

`typealias FunctionInvoke = (receiver: `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?, args: `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?>) -> `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun-annotations/src/main/java/com/nextfaze/devfun/core/Definitions.kt#L31)

Definition of generated function to call that invokes the function definition.

* The **receiver** is the object to be invoked against (pass in `null` for static/`object`) types.
Use convenience extension functions (e.g. [FunctionItem](-function-item/index.md)`.receiverClassForInvocation`) to more easily locate/resolve receiver instance.

* The **args** is the arguments for the method, matching the methods argument count and ordering.
Similarly to receiver, convenience extension functions exist to assist with argument resolution.

Note: At present nullable types are not inherently supported.
KAPT does not provide enough information to determine if a type is nullable or not (and there are other
issues to be considered). It is intended to be permitted in the future.

**Return**
Invocation of function.

