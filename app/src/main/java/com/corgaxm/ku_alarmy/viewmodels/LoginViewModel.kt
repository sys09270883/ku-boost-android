package com.corgaxm.ku_alarmy.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.corgaxm.ku_alarmy.data.auth.AuthRepository
import com.corgaxm.ku_alarmy.data.auth.LoginResponse
import com.corgaxm.ku_alarmy.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    val username: MutableLiveData<String> by lazy {
        MutableLiveData<String>().apply {
            postValue("")
        }
    }

    val password: MutableLiveData<String> by lazy {
        MutableLiveData<String>().apply {
            postValue("")
        }
    }

    private val _loading: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply {
            postValue(false)
        }
    }
    val loading get() = _loading

    var loginResource = MutableLiveData<Resource<LoginResponse>>()

    fun clearLoginResource() {
        loginResource = MutableLiveData<Resource<LoginResponse>>()
    }

    fun login() {
        val username = username.value ?: return
        val password = password.value ?: return

        _loading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                loginResource.postValue(
                    authRepository.makeLoginRequest(
                        username,
                        password
                    )
                )
            }
            _loading.postValue(false)
        }
    }

}