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

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {
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

    val loginResource = MutableLiveData<Resource<LoginResponse>>()

    fun login() {
        _loading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                loginResource.postValue(
                    loginRepository.makeLoginRequest(
                        username.value ?: "",
                        password.value ?: ""
                    )
                )
            }
            _loading.postValue(false)
        }
    }

}