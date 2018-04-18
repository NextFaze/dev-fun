/**
 * Various extension functions to assist with function invocation.
 *
 * __These are somewhat internal/experimental.__
 */
package com.nextfaze.devfun.invoke

import com.nextfaze.devfun.core.*
import com.nextfaze.devfun.inject.InstanceProvider
import com.nextfaze.devfun.internal.*
import java.lang.reflect.Method
import kotlin.reflect.KClass

/**
 * Get the receiver class for this function definition.
 *
 * @see FunctionItem.receiverClass
 * @see Method.receiverClass
 * @see FunctionDefinition.receiverClassForInvocation
 */
val FunctionDefinition.receiverClass: KClass<*> get() = method.declaringClass.kotlin

/**
 * Get the receiver class for this function definition if you intend to invoke it. That is, it will return `null` if the type isn't needed.
 *
 * @see FunctionDefinition.receiverClass
 * @see FunctionItem.receiverClassForInvocation
 */
val FunctionDefinition.receiverClassForInvocation: KClass<*>? get() = if (method.isStatic) null else receiverClass

/**
 * Get the receiver instance for this function definition to be used for invocation.
 *
 * @see FunctionItem.receiverInstance
 * @see Method.receiverInstance
 */
fun FunctionDefinition.receiverInstance(instanceProvider: InstanceProvider = devFun.instanceProviders) =
    receiverClassForInvocation?.let { instanceProvider[it] }

/**
 * Get the parameter instances for this function definition for invocation.
 *
 * @see FunctionItem.parameterInstances
 * @see DevFun.instanceProviders
 */
fun FunctionDefinition.parameterInstances(instanceProvider: InstanceProvider = devFun.instanceProviders, args: FunctionArgs) =
    method.parameterTypes.mapIndexed { index: Int, clazz: Class<*> ->
        args.getNonNullOrElse(index) { instanceProvider[clazz.kotlin] }
    }

/**
 * Get the receiver class for this function item.
 *
 * @see FunctionDefinition.receiverClass
 * @see Method.receiverClass
 * @see FunctionItem.receiverClassForInvocation
 */
val FunctionItem.receiverClass get() = function.receiverClass

/**
 * Get the receiver class for this function item if you intend to invoke it. That is, it will return `null` if the type isn't needed.
 *
 * @see FunctionItem.receiverClass
 * @see FunctionDefinition.receiverClassForInvocation
 */
val FunctionItem.receiverClassForInvocation: KClass<*>? get() = if (function.method.isStatic) null else receiverClass

/**
 * Get the receiver instance for this function item to be used for invocation.
 *
 * @see FunctionDefinition.receiverInstance
 * @see Method.receiverInstance
 */
fun FunctionItem.receiverInstance(instanceProvider: InstanceProvider = devFun.instanceProviders) =
    receiverClassForInvocation?.let { instanceProvider[it] }

/**
 * Get the parameter instances for this function item for invocation.
 *
 * @see FunctionDefinition.parameterInstances
 * @see DevFun.instanceProviders
 */
fun FunctionItem.parameterInstances(instanceProvider: InstanceProvider = devFun.instanceProviders) =
    function.method.parameterTypes.mapIndexed { index: Int, clazz: Class<*> ->
        args.getNonNullOrElse(index) { instanceProvider[clazz.kotlin] }
    }

/**
 * Get the receiver class for this method.
 *
 * @see FunctionDefinition.receiverClass
 * @see FunctionItem.receiverClass
 * @see Method.receiverClassForInvocation
 */
val Method.receiverClass: KClass<*> get() = declaringClass.kotlin


/**
 * Get the receiver class for this function definition if you intend to invoke it. That is, it will return `null` if the type isn't needed.
 *
 * @see Method.receiverClass
 * @see FunctionDefinition.receiverClassForInvocation
 * @see FunctionItem.receiverClassForInvocation
 */
val Method.receiverClassForInvocation: KClass<*>? get() = if (isStatic) null else receiverClass

/**
 * Get the receiver instance for this method to be used for invocation.
 *
 * @see FunctionDefinition.receiverInstance
 * @see FunctionItem.receiverInstance
 */
fun Method.receiverInstance(instanceProvider: InstanceProvider = devFun.instanceProviders) =
    receiverClassForInvocation?.let { instanceProvider[it] }

/**
 * Get the parameter instances for this method for invocation.
 *
 * Be aware; this is intended for working with the method directly, a `null` value means no arguments.
 * The [FunctionItem.invoke] and [FunctionDefinition.invoke] handle 0 or more arguments automatically.
 * i.e. An empty list is no arguments, however this is not the same when invoking a `Method` object directly (it will see an empty list/array as an argument)
 *
 * @see FunctionDefinition.parameterInstances
 * @see FunctionItem.parameterInstances
 */
fun Method.parameterInstances(instanceProvider: InstanceProvider = devFun.instanceProviders, args: FunctionArgs = null) =
    parameterTypes.takeIf { it.isNotEmpty() }?.mapIndexed { index: Int, clazz: Class<*> ->
        args.getNonNullOrElse(index) { instanceProvider[clazz.kotlin] }
    }

private inline fun <reified T : Any?> List<Any?>?.getNonNullOrElse(i: Int, defaultValue: (Int) -> T) =
    this?.getOrElse(i, defaultValue).takeUnless { it === Unit } as? T ?: defaultValue(i)