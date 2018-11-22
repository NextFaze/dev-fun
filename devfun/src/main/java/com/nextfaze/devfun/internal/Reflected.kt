package com.nextfaze.devfun.internal

import android.text.SpannableStringBuilder
import com.nextfaze.devfun.core.devFun
import com.nextfaze.devfun.function.FunctionArgs
import com.nextfaze.devfun.inject.InstanceProvider
import com.nextfaze.devfun.inject.isSubclassOf
import com.nextfaze.devfun.internal.log.*
import com.nextfaze.devfun.internal.reflect.*
import com.nextfaze.devfun.internal.string.*
import com.nextfaze.devfun.invoke.doInvoke
import com.nextfaze.devfun.invoke.parameterInstances
import com.nextfaze.devfun.invoke.receiverInstance
import java.lang.reflect.Field
import java.lang.reflect.Method
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1
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

interface ReflectedMethod : Function0<Any?>, Function1<InstanceProvider, Any?> {
    val method: Method
    val clazz: KClass<*>
    val name: String
    val isProperty: Boolean
    val isStatic: Boolean
    val fieldName: String
    val parameterTypes: List<KClass<*>>

    fun receiverInstance(instanceProvider: InstanceProvider): Any?
    val receiver: Any?

    fun parameterInstances(instanceProvider: InstanceProvider, suppliedArgs: FunctionArgs = null): FunctionArgs
    val parameters: FunctionArgs
}

interface ReflectedProperty {
    val field: Field?
    val property: KProperty<*>
    val getter: Method?
    val setter: Method?

    val desc: String
    val descWithDeclaringClass: String
    fun getDesc(withDeclaringClass: Boolean = false): String

    val isLateinit: Boolean
    val type: KClass<*>

    fun getValue(instanceProvider: InstanceProvider): Any?
    fun setValue(instanceProvider: InstanceProvider, value: Any?): Any?
    var value: Any?

    fun isUninitialized(receiver: Any?): Boolean
    val isUninitialized: Boolean
}

fun Method.toReflected(instanceProviders: InstanceProvider = devFun.instanceProviders): ReflectedMethod =
    ReflectedMethodImpl(this, instanceProviders).let {
        when {
            it.isProperty -> ReflectedPropertyImpl(it, instanceProviders)
            else -> it
        }
    }

private class ReflectedPropertyImpl(val reflectedMethod: ReflectedMethod, val instanceProviders: InstanceProvider) : ReflectedProperty,
    ReflectedMethod by reflectedMethod {

    override fun invoke() = value
    override fun invoke(instanceProvider: InstanceProvider) = getValue(instanceProvider)

    override val field by lazy {
        try {
            clazz.java.getDeclaredField(fieldName).apply { isAccessible = true }
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

    override val getter by lazy {
        try {
            property.getter.javaMethod
        } catch (t: UnsupportedOperationException) { // "Packages and file facades are not yet supported in Kotlin reflection"
            // file-level property
            val getterName = "get${method.name.substringBeforeLast('$').capitalize()}"
            method.declaringClass.getDeclaredMethod(getterName, *method.parameterTypes)
        }?.apply { isAccessible = true }
    }
    override val setter by lazy {
        try {
            val property = property
            if (property is KMutableProperty<*>) property.setter.javaMethod else null
        } catch (t: UnsupportedOperationException) { // "Packages and file facades are not yet supported in Kotlin reflection"
            // file-level property
            val setterName = "set${method.name.substringBeforeLast('$').capitalize()}"
            method.declaringClass.getDeclaredMethod(setterName, *method.parameterTypes)
        }?.apply { isAccessible = true }
    }

    override val desc by lazy { getDesc(false) }
    override val descWithDeclaringClass by lazy { getDesc(true) }
    override fun getDesc(withDeclaringClass: Boolean): String {
        val lateInit = if (property.isLateinit) "lateinit " else ""
        val varType = if (property is KMutableProperty<*>) "var" else "val"
        val declaringClass = if (withDeclaringClass) clazz.simpleName?.let { "$it." } ?: "" else ""
        return "$lateInit$varType $declaringClass$fieldName: ${property.simpleName}"
    }

    override val isLateinit by lazy { property.isLateinit }
    override val type by lazy { property.type }

    override fun getValue(instanceProvider: InstanceProvider) =
        when {
            getter != null -> getter!!.doInvoke(instanceProvider)
            else -> this.field?.get(receiverInstance(instanceProvider))
        }

    override fun setValue(instanceProvider: InstanceProvider, value: Any?) =
        when {
            setter != null -> setter!!.doInvoke(instanceProvider, listOf(value))
            else -> this.field?.set(receiverInstance(instanceProvider), value)
        }

    override var value: Any?
        get() = getValue(instanceProviders)
        set(value) {
            setValue(instanceProviders, value)
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

internal fun ReflectedProperty.toStringRepresentation(withDeclaringClass: Boolean = false): CharSequence {
    val isUninitialized by lazy { isUninitialized }
    val value = value
    return SpannableStringBuilder().apply {
        this += getDesc(withDeclaringClass)
        this += " = "
        when {
            isLateinit && value == null -> this += i("undefined")
            isUninitialized -> this += i("uninitialized")
            value != null && type.isSubclassOf<CharSequence>() -> this += """"$value""""
            else -> this += "$value"
        }
        if (isUninitialized) {
            this += "\n"
            this += color(scale(i("\t(tap will initialize)"), 0.85f), 0xFFAAAAAA.toInt())
        }
    }
}

private data class ReflectedMethodImpl(override val method: Method, val instanceProviders: InstanceProvider) : ReflectedMethod {
    init {
        method.isAccessible = true
    }

    override val clazz = method.declaringClass.kotlin
    override val name: String = method.name
    override val isProperty = method.isProperty
    override val isStatic = method.isStatic
    override val fieldName by lazy { method.name.substringBefore('$') }
    override val parameterTypes by lazy { method.parameterTypes.map { it.kotlin } }

    override fun receiverInstance(instanceProvider: InstanceProvider) = method.receiverInstance(instanceProvider)
    override val receiver get() = receiverInstance(instanceProviders)

    override fun parameterInstances(instanceProvider: InstanceProvider, suppliedArgs: FunctionArgs) =
        method.parameterInstances(instanceProvider, suppliedArgs)

    override val parameters get() = parameterInstances(instanceProviders)

    override fun invoke(instanceProvider: InstanceProvider) = method.doInvoke(instanceProvider)
    override fun invoke() = invoke(instanceProviders)
}
