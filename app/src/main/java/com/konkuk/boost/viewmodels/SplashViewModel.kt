package com.konkuk.boost.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.konkuk.boost.data.auth.LoginResponse
import com.konkuk.boost.data.grade.UserInformationResponse
import com.konkuk.boost.repositories.AuthRepository
import com.konkuk.boost.repositories.GradeRepository
import com.konkuk.boost.utils.UseCase
import kotlinx.coroutines.launch

class SplashViewModel(
    private val authRepository: AuthRepository,
    private val gradeRepository: GradeRepository
) : ViewModel() {
    var loginResource = MutableLiveData<UseCase<LoginResponse>>()

    val username = MutableLiveData("")

    val userInfoResponse = MutableLiveData<UseCase<UserInformationResponse>>()

    val eventBit = MutableLiveData(0b00)

    fun updateEvent(position: Int) {
        val event = eventBit.value ?: 0
        eventBit.value = event or position
    }

    fun autoLogin() {
        viewModelScope.launch {
            loginResource.postValue(authRepository.makeAutoLoginRequest())
        }
    }

    fun fetchUserInfo() {
        viewModelScope.launch {
            userInfoResponse.postValue(gradeRepository.makeUserInformationRequest())
        }
    }

    fun clearLoginResource() {
        loginResource = MutableLiveData<UseCase<LoginResponse>>()
        eventBit.postValue(0b00)
    }

    fun getUsername() = authRepository.getUsername()
}