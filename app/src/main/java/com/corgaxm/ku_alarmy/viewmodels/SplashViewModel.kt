package com.corgaxm.ku_alarmy.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.corgaxm.ku_alarmy.data.UseCase
import com.corgaxm.ku_alarmy.data.auth.AuthRepository
import com.corgaxm.ku_alarmy.data.auth.LoginResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    var loginResource = MutableLiveData<UseCase<LoginResponse>>()

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
}