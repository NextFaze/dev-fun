/**
 * Various extension functions to assist with function invocation.
 *
 * __These are somewhat internal/experimental.__
 */
package com.nextfaze.devfun.invoke

import com.nextfaze.devfun.core.*
import com.nextfaze.devfun.inject.InstanceProvider
import java.lang.reflect.Modifier
import kotlin.reflect.KClass

/**
 * Get the receiver class for this function definition.
 *
 * @see FunctionItem.receiverClass
 */
val FunctionDefinition.receiverClass get() = method.declaringClass.kotlin

/**
 * Get the receiver class for this function definition if you intend to invoke it. That is, it will return `null` if the type isn't needed.
 *
 * @see FunctionItem.receiverClassForInvocation
 */
val FunctionDefinition.receiverClassForInvocation: KClass<*>? get() = if (Modifier.isStatic(method.modifiers)) null else receiverClass

/**
 * Get the receiver instance for this function definition to be used for invocation.
 *
 * @see FunctionItem.receiverInstance
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
 */
val FunctionItem.receiverClass get() = function.receiverClass

/**
 * Get the receiver class for this function item if you intend to invoke it. That is, it will return `null` if the type isn't needed.
 *
 * @see FunctionDefinition.receiverClassForInvocation
 */
val FunctionItem.receiverClassForInvocation: KClass<*>? get() = if (Modifier.isStatic(function.method.modifiers)) null else receiverClass

/**
 * Get the receiver instance for this function item to be used for invocation.
 *
 * @see FunctionDefinition.receiverInstance
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

private inline fun <reified T : Any?> List<Any?>?.getNonNullOrElse(i: Int, defaultValue: (Int) -> T) =
    this?.getOrElse(i, defaultValue).takeUnless { it === Unit } as? T ?: defaultValue(i)
