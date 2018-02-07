package com.nextfaze.devfun.demo.util

import android.content.SharedPreferences
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import kotlin.reflect.KProperty

typealias KxPreference<T> = KxObservablePreference<T, T>

interface KxNullablePreference<T : Any> : KxObservablePreference<T?, Optional<T>>

interface KxObservablePreference<TValue : Any?, TObservable : Any> {
    var value: TValue

    val observable: Observable<TObservable>

    val isSet: Boolean

    fun delete()

    operator fun getValue(thisRef: Any?, property: KProperty<*>): TValue
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: TValue)
}


class KxSharedPreferences(val preferences: SharedPreferences) {
    private val keyChanges = Observable.create<String> { emitter: ObservableEmitter<String> ->
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key -> emitter.onNext(key) }
        preferences.registerOnSharedPreferenceChangeListener(listener)
        emitter.setCancellable { preferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    operator fun get(key: String, default: String): KxPreference<String> = KxStringPref(preferences, key, default, keyChanges)
    operator fun get(key: String, default: Int): KxPreference<Int> = KxIntPref(preferences, key, default, keyChanges)
    operator fun get(key: String, default: Boolean): KxPreference<Boolean> = KxBooleanPref(preferences, key, default, keyChanges)

    operator fun get(key: String, default: Optional<String>): KxNullablePreference<String> =
        KxNullableStringPref(preferences, key, default, keyChanges)
}


private abstract class KxPreferenceBase<TValue : Any?, TObservable : Any>(
    protected val preferences: SharedPreferences,
    protected val key: String,
    protected val default: TValue
) : KxObservablePreference<TValue, TObservable> {
    override val isSet: Boolean get() = preferences.contains(key)
    override fun delete() = preferences.edit().remove(key).apply()
    override operator fun getValue(thisRef: Any?, property: KProperty<*>): TValue = value
    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: TValue) {
        this.value = value
    }

    override fun toString() = "$key=$value ($default)"
}


private abstract class KxPreferenceImpl<TValue : Any, TObservable : TValue>(
    preferences: SharedPreferences,
    key: String,
    default: TValue,
    keyChanges: Observable<String>
) : KxPreferenceBase<TValue, TObservable>(preferences, key, default) {

    abstract override var value: TValue

    @Suppress("UNCHECKED_CAST")
    override val observable: Observable<TObservable> by lazy {
        keyChanges.filter { changedKey -> key == changedKey }
            .startWith("<init>") // Dummy value to trigger initial load.
            .map { value as TObservable }
    }
}

private abstract class KxNullablePreferenceImpl<TValue : Any>(
    preferences: SharedPreferences,
    key: String,
    default: TValue?,
    keyChanges: Observable<String>
) : KxPreferenceBase<TValue?, Optional<TValue>>(preferences, key, default) {

    abstract override var value: TValue?

    override val observable: Observable<Optional<TValue>> by lazy {
        keyChanges.filter { changedKey -> key == changedKey }
            .startWith("<init>") // Dummy value to trigger initial load.
            .map { value.toOptional() }
    }
}


private class KxStringPref(preferences: SharedPreferences, key: String, default: String, keyChanges: Observable<String>) :
    KxPreferenceImpl<String, String>(preferences, key, default, keyChanges) {
    override var value: String
        get() = preferences.getString(key, default)
        set(value) = preferences.edit().putString(key, value).apply()
}

private class KxNullableStringPref(preferences: SharedPreferences, key: String, default: Optional<String>, keyChanges: Observable<String>) :
    KxNullablePreferenceImpl<String>(preferences, key, default.value, keyChanges), KxNullablePreference<String> {
    override var value: String?
        get() = preferences.getString(key, default)
        set(value) = preferences.edit().putString(key, value).apply()
}


private class KxIntPref(preferences: SharedPreferences, key: String, default: Int, keyChanges: Observable<String>) :
    KxPreferenceImpl<Int, Int>(preferences, key, default, keyChanges) {
    override var value: Int
        get() = preferences.getInt(key, default)
        set(value) = preferences.edit().putInt(key, value).apply()
}


private class KxBooleanPref(preferences: SharedPreferences, key: String, default: Boolean, keyChanges: Observable<String>) :
    KxPreferenceImpl<Boolean, Boolean>(preferences, key, default, keyChanges) {
    override var value: Boolean
        get() = preferences.getBoolean(key, default)
        set(value) = preferences.edit().putBoolean(key, value).apply()
}
