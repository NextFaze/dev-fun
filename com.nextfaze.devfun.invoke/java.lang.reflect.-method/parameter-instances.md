[gh-pages](../../index.md) / [com.nextfaze.devfun.invoke](../index.md) / [java.lang.reflect.Method](index.md) / [parameterInstances](./parameter-instances.md)

# parameterInstances

`fun `[`Method`](https://developer.android.com/reference/java/lang/reflect/Method.html)`.parameterInstances(instanceProvider: `[`InstanceProvider`](../../com.nextfaze.devfun.inject/-instance-provider/index.md)` = devFun.instanceProviders, suppliedArgs: `[`FunctionArgs`](../../com.nextfaze.devfun.function/-function-args.md)` = null): `[`List`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)`<`[`Any`](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)`?>?` [(source)](https://github.com/NextFaze/dev-fun/tree/master/devfun/src/main/java/com/nextfaze/devfun/invoke/Extensions.kt#L148)

Get the parameter instances for this method for invocation.

Be aware; this is intended for working with the method directly, a `null` value means no arguments.

The [FunctionItem.invoke](../../com.nextfaze.devfun.function/-function-item/invoke.md) and [FunctionDefinition.invoke](../../com.nextfaze.devfun.function/-function-definition/invoke.md) handle 0 or more arguments automatically - i.e. they return an empty list to
signify no arguments. However when invoking using [Method](https://developer.android.com/reference/java/lang/reflect/Method.html) directly, passing an empty list/array will be seen as an argument instead.

Thus a return of `null` from here means no arguments, which requires calling `method.invoke(receiver)` rather than `method.invoke(receiver, args)`.

If you just want to invoke the method then use the [doInvoke](do-invoke.md) extension function.

### Parameters

`instanceProvider` - The instance provider to use for parameters instances. *(default=`devFun.instanceProviders`)*

`suppliedArgs` - User-provided arguments (source-defined order). Elements that are `null` or out of bounds will fallback to [instanceProvider](parameter-instances.md#com.nextfaze.devfun.invoke$parameterInstances(java.lang.reflect.Method, com.nextfaze.devfun.inject.InstanceProvider, kotlin.collections.List((kotlin.Any)))/instanceProvider).

**See Also**

[FunctionDefinition.parameterInstances](../parameter-instances.md)

[FunctionItem.parameterInstances](../parameter-instances.md)

