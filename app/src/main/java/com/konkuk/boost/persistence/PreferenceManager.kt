package com.konkuk.boost.persistence

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class PreferenceManager(private val context: Context) {

    companion object {
        private const val PREF_NAME = "app_pref"
        private const val MASTER_KEY_ALIAS = "master_key"
        private const val KEY_SIZE = 256
        private const val USERNAME = "username"
        private const val PASSWORD = "password"
        private const val COOKIE = "cookie"
        private const val NAME = "name"
        private const val STD_NO = "std_no"
        private const val STATE = "state"
        private const val DEPT = "dept"
        private const val CODE = "code"
        private const val HAS_DATA = "has_data"
        private const val ACCESS_TOKEN = "access_token"
        private const val SELECTED_SEMESTER = "selected_semester"
        private const val DEFAULT_STRING = ""
        private const val DEFAULT_INT = 2021
        private const val DEFAULT_BOOLEAN = false
    }

    private val pref: SharedPreferences by lazy { getEncryptedSharedPreference() }
    var username: String by pref.stringPreference(USERNAME)
    var password: String by pref.stringPreference(PASSWORD)
    var cookie: String by pref.stringPreference(COOKIE)
    var name: String by pref.stringPreference(NAME)
    var stdNo: Int by pref.intPreference(STD_NO)
    var state: String by pref.stringPreference(STATE)
    var dept: String by pref.stringPreference(DEPT)
    var code: String by pref.stringPreference(CODE)
    var hasData: Boolean by pref.booleanPreference(HAS_DATA)
    var accessToken: String by pref.stringPreference(ACCESS_TOKEN)
    var selectedSemester: Int by pref.intPreference(SELECTED_SEMESTER)

    private fun getEncryptedSharedPreference(): SharedPreferences {
        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            MASTER_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(KEY_SIZE)
            .build()

        val masterKeyAlias = MasterKey.Builder(context, MASTER_KEY_ALIAS)
            .setKeyGenParameterSpec(keyGenParameterSpec)
            .build()

        return EncryptedSharedPreferences.create(
            context,
            PREF_NAME,
            masterKeyAlias,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun setAuthInfo(username: String, password: String) {
        this.username = username
        this.password = password
    }

    fun setUserInfo(name: String, stdNo: Int, state: String, dept: String, code: String) {
        this.name = name
        this.stdNo = stdNo
        this.state = state
        this.dept = dept
        this.code = code
    }

    fun clearAll() {
        setAuthInfo(DEFAULT_STRING, DEFAULT_STRING)
        setUserInfo(DEFAULT_STRING, DEFAULT_INT, DEFAULT_STRING, DEFAULT_STRING, DEFAULT_STRING)
        cookie = DEFAULT_STRING
        hasData = DEFAULT_BOOLEAN
    }

    private fun SharedPreferences.stringPreference(
        key: String,
        defaultValue: String = DEFAULT_STRING
    ): ReadWriteProperty<Any, String> = object : ReadWriteProperty<Any, String> {
        override fun getValue(thisRef: Any, property: KProperty<*>): String =
            getString(key, defaultValue) ?: defaultValue

        override fun setValue(thisRef: Any, property: KProperty<*>, value: String) =
            edit().putString(key, value).apply()
    }

    private fun SharedPreferences.intPreference(
        key: String,
        defaultValue: Int = DEFAULT_INT
    ): ReadWriteProperty<Any, Int> = object : ReadWriteProperty<Any, Int> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Int =
            getInt(key, defaultValue)

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) =
            edit().putInt(key, value).apply()
    }

    private fun SharedPreferences.booleanPreference(
        key: String,
        defaultValue: Boolean = DEFAULT_BOOLEAN
    ): ReadWriteProperty<Any, Boolean> = object : ReadWriteProperty<Any, Boolean> {
        override fun getValue(thisRef: Any, property: KProperty<*>): Boolean =
            getBoolean(key, defaultValue)

        override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) =
            edit().putBoolean(key, value).apply()
    }

}