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
        private const val NAME = "name"
        private const val STD_NO = "std_no"
        private const val STATE = "state"
        private const val DEPT = "dept"
        private const val CODE = "code"
        private const val HAS_DATA = "has_data"
        private const val KUIS_COOKIE = "kuis_cookie"
        private const val LIBRARY_TOKEN = "library_token"
    }

    private val pref: SharedPreferences by lazy { getEncryptedSharedPreference() }
    var username: String by pref.stringPreference(USERNAME)
    var password: String by pref.stringPreference(PASSWORD)
    var name: String by pref.stringPreference(NAME)
    var stdNo: Int by pref.intPreference(STD_NO)
    var state: String by pref.stringPreference(STATE)
    var dept: String by pref.stringPreference(DEPT)
    var code: String by pref.stringPreference(CODE)
    var hasData: Boolean by pref.booleanPreference(HAS_DATA)
    var kuisCookie: String by pref.stringPreference(KUIS_COOKIE)
    var libraryToken: String by pref.stringPreference(LIBRARY_TOKEN)

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
        setAuthInfo(PREF_DEFAULT_STRING, PREF_DEFAULT_STRING)
        setUserInfo(
            PREF_DEFAULT_STRING,
            PREF_DEFAULT_INT,
            PREF_DEFAULT_STRING,
            PREF_DEFAULT_STRING,
            PREF_DEFAULT_STRING
        )
        hasData = PREF_DEFAULT_BOOLEAN
        kuisCookie = PREF_DEFAULT_STRING
        libraryToken = PREF_DEFAULT_STRING
    }

}