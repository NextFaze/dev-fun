@file:Suppress("PLATFORM_CLASS_MAPPED_TO_KOTLIN")

package com.nextfaze.devfun.internal.pref

import android.content.Context
import android.content.SharedPreferences
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import java.lang.Enum as JavaLangEnum

typealias OnChange<T> = (T, T) -> Unit

interface KPreference<TValue : Any?> {
    var value: TValue

    val isSet: Boolean

    fun delete()

    operator fun getValue(thisRef: Any?, property: KProperty<*>): TValue
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: TValue)
}

interface KNullablePreference<T : Any> : KPreference<T?>


class KSharedPreferences(private val preferences: SharedPreferences) {
    companion object {
        fun named(context: Context, name: String) =
            KSharedPreferences(context.getSharedPreferences(name, Context.MODE_PRIVATE))
    }

    fun clear() {
        preferences.edit().clear().apply()
    }

    operator fun get(key: String, default: String): KPreference<String> = KStringPref(preferences, key, default)
    operator fun get(key: String, default: Int): KPreference<Int> = KIntPref(preferences, key, default)
    operator fun get(key: String, default: Float): KPreference<Float> = KFloatPref(preferences, key, default)
    operator fun get(key: String, default: Boolean, onChange: OnChange<Boolean>? = null): KPreference<Boolean> =
        KBooleanPref(preferences, key, default, onChange)

    operator fun <E : Enum<E>> get(key: String, default: E, onChange: OnChange<E>? = null): KPreference<E> =
        KEnumPref(preferences, key, default, onChange)

    operator fun get(key: String, default: Int? = null): KNullablePreference<Int> =
        KNullableIntPref(preferences, key, default)
}

private abstract class KPreferenceBase<TValue : Any?>(
    protected val preferences: SharedPreferences,
    protected val key: String,
    protected val default: TValue
) : KPreference<TValue> {
    override val isSet: Boolean get() = preferences.contains(key)
    override fun delete() = preferences.edit().remove(key).apply()

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): TValue = value
    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: TValue) {
        this.value = value
    }

    override fun toString() = "$key=$value ($default)"
}


private abstract class KPreferenceImpl<TValue : Any>(
    preferences: SharedPreferences,
    key: String,
    default: TValue
) : KPreferenceBase<TValue>(preferences, key, default) {
    abstract override var value: TValue
}

private abstract class KNullablePreferenceImpl<TValue : Any>(
    preferences: SharedPreferences,
    key: String,
    default: TValue?
) : KPreferenceBase<TValue?>(preferences, key, default) {
    abstract override var value: TValue?
}


private class KStringPref(preferences: SharedPreferences, key: String, default: String) :
    KPreferenceImpl<String>(preferences, key, default) {
    override var value: String
        get() = preferences.getString(key, default)
        set(value) = preferences.edit().putString(key, value).apply()
}


private class KIntPref(preferences: SharedPreferences, key: String, default: Int) :
    KPreferenceImpl<Int>(preferences, key, default) {
    override var value: Int
        get() = preferences.getInt(key, default)
        set(value) = preferences.edit().putInt(key, value).apply()
}

private class KNullableIntPref(preferences: SharedPreferences, key: String, default: Int?) :
    KNullablePreferenceImpl<Int>(preferences, key, default), KNullablePreference<Int> {
    override var value: Int?
        get() = if (preferences.contains(key)) preferences.getInt(key, 0) else null
        set(value) = preferences.edit().apply {
            when (value) {
                null -> remove(key)
                else -> putInt(key, value)
            }
        }.apply()
}


private class KFloatPref(preferences: SharedPreferences, key: String, default: Float) :
    KPreferenceImpl<Float>(preferences, key, default) {
    override var value: Float
        get() = preferences.getFloat(key, default)
        set(value) = preferences.edit().putFloat(key, value).apply()
}

private class KBooleanPref(preferences: SharedPreferences, key: String, default: Boolean, private val onChange: OnChange<Boolean>?) :
    KPreferenceImpl<Boolean>(preferences, key, default) {
    override var value: Boolean
        get() = preferences.getBoolean(key, default)
        set(value) {
            val before = if (onChange != null) this.value else null
            preferences.edit().putBoolean(key, value).apply()
            if (onChange != null && before != null && before != value) {
                onChange.invoke(before, value)
            }
        }
}

private class KEnumPref<E : Enum<E>>(preferences: SharedPreferences, key: String, default: E, private val onChange: OnChange<E>?) :
    KPreferenceImpl<E>(preferences, key, default) {

    @Suppress("UNCHECKED_CAST")
    private val enumClass = default::class as KClass<E>

    override var value: E
        get() = enumClass.enumValueOf(preferences.getString(key, default.name)) ?: default
        set(value) {
            val before = if (onChange != null) this.value else null
            preferences.edit().putString(key, value.name).apply()
            if (onChange != null && before != null && before != value) {
                onChange.invoke(before, value)
            }
        }
}

private fun <E : Enum<E>> KClass<E>.enumValueOf(name: String): E? =
    try {
        JavaLangEnum.valueOf(java, name)
    } catch (ignore: Throwable) {
        null
    }
