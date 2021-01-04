package com.konkuk.boost.persistence

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class PreferenceManager(private val context: Context) {

    companion object {
        private const val PREF_NAME = "app_pref"
        private const val MASTER_KEY_ALIAS = "master_key"
        private const val KEY_SIZE = 256
        private const val USERNAME = "username"
        private const val PASSWORD = "password"
        private const val COOKIE = "cookie"
        private const val NAME = "name"
        private const val STD_NO = "stdNo"
        private const val STATE = "state"
        private const val DEPT = "dept"
        private const val CODE = "code"
        private const val HAS_DATA = "has_data"
        private const val DEFAULT = ""
        private const val DEFAULT_INT = 2021
        private const val DEFAULT_BOOLEAN = false
    }

    private fun pref(): SharedPreferences {
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

    private fun editor() = pref().edit()

    fun setAuthInfo(username: String, password: String) =
        editor().apply {
            putString(USERNAME, username)
            putString(PASSWORD, password)
        }.apply()

    fun setCookie(cookie: String) = editor().putString(COOKIE, cookie).apply()

    fun setUserInfo(name: String, stdNo: Int, state: String, dept: String, code: String) =
        editor().apply {
            putString(NAME, name)
            putInt(STD_NO, stdNo)
            putString(STATE, state)
            putString(DEPT, dept)
            putString(CODE, code)
        }.apply()

    fun setHasData(hasData: Boolean) = editor().putBoolean(HAS_DATA, hasData).apply()

    fun getUsername() = pref().getString(USERNAME, DEFAULT)!!

    fun getPassword() = pref().getString(PASSWORD, DEFAULT)!!

    fun getCookie() = pref().getString(COOKIE, DEFAULT)!!

    fun getName() = pref().getString(NAME, DEFAULT)!!

    fun getStdNo() = pref().getInt(STD_NO, DEFAULT_INT)

    fun getState() = pref().getString(STATE, DEFAULT)!!

    fun getDept() = pref().getString(DEPT, DEFAULT)!!

    fun getCode() = pref().getString(CODE, DEFAULT)!!

    fun getHasData() = pref().getBoolean(HAS_DATA, false)

    fun clearAll() {
        setAuthInfo(DEFAULT, DEFAULT)
        setUserInfo(DEFAULT, DEFAULT_INT, DEFAULT, DEFAULT, DEFAULT)
        setCookie(DEFAULT)
        setHasData(DEFAULT_BOOLEAN)
    }

}