[gh-pages](../../index.md) / [com.nextfaze.devfun.invoke](../index.md) / [java.lang.reflect.Method](index.md) / [receiverClassForInvocation](./receiver-class-for-invocation.md)

# receiverClassForInvocation

`inline val `[`Method`](https://developer.android.com/reference/java/lang/reflect/Method.html)`.receiverClassForInvocation: `[`KClass`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)`<*>?` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/invoke/Extensions.kt#L128)

Get the receiver class for this function definition if you intend to invoke it. That is, it will return `null` if the type isn't needed.

**See Also**

[Method.receiverClass](receiver-class.md)

[FunctionDefinition.receiverClassForInvocation](../receiver-class-for-invocation.md)

[FunctionItem.receiverClassForInvocation](../receiver-class-for-invocation.md)

