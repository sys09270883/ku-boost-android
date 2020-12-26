package com.corgaxm.ku_alarmy.utils

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class SettingsManager(context: Context) {

    private val dataStore = context.createDataStore(name = "settings_pref")

    val usernameFlow = dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map {
            val username = it[USERNAME] ?: ""
            username
        }

    val passwordFlow = dataStore.data
        .catch {
            if (it is IOException) {
                it.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map {
            val password = it[PASSWORD] ?: ""
            password
        }

    companion object {
        val USERNAME = preferencesKey<String>("username")
        val PASSWORD = preferencesKey<String>("password")
    }

    suspend fun setUserInfo(username: String, password: String) {
        dataStore.edit {
            it[USERNAME] = username
            it[PASSWORD] = password
        }
    }

}