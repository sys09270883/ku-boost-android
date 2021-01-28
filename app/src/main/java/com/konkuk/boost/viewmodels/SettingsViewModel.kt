package com.konkuk.boost.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.boost.repositories.AuthRepository
import com.konkuk.boost.utils.UseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _name = MutableLiveData(authRepository.getName())
    val name get() = _name

    private val _dept = MutableLiveData(authRepository.getDept())
    val dept get() = _dept

    private val _stdNo = MutableLiveData(authRepository.getStdNo())
    val stdNo get() = _stdNo

    var logoutResponse = MutableLiveData<UseCase<Unit>>()

    fun clearLogoutResponse() {
        logoutResponse = MutableLiveData<UseCase<Unit>>()
    }

    fun logout() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                logoutResponse.postValue(authRepository.makeLogoutRequest())
            }
        }
    }
}