package com.konkuk.boost.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.boost.data.auth.ChangePasswordResponse
import com.konkuk.boost.data.auth.LoginResponse
import com.konkuk.boost.data.grade.UserInformationResponse
import com.konkuk.boost.repositories.AuthRepository
import com.konkuk.boost.repositories.GradeRepository
import com.konkuk.boost.utils.UseCase
import kotlinx.coroutines.launch

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

    val eventBit = MutableLiveData(0b00)

    fun updateEvent(position: Int) {
        val event = eventBit.value ?: 0
        eventBit.value = event or position
    }

    var loginResource = MutableLiveData<UseCase<LoginResponse>>()

    fun clearLoginResource() {
        loginResource = MutableLiveData<UseCase<LoginResponse>>()
        eventBit.postValue(0b00)
    }

    val userInfoResponse = MutableLiveData<UseCase<UserInformationResponse>>()

    fun fetchUserInfo() {
        viewModelScope.launch {
            userInfoResponse.postValue(gradeRepository.makeUserInformationRequest())
        }
    }

    fun login() {
        val username = username.value ?: return
        val password = password.value ?: return

        _loading.value = true
        viewModelScope.launch {
            loginResource.postValue(authRepository.makeLoginRequest(username, password))
        }
    }

    val changePasswordResponse = MutableLiveData<UseCase<ChangePasswordResponse>>()

    fun changePasswordAfter90Days() {
        val username = username.value ?: return
        val password = password.value ?: return

        viewModelScope.launch {
            changePasswordResponse.postValue(
                authRepository.makeChangePasswordRequest(
                    username,
                    password
                )
            )
        }
    }

}