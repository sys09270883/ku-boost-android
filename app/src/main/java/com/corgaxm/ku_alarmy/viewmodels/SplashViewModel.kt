package com.corgaxm.ku_alarmy.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.corgaxm.ku_alarmy.data.login.LoginRepository
import com.corgaxm.ku_alarmy.data.login.LoginResponse
import com.corgaxm.ku_alarmy.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashViewModel(private val repository: LoginRepository) : ViewModel() {

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                loginResource.postValue(repository.makeAutoLoginRequest())
            }
        }
    }

    var loginResource = MutableLiveData<Resource<LoginResponse>>()

    fun clearLoginResource() {
        loginResource = MutableLiveData<Resource<LoginResponse>>()
    }
}