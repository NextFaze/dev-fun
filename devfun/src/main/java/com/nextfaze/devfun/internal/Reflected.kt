package com.nextfaze.devfun.internal

import com.nextfaze.devfun.core.devFun
import com.nextfaze.devfun.inject.InstanceProvider
import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.internal.reflect.*
import com.nextfaze.devfun.invoke.parameterInstances
import com.nextfaze.devfun.invoke.receiverInstance
import java.lang.reflect.Field
import java.lang.reflect.Method
import kotlin.reflect.*
import kotlin.reflect.full.IllegalPropertyDelegateAccessException
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaMethod
import kotlin.reflect.jvm.kotlinProperty

private val log = logger("Reflection")

private val KProperty<*>.simpleName get() = "${type.simpleName}${if (returnType.isMarkedNullable) "?" else ""}"
private val KProperty<*>.type get() = returnType.classifier as KClass<*>

@Suppress("UNCHECKED_CAST")
private fun KProperty<*>.isUninitialized(receiver: Any?): Boolean {
    fun tryGet() = try {
        when (this) {
            is KProperty0<*> -> (getDelegate() as? Lazy<*>)?.isInitialized() == false
            is KProperty1<*, *> -> ((this as KProperty1<Any?, Any>).getDelegate(receiver) as? Lazy<*>)?.isInitialized() == false
            else -> false
        }
    } catch (t: IllegalPropertyDelegateAccessException) {
        log.d(t) { "Kotlin Bug: Ignoring IllegalPropertyDelegateAccessException for $this" }
        null
    }

    isAccessible = true
    return tryGet() ?: tryGet() ?: false // try it again if it first fails
}

interface ReflectedMethod {
    val method: Method
    val declaringClass: Class<*>
    val clazz: KClass<*>
    val name: String
    val isProperty: Boolean
    val fieldName: String

    fun receiverInstance(instanceProvider: InstanceProvider): Any?
    val receiver: Any?

    fun invoke(instanceProvider: InstanceProvider): Any?
    fun invoke(): Any?
}

interface ReflectedProperty {
    val field: Field?
    val property: KProperty<*>
    val getter: Method?
    val setter: Method?
    val desc: String

    val isLateinit: Boolean
    val type: KClass<*>

    fun getValue(instanceProvider: InstanceProvider): Any?
    fun setValue(instanceProvider: InstanceProvider, value: Any?): Any?
    var value: Any?

    fun isUninitialized(receiver: Any?): Boolean
    val isUninitialized: Boolean
}

fun Method.toReflected(): ReflectedMethod =
    ReflectedMethodImpl(this).let {
        when {
            it.isProperty -> ReflectedPropertyImpl(it)
            else -> it
        }
    }

private class ReflectedPropertyImpl(val reflectedMethod: ReflectedMethod) : ReflectedProperty,
    ReflectedMethod by reflectedMethod {
    override val field by lazy {
        try {
            declaringClass.getDeclaredField(fieldName).apply { isAccessible = true }
        } catch (ignore: NoSuchFieldException) {
            null // is property without backing field (i.e. has custom getter/setter)
        }
    }

    override val property by lazy {
        when {
            field != null -> field!!.kotlinProperty!!
            else -> clazz.declaredMemberProperties.first { it.name == fieldName }
        }.apply { isAccessible = true }
    }

    override val getter by lazy { property.getter.javaMethod?.apply { isAccessible = true } }
    override val setter by lazy {
        val property = property
        if (property is KMutableProperty<*>) property.setter.javaMethod?.apply { isAccessible = true } else null
    }

    override val desc by lazy {
        val lateInit = if (property.isLateinit) "lateinit " else ""
        val varType = if (property is KMutableProperty<*>) "var" else "val"
        "$lateInit$varType $fieldName: ${property.simpleName}"
    }

    override val isLateinit by lazy { property.isLateinit }
    override val type by lazy { property.type }

    override fun getValue(instanceProvider: InstanceProvider) =
        when {
            getter != null -> getter!!.invoke(receiverInstance(instanceProvider))
            else -> this.field?.get(receiverInstance(instanceProvider))
        }

    override fun setValue(instanceProvider: InstanceProvider, value: Any?) =
        when {
            setter != null -> setter!!.invoke(receiverInstance(instanceProvider), value)
            else -> this.field?.set(receiverInstance(instanceProvider), value)
        }

    override var value: Any?
        get() = getValue(devFun.instanceProviders)
        set(value) {
            setValue(devFun.instanceProviders, value)
        }

    override fun isUninitialized(receiver: Any?) = property.isUninitialized(receiver)
    override val isUninitialized: Boolean get() = isUninitialized(receiver)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ReflectedPropertyImpl

        if (reflectedMethod != other.reflectedMethod) return false
        if (isProperty != other.isProperty) return false

        return true
    }

    override fun hashCode(): Int {
        var result = reflectedMethod.hashCode()
        result = 31 * result + isProperty.hashCode()
        return result
    }

    override fun toString() = "ReflectedProperty(reflected=$reflectedMethod)"
}

private data class ReflectedMethodImpl(override val method: Method) : ReflectedMethod {
    override val declaringClass: Class<*> = method.declaringClass
    override val clazz = declaringClass.kotlin
    override val name: String = method.name
    override val isProperty = method.isProperty

    override val fieldName by lazy { method.name.substringBefore('$') }

    override fun receiverInstance(instanceProvider: InstanceProvider) = method.receiverInstance(instanceProvider)
    override val receiver get() = receiverInstance(devFun.instanceProviders)

    override fun invoke(instanceProvider: InstanceProvider): Any? {
        val args = method.parameterInstances(instanceProvider)
        return when (args) {
            null -> method.invoke(method.receiverInstance(instanceProvider))
            else -> method.invoke(method.receiverInstance(instanceProvider), args)
        }
    }

    override fun invoke() = invoke(devFun.instanceProviders)
}
