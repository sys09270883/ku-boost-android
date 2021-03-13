package com.konkuk.boost.persistence

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal const val PREF_DEFAULT_STRING = ""
internal const val PREF_DEFAULT_INT = 0
internal const val PREF_DEFAULT_BOOLEAN = false

fun SharedPreferences.stringPreference(
    key: String,
    defaultValue: String = PREF_DEFAULT_STRING
): ReadWriteProperty<Any, String> = object : ReadWriteProperty<Any, String> {
    override fun getValue(thisRef: Any, property: KProperty<*>): String =
        getString(key, defaultValue) ?: defaultValue

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String) =
        edit().putString(key, value).apply()
}

fun SharedPreferences.intPreference(
    key: String,
    defaultValue: Int = PREF_DEFAULT_INT
): ReadWriteProperty<Any, Int> = object : ReadWriteProperty<Any, Int> {
    override fun getValue(thisRef: Any, property: KProperty<*>): Int =
        getInt(key, defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) =
        edit().putInt(key, value).apply()
}

fun SharedPreferences.booleanPreference(
    key: String,
    defaultValue: Boolean = PREF_DEFAULT_BOOLEAN
): ReadWriteProperty<Any, Boolean> = object : ReadWriteProperty<Any, Boolean> {
    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean =
        getBoolean(key, defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) =
        edit().putBoolean(key, value).apply()
}