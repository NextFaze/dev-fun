[gh-pages](../../index.md) / [com.nextfaze.devfun.invoke](../index.md) / [java.lang.reflect.Method](index.md) / [doInvoke](./do-invoke.md)

# doInvoke

`fun `[`Method`](https://developer.android.com/reference/java/lang/reflect/Method.html)`.doInvoke(instanceProvider: `[`InstanceProvider`](../../com.nextfaze.devfun.inject/-instance-provider/index.md)` = devFun.instanceProviders, suppliedArgs: `[`FunctionArgs`](../../com.nextfaze.devfun.core/-function-args.md)` = null): `[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/invoke/Extensions.kt#L163)

Invokes a Method using DevFun to source instances.

Automatically handles static and args etc.

### Parameters

`instanceProvider` - The instance provider to use for parameters instances. *(default=`devFun.instanceProviders`)*

`suppliedArgs` - User-provided arguments (source-defined order). Elements that are `null` or out of bounds will fallback to [instanceProvider](do-invoke.md#com.nextfaze.devfun.invoke$doInvoke(java.lang.reflect.Method, com.nextfaze.devfun.inject.InstanceProvider, kotlin.collections.List((kotlin.Any)))/instanceProvider).

**See Also**

[Method.receiverInstance](receiver-instance.md)

[Method.parameterInstances](parameter-instances.md)

