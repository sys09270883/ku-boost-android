package com.konkuk.boost.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.boost.utils.UseCase
import com.konkuk.boost.repositories.AuthRepository
import com.konkuk.boost.data.auth.LoginResponse
import com.konkuk.boost.repositories.GradeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val gradeRepository: GradeRepository
) : ViewModel() {
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

    var loginResource = MutableLiveData<UseCase<LoginResponse>>()

    fun clearLoginResource() {
        loginResource = MutableLiveData<UseCase<LoginResponse>>()
    }

    fun login() {
        val username = username.value ?: return
        val password = password.value ?: return

        _loading.value = true
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                loginResource.postValue(authRepository.makeLoginRequest(username, password))
                gradeRepository.makeUserInformationRequest()
            }
            _loading.postValue(false)
        }
    }

}