package com.konkuk.boost.persistence

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.map

class SettingsManager(context: Context) {

    private val dataStore = context.createDataStore(name = "settings_pref")

    val usernameFlow = dataStore.data.map { it[USERNAME] ?: "" }

    val passwordFlow = dataStore.data.map { it[PASSWORD] ?: "" }

    val cookieFlow = dataStore.data.map { it[COOKIE] ?: "" }

    val nameFlow = dataStore.data.map { it[NAME] ?: "" }

    val stdNoFlow = dataStore.data.map { it[STUDENT_NUMBER] ?: 2020 }

    val stateFlow = dataStore.data.map { it[STATE] ?: "" }

    val deptFlow = dataStore.data.map { it[DEPT] ?: "" }

    val codeFlow = dataStore.data.map { it[CODE] ?: "" }

    companion object {
        val USERNAME = preferencesKey<String>("username")   // ex) sys0927
        val PASSWORD = preferencesKey<String>("password")
        val COOKIE = preferencesKey<String>("cookie")
        val NAME = preferencesKey<String>("name")           // ex) 신윤섭
        val STUDENT_NUMBER = preferencesKey<Int>("stdNo")
        val STATE = preferencesKey<String>("state")
        val DEPT = preferencesKey<String>("dept")
        val CODE = preferencesKey<String>("code")           // ex) A08001
    }

    suspend fun setAuthInfo(username: String, password: String) {
        dataStore.edit {
            it[USERNAME] = username
            it[PASSWORD] = password
        }
    }

    suspend fun setUserInfo(name: String, stdNo: Int, state: String, dept: String, code: String) {
        dataStore.edit {
            it[NAME] = name
            it[STUDENT_NUMBER] = stdNo
            it[STATE] = state
            it[DEPT] = dept
            it[CODE]= code
        }
    }

    suspend fun setCookie(cookie: String) {
        dataStore.edit {
            it[COOKIE] = cookie
        }
    }

}