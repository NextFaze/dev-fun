[gh-pages](../../index.md) / [com.nextfaze.devfun.invoke](../index.md) / [java.lang.reflect.Method](index.md) / [parameterInstances](./parameter-instances.md)

# parameterInstances

`fun `[`Method`](https://developer.android.com/reference/java/lang/reflect/Method.html)`.parameterInstances(instanceProvider: `[`InstanceProvider`](../../com.nextfaze.devfun.inject/-instance-provider/index.md)` = devFun.instanceProviders, args: `[`FunctionArgs`](../../com.nextfaze.devfun.core/-function-args.md)` = null): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?>?` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/invoke/Extensions.kt#L126)

Get the parameter instances for this method for invocation.

Be aware; this is intended for working with the method directly, a `null` value means no arguments.
The [FunctionItem.invoke](../../com.nextfaze.devfun.core/-function-item/invoke.md) and [FunctionDefinition.invoke](../../com.nextfaze.devfun.core/-function-definition/invoke.md) handle 0 or more arguments automatically.
i.e. An empty list is no arguments, however this is not the same when invoking a `Method` object directly (it will see an empty list/array as an argument)

**See Also**

[FunctionDefinition.parameterInstances](../parameter-instances.md)

[FunctionItem.parameterInstances](../parameter-instances.md)

