package com.konkuk.boost.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.boost.utils.UseCase
import com.konkuk.boost.repositories.AuthRepository
import com.konkuk.boost.data.auth.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    var loginResource = MutableLiveData<UseCase<LoginResponse>>()

    val username = MutableLiveData("")

    fun autoLogin() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                loginResource.postValue(authRepository.makeAutoLoginRequest())
            }
        }
    }

    fun clearLoginResource() {
        loginResource = MutableLiveData<UseCase<LoginResponse>>()
    }

    fun getUsername() = authRepository.getUsername()
}